package services.composer.entity;

import java.util.List;

public class DAOClass extends BaseClass {
    private EntityClass entityClass;

    public DAOClass(EntityClass entityClass, List<String> imports, String classPackage, List<Integer> modifiers, String name, List<Attribute> attributes, List<Method> methods, List<String> classesToExtend, List<String> classesToImplement) {
        super(imports, classPackage, modifiers, name, attributes, methods, classesToExtend, classesToImplement);
        this.entityClass = entityClass;
    }

    public EntityClass getEntityClass() {
        return this.entityClass;
    }
}
