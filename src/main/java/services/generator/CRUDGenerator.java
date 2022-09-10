package services.generator;

import database.metadata.DatabaseMetadata;

import database.metadata.TableMetadata;
import exception.FileException;
import org.apache.commons.io.FilenameUtils;
import services.composer.ApplicationClassComposer;
import services.composer.DAOClassComposer;
import services.composer.ExampleClassComposer;
import services.composer.EntityClassComposer;
import services.composer.entity.*;
import util.DatabaseUtil;
import util.FileUtil;
import util.FormatterUtil;
import util.PropertiesUtil;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CRUDGenerator {
    private final String databaseURL;
    private final String entityClassesDir;
    private final String daoClassesDir;
    private final String exampleClassesDir;
    private final String applicationClassDir;

    private final List<EntityClass> entityClasses = new ArrayList<>();
    private final List<DAOClass> daoClasses = new ArrayList<>();
    private final List<ExampleClass> exampleClasses = new ArrayList<>();

    public CRUDGenerator() throws Exception {
        try {
            Properties properties = PropertiesUtil.getProperties();

            this.databaseURL = properties.getProperty("database.url");
            this.entityClassesDir = properties.getProperty("entityClass.dir");
            this.daoClassesDir = properties.getProperty("daoClass.dir");
            this.exampleClassesDir = properties.getProperty("exampleClass.dir");
            this.applicationClassDir = properties.getProperty("applicationClass.dir");
        } catch (Exception e) {
            String message = "\nCouldn't load properties file.\n";
            throw new Exception(message);
        }
    }

    public void run() {
        this.generateClasses();

        System.out.println("\nGenerating application class...");

        BaseClass applicationClass = ApplicationClassComposer.composeClass(this.exampleClasses);

        System.out.println("\nApplication class have been generated!\n");

        this.writeToFile(applicationClass, this.applicationClassDir);
    }

    private void generateClasses() {
        System.out.println("\nGenerating classes...");

        this.generateEntityClasses();
        this.generateDAOClasses();
        this.generateExampleClasses();

        System.out.println("\nClasses have been generated!");
    }

    private void generateEntityClasses() {
        if (!this.entityClasses.isEmpty()) {
            return;
        }

        System.out.println("\nGenerating entity classes...");

        try {
            Connection connection = DatabaseUtil.getConnection(this.databaseURL);
            DatabaseMetadata databaseMetadata = new DatabaseMetadata(connection);
            List<TableMetadata> tablesMetadata = databaseMetadata.getTablesMetadata();

            String classPackage = FileUtil.getNameFrom(this.entityClassesDir);

            for (TableMetadata tableMetadata : tablesMetadata) {
                EntityClass entityClass = EntityClassComposer.composeClass(tableMetadata, classPackage);
                this.writeToFile(entityClass, this.entityClassesDir);
                this.entityClasses.add(entityClass);
            }

            System.out.println("\nEntity classes have been generated!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateDAOClasses() {
        if (!this.daoClasses.isEmpty()) {
            return;
        }

        this.generateEntityClasses();

        System.out.println("\nGenerating DAO classes...");

        String classPackage = FileUtil.getNameFrom(this.daoClassesDir);

        for (EntityClass entityClass : this.entityClasses) {
            DAOClass daoClass = DAOClassComposer.composeClass(entityClass, classPackage);
            this.writeToFile(daoClass, this.daoClassesDir);
            this.daoClasses.add(daoClass);
        }

        System.out.println("\nDAO classes have been generated!");
    }

    private void generateExampleClasses() {
        if (!this.exampleClasses.isEmpty()) {
            return;
        }

        this.generateDAOClasses();

        System.out.println("\nGenerating example classes...");

        String classPackage = FileUtil.getNameFrom(this.exampleClassesDir);

        for (DAOClass daoClass : this.daoClasses) {
            ExampleClass exampleClass = ExampleClassComposer.composeClass(classPackage, daoClass);
            this.writeToFile(exampleClass, this.exampleClassesDir);
            this.exampleClasses.add(exampleClass);
        }

        System.out.println("\nExample classes have been generated!");
    }

    private void writeToFile(BaseClass baseClass, String targetDirectory) {
        try {
            String className = baseClass.getName();
            String sourceCode = baseClass.toString();
            String formattedSourceCode = FormatterUtil.format(sourceCode);
            String filename = String.format("%s.java", className);
            String pathname = FilenameUtils.concat(targetDirectory, filename);
            FileUtil.writeTo(formattedSourceCode, pathname);
        } catch (FileException fileException) {
            fileException.printStackTrace();
        }
    }
}
