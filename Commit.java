import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedList;

public class Commit {
    //private static final String Util = null;
    //private File commit;
    private String prevCommit;
    private String currentContent;
    private String treeSha;
    private String head;
    private ArrayList<String> addFiles;
    private ArrayList<String> deleteFiles;
    private ArrayList<String> editFiles;

    public Commit(String prevCommit, String author, String summary) throws Exception {
       this.prevCommit = prevCommit;
        addFiles = new ArrayList<String>();
        deleteFiles = new ArrayList<String>();
        editFiles = new ArrayList<String>();
        File commit = new File("Commit");
        PrintWriter commitWriter = new PrintWriter(new FileWriter(commit, false));
        //Tree tree = new Tree();
        String treeContent = getTreeContent();
        //System.out.print(treeContent);
        treeSha = Util.hashString(treeContent);
        String treeName = "objects/" + treeSha;
        File tree = new File(treeName);
        PrintWriter treeWriter = new PrintWriter(new FileWriter(tree), false);
        treeWriter.write(treeContent);
        treeWriter.close();
        
        //Index index = new Index();
        //index.addBlob(treeName);

        String date = getDate();
        String temp = prevCommit;

        if(prevCommit == null)
        {
            temp = "";
        }
        currentContent = treeSha + '\n' + temp + '\n' + '\n' + author + '\n' + date + '\n' + summary;
        commitWriter.print(currentContent);
        commitWriter.close();

        PrintWriter overwrite = new PrintWriter(new FileWriter("index"), false);
        overwrite.write("");
        overwrite.close();

        String currentCommitSha = "objects/" + Util.hashString(currentContent);
        File newFile = new File(currentCommitSha);

        commit.renameTo(newFile);

        updatePreviousCommit();
        
        File head = new File("HEAD");
        PrintWriter pw2 = new PrintWriter(new FileWriter(head, false));
        pw2.print(currentCommitSha);
        pw2.close();

    }

    public String getCurrentSHA() throws NoSuchAlgorithmException
    {
        return Util.hashString(currentContent);
    }

    public String getTreeContent() throws IOException, Exception
    {
        String newIndexContent = getCurrentIndexContent();
        //.out.print(newIndexContent);
        treeSha = Util.hashString(newIndexContent);
        if(deleteFile != "")
        {
            File prevCommitFile = new File("objects/" + prevCommit);
            BufferedReader commitReader = new BufferedReader(new FileReader(prevCommitFile));
            String prevTree = commitReader.readLine();
            commitReader.close();
            String newTreeContent = getContentExceptTargetFile(prevTree, deleteFile, "");
            getContentExceptCommit(newTreeContent);
            if((newTreeContent != null) && (newTreeContent != ""))
            {
                newTreeContent = newTreeContent.substring(0, newTreeContent.length() -1);
            }
            if((newIndexContent != "") && (newTreeContent != ""))
            {
                newIndexContent += "\n";
            }
            newIndexContent += newTreeContent;
            treeSha = Util.hashString(newIndexContent);
        }

        if(prevCommit != null)
        {
            if((newIndexContent != null) && (newIndexContent != ""))
            {
                newIndexContent += "\n";
            }
            newIndexContent += "tree : " + prevCommit;
        }

        PrintWriter overwrite = new PrintWriter(new FileWriter("index"), false);
        overwrite.write(newIndexContent);
        overwrite.close();
        return newIndexContent;
    }


    private void getContentExceptCommit(String newTreeContent) throws IOException
    {
        File temp = new File("temp");
        PrintWriter print = new PrintWriter(new FileWriter(temp), false);
        print.print(newTreeContent);
        print.close();
        BufferedReader treeReader = new BufferedReader(new FileReader("temp"));
        String tempContent = "";
        while(treeReader.ready())
        {
            String line = treeReader.readLine();
            String[] splits = line.split(" : ");
            if(splits.length == 3)
            {
                tempContent += line;
                tempContent += "\n";
            }
        }
        newTreeContent = tempContent;
        temp.delete();
        treeReader.close();
    }

    private void getContentExceptTargetFile(String tree) throws IOException
    {
        BufferedReader treeReader = new BufferedReader(new FileReader("objects/" + tree));
        String line;
        while(treeReader.ready() && (!((line = treeReader.readLine())).equals("")))
        {  
            String[] splits = line.split(" : ");
            if(splits.length == 3)
            {
                //blob
                if(splits[0].equals("blob"))
                {
                    //find target
                    for(int i = 0; i < deleteFiles.size(); i++)
                    {
                        //if it's target
                        if(splits[2].equals(deleteFiles.get(i)))
                        {

                        }
                        else if(splits[2].equals(editFiles.get(i)))
                        {
                            
                        }
                        //if it's not
                        else
                        {
                            addFiles.add(line);
                        }
                    }
                }
                //tree
                else
                {
                    //find target
                    for(int i = 0; i < deleteFiles.size(); i++)
                    {
                        //if it's target
                        if(splits[2].equals(deleteFiles.get(i)))
                        {

                        }
                        else if(splits[2].equals(editFiles.get(i)))
                        {

                        }
                        //if it's not
                        else
                        {
                            getContentExceptTargetFile(splits[1]);
                        }
                    }
                }
            }
            //commit
            else
            {
                getContentExceptTargetFile(splits[1]);
            }
        }
        treeReader.close();
    }

    private void getCurrentIndexContent() throws IOException, Exception
    {
        BufferedReader reader = new BufferedReader(new FileReader("index"));
        while(reader.ready())
        {
            String line = reader.readLine();
            if(line.substring(0, 9).equals("*deleted*"))
            {
                deleteFiles.add(line.substring(9));
            }
            else if(line.substring(0, 9).equals("*edited*"))
            {
                editFiles.add(line.substring(9));
            }
            else
            {
                addFiles.add(line);
            }
        }
        reader.close();
    }

    public void updatePreviousCommit() throws IOException, NoSuchAlgorithmException
    {
        if(prevCommit == null)
        {
            return;
        }
        String path = "objects/" + prevCommit;
        File file = new File(path);
        String content = "";
        try (BufferedReader br = new BufferedReader(new FileReader(file));)
        {
            for(int i = 0; i < 2; i++)
            {
                content += br.readLine();
                content += "\n";
            }
            content += getCurrentSHA();
            br.readLine();
            while(br.ready())
            {
                content += "\n";
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
        br.close();
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

    public static String getDate() {
        java.util.Date date = new java.util.Date();
        String d = date.toString();
        String ret = d.substring(4, 10) + ", " + d.substring(24);
        return ret;
    }
}