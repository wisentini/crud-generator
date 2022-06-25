package util;

import exception.FileException;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileUtil {
    public static File loadFile(String pathname) {
        var directoryPathname = FilenameUtils.getFullPathNoEndSeparator(pathname);
        var directory = new File(directoryPathname);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        var file = new File(pathname);
        return file;
    }

    public static String getNameFrom(String pathname) {
        var file = loadFile(pathname);
        var name = file.getName();
        return name;
    }

    public static void writeTo(String content, String pathname) throws FileException {
        Writer writer = null;

        try {
            var file = loadFile(pathname);
            var fileOutputStream = new FileOutputStream(file);
            var outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
            writer = new BufferedWriter(outputStreamWriter);
            writer.write(content);
        } catch (FileNotFoundException | SecurityException e) {
            var message = String.format("Couldn't create \"%s\".\n", pathname);
            throw new FileException(message);
        } catch (IOException e) {
            var message = String.format("Couldn't write to \"%s\".\n", pathname);
            throw new FileException(message);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    var message = String.format("\nCouldn't close \"%s\".", pathname);
                    System.err.println(message);
                }
            }
        }
    }
}
