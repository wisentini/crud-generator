package util;

import exception.FileException;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class PropertiesUtil {
    public static final String PATHNAME = "config/database.properties";

    public static Properties getProperties() throws FileException {
        try {
            File file = FileUtil.loadFile(PATHNAME);
            FileInputStream inputStream = new FileInputStream(file);
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        } catch (Exception exception) {
            var message = "\nCouldn't load properties file.\n";
            throw new FileException(message);
        }
    }
}
