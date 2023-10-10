import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;

public class Commit {
    //private static final String Util = null;
    private LinkedList<File> list;
    //private File commit;
    private String prevCommit;
    private String current;
    private String  treeSha;

    public Commit(String prevCommit, String author, String summary) throws Exception {
        this.prevCommit = prevCommit;
        File commit = new File("Commit");
        PrintWriter pw = new PrintWriter(new FileWriter(commit, false));
        //Tree tree = new Tree();
        treeSha = createTree();
        String date = getDate();
        current = treeSha + '\n' + prevCommit + '\n' + null + '\n' + author + '\n' + date + '\n' + summary;
        pw.print(current);
        pw.close();

        File newFile = new File("objects/" + Util.hashString(current));
        commit.renameTo(newFile);
    }

    public String createTree() throws IOException, Exception
    {
        Tree tree = new Tree();
        String content = Util.readFile("index");
        if(content != "")
        {
            content = content.substring(0, content.length() - 1);
        }
        if(prevCommit != null)
        {
            content += "\ntree : " + prevCommit;
        }
        String fileName = "objects/" + Util.hashString(content);
        File file = new File(fileName);
        PrintWriter pw = new PrintWriter(new FileWriter(file), false);
        pw.write(content);
        pw.close();

        String SHA = Util.hashString(content);

        Index index = new Index();
        index.addBlob(fileName);
        /* 
        Tree tree = new Tree();
        BufferedReader br = new BufferedReader(new FileReader("index"));
        String content = "";
        while(br.ready())
        {
            tree.add(br.readLine());
            content += br.readLine();
        }
        br.close();
        if(prevCommit != null)
        {
            tree.add("tree : " + prevCommit);
            content += "\ntree : " + prevCommit;
        }
        tree.writeToFile();*/

        //Util.writeFile(SHA, content);
        PrintWriter overwrite = new PrintWriter(new FileWriter("index"), false);
        overwrite.write("");
        overwrite.close();
        return SHA;

    }

    public void updatePreviousCommit() throws IOException, NoSuchAlgorithmException
    {
        String path = "objects/" + prevCommit;
        File file = new File(path);
        String content = "";
        try (BufferedReader br = new BufferedReader(new FileReader(file));)
        {
            for(int i = 0; i < 2; i++)
            {
                content += br.readLine();
            }
            content += Util.hashString(current);
            while(br.ready())
            {
                content += br.readLine();
            }
            br.close();
        } 
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }      
        Util.writeFile(path, content);
    }

    public String getTreeFromSHA1(String SHA1ofCommit) throws IOException
    {
        File file = new File("objects", SHA1ofCommit);
        BufferedReader firstLine = new BufferedReader(new FileReader(file));
        String line = firstLine.readLine();
        firstLine.close();
        return line;
    }

    public String generateSha1(File f) throws NoSuchAlgorithmException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(f));
        int line = 0;
        StringBuilder sb = new StringBuilder();

        while(br.ready()) {
            if(line == 0)
                sb.append(br.readLine());
            if(line != 3)
                sb.append('\n' + br.readLine());
            line++;
        }

        String str = sb.toString();
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] ret = (md.digest(str.getBytes()));
        return byteArrayToHexString(ret);
    }

    public String byteArrayToHexString(byte[] b) {
        String result = "";
        for (int i=0; i < b.length; i++) {
          result +=
                Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return result;
    }

    public String getDate() {
        java.util.Date date = new java.util.Date();
        String d = date.toString();
        String ret = d.substring(4, 10) + ", " + d.substring(24);
        return ret;
    }
}