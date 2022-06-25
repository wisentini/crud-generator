package util;

import java.lang.invoke.MethodType;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class ClassUtil {
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getWrapperClass(Class<T> clazz) {
        var wrapperClass = (Class<T>) MethodType.methodType(clazz).wrap().returnType();
        return wrapperClass;
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> getPrimitiveClass(Class<T> clazz) {
        var primitiveClass = (Class<T>) MethodType.methodType(clazz).unwrap().returnType();
        return primitiveClass;
    }

    public static Class<?> getWrapperClassFromPrimitiveClassString(String primitiveClassString) {
        var wrapperClass = switch (primitiveClassString) {
            case "String" -> String.class;
            case "BigDecimal" -> BigDecimal.class;
            case "boolean" -> Boolean.class;
            case "byte" -> Byte.class;
            case "short" -> Short.class;
            case "int" -> Integer.class;
            case "long" -> Long.class;
            case "float" -> Float.class;
            case "double" -> Double.class;
            case "byte[]" -> Byte[].class;
            case "Date" -> Date.class;
            case "Time" -> Time.class;
            case "Timestamp" -> Timestamp.class;
            default -> Object.class;
        };

        return wrapperClass;
    }

    public static Class<?> getPrimitiveClassFromWrapperClassString(String wrapperClassString) {
        var primitiveClass = switch (wrapperClassString) {
            case "String" -> String.class;
            case "BigDecimal" -> BigDecimal.class;
            case "Boolean" -> boolean.class;
            case "Byte" -> byte.class;
            case "Short" -> short.class;
            case "Integer" -> int.class;
            case "Long" -> long.class;
            case "Float" -> float.class;
            case "Double" -> double.class;
            case "Byte[]" -> byte[].class;
            case "Date" -> Date.class;
            case "Time" -> Time.class;
            case "Timestamp" -> Timestamp.class;
            default -> Object.class;
        };

        return primitiveClass;
    }
}
