package util;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import com.google.googlejavaformat.java.JavaFormatterOptions;

public class FormatterUtil {
    public static String format(String sourceCode) {
        try {
            JavaFormatterOptions javaFormatterOptions = JavaFormatterOptions.builder().style(JavaFormatterOptions.Style.AOSP).build();
            Formatter formatter = new Formatter(javaFormatterOptions);
            return formatter.formatSourceAndFixImports(sourceCode);
        } catch (FormatterException formatterException) {
            System.err.println(formatterException.getMessage());
        }

        return sourceCode;
    }
}
