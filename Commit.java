import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;

public class Commit {
    private LinkedList<File> list;
    private File commit;
    private String prevCommit;
    private String treeSha;

    public Commit(String prevCommit, String author, String summary) throws Exception {
        this.prevCommit = prevCommit;
        commit = new File("Commit");
        PrintWriter pw = new PrintWriter(commit);
        Tree tree = new Tree();
        treeSha = tree.getSha1(Blob.readFile("index"));
        String date = getDate();
        pw.print(treeSha + '\n' + prevCommit + '\n' + '\n' + author + '\n' + date + '\n' + summary);
        pw.close();

        String newEntry = "tree : " + tree.getSha1(Blob.readFile("Commit"));
        tree.add(newEntry);
        tree.writeToFile();
    }

    

    public void updatePrevious() throws IOException
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
            content += treeSha;
            while(br.ready())
            {
                content += br.readLine();
            }
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