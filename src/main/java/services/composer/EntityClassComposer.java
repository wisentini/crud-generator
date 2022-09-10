package services.composer;

import database.metadata.ColumnMetadata;
import database.metadata.TableMetadata;
import services.composer.entity.Attribute;
import services.composer.entity.EntityClass;
import services.composer.entity.Method;
import services.composer.entity.Parameter;
import services.composer.entity.Annotation;
import services.composer.entity.AnnotationElement;
import util.ClassUtil;
import util.StringUtil;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class EntityClassComposer {
    public static EntityClass composeClass(TableMetadata tableMetadata, String classPackage) {
        List<Attribute> attributes = composeAttributes(tableMetadata);
        String className = tableMetadata.getName();
        List<Method> methods = composeMethods(className, attributes);
        List<Annotation> annotations = composeClassAnnotations(className);

        List<String> imports = List.of("javax.persistence.*");
        List<Integer> modifiers = List.of(Modifier.PUBLIC);
        ColumnMetadata primaryKeyColumn = tableMetadata.getPrimaryKeyColumn();
        Class<?> primaryKeyColumnClass = primaryKeyColumn.getTypeClass();
        Class<?> wrapperClass = ClassUtil.getWrapperClass(primaryKeyColumnClass);
        String wrapperClassName = wrapperClass.getSimpleName();
        List<String> classesToExtend = new ArrayList<>();
        String baseEntityInterface = String.format("BaseEntity<%s>", wrapperClassName);
        List<String> classesToImplement = List.of(baseEntityInterface);

        return new EntityClass(imports, classPackage, annotations, modifiers, className, attributes, methods, classesToExtend, classesToImplement, primaryKeyColumnClass);
    }

    public static List<Annotation> composeClassAnnotations(String className) {
        Annotation entityAnnotation = new Annotation("Entity");

        String tableAnnotationNameElementName = "name";
        String tableAnnotationNameElementValue = String.format("\"`%s`\"", className);
        AnnotationElement tableAnnotationNameElement = new AnnotationElement(tableAnnotationNameElementName, tableAnnotationNameElementValue);

        String tableAnnotationSchemaElementName = "schema";
        String tableAnnotationSchemaElementValue = "\"public\"";
        AnnotationElement tableAnnotationSchemaElement = new AnnotationElement(tableAnnotationSchemaElementName, tableAnnotationSchemaElementValue);

        List<AnnotationElement> tableAnnotationElements = List.of(tableAnnotationNameElement, tableAnnotationSchemaElement);

        Annotation tableAnnotation = new Annotation("Table", tableAnnotationElements);

        return List.of(entityAnnotation, tableAnnotation);
    }

    private static List<Method> composeMethods(String className, List<Attribute> attributes) {
        Method constructor = composeConstructor(className, attributes);

        List<Attribute> emptyConstructorAttributes = new ArrayList<Attribute>();
        Method emptyConstructor = composeConstructor(className, emptyConstructorAttributes);

        Method partialConstructor = composePartialConstructor(className, attributes);

        List<Method> getters = composeGetters(attributes);
        List<Method> setters = composeSetters(attributes);

        Method toStringMethod = composeToStringMethod(attributes);

        List<Method> methods = new ArrayList<>();

        methods.add(emptyConstructor);
        methods.add(partialConstructor);
        methods.add(constructor);
        methods.add(toStringMethod);
        methods.addAll(getters);
        methods.addAll(setters);

        return methods;
    }

    private static Attribute composeAttribute(ColumnMetadata columnMetadata) {
        String columnName = columnMetadata.getName();
        String columnType = columnMetadata.getTypeClassName();
        int columnSize = columnMetadata.getSize();
        boolean columnIsPrimaryKey = columnMetadata.getIsPrimaryKey();

        String attributeName = columnName;

        String columnAnnotationElementName = "name";
        String columnAnnotationElementValue = String.format("\"`%s`\"", columnName);
        AnnotationElement columnAnnotationElement = new AnnotationElement(columnAnnotationElementName, columnAnnotationElementValue);
        String columnAnnotationName = "Column";
        List<AnnotationElement> columnAnnotationElements = List.of(columnAnnotationElement);
        Annotation columnAnnotation = new Annotation(columnAnnotationName, columnAnnotationElements);

        LinkedList<Annotation> annotations = new LinkedList<>(List.of(columnAnnotation));
        List<Integer> modifiers = List.of(Modifier.PRIVATE);

        if (columnIsPrimaryKey) {
            String generatedValueElementName = "strategy";
            String generatedValueElementValue = "GenerationType.IDENTITY";
            AnnotationElement generatedValueElement = new AnnotationElement(generatedValueElementName, generatedValueElementValue);
            String generatedValueAnnotationName = "GeneratedValue";
            List<AnnotationElement> generatedValueElements = List.of(generatedValueElement);
            Annotation generatedValueAnnotation = new Annotation(generatedValueAnnotationName, generatedValueElements);
            annotations.addFirst(generatedValueAnnotation);

            String idAnnotationName = "Id";
            Annotation idAnnotation = new Annotation(idAnnotationName);
            annotations.addFirst(idAnnotation);

            attributeName = "id";
        }

        return new Attribute(attributeName, columnType, columnSize, columnIsPrimaryKey, annotations, modifiers, columnName);
    }

    private static List<Attribute> composeAttributes(TableMetadata tableMetadata) {
        List<ColumnMetadata> columnsMetadata = tableMetadata.getColumnsMetadata();
        List<Attribute> attributes = new ArrayList<>();

        for (ColumnMetadata columnMetadata : columnsMetadata) {
            attributes.add(composeAttribute(columnMetadata));
        }

        return attributes;
    }

    private static String composeConstructorBody(List<Attribute> attributes) {
        StringBuilder constructorBodyBuilder = new StringBuilder();

        for (Attribute attribute : attributes) {
            String attributeName = attribute.getName();
            String attributeColumnName = attribute.getColumnName();
            String constructorBody = "";

            if (attribute.getIsPrimaryKey()) {
                constructorBody = String.format("this.id = %s;\n", attributeColumnName);
            } else {
                constructorBody = String.format("this.%s = %s;\n", attributeName, attributeName);
            }

            constructorBodyBuilder.append(constructorBody);
        }

        return constructorBodyBuilder.toString();
    }

    private static String composePartialConstructorBody(List<Attribute> attributes) {
        StringBuilder constructorBodyBuilder = new StringBuilder();

        for (Attribute attribute : attributes) {
            String attributeName = attribute.getName();
            boolean attributeIsPrimaryKey = attribute.getIsPrimaryKey();
            String constructorBody = "";

            if (!attributeIsPrimaryKey) {
                constructorBody = String.format("this.%s = %s;\n", attributeName, attributeName);
            }

            constructorBodyBuilder.append(constructorBody);
        }

        return constructorBodyBuilder.toString();
    }

    private static List<Parameter> composeParameters(List<Attribute> attributes) {
        List<Parameter> parameters = new ArrayList<>();

        for (Attribute attribute : attributes) {
            String attributeColumnName = attribute.getColumnName();
            String attributeType = attribute.getType();
            boolean attributeIsPrimaryKey = attribute.getIsPrimaryKey();
            Parameter parameter = new Parameter(attributeColumnName, attributeType, attributeIsPrimaryKey);
            parameters.add(parameter);
        }

        return parameters;
    }

    private static Method composePartialConstructor(String name, List<Attribute> attributes) {
        List<Integer> modifiers = List.of(Modifier.PUBLIC);
        List<String> thrownExceptions = new ArrayList<>();
        String returnType = "";
        List<Parameter> parameters = composeParameters(attributes).stream().filter(parameter -> !parameter.getIsPrimaryKey()).toList();
        String body = composePartialConstructorBody(attributes);

        return new Method(modifiers, thrownExceptions, returnType, name, parameters, body);
    }

    private static Method composeConstructor(String name, List<Attribute> attributes) {
        List<Integer> modifiers = List.of(Modifier.PUBLIC);
        List<String> thrownExceptions = new ArrayList<>();
        String returnType = "";
        List<Parameter> parameters = composeParameters(attributes);
        String body = composeConstructorBody(attributes);

        return new Method(modifiers, thrownExceptions, returnType, name, parameters, body);
    }

    private static Method composeGetter(Attribute attribute) {
        boolean attributeIsPrimaryKey = attribute.getIsPrimaryKey();
        String attributeType = attribute.getType();

        if (attributeIsPrimaryKey) {
            Class<?> attributeTypeWrapperClass = ClassUtil.getWrapperClassFromPrimitiveClassString(attributeType);
            attributeType = attributeTypeWrapperClass.getSimpleName();
        }

        List<Integer> modifiers = List.of(Modifier.PUBLIC);
        List<String> thrownExceptions = new ArrayList<>();
        String returnType = attributeType;
        String attributeName = attribute.getName();
        List<Parameter> parameters = new ArrayList<>();
        String name = StringUtil.composeGetterMethodName(attributeName);
        String body = String.format("return this.%s;", attributeName);

        return new Method(modifiers, thrownExceptions, returnType, name, parameters, body);
    }

    private static List<Method> composeGetters(List<Attribute> attributes) {
        List<Method> getters = new ArrayList<>();

        for (Attribute attribute : attributes) {
            getters.add(composeGetter(attribute));
        }

        return getters;
    }

    private static Method composeSetter(Attribute attribute) {
        List<Integer> modifiers = List.of(Modifier.PUBLIC);
        List<String> thrownExceptions = new ArrayList<>();
        String returnType = "void";
        String attributeName = attribute.getName();
        Parameter parameter = new Parameter(attribute);
        List<Parameter> parameters = List.of(parameter);
        String name = StringUtil.composeSetterMethodName(attributeName);
        String body = String.format("this.%s = %s;", attributeName, attributeName);

        return new Method(modifiers, thrownExceptions, returnType, name, parameters, body);
    }

    private static List<Method> composeSetters(List<Attribute> attributes) {
        List<Method> setters = new ArrayList<>();

        for (Attribute attribute : attributes) {
            if (!attribute.getIsPrimaryKey()) {
                setters.add(composeSetter(attribute));
            }
        }

        return setters;
    }

    private static String composeToStringMethodBody(List<Attribute> attributes) {
        List<String> placeholders = new ArrayList<>();
        List<String> values = new ArrayList<>();

        for (Attribute attribute : attributes) {
            String attributeName = attribute.getName();
            String attributeColumnName = attribute.getColumnName();
            String placeholder = String.format("%s: %%s", attributeColumnName);
            String value = String.format("this.%s", attributeName);

            placeholders.add(placeholder);
            values.add(value);
        }

        String placeholdersString = String.join(", ", placeholders);
        String valuesString = String.join(", ", values);

        return """
            String className = this.getClass().getSimpleName();
            return String.format("%%s(%s)", className, %s);
        """.formatted(placeholdersString, valuesString);
    }

    private static Method composeToStringMethod(List<Attribute> attributes) {
        Annotation overrideAnnotation = new Annotation("Override");
        List<Annotation> annotations = List.of(overrideAnnotation);
        List<Integer> modifiers = List.of(Modifier.PUBLIC);
        List<String> thrownExceptions = new ArrayList<>();
        String returnType = "String";
        String name = "toString";
        List<Parameter> parameters = new ArrayList<>();
        String body = composeToStringMethodBody(attributes);

        return new Method(annotations, modifiers, thrownExceptions, returnType, name, parameters, body);
    }
}
