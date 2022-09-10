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
        return DriverManager.getConnection(databaseURL);
    }

    public static Class<?> convertSQLTypeToClass(int type) {
        return switch (type) {
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
    }

    private static String generateRandomString(int size) {
        int availableCharactersLength = AVAILABLE_CHARACTERS.length();

        SecureRandom random = new SecureRandom();
        int finalSize = size;

        if (size <= 0) {
            throw new IllegalArgumentException("String size must be a positive integer.");
        } else if (size > GOOD_STRING_SIZE) {
            finalSize = random.nextInt(MIN_STRING_SIZE, MAX_STRING_SIZE);
        }

        StringBuilder stringBuilder = new StringBuilder(finalSize);

        stringBuilder.append('"');

        for (int i = 0; i < finalSize; i++) {
            int randomIndex = random.nextInt(availableCharactersLength);
            char randomCharacter = AVAILABLE_CHARACTERS.charAt(randomIndex);
            stringBuilder.append(randomCharacter);
        }

        stringBuilder.append('"');

        return stringBuilder.toString();
    }

    public static String generateValue(String type, int size) {
        SecureRandom random = new SecureRandom();

        return switch (type) {
            case "int", "Integer" -> {
                int randomInt = random.nextInt(size);
                yield String.valueOf(randomInt);
            }
            case "long", "Long" -> {
                long randomLong = random.nextLong(size);
                yield String.valueOf(randomLong);
            }
            case "boolean", "Boolean" -> {
                boolean randomBoolean = random.nextBoolean();
                yield String.valueOf(randomBoolean);
            }
            case "float", "Float" -> {
                float randomFloat = random.nextFloat(size);
                yield String.valueOf(randomFloat);
            }
            case "double", "Double" -> {
                double randomDouble = random.nextDouble(size);
                yield String.valueOf(randomDouble);
            }
            case "char", "Character", "String" -> generateRandomString(size);
            default -> null;
        };
    }
}
