import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Blob
{
     /*A Blob 
    - Take a file on disk and turn it into a 'blob' by... 
        - Creates a SHA1 String given the whole file data 
                (hint: you can lookup and copy code to generate a SHA1 Hash as a String)
        - Writes a new file to disk inside an 'objects' folder
            - The new filename contains ONLY the SHA1 Hash
            - The file contains the same contents of the original file
            **Optional Stretch Goal:  Saves and reads the data as zip-compressed data bytes instead of a raw text / String
        - Contains another function to get the generated SHA1 as a String */

    private String fileName;

    public Blob(String fileName) throws IOException
    {
        this.fileName = fileName;
        File file = new File(fileName);
		if (!file.exists()) 
		{
			throw new FileNotFoundException();
		}
    }

    public void writeToNewFile() throws IOException
    {
        writeFile(convertToSHA1(fileName), readFile(fileName));
    }

    public String convertToSHA1(String fileName) 
    {
        byte[] convertme = fileName.getBytes();

        MessageDigest md = null;
        try 
        {
            md = MessageDigest.getInstance("SHA-1");
        }
        catch(NoSuchAlgorithmException e) 
        {
            e.printStackTrace();
        } 
        return new String(md.digest(convertme));
    }

	private void writeFile(String fileName, String inputContent) throws IOException // inputContent = what the user want to write to the														// file
	{
		PrintWriter pw = new PrintWriter("This is the output file name"); // the name of the output file
		pw.print(inputContent); // print the content
		pw.close();

	}

	private String readFile(String fileName) throws IOException 
    {
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

    public String getStringFileName()
    {
        return fileName;
    }
 }


   