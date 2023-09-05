import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Git
{
    /*A Blob 
Take a file on disk and turn it into a 'blob' by... 
Creates a SHA1 String given the whole file data (hint: you can lookup and copy code to generate a SHA1 Hash as a String)
Writes a new file to disk inside an 'objects' folder
The new filename contains ONLY the SHA1 Hash
The file contains the same contents of the original file
**Optional Stretch Goal:  Saves and reads the data as zip-compressed data bytes instead of a raw text / String
Contains another function to get the generated SHA1 as a String */

    public void Blon(File file)
    {
        byte[] convertme = file.getName().getBytes();
        File newFile = new File(toSHA1(convertme));
    }

    public static String toSHA1(byte[] convertme) 
    {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
        }
        catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        } 
        return new String(md.digest(convertme));
    }
}