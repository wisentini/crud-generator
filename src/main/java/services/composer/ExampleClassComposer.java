package services.composer;

import org.w3c.dom.Attr;
import services.composer.entity.*;
import util.ClassUtil;
import util.DatabaseUtil;
import util.StringUtil;

import java.lang.reflect.Modifier;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExampleClassComposer {
    public static ExampleClass composeClass(String classPackage, DAOClass daoClass) {
        EntityClass entityClass = daoClass.getEntityClass();
        String entityClassName = entityClass.getName();
        String className = String.format("%sExample", entityClassName);

        String entityClassToImport = String.format("entity.%s", entityClassName);
        String exceptionClassToImport = "exception.DatabaseException";
        String persistenceClassToImport = "javax.persistence.EntityManager";
        List<String> imports = List.of(entityClassToImport, exceptionClassToImport, persistenceClassToImport);

        List<Attribute> attributes = new ArrayList<>();
        List<Method> methods = composeMethods(daoClass);
        List<Integer> modifiers = List.of(Modifier.PUBLIC);

        Class<?> primaryKeyColumnClass = entityClass.getPrimaryKeyColumnClass();
        Class<?> primaryKeyColumnWrapperClass = ClassUtil.getWrapperClass(primaryKeyColumnClass);
        String primaryKeyColumWrapperClassName = primaryKeyColumnWrapperClass.getSimpleName();
        String classToExtend = String.format("BaseExample<%s, %s>", primaryKeyColumWrapperClassName, entityClassName);
        List<String> classesToExtend = List.of(classToExtend);

        List<String> classesToImplement = new ArrayList<>();

        return new ExampleClass(entityClass, daoClass, imports, classPackage, modifiers, className, attributes, methods, classesToExtend, classesToImplement);
    }

    private static String generateRandomAttributeValue(Attribute attribute) {
        String type = attribute.getType();
        int size = attribute.getSize();
        return DatabaseUtil.generateValue(type, size);
    }

    private static String generateConstructorParameters(EntityClass entityClass) {
        List<Attribute> attributes = entityClass.getAttributes();
        List<String> values = new ArrayList<>();

        for (Attribute attribute : attributes) {
            if (!attribute.getIsPrimaryKey()) {
                values.add(generateRandomAttributeValue(attribute));
            }
        }

        return String.join(", ", values);
    }

    private static List<Method> composeMethods(DAOClass daoClass) {
        Method constructorMethod = composeConstructorMethod(daoClass);
        Method saveMethod = composeSaveMethod(daoClass);
        Method updateMethod = composeUpdateMethod(daoClass);
        return List.of(constructorMethod, saveMethod, updateMethod);
    }

    private static Method composeConstructorMethod(DAOClass daoClass) {
        EntityClass entityClass = daoClass.getEntityClass();
        String entityClassName = entityClass.getName();
        Parameter constructorParameter = new Parameter("entityManager", "EntityManager");
        List<Parameter> parameters = List.of(constructorParameter);
        List<Integer> modifiers = List.of(Modifier.PUBLIC);
        List<String> thrownExceptions = new ArrayList<>();
        String returnType = "";
        String name = String.format("%sExample", entityClassName);
        String body = String.format("super(entityManager, %s.class);", entityClassName);

        return new Method(modifiers, thrownExceptions, returnType, name, parameters, body);
    }

    private static Method composeSaveMethod(DAOClass daoClass) {
        EntityClass entityClass = daoClass.getEntityClass();
        String entityClassName = entityClass.getName();

        List<Integer> modifiers = List.of(Modifier.PROTECTED);
        List<String> thrownExceptions = List.of("DatabaseException");
        String returnType = "void";
        String name = "save";
        List<Parameter> parameters = new ArrayList<>();
        String constructorParameters = generateConstructorParameters(entityClass);
        String body = """
            %s entity = new %s(%s);
            this.entity = entity;
            System.out.print("\\nSaving entity:");
            System.out.print("\\n\\tOBS.: Entity's ID is shown as 0 in here because it will be created by the database later.\\n");
            System.out.printf("\\n\\t%%s\\n", entity);
            this.dao.save(entity);
        """.formatted(entityClassName, entityClassName, constructorParameters);

        return new Method(modifiers, thrownExceptions, returnType, name, parameters, body);
    }

    private static Attribute getNonPrimaryKeyRandomAttribute(EntityClass entityClass) {
        List<Attribute> entityClassAttributes = entityClass.getAttributes();
        int entityClassAttributesSize = entityClassAttributes.size();
        SecureRandom secureRandom = new SecureRandom();

        while (true) {
            int randomIndex = secureRandom.nextInt(entityClassAttributesSize);
            Attribute randomEntityClassAttribute = entityClassAttributes.get(randomIndex);

            if (!randomEntityClassAttribute.getIsPrimaryKey()) {
                return randomEntityClassAttribute;
            }
        }
    }

    private static Method composeUpdateMethod(DAOClass daoClass) {
        List<Integer> modifiers = List.of(Modifier.PROTECTED);
        List<String> thrownExceptions = List.of("DatabaseException");
        EntityClass entityClass = daoClass.getEntityClass();
        String entityClassName = entityClass.getName();

        Attribute randomEntityClassAttribute = getNonPrimaryKeyRandomAttribute(entityClass);
        String randomEntityClassAttributeName = randomEntityClassAttribute.getName();
        String randomEntityClassAttributeSetterName = StringUtil.composeSetterMethodName(randomEntityClassAttributeName);
        String randomEntityClassAttributeValue = generateRandomAttributeValue(randomEntityClassAttribute);

        String returnType = "void";
        String name = "update";
        List<Parameter> parameters = new ArrayList<>();
        String body = """
            System.out.print("\\nUpdating entity:");
            System.out.printf("\\n\\t%%s\\n", entity);
            
            %s entity = this.findOne();
            entity.%s(%s);
            
            System.out.print("\\nEntity updated:");
            System.out.printf("\\n\\t%%s\\n", entity);
            
            this.dao.update(entity);
        """.formatted(entityClassName, randomEntityClassAttributeSetterName, randomEntityClassAttributeValue);

        return new Method(modifiers, thrownExceptions, returnType, name, parameters, body);
    }
}
