package services.generator;

import database.metadata.DatabaseMetadata;

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

import java.util.ArrayList;
import java.util.List;

public class CRUDGenerator {
    private final String databaseURL;
    private final String entityClassesDir;
    private final String daoClassesDir;
    private final String exampleClassesDir;
    private final String applicationClassDir;
    private List<EntityClass> entityClasses;
    private List<DAOClass> daoClasses;
    private List<ExampleClass> exampleClasses;

    public CRUDGenerator() throws Exception {
        try {
            var properties = PropertiesUtil.getProperties();

            this.databaseURL = properties.getProperty("database.url");
            this.entityClassesDir = properties.getProperty("entityClass.dir");
            this.daoClassesDir = properties.getProperty("daoClass.dir");
            this.exampleClassesDir = properties.getProperty("exampleClass.dir");
            this.applicationClassDir = properties.getProperty("applicationClass.dir");

            this.entityClasses = new ArrayList<>();
            this.daoClasses = new ArrayList<>();
            this.exampleClasses = new ArrayList<>();
        } catch (Exception e) {
            var message = "\nCouldn't load properties file.\n";
            throw new Exception(message);
        }
    }

    public void run() {
        this.generateClasses();

        System.out.println("\nGenerating application class...");

        var applicationClass = ApplicationClassComposer.composeClass(this.exampleClasses);

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
        if (!(this.entityClasses == null || this.entityClasses.isEmpty())) {
            return;
        }

        System.out.println("\nGenerating entity classes...");

        try {
            var connection = DatabaseUtil.getConnection(this.databaseURL);
            var databaseMetadata = new DatabaseMetadata(connection);
            var tablesMetadata = databaseMetadata.getTablesMetadata();

            var classPackage = FileUtil.getNameFrom(this.entityClassesDir);

            for (var tableMetadata : tablesMetadata) {
                var entityClass = EntityClassComposer.composeClass(tableMetadata, classPackage);
                this.writeToFile(entityClass, this.entityClassesDir);
                this.entityClasses.add(entityClass);
            }

            System.out.println("\nEntity classes have been generated!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateDAOClasses() {
        if (!(this.daoClasses == null || this.daoClasses.isEmpty())) {
            return;
        }

        this.generateEntityClasses();

        System.out.println("\nGenerating DAO classes...");

        var classPackage = FileUtil.getNameFrom(this.daoClassesDir);

        for (var entityClass : this.entityClasses) {
            var daoClass = DAOClassComposer.composeClass(entityClass, classPackage);
            this.writeToFile(daoClass, this.daoClassesDir);
            this.daoClasses.add(daoClass);
        }

        System.out.println("\nDAO classes have been generated!");
    }

    private void generateExampleClasses() {
        if (!(this.exampleClasses == null || this.exampleClasses.isEmpty())) {
            return;
        }

        this.generateDAOClasses();

        System.out.println("\nGenerating example classes...");

        var classPackage = FileUtil.getNameFrom(this.exampleClassesDir);

        for (var daoClass : this.daoClasses) {
            var exampleClass = ExampleClassComposer.composeClass(classPackage, daoClass);
            this.writeToFile(exampleClass, this.exampleClassesDir);
            this.exampleClasses.add(exampleClass);
        }

        System.out.println("\nExample classes have been generated!");
    }

    private void writeToFile(BaseClass baseClass, String targetDirectory) {
        try {
            var className = baseClass.getName();
            var sourceCode = baseClass.toString();
            var formattedSourceCode = FormatterUtil.format(sourceCode);
            var filename = String.format("%s.java", className);
            var pathname = FilenameUtils.concat(targetDirectory, filename);
            FileUtil.writeTo(formattedSourceCode, pathname);
        } catch (FileException fileException) {
            var message = fileException.getMessage();
            System.err.println(message);
        }
    }
}
