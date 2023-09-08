import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

public class Index
{
    /*Can "initialize" or "init" a project which
        If the file does not yet exist:
            creates an empty file named 'index'
        If the folder does not yet exist:
            creates a directory named 'objects'
    Can add Blobs given a filename
        Creates a Blob for the given filename using the Blob behavior above
        Generates an entry for the original filename and Blob'd SHA1 as key/value pairs
        The pairs must be delimited by " : " 
            Appends the pair of original filename / SHA1 on a unique line in a file named 'index'
    Can remove Blobs given a filename
        Removes the filename and Blob SHA1 from the key/value pair */
    public Index(String fileName)
    {
        initialize(fileName);
    }

    private void initialize(String fileName)
    {
        File file = new File(fileName);

		if (!file.exists()) 
		{
			File newFile = new File("index");
		}

        new File("objects").mkdirs();
    }

    public void addBlob(String fileName) throws IOException
    {
        Blob blob = new Blob(fileName);
        blob.createFile();
        
        PrintWriter pw = new PrintWriter("index");
        pw.println(namePairs(fileName));
        pw.close();
    }

    public void removeBlob(String fileName) throws Exception

    {
        File file = new File(fileName);
        Blob blob = new Blob(fileName);
        if (!file.exists()) 
        {
            throw new Exception("file not found");
        }
        else
        {
            File doomedFile = new File ("objects", blob.getSHA1String());
            doomedFile.delete();
            removeLine(namePairs(fileName));
        }
    }
    private void removeLine(String lineContent) throws IOException
    {
        File file = new File("Index");
        File temp = new File("_temp_");
        PrintWriter out = new PrintWriter(new FileWriter(temp));
        Files.lines(file.toPath())
        .filter(line -> !line.contains(lineContent))
        .forEach(out::println);
        out.flush();
        out.close();
        temp.renameTo(file);
    }
    private String namePairs(String fileName) throws IOException
    {
        Blob blob = new Blob(fileName);
        return blob.getFileName() + ": " + blob.getSHA1String();
    }
}
