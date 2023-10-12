import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Tree {
    protected HashMap<String, String> blobMap;
    protected HashMap<String, String> treeMap;
    protected HashSet<String> treeSet;
    //private String content;

    public Tree() {
        blobMap = new HashMap<>();
        treeSet = new HashSet<>();
        treeMap = new HashMap<>();
        //content = "";
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

        for (Map.Entry<String, String> entry1 : blobMap.entrySet()) {
            builder.append("blob : " + entry1.getValue() + " : " + entry1.getKey() + "\n");
        }

        for (String hash : treeSet.toArray(new String[treeSet.size()])) {
            builder.append("tree : " + hash + "\n");
        }

        for (Map.Entry<String, String> entry2 : treeMap.entrySet()) {
            builder.append("tree : " + entry2.getValue() + " : " + entry2.getKey() + "\n");
        }

        builder.deleteCharAt(builder.length() - 1);

        String result = builder.toString();
        String finalSha = Util.hashString(result);
        //content = result;
        System.out.println(result);
        
        Util.writeFile("objects/" + finalSha, result);
    }

    public String getSha1(String input) throws IOException, NoSuchAlgorithmException {
        return Util.hashString(input);
    }

    public String addDirectory(String directoryPath) throws NoSuchAlgorithmException, Exception
    {
        String content = addDirectoryHelper(directoryPath);
        
        //empty folder
        if(content.equals(""))
        {
            String emptyStringShaName = getSha1("");
            File file = new File("objects/" + emptyStringShaName);
            try (FileWriter fw = new FileWriter(file)) 
            {
                fw.write("");
                fw.close();
            }
            PrintWriter pw = new PrintWriter(new FileWriter("index", true));
            BufferedReader br = new BufferedReader(new FileReader("index")); 
            if (br.readLine() != null) 
            {
                pw.append("\n");
            } 
            String treeEntry = "tree : " + emptyStringShaName + " : " + directoryPath;
            pw.append(treeEntry);
            pw.close();
            br.close();
            add(treeEntry);
            content = treeEntry;
        }
        return getSha1(content);
    }

    public String addDirectoryHelper(String directoryPath) throws Exception
    {
        String content = ""; //SHA1 - the saved Tree location in the objects folder
        File directory = new File(directoryPath);
        if(!directory.isDirectory())
        {
            throw new Exception("Invalid directory path");
        }
        Index index = new Index();
        File [] dircetoryList = directory.listFiles();
        //go through everything in the folder
        for(File fileEntry : dircetoryList)
        { 
            //if the entry is a folder
            if(fileEntry.isDirectory())
            {
                String temp = "";
                String folderName = directoryPath + "/" + fileEntry.getName();
                String fileContent = addDirectoryHelper(folderName);
                String SHA = Util.hashString(fileContent);
                File tree = new File("objects/" + SHA);
                try (FileWriter fw = new FileWriter(tree)) 
                {
                    fw.write(fileContent);
                    fw.close();
                }
                PrintWriter pw = new PrintWriter(new FileWriter("index", true));
                BufferedReader br = new BufferedReader(new FileReader("index")); 
                if (br.readLine() != null) 
                {
                    pw.append("\n");
                } 
                String treeEntry = "tree : " + SHA + " : " + folderName;

                pw.append(treeEntry);
                pw.close();
                br.close();
                add(treeEntry);
                content += treeEntry + "\n";  
            }
            //if the entry is a file
            else
            {
                String fileName = directoryPath + "/" + fileEntry.getName();
                index.addBlob(fileName);
                String blobEntry = "blob : " + getSha1(Util.readFile(fileName)) + " : " + fileName;
                add(blobEntry);
                content += blobEntry + "\n";
            }
        }
        if(content != "")
        {
            content = content.substring(0, content.length() - 1);
        }
        return content;
    }
}
