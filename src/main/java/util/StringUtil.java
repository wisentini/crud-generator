package util;

public class StringUtil {
    public static String decapitalize(String string) {
        if (string == null || string.length() == 0) {
            return string;
        }

        var charArray = string.toCharArray();
        var character = charArray[0];
        charArray[0] = Character.toLowerCase(character);
        var decapitalizedString = new String(charArray);

        return decapitalizedString;
    }
}
