import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Blob {
    /*
     * A Blob
     * - Take a file on disk and turn it into a 'blob' by...
     * - Creates a SHA1 String given the whole file data
     * (hint: you can lookup and copy code to generate a SHA1 Hash as a String)
     * - Writes a new file to disk inside an 'objects' folder
     * - The new filename contains ONLY the SHA1 Hash
     * - The file contains the same contents of the original file
     ** Optional Stretch Goal: Saves and reads the data as zip-compressed data bytes
     * instead of a raw text / String
     * - Contains another function to get the generated SHA1 as a String
     */

    private String fileName, sha1String;

    public Blob(String fileName) throws Exception {
        this.fileName = fileName;
        File file = new File(fileName);
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        this.sha1String = Util.hashString(readFile(fileName));
    }

    public void createFile() throws Exception {
        String content = readFile(fileName);
        String path = "objects" + File.separator + this.sha1String;
        File f = new File(path);
        f.getParentFile().mkdirs();
        f.createNewFile();
        FileWriter fw = new FileWriter(f);
        fw.write(content);
        fw.close();
    }

    public static String readFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName)); // the name of the file that want to read
        try {
            String string = "";
            while (br.ready()) {
                string += (char) br.read(); // read the char in the file, store to a string
            }
            br.close();
            return string; // return the string
        } catch (FileNotFoundException e) // if the file name is not found
        {
            return "File not found, whoops!";
        }
    }

    public String getFileName() {
        return fileName;
    }

    public String getSHA1String() {
        return sha1String;
    }
}