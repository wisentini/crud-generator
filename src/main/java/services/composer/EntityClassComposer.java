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

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class EntityClassComposer {
    public static EntityClass composeClass(TableMetadata tableMetadata, String classPackage) {
        var attributes = composeAttributes(tableMetadata);
        var className = tableMetadata.getName();
        var methods = composeMethods(className, attributes);
        var annotations = composeClassAnnotations(className);

        var imports = List.of("javax.persistence.*");
        var modifiers = List.of(Modifier.PUBLIC);
        var primaryKeyColumn = tableMetadata.getPrimaryKeyColumn();
        var primaryKeyColumnClass = primaryKeyColumn.getTypeClass();
        var wrapperClass = ClassUtil.getWrapperClass(primaryKeyColumnClass);
        var wrapperClassName = wrapperClass.getSimpleName();
        var classesToExtend = new ArrayList<String>();
        var baseEntityInterface = String.format("BaseEntity<%s>", wrapperClassName);
        var classesToImplement = List.of(baseEntityInterface);

        var entityClass = new EntityClass(imports, classPackage, annotations, modifiers, className, attributes, methods, classesToExtend, classesToImplement, primaryKeyColumnClass);
        return entityClass;
    }

    public static List<Annotation> composeClassAnnotations(String className) {
        var entityAnnotation = new Annotation("Entity");

        var tableAnnotationNameElementName = "name";
        var tableAnnotationNameElementValue = String.format("\"`%s`\"", className);
        var tableAnnotationNameElement = new AnnotationElement(tableAnnotationNameElementName, tableAnnotationNameElementValue);

        var tableAnnotationSchemaElementName = "schema";
        var tableAnnotationSchemaElementValue = "\"public\"";
        var tableAnnotationSchemaElement = new AnnotationElement(tableAnnotationSchemaElementName, tableAnnotationSchemaElementValue);

        var tableAnnotationElements = List.of(tableAnnotationNameElement, tableAnnotationSchemaElement);

        var tableAnnotation = new Annotation("Table", tableAnnotationElements);

        var annotations = List.of(entityAnnotation, tableAnnotation);
        return annotations;
    }

    private static List<Method> composeMethods(String className, List<Attribute> attributes) {
        var constructor = composeConstructor(className, attributes);

        var emptyConstructorAttributes = new ArrayList<Attribute>();
        var emptyConstructor = composeConstructor(className, emptyConstructorAttributes);

        var partialConstructor = composePartialConstructor(className, attributes);

        var getters = composeGetters(attributes);
        var setters = composeSetters(attributes);

        var toStringMethod = composeToStringMethod(attributes);

        var methods = new ArrayList<Method>();

        methods.add(emptyConstructor);
        methods.add(partialConstructor);
        methods.add(constructor);
        methods.add(toStringMethod);
        methods.addAll(getters);
        methods.addAll(setters);

        return methods;
    }

    private static Attribute composeAttribute(ColumnMetadata columnMetadata) {
        var columnName = columnMetadata.getName();
        var columnType = columnMetadata.getTypeClassName();
        var columnSize = columnMetadata.getSize();
        var columnIsPrimaryKey = columnMetadata.getIsPrimaryKey();

        var attributeName = columnName;

        var columnAnnotationElementName = "name";
        var columnAnnotationElementValue = String.format("\"`%s`\"", columnName);
        var columnAnnotationElement = new AnnotationElement(columnAnnotationElementName, columnAnnotationElementValue);
        var columnAnnotationName = "Column";
        var columnAnnotationElements = List.of(columnAnnotationElement);
        var columnAnnotation = new Annotation(columnAnnotationName, columnAnnotationElements);

        var annotations = new LinkedList<>(List.of(columnAnnotation));
        var modifiers = List.of(Modifier.PRIVATE);

        if (columnIsPrimaryKey) {
            var generatedValueElementName = "strategy";
            var generatedValueElementValue = "GenerationType.IDENTITY";
            var generatedValueElement = new AnnotationElement(generatedValueElementName, generatedValueElementValue);
            var generatedValueAnnotationName = "GeneratedValue";
            var generatedValueElements = List.of(generatedValueElement);
            var generatedValueAnnotation = new Annotation(generatedValueAnnotationName, generatedValueElements);
            annotations.addFirst(generatedValueAnnotation);

            var idAnnotationName = "Id";
            var idAnnotation = new Annotation(idAnnotationName);
            annotations.addFirst(idAnnotation);

            attributeName = "id";
        }

        var attribute = new Attribute(attributeName, columnType, columnSize, columnIsPrimaryKey, annotations, modifiers, columnName);
        return attribute;
    }

    private static List<Attribute> composeAttributes(TableMetadata tableMetadata) {
        var columnsMetadata = tableMetadata.getColumnsMetadata();
        var attributes = new ArrayList<Attribute>();

        for (var columnMetadata : columnsMetadata) {
            var attribute = composeAttribute(columnMetadata);
            attributes.add(attribute);
        }

        return attributes;
    }

    private static String composeConstructorBody(List<Attribute> attributes) {
        var constructorBodyBuilder = new StringBuilder();

        for (var attribute : attributes) {
            var attributeName = attribute.getName();
            var attributeColumnName = attribute.getColumnName();
            var constructorBody = "";

            if (attribute.getIsPrimaryKey()) {
                constructorBody = String.format("this.id = %s;\n", attributeColumnName);
            } else {
                constructorBody = String.format("this.%s = %s;\n", attributeName, attributeName);
            }

            constructorBodyBuilder.append(constructorBody);
        }

        var constructorBody = constructorBodyBuilder.toString();
        return constructorBody;
    }

    private static String composePartialConstructorBody(List<Attribute> attributes) {
        var constructorBodyBuilder = new StringBuilder();

        for (var attribute : attributes) {
            var attributeName = attribute.getName();
            var attributeIsPrimaryKey = attribute.getIsPrimaryKey();
            var constructorBody = "";

            if (!attributeIsPrimaryKey) {
                constructorBody = String.format("this.%s = %s;\n", attributeName, attributeName);
            }

            constructorBodyBuilder.append(constructorBody);
        }

        var constructorBody = constructorBodyBuilder.toString();
        return constructorBody;
    }

    private static List<Parameter> composeParameters(List<Attribute> attributes) {
        var parameters = new ArrayList<Parameter>();

        for (var attribute : attributes) {
            var attributeColumnName = attribute.getColumnName();
            var attributeType = attribute.getType();
            var attributeIsPrimaryKey = attribute.getIsPrimaryKey();
            var parameter = new Parameter(attributeColumnName, attributeType, attributeIsPrimaryKey);
            parameters.add(parameter);
        }

        return parameters;
    }

    private static Method composePartialConstructor(String name, List<Attribute> attributes) {
        var modifiers = List.of(Modifier.PUBLIC);
        var thrownExceptions = new ArrayList<String>();
        var returnType = "";
        var parameters = composeParameters(attributes).stream().filter(parameter -> !parameter.getIsPrimaryKey()).toList();
        var body = composePartialConstructorBody(attributes);

        var method = new Method(modifiers, thrownExceptions, returnType, name, parameters, body);
        return method;
    }

    private static Method composeConstructor(String name, List<Attribute> attributes) {
        var modifiers = List.of(Modifier.PUBLIC);
        var thrownExceptions = new ArrayList<String>();
        var returnType = "";
        var parameters = composeParameters(attributes);
        var body = composeConstructorBody(attributes);

        var method = new Method(modifiers, thrownExceptions, returnType, name, parameters, body);
        return method;
    }

    private static Method composeGetter(Attribute attribute) {
        var attributeIsPrimaryKey = attribute.getIsPrimaryKey();
        var attributeType = attribute.getType();

        if (attributeIsPrimaryKey) {
            var attributeTypeWrapperClass = ClassUtil.getWrapperClassFromPrimitiveClassString(attributeType);
            attributeType = attributeTypeWrapperClass.getSimpleName();
        }

        var modifiers = List.of(Modifier.PUBLIC);
        var thrownExceptions = new ArrayList<String>();
        var returnType = attributeType;
        var attributeName = attribute.getName();
        var parameters = new ArrayList<Parameter>();
        var name = String.format("get%s", attributeName);
        var body = String.format("return this.%s;", attributeName);

        var method = new Method(modifiers, thrownExceptions, returnType, name, parameters, body);
        return method;
    }

    private static List<Method> composeGetters(List<Attribute> attributes) {
        var getters = new ArrayList<Method>();

        for (var attribute : attributes) {
            var getter = composeGetter(attribute);
            getters.add(getter);
        }

        return getters;
    }

    private static Method composeSetter(Attribute attribute) {
        var modifiers = List.of(Modifier.PUBLIC);
        var thrownExceptions = new ArrayList<String>();
        var returnType = "void";
        var attributeName = attribute.getName();
        var parameter = new Parameter(attribute);
        var parameters = List.of(parameter);
        var name = String.format("set%s", attributeName);
        var body = String.format("this.%s = %s;", attributeName, attributeName);

        var method = new Method(modifiers, thrownExceptions, returnType, name, parameters, body);
        return method;
    }

    private static List<Method> composeSetters(List<Attribute> attributes) {
        var setters = new ArrayList<Method>();

        for (var attribute : attributes) {
            if (attribute.getIsPrimaryKey()) {
                continue;
            }

            var setter = composeSetter(attribute);
            setters.add(setter);
        }

        return setters;
    }

    private static String composeToStringMethodBody(List<Attribute> attributes) {
        var placeholders = new ArrayList<String>();
        var values = new ArrayList<String>();

        for (var attribute : attributes) {
            var attributeName = attribute.getName();
            var attributeColumnName = attribute.getColumnName();
            var placeholder = String.format("%s: %%s", attributeColumnName);
            var value = String.format("this.%s", attributeName);

            placeholders.add(placeholder);
            values.add(value);
        }

        var placeholdersString = String.join(", ", placeholders);
        var valuesString = String.join(", ", values);

        var toStringMethodBody = """
                var className = this.getClass().getSimpleName();
                var string = String.format("%%s(%s)", className, %s);
                return string;
                """.formatted(placeholdersString, valuesString);

        return toStringMethodBody;
    }

    private static Method composeToStringMethod(List<Attribute> attributes) {
        var overrideAnnotation = new Annotation("Override");
        var annotations = List.of(overrideAnnotation);
        var modifiers = List.of(Modifier.PUBLIC);
        var thrownExceptions = new ArrayList<String>();
        var returnType = "String";
        var name = "toString";
        var parameters = new ArrayList<Parameter>();
        var body = composeToStringMethodBody(attributes);

        var method = new Method(annotations, modifiers, thrownExceptions, returnType, name, parameters, body);
        return method;
    }
}
