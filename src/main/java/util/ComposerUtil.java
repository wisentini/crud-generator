package util;

import services.composer.entity.*;
import services.composer.entity.Annotation;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Collectors;

public class ComposerUtil {
    public static String composeImports(List<String> imports) {
        var stringBuilder = new StringBuilder();

        for (var importString : imports) {
            var formattedImport = String.format("import %s;\n", importString);
            stringBuilder.append(formattedImport);
        }

        return stringBuilder.toString();
    }

    public static String composeAttributes(List<Attribute> attributes) {
        var stringBuilder = new StringBuilder();

        for (var attribute : attributes) {
            var string = String.format("%s;\n", attribute);
            stringBuilder.append(string);
        }

        return stringBuilder.toString();
    }

    public static String composeMethods(List<Method> methods) {
        if (methods == null || methods.isEmpty()) {
            return "";
        } else {
            return methods.stream().map(Method::toString).collect(Collectors.joining("\n"));
        }
    }

    public static String composeAnnotations(List<Annotation> annotations) {
        if (annotations == null || annotations.isEmpty()) {
            return "";
        } else {
            return annotations.stream().map(Annotation::toString).collect(Collectors.joining("\n"));
        }
    }

    public static String composeModifiers(List<Integer> modifiers) {
        var totalSumOfModifiers = 0;

        for (var modifier : modifiers) {
            totalSumOfModifiers += modifier;
        }

        var modifiersString = Modifier.toString(totalSumOfModifiers);
        return modifiersString;
    }

    public static String composeThrownExceptions(List<String> thrownExceptions) {
        if (thrownExceptions == null || thrownExceptions.isEmpty()) {
            return "";
        } else {
            String exceptionsToThrow = String.join(", ", thrownExceptions);
            return String.format("throws %s", exceptionsToThrow);
        }
    }

    public static String composeParameters(List<Parameter> parameters) {
        if (parameters == null || parameters.isEmpty()) {
            return "";
        } else {
            return parameters.stream().map(Parameter::toString).collect(Collectors.joining(", "));
        }
    }

    public static String composeClassesToExtend(List<String> classesToExtend) {
        if (classesToExtend == null || classesToExtend.isEmpty()) {
            return "";
        } else {
            var classes = String.join(", ", classesToExtend);
            return String.format("extends %s", classes);
        }
    }

    public static String composeClassesToImplement(List<String> classesToImplement) {
        if (classesToImplement == null || classesToImplement.isEmpty()) {
            return "";
        } else {
            var classes = String.join(", ", classesToImplement);
            return String.format("implements %s", classes);
        }
    }

    public static String composeClassPackage(String classPackage) {
        if (classPackage == null || classPackage.isEmpty()) {
            return "";
        } else {
            return String.format("package %s;", classPackage);
        }
    }
}
