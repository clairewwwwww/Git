import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

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

    public void createFile() throws IOException 
    {
        String path = "objects" + File.separator + getSHA1String();
        File f = new File(path);
        f.getParentFile().mkdirs(); 
        f.createNewFile();
        FileWriter fw = new FileWriter(f);
        fw.write(readFile(fileName));
        fw.close();
    }
    

    public String convertToSHA1(String fileName)
    {
        String sha1 = "";
        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(fileName.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return sha1;
    }
    
    private static String byteToHex(final byte[] hash)
    {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
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

    public String getFileName()
    {
        return fileName;
    }

    public String getSHA1String()
    {
        return convertToSHA1(fileName);
    }
 }