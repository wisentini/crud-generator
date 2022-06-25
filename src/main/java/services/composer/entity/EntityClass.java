package services.composer.entity;

import util.ComposerUtil;

import java.util.List;

public class EntityClass extends BaseClass {
    private Class<?> primaryKeyColumnClass;

    public EntityClass(List<String> imports, String classPackage, List<Annotation> annotations, List<Integer> modifiers, String name, List<Attribute> attributes, List<Method> methods, List<String> classesToExtend, List<String> classesToImplement, Class<?> primaryKeyColumnClass) {
        super(imports, classPackage, annotations, modifiers, name, attributes, methods, classesToExtend, classesToImplement);
        this.primaryKeyColumnClass = primaryKeyColumnClass;
    }

    @Override
    public String toString() {
        var imports = ComposerUtil.composeImports(this.imports);
        var annotations = ComposerUtil.composeAnnotations(this.annotations);
        var modifiers = ComposerUtil.composeModifiers(this.modifiers);
        var attributes = ComposerUtil.composeAttributes(this.attributes);
        var methods = ComposerUtil.composeMethods(this.methods);
        var classesToExtend = ComposerUtil.composeClassesToExtend(this.classesToExtend);
        var classesToImplement = ComposerUtil.composeClassesToImplement(this.classesToImplement);

        var string = """
                package %s;
                
                %s
                
                %s %s class %s %s %s {
                    %s
                    
                    %s
                }
                """.formatted(this.classPackage, imports, annotations, modifiers, this.name, classesToExtend, classesToImplement, attributes, methods);

        var trimmedString = string.trim();
        return trimmedString;
    }

    public Class<?> getPrimaryKeyColumnClass() {
        return this.primaryKeyColumnClass;
    }

    public Attribute getPrimaryKeyAttribute() {
        for (Attribute attribute : this.attributes) {
            if (attribute.getIsPrimaryKey()) {
                return attribute;
            }
        }

        return null;
    }
}
