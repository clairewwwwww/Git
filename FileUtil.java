import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {
    public static void writeFile(String path, String content) throws IOException {
        FileWriter writer = new FileWriter(path, false);
        writer.write(content);
        writer.close();
    }

    public static String readFile(String path) throws IOException {
        return Files.readString(Paths.get(path));
    }

    public static boolean exists(String path) {
        return new File(path).exists();
    }

    public static boolean deleteDirectory(String path) throws IOException {
        File directory = new File(path);
        File[] directoryFiles = directory.listFiles();
        if (directoryFiles != null) {
            for (File file : directoryFiles) {
                deleteDirectory(file.getPath());
            }
        }
        return directory.delete();
    }

    public static void deleteFile(String path) throws IOException {
        Files.deleteIfExists(Paths.get(path));
    }

    public static void createFile(String path) throws IOException {
        Files.createFile(Paths.get(path));
    }
}
