package services.composer;

import services.composer.entity.*;
import util.ClassUtil;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class DAOClassComposer {
    public static DAOClass composeClass(EntityClass entityClass, String classPackage) {
        var entityClassName = entityClass.getName();
        var attributes = new ArrayList<Attribute>();
        var primaryKeyColumnClass = entityClass.getPrimaryKeyColumnClass();
        var constructor = composeConstructor(entityClassName);
        var methods = List.of(constructor);

        var entityClassToImport = String.format("entity.%s", entityClassName);
        var persistenceClassToImport = "javax.persistence.*";
        var imports = List.of(entityClassToImport, persistenceClassToImport);
        var modifiers = List.of(Modifier.PUBLIC);
        var name = String.format("%sDAO", entityClassName);
        var primaryKeyColumnWrapperClass = ClassUtil.getWrapperClass(primaryKeyColumnClass);
        var primaryKeyColumWrapperClassName = primaryKeyColumnWrapperClass.getSimpleName();
        var classToExtend = String.format("BaseDAO<%s, %s>", primaryKeyColumWrapperClassName, entityClassName);
        var classesToExtend = List.of(classToExtend);
        var classesToImplement = new ArrayList<String>();

        var daoClass = new DAOClass(entityClass, imports, classPackage, modifiers, name, attributes, methods, classesToExtend, classesToImplement);
        return daoClass;
    }

    private static Method composeConstructor(String entityClassName) {
        var constructorParameter = new Parameter("entityManager", "EntityManager");
        var parameters = List.of(constructorParameter);
        var modifiers = List.of(Modifier.PUBLIC);
        var thrownExceptions = new ArrayList<String>();
        var returnType = "";
        var name = String.format("%sDAO", entityClassName);
        var body = String.format("super(entityManager, %s.class);", entityClassName);

        var method = new Method(modifiers, thrownExceptions, returnType, name, parameters, body);
        return method;
    }
}
