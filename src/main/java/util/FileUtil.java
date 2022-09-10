package util;

import exception.FileException;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileUtil {
    public static File loadFile(String pathname) {
        String directoryPathname = FilenameUtils.getFullPathNoEndSeparator(pathname);
        File directory = new File(directoryPathname);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        return new File(pathname);
    }

    public static String getNameFrom(String pathname) {
        File file = loadFile(pathname);
        return file.getName();
    }

    public static void writeTo(String content, String pathname) throws FileException {
        Writer writer = null;

        try {
            File file = loadFile(pathname);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
            writer = new BufferedWriter(outputStreamWriter);
            writer.write(content);
        } catch (FileNotFoundException | SecurityException e) {
            String message = String.format("Couldn't create \"%s\".\n", pathname);
            throw new FileException(message);
        } catch (IOException e) {
            String message = String.format("Couldn't write to \"%s\".\n", pathname);
            throw new FileException(message);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    String message = String.format("\nCouldn't close \"%s\".", pathname);
                    System.err.println(message);
                }
            }
        }
    }
}
