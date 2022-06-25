package services.composer.entity;

import java.util.List;

public class ExampleClass extends BaseClass {
    private EntityClass entityClass;
    private DAOClass daoClass;

    public ExampleClass(EntityClass entityClass, DAOClass daoClass, List<String> imports, String classPackage, List<Integer> modifiers, String name, List<Attribute> attributes, List<Method> methods, List<String> classesToExtend, List<String> classesToImplement) {
        super(imports, classPackage, modifiers, name, attributes, methods, classesToExtend, classesToImplement);
        this.entityClass = entityClass;
        this.daoClass = daoClass;
    }

    public EntityClass getModelClass() {
        return this.entityClass;
    }

    public DAOClass getDAOClass() {
        return this.daoClass;
    }
}
