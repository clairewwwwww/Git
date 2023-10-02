import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Tree {
    protected HashMap<String, String> blobMap;
    protected HashSet<String> treeSet;

    public Tree() {
        blobMap = new HashMap<>();
        treeSet = new HashSet<>();
    }

    public void add(String addString) throws Exception {
        String[] splits = addString.split(" : ");

        if (splits.length == 2) {
            // Adding a tree
            if (splits[0].equals("tree")) {
                if (treeSet.contains(splits[1])) {
                    throw new Exception("Cannot add a duplicate tree");
                }
                treeSet.add(splits[1]);
                return;
            }
        } else if (splits.length == 3 ) {
            // Adding a blob
            if(splits[0].equals("blob"))
            {
                if (blobMap.containsKey(splits[2])) {
                    throw new Exception("Cannot add a blob with a duplicate filename");
                }
                blobMap.put(splits[2], splits[1]);
                return;
            }
            else if(splits[0].equals("tree"))
            {
                if(treeMap.containsKey(splits[2]))
                {
                    throw new Exception("Connot add a tree with a duplicate foldername");
                }
                treeMap.put(splits[2], splits[1]);
                return;
            }
        throw new Exception("Invalid add format");
        }
    }

    public boolean remove(String key) {
        if (blobMap.containsKey(key)) {
            blobMap.remove(key);
            return true;
        } else if (treeSet.contains(key)) {
            treeSet.remove(key);
            return true;
        }
        return false;
    }

    public void writeToFile() throws IOException, NoSuchAlgorithmException {
        StringBuilder builder = new StringBuilder();

        for (Map.Entry<String, String> entry : blobMap.entrySet()) {
            builder.append("blob : " + entry.getValue() + " : " + entry.getKey() + "\n");
        }

        for (String hash : treeSet.toArray(new String[treeSet.size()])) {
            builder.append("tree : " + hash + "\n");
        }

        builder.deleteCharAt(builder.length() - 1);

        String result = builder.toString();
        System.out.println(result);
        Util.writeFile("objects/" + Util.hashString(result), result);
    }

    public String getSha1(String input) throws IOException, NoSuchAlgorithmException {
        return Util.hashString(input);
    }

    public String addDirectory(String directoryPath) throws Exception
    {
        String SHA1 = ""; //SHA1 - the saved Tree location in the objects folder
        File directory = new File("directoryPath");
        if(!directory.isDirectory())
        {
            throw new Exception("Invalid directory path");
        }
        for(File fileEntry : directory.listFiles())
        {
            if(fileEntry.isDirectory())
            {
                String folderName = fileEntry.getName();
                String treeEntry = "tree : " + getSha1(Blob.readFile(fileEntry.getName()));
                add(treeEntry);
                addDirectory(folderName);
            }
            else
            {
                String fileName = fileEntry.getName();
                Blob blob = new Blob (fileName);
                blob.createFile();
                String blobEntry = "blob : " + getSha1(Blob.readFile(fileName)) + " : " + fileName;
                add(blobEntry); 
            }
        }
        return SHA1;
    }

    /* 
    public void listFilesForFolder(final File folder) 
    {
        for (final File fileEntry : folder.listFiles()) 
        {
            if (fileEntry.isDirectory()) 
            {
                listFilesForFolder(fileEntry);
            } 
            else 
            {
                System.out.println(fileEntry.getName()); //getPath;
            }
        }
    }*/
}
    //final File folder = new File("/home/you/Desktop");
    //listFilesForFolder(folder);
