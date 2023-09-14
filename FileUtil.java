import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {
    public static void writeToFile(String path, String content) throws IOException {
        FileWriter writer = new FileWriter(path, false);
        writer.write(content);
        writer.close();
    }

    public static void deleteFile(String path) throws IOException {
        Files.deleteIfExists(Paths.get(path));
    }

    public static void createFile(String path) throws IOException {
        Files.createFile(Paths.get(path));
    }
}
