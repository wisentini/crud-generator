package util;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;

public class FormatterUtil {
    public static String format(String sourceCode) {
        try {
            var formatter = new Formatter();
            var formattedSourceCode = formatter.formatSource(sourceCode);
            return formattedSourceCode;
        } catch (FormatterException formatterException) {
            var message = formatterException.getMessage();
            System.err.println(message);
        }

        return sourceCode;
    }
}
