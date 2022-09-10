package util;

public class StringUtil {
    public static String capitalize(String string) {
        if (string == null || string.length() == 0) {
            return string;
        }

        char[] charArray = string.toCharArray();
        char character = charArray[0];
        charArray[0] = Character.toUpperCase(character);

        return new String(charArray);
    }

    public static String composeGetterMethodName(String attribute) {
        String lowerCaseAttribute = attribute.toLowerCase();
        String attributeCapitalized = capitalize(lowerCaseAttribute);
        return String.format("get%s", attributeCapitalized);
    }

    public static String composeSetterMethodName(String attribute) {
        String lowerCaseAttribute = attribute.toLowerCase();
        String attributeCapitalized = capitalize(lowerCaseAttribute);
        return String.format("set%s", attributeCapitalized);
    }
}
