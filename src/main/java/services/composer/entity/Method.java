package services.composer.entity;

import util.ComposerUtil;

import java.util.List;

public class Method {
    private List<Annotation> annotations;
    private List<Integer> modifiers;
    private List<String> thrownExceptions;
    private String returnType;
    private String name;
    private List<Parameter> parameters;
    private String body;

    public Method(List<Integer> modifiers, List<String> thrownExceptions, String returnType, String name, List<Parameter> parameters, String body) {
        this.modifiers = modifiers;
        this.thrownExceptions = thrownExceptions;
        this.returnType = returnType;
        this.name = name;
        this.parameters = parameters;
        this.body = body;
    }

    public Method(List<Annotation> annotations, List<Integer> modifiers, List<String> thrownExceptions, String returnType, String name, List<Parameter> parameters, String body) {
        this(modifiers, thrownExceptions, returnType, name, parameters, body);
        this.annotations = annotations;
    }

    @Override
    public String toString() {
        String annotations = ComposerUtil.composeAnnotations(this.annotations);
        String modifiers = ComposerUtil.composeModifiers(this.modifiers);
        String thrownExceptions = ComposerUtil.composeThrownExceptions(this.thrownExceptions);
        String parameters = ComposerUtil.composeParameters(this.parameters);

        var string = """
            %s %s %s %s(%s) %s {
                %s
            }
            """.formatted(annotations, modifiers, returnType, this.name, parameters, thrownExceptions, this.body);

        return string.trim();
    }
}
