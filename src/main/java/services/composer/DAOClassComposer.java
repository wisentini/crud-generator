package services.composer;

import services.composer.entity.*;
import util.ClassUtil;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class DAOClassComposer {
    public static DAOClass composeClass(EntityClass entityClass, String classPackage) {
        String entityClassName = entityClass.getName();
        List<Attribute> attributes = new ArrayList<>();
        Class<?> primaryKeyColumnClass = entityClass.getPrimaryKeyColumnClass();
        Method constructor = composeConstructor(entityClassName);
        List<Method> methods = List.of(constructor);

        String entityClassToImport = String.format("entity.%s", entityClassName);
        String persistenceClassToImport = "javax.persistence.*";
        List<String> imports = List.of(entityClassToImport, persistenceClassToImport);
        List<Integer> modifiers = List.of(Modifier.PUBLIC);
        String name = String.format("%sDAO", entityClassName);
        Class<?> primaryKeyColumnWrapperClass = ClassUtil.getWrapperClass(primaryKeyColumnClass);
        String primaryKeyColumWrapperClassName = primaryKeyColumnWrapperClass.getSimpleName();
        String classToExtend = String.format("BaseDAO<%s, %s>", primaryKeyColumWrapperClassName, entityClassName);
        List<String> classesToExtend = List.of(classToExtend);
        List<String> classesToImplement = new ArrayList<>();

        return new DAOClass(entityClass, imports, classPackage, modifiers, name, attributes, methods, classesToExtend, classesToImplement);
    }

    private static Method composeConstructor(String entityClassName) {
        Parameter constructorParameter = new Parameter("entityManager", "EntityManager");
        List<Parameter> parameters = List.of(constructorParameter);
        List<Integer> modifiers = List.of(Modifier.PUBLIC);
        List<String> thrownExceptions = new ArrayList<>();
        String returnType = "";
        String name = String.format("%sDAO", entityClassName);
        String body = String.format("super(entityManager, %s.class);", entityClassName);

        return new Method(modifiers, thrownExceptions, returnType, name, parameters, body);
    }
}
