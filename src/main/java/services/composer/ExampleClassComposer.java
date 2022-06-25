package services.composer;

import services.composer.entity.*;
import util.ClassUtil;
import util.DatabaseUtil;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExampleClassComposer {
    public static ExampleClass composeClass(String classPackage, DAOClass daoClass) {
        var entityClass = daoClass.getEntityClass();
        var entityClassName = entityClass.getName();
        var className = String.format("%sExample", entityClassName);

        var entityClassToImport = String.format("entity.%s", entityClassName);
        var exceptionClassToImport = "exception.DatabaseException";
        var persistenceClassToImport = "javax.persistence.EntityManager";
        var imports = List.of(entityClassToImport, exceptionClassToImport, persistenceClassToImport);

        var attributes = new ArrayList<Attribute>();
        var methods = composeMethods(daoClass);
        var modifiers = List.of(Modifier.PUBLIC);

        var primaryKeyColumnClass = entityClass.getPrimaryKeyColumnClass();
        var primaryKeyColumnWrapperClass = ClassUtil.getWrapperClass(primaryKeyColumnClass);
        var primaryKeyColumWrapperClassName = primaryKeyColumnWrapperClass.getSimpleName();
        var classToExtend = String.format("BaseExample<%s, %s>", primaryKeyColumWrapperClassName, entityClassName);
        var classesToExtend = List.of(classToExtend);

        var classesToImplement = new ArrayList<String>();

        var exampleClass = new ExampleClass(entityClass, daoClass, imports, classPackage, modifiers, className, attributes, methods, classesToExtend, classesToImplement);
        return exampleClass;
    }

    private static String generateRandomAttributeValue(Attribute attribute) {
        var type = attribute.getType();
        var size = attribute.getSize();
        var value = DatabaseUtil.generateValue(type, size);
        return value;
    }

    private static String generateConstructorParameters(EntityClass entityClass) {
        var attributes = entityClass.getAttributes();
        var values = new ArrayList<String>();

        for (var attribute : attributes) {
            if (attribute.getIsPrimaryKey()) {
                continue;
            }

            var value = generateRandomAttributeValue(attribute);
            values.add(value);
        }

        var constructorParametersString = String.join(", ", values);
        return constructorParametersString;
    }

    private static List<Method> composeMethods(DAOClass daoClass) {
        var constructorMethod = composeConstructorMethod(daoClass);
        var saveMethod = composeSaveMethod(daoClass);
        var updateMethod = composeUpdateMethod(daoClass);
        var methods = List.of(constructorMethod, saveMethod, updateMethod);
        return methods;
    }

    private static Method composeConstructorMethod(DAOClass daoClass) {
        var entityClass = daoClass.getEntityClass();
        var entityClassName = entityClass.getName();
        var constructorParameter = new Parameter("entityManager", "EntityManager");
        var parameters = List.of(constructorParameter);
        var modifiers = List.of(Modifier.PUBLIC);
        var thrownExceptions = new ArrayList<String>();
        var returnType = "";
        var name = String.format("%sExample", entityClassName);
        var body = String.format("super(entityManager, %s.class);", entityClassName);

        var method = new Method(modifiers, thrownExceptions, returnType, name, parameters, body);
        return method;
    }

    private static Method composeSaveMethod(DAOClass daoClass) {
        var entityClass = daoClass.getEntityClass();
        var entityClassName = entityClass.getName();

        var modifiers = List.of(Modifier.PROTECTED);
        var thrownExceptions = List.of("DatabaseException");
        var returnType = "void";
        var name = "save";
        var parameters = new ArrayList<Parameter>();
        var constructorParameters = generateConstructorParameters(entityClass);
        var body = """
                var entity = new %s(%s);
                this.entity = entity;
                System.out.print("\\nSaving entity:");
                System.out.print("\\n\\tOBS.: Entity's ID is shown as 0 in here because it will be created by the database later.\\n");
                System.out.printf("\\n\\t%%s\\n", entity);
                this.dao.save(entity);
                """.formatted(entityClassName, constructorParameters);

        var method = new Method(modifiers, thrownExceptions, returnType, name, parameters, body);
        return method;
    }

    private static Attribute getNonPrimaryKeyRandomAttribute(EntityClass entityClass) {
        var entityClassAttributes = entityClass.getAttributes();
        var entityClassAttributesSize = entityClassAttributes.size();
        var random = new Random();

        while (true) {
            var randomIndex = random.nextInt(entityClassAttributesSize);
            var randomEntityClassAttribute = entityClassAttributes.get(randomIndex);

            if (!randomEntityClassAttribute.getIsPrimaryKey()) {
                return randomEntityClassAttribute;
            }
        }
    }

    private static Method composeUpdateMethod(DAOClass daoClass) {
        var modifiers = List.of(Modifier.PROTECTED);
        var thrownExceptions = List.of("DatabaseException");
        var entityClass = daoClass.getEntityClass();

        var randomEntityClassAttribute = getNonPrimaryKeyRandomAttribute(entityClass);
        var randomEntityClassAttributeName = randomEntityClassAttribute.getName();
        var randomEntityClassAttributeValue = generateRandomAttributeValue(randomEntityClassAttribute);

        var returnType = "void";
        var name = "update";
        var parameters = new ArrayList<Parameter>();
        var body = """
                System.out.print("\\nUpdating entity:");
                System.out.printf("\\n\\t%%s\\n", entity);
                
                var entity = this.findOne();
                entity.set%s(%s);
                
                System.out.print("\\nEntity updated:");
                System.out.printf("\\n\\t%%s\\n", entity);
                
                this.dao.update(entity);
                """.formatted(randomEntityClassAttributeName, randomEntityClassAttributeValue);

        var method = new Method(modifiers, thrownExceptions, returnType, name, parameters, body);
        return method;
    }
}
