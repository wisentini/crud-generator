package util;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.sql.*;

public class DatabaseUtil {
    private static final int MIN_STRING_SIZE = 2;
    private static final int GOOD_STRING_SIZE = 15;
    private static final int MAX_STRING_SIZE = 31;

    private static final String AVAILABLE_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!#$%&*?";

    public static Connection getConnection(String databaseURL) throws SQLException {
        var connection = DriverManager.getConnection(databaseURL);
        return connection;
    }

    public static Class<?> convertSQLTypeToClass(int type) {
        var typeClass = switch (type) {
            case Types.CHAR, Types.VARCHAR, Types.LONGVARCHAR -> String.class;
            case Types.NUMERIC, Types.DECIMAL -> BigDecimal.class;
            case Types.BIT -> boolean.class;
            case Types.TINYINT -> byte.class;
            case Types.SMALLINT -> short.class;
            case Types.INTEGER -> int.class;
            case Types.BIGINT -> long.class;
            case Types.REAL, Types.FLOAT -> float.class;
            case Types.DOUBLE -> double.class;
            case Types.BINARY, Types.VARBINARY, Types.LONGVARBINARY -> byte[].class;
            case Types.DATE -> Date.class;
            case Types.TIME -> Time.class;
            case Types.TIMESTAMP -> Timestamp.class;
            default -> Object.class;
        };

        return typeClass;
    }

    private static String generateRandomString(int size) {
        var availableCharactersLength = AVAILABLE_CHARACTERS.length();

        var random = new SecureRandom();
        var finalSize = size;

        if (size <= 0) {
            throw new IllegalArgumentException("String size must be a positive integer.");
        } else if (size > GOOD_STRING_SIZE) {
            finalSize = random.nextInt(MIN_STRING_SIZE, MAX_STRING_SIZE);
        }

        var stringBuilder = new StringBuilder(finalSize);

        stringBuilder.append('"');

        for (int i = 0; i < finalSize; i++) {
            var randomIndex = random.nextInt(availableCharactersLength);
            var randomCharacter = AVAILABLE_CHARACTERS.charAt(randomIndex);
            stringBuilder.append(randomCharacter);
        }

        stringBuilder.append('"');

        var stringBuilderString = stringBuilder.toString();
        return stringBuilderString;
    }

    public static String generateValue(String type, int size) {
        var random = new SecureRandom();

        return switch (type) {
            case "int", "Integer" -> {
                var randomInt = random.nextInt(size);
                var randomIntString = String.valueOf(randomInt);
                yield randomIntString;
            }
            case "long", "Long" -> {
                var randomLong = random.nextLong(size);
                var randomLongString = String.valueOf(randomLong);
                yield randomLongString;
            }
            case "boolean", "Boolean" -> {
                var randomBoolean = random.nextBoolean();
                var randomBooleanString = String.valueOf(randomBoolean);
                yield randomBooleanString;
            }
            case "float", "Float" -> {
                var randomFloat = random.nextFloat(size);
                var randomFloatString = String.valueOf(randomFloat);
                yield randomFloatString;
            }
            case "double", "Double" -> {
                var randomDouble = random.nextDouble(size);
                var randomDoubleString = String.valueOf(randomDouble);
                yield randomDoubleString;
            }
            case "char", "Character", "String" -> {
                var randomString = generateRandomString(size);
                yield randomString;
            }
            default -> null;
        };
    }
}
