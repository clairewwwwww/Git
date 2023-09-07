import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
    public Index()
    {

    }

    public void initialize(String fileName)
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
        Blob newBlob = new Blob(fileName);
        newBlob.move();
    }
}
