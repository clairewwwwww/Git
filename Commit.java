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

    public Commit(String prevCommit, String author, String summary) throws NoSuchAlgorithmException, IOException {
        commit = new File("Commit");
        PrintWriter pw = new PrintWriter(commit);
        Tree tree = new Tree();
        String treeSha = tree.getSha1();
        String date = getDate();
        pw.print(treeSha + '\n' + prevCommit + '\n' + '\n' + author + '\n' + date + '\n' + summary);
        pw.close();
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