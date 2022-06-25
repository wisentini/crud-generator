package services.composer.entity;

import util.ComposerUtil;

import java.util.List;

public class BaseClass {
    protected List<String> imports;
    protected String classPackage;
    protected List<Annotation> annotations;
    protected List<Integer> modifiers;
    protected String name;
    protected List<Attribute> attributes;
    protected List<Method> methods;
    protected List<String> classesToExtend;
    protected List<String> classesToImplement;

    public BaseClass(List<String> imports, String classPackage, List<Integer> modifiers, String name, List<Attribute> attributes, List<Method> methods, List<String> classesToExtend, List<String> classesToImplement) {
        this.imports = imports;
        this.classPackage = classPackage;
        this.modifiers = modifiers;
        this.name = name;
        this.attributes = attributes;
        this.methods = methods;
        this.classesToExtend = classesToExtend;
        this.classesToImplement = classesToImplement;
    }

    public BaseClass(List<String> imports, String classPackage, List<Annotation> annotations, List<Integer> modifiers, String name, List<Attribute> attributes, List<Method> methods, List<String> classesToExtend, List<String> classesToImplement) {
        this(imports, classPackage, modifiers, name, attributes, methods, classesToExtend, classesToImplement);
        this.annotations = annotations;
    }

    @Override
    public String toString() {
        var classPackage = ComposerUtil.composeClassPackage(this.classPackage);
        var imports = ComposerUtil.composeImports(this.imports);
        var modifiers = ComposerUtil.composeModifiers(this.modifiers);
        var attributes = ComposerUtil.composeAttributes(this.attributes);
        var methods = ComposerUtil.composeMethods(this.methods);
        var classesToExtend = ComposerUtil.composeClassesToExtend(this.classesToExtend);
        var classesToImplement = ComposerUtil.composeClassesToImplement(this.classesToImplement);

        var result = """
                %s
                
                %s
                
                %s class %s %s %s {
                    %s
                    
                    %s
                }
                """.formatted(classPackage, imports, modifiers, this.name, classesToExtend, classesToImplement, attributes, methods);

        var trimmedResult = result.trim();
        return trimmedResult;
    }

    public String getName() {
        return this.name;
    }

    public List<Attribute> getAttributes() {
        return this.attributes;
    }
}
