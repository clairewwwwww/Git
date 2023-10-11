import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CommitTest 
{
    private String testCase1Blob;
    private String testCase2Blob;
    private String testCase3Blob;
    private String testCase4Blob;
    private String testCase1SHA;
    private String testCase2SHA;
    private String testCase3SHA;
    private String testCase4SHA;


    public CommitTest()
    {

        testCase1Blob = "this is the content for testCase1";
        testCase2Blob = "this is the content for testCase2";
        testCase3Blob = "this is the content for testCase3";
        testCase4Blob = "this is the content for testCase4";
        testCase1SHA = "b43170f3ce583d5aa4cf796bcba18bf4dfb47a84";
        testCase2SHA = "475b6b0f321c21893de9d2a828d399f22e341fec";
        testCase3SHA = "85d25f2ccd2fed2f8368498fd8f52ebefdeedb4f";
        testCase4SHA = "4d75b3656cfb85c3fb1d56d459b63e0d2862639a";
    }

    @Test
    @DisplayName("SHA test")
    void testSHA() throws NoSuchAlgorithmException
    {
        assertEquals("aaf4c61ddcc5e8a2dabede0f3b482cd9aea9434d", Util.hashString("hello"));
        assertEquals("da39a3ee5e6b4b0d3255bfef95601890afd80709", Util.hashString(""));
        assertEquals("adc83b19e793491b1c6ea0fd8b46cd9f32e592fc", Util.hashString("\n"));
    }

    @Test
    @DisplayName("basic test")
    void basic() throws IOException
    {
        File file1 = new File("objects/" + testCase1SHA);
        assertTrue(file1.exists());
        assertEquals(testCase1Blob, Util.readFile("objects/" + testCase1SHA));

        File file2 = new File("objects/" + testCase2SHA);
        assertTrue(file2.exists());
        assertEquals(testCase2Blob, Util.readFile("objects/" + testCase2SHA));

        File file3 = new File("objects/" + testCase3SHA);
        assertTrue(file3.exists());
        assertEquals(testCase3Blob, Util.readFile("objects/" + testCase3SHA));

        File file4 = new File("objects/" + testCase4SHA);
        assertTrue(file4.exists());
        assertEquals(testCase4Blob, Util.readFile("objects/" + testCase4SHA));

    }
    public void case1SetUp() throws Exception
    {
        Util.deleteDirectory("objects");
        Util.deleteDirectory("testFolder1");
        Util.deleteFile("index");
        //add two files
        File file1 = new File("testCase1");
        Util.writeFile("testCase1", "this is the content for testCase1");
        File file2 = new File("testCase2");
        Util.writeFile("testCase2", "this is the content for testCase2");

        Index index = new Index();
        index.addBlob("testCase1");
        index.addBlob("testCase2");
    }
    @Test
    @DisplayName("test 1 commit")
    void testCase1() throws Exception
    {
        case1SetUp();

        //Verify the commit has correct Tree, Prev and Next SHA1s
        Commit commit = new Commit(null, "claire", "testing commit 1");

        //sha --> 9644dcc839e35631be3d1d6ea3eed804d513033a
        //blob : b43170f3ce583d5aa4cf796bcba18bf4dfb47a84 : testCase1
        //blob : 475b6b0f321c21893de9d2a828d399f22e341fec : testCase2
        String expectedTreeSHA = "9644dcc839e35631be3d1d6ea3eed804d513033a";
        File tree = new File("objects/" + expectedTreeSHA);
        assertTrue(tree.exists());

        //correct commitSHA
        String commitContent = getBasicContent(expectedTreeSHA, "", "", 1);
        String expectedSHA = Util.hashString(commitContent);
        File commitFile = new File("objects/" + expectedSHA);
        assertTrue(commitFile.exists());

        //correct Tree?
        String actualTree = getLine(commitFile, 1);
        assertEquals(expectedTreeSHA, actualTree);

        //correct Prev?
        String actualPrev = getLine(commitFile, 2);
        assertEquals("", actualPrev);

        //correct Next?
        String actualNext = getLine(commitFile, 3);
        assertEquals("", actualNext);
    }
    
    public void case2SetUp() throws Exception
    {
        Util.deleteDirectory("objects");
        Util.deleteDirectory("testFolder1");
        Util.deleteFile("index");
        
        //add two files
        File file1 = new File("testCase1");
        Util.writeFile("testCase1", "this is the content for testCase1");
        File file2 = new File("testCase2");
        Util.writeFile("testCase2", "this is the content for testCase2");

        Index index1 = new Index();
        index1.addBlob("testCase1");
        index1.addBlob("testCase2");

        Commit firstCommit = new Commit(null, "claire", "testing commit 1");
        String commit1SHA = firstCommit.getCurrentSHA();

        //add two files and one folder

        File file3 = new File("testCase3");
        Util.writeFile("testCase3", "this is the content for testCase3");
        File file4 = new File("testCase4");
        Util.writeFile("testCase4", "this is the content for testCase4");
        File folder1 = new File("testFolder1");
        folder1.mkdirs();

        Index index2 = new Index();
        index2.addBlob("testCase3");
        index2.addBlob("testCase4");
        index2.addTree("testFolder1");

        Commit secondCommit = new Commit(commit1SHA, "claire", "testing commit 2");
    }
    @Test
    @DisplayName("creates 2 commit")
    /*
    1. Include at least 2 added files into each commit
    2. Include at least 1 folder in one of the commits
    3. Verify the commits have correct Tree, Prev and Next SHA1s
    4. Verify the Tree contents are correct */
    void testCase2() throws Exception
    {
        case2SetUp();

        //tree1
        //sha --> 9644dcc839e35631be3d1d6ea3eed804d513033a
        //blob : b43170f3ce583d5aa4cf796bcba18bf4dfb47a84 : testCase1
        //blob : 475b6b0f321c21893de9d2a828d399f22e341fec : testCase2
        String expectedTree1SHA = "9644dcc839e35631be3d1d6ea3eed804d513033a";
        String expectedTreeContent = "blob : b43170f3ce583d5aa4cf796bcba18bf4dfb47a84 : testCase1\n" + 
        "blob : 475b6b0f321c21893de9d2a828d399f22e341fec : testCase2";

        File tree1 = new File("objects/" + expectedTree1SHA);
        assertTrue(tree1.exists());
        assertEquals(expectedTreeContent, Util.readFile(tree1.getAbsolutePath()));

        //commit1
        //correct commitSHA
        String expectedCommit1SHA = Util.hashString(getBasicContent(expectedTree1SHA, "", "", 1));
        File commit1 = new File("objects/" + expectedCommit1SHA);
        assertTrue(commit1.exists());


        //tree2
        //sha --> 
        //blob : 85d25f2ccd2fed2f8368498fd8f52ebefdeedb4f : testCase3
        //blob : 4d75b3656cfb85c3fb1d56d459b63e0d2862639a : testCase4
        //tree : da39a3ee5e6b4b0d3255bfef95601890afd80709 : testFolder1
        //tree : (commit1SHA)

        String expectedTree2Content = "blob : 85d25f2ccd2fed2f8368498fd8f52ebefdeedb4f : testCase3\n" +
        "blob : 4d75b3656cfb85c3fb1d56d459b63e0d2862639a : testCase4\n" +
        "tree : da39a3ee5e6b4b0d3255bfef95601890afd80709 : testFolder1\n" +
        "tree : " + expectedCommit1SHA;
        String expectedTree2SHA = Util.hashString(expectedTree2Content);

        File tree2 = new File("objects/" + expectedTree2SHA);
        assertTrue(tree2.exists());
        assertEquals(expectedTree2Content, Util.readFile(tree2.getAbsolutePath()));

        //commit2
        //correct commitSHA
        String expectedCommit2SHA = Util.hashString(getBasicContent(expectedTree2SHA, expectedCommit1SHA, "", 2));
        File commit2 = new File("objects/" + expectedCommit2SHA);
        assertTrue(commit2.exists());

        //tree
        String actualCommit1Tree = getLine(commit1, 1);
        assertEquals(expectedTree1SHA, actualCommit1Tree);

        //prev
        String actualCommit1Prev = getLine(commit1, 2);
        assertEquals("", actualCommit1Prev);

        //next
        String actualCommit1Next = getLine(commit1, 3);
        assertEquals(expectedCommit2SHA, actualCommit1Next);

        //tree
        String actualCommit2Tree = getLine(commit2, 1);
        assertEquals(expectedTree2SHA, actualCommit2Tree);

        //prev
        String actualCommit2Prev = getLine(commit2, 2);
        assertEquals(expectedCommit1SHA, actualCommit2Prev);

        //next
        String actualCommit2Next =  getLine(commit2, 3);
        assertEquals("", actualCommit2Next);

    }

    public void case3SetUp() throws Exception
    {
        Util.deleteDirectory("objects");
        Util.deleteDirectory("testFolder1");
        Util.deleteFile("index");
        
        //COMMIT one
        //add two files
        File file1 = new File("testCase1");
        Util.writeFile("testCase1", "this is the content for testCase1");
        File file2 = new File("testCase2");
        Util.writeFile("testCase2", "this is the content for testCase2");

        Index index1 = new Index();
        index1.addBlob("testCase1");
        index1.addBlob("testCase2");

        Commit firstCommit = new Commit(null, "claire", "testing commit 1");
        String commit1SHA = firstCommit.getCurrentSHA();

        //COMMIT two
        //add two files and one folder
        File file3 = new File("testCase3");
        Util.writeFile("testCase3", "this is the content for testCase3");
        File file4 = new File("testCase4");
        Util.writeFile("testCase4", "this is the content for testCase4");
        File folder1 = new File("testFolder1");
        folder1.mkdirs();

        Index index2 = new Index();
        index2.addBlob("testCase3");
        index2.addBlob("testCase4");
        index2.addTree("testFolder1");

        Commit secondCommit = new Commit(commit1SHA, "claire", "testing commit 2");

        //COMMIT three
        //add two files
        File file5 = new File("testCase5");
        Util.writeFile("testCase5", "this is the content for testCase5");
        File file6 = new File("testCase6");
        Util.writeFile("testCase6", "this is the content for testCase6");

        Index index3 = new Index();
        index3.addBlob("testCase5");
        index3.addBlob("testCase6");

        String commit2SHA = secondCommit.getCurrentSHA();
        Commit thirdCommit = new Commit(commit2SHA, "claire", "testing commit 3");
        

        //add two files and one folder
        File file7 = new File("testCase7");
        Util.writeFile("testCase7", "this is the content for testCase7");
        File file8 = new File("testCase8");
        Util.writeFile("testCase8", "this is the content for testCase8");
        File folder2 = new File("testFolder2");
        folder2.mkdirs();

        Index index4= new Index();
        index4.addBlob("testCase7");
        index4.addBlob("testCase8");
        index4.addTree("testFolder2");

        String commit3SHA = thirdCommit.getCurrentSHA();
        Commit fourthCommit = new Commit(commit3SHA, "claire", "testing commit 4");

    }

    @Test
    @DisplayName("creates 4 commit")
    /*
    1. Create a test which creates 4 commits
    2. Each commit must contain at least 2 new files, all of which have unique file data
    3. 2 Commits must contain at least one new folder
    4. Test tree contents, commit contents for prev and next and trees*/
    void testCase3() throws Exception
    {
        case3SetUp();

        String expectedTree1Content = "blob : b43170f3ce583d5aa4cf796bcba18bf4dfb47a84 : testCase1\n" + 
        "blob : 475b6b0f321c21893de9d2a828d399f22e341fec : testCase2";
        String expectedTree1SHA = "9644dcc839e35631be3d1d6ea3eed804d513033a";

        String expectedCommit1SHA = Util.hashString(getBasicContent(expectedTree1SHA, "", "", 1));
        File commit1 = new File("objects/" + expectedCommit1SHA);
        assertTrue(commit1.exists());

        String expectedTree2Content = "blob : 85d25f2ccd2fed2f8368498fd8f52ebefdeedb4f : testCase3\n" + 
        "blob : 4d75b3656cfb85c3fb1d56d459b63e0d2862639a : testCase4\n" + 
        "tree : da39a3ee5e6b4b0d3255bfef95601890afd80709 : testFolder1\n" + 
        "tree : " + expectedCommit1SHA;
        String expectedTree2SHA = Util.hashString(expectedTree2Content);

        String expectedCommit2SHA = Util.hashString(getBasicContent(expectedTree2SHA, expectedCommit1SHA, "", 2));
        File commit2 = new File("objects/" + expectedCommit2SHA);
        assertTrue(commit2.exists());


        String expectedTree3Content = "blob : 427cb1dcd72fd646c81c7d71ecb80008ab855003 : testCase5\n" + 
        "blob : 3619e67c8c8bdb48c096bf767f319c7f2d6e7b6b : testCase6\n" +
        "tree : " + expectedCommit2SHA;
        String expectedTree3SHA = Util.hashString(expectedTree3Content);

        String expectedCommit3SHA = Util.hashString(getBasicContent(expectedTree3SHA, expectedCommit2SHA, "", 3));
        File commit3 = new File("objects/" + expectedCommit3SHA);
        assertTrue(commit3.exists());


        String expectedTree4Content = "blob : 13df75729db599b9da266be79a431547838a5932 : testCase7\n" +
        "blob : f08eada325e3ae1d1bd62c66f5c930e441550f96 : testCase8\n" +
        "tree : da39a3ee5e6b4b0d3255bfef95601890afd80709 : testFolder2\n" +
        "tree : " + expectedCommit3SHA;
        String expectedTree4SHA = Util.hashString(expectedTree4Content);

        String expectedCommit4SHA = Util.hashString(getBasicContent(expectedTree4SHA, expectedCommit3SHA, "", 4));
        File commit4 = new File("objects/" + expectedCommit4SHA);
        assertTrue(commit4.exists());

        //1
        //tree
        String actualCommit1Tree = getLine(commit1, 1);
        assertEquals(expectedTree1SHA, actualCommit1Tree);

        //prev
        String actualCommit1Prev = getLine(commit1, 2);
        assertEquals("", actualCommit1Prev);

        //next
        String actualCommit1Next = getLine(commit1, 3);
        assertEquals(expectedCommit2SHA, actualCommit1Next);

        //2
        //tree
        String actualCommit2Tree = getLine(commit2, 1);
        assertEquals(expectedTree2SHA, actualCommit2Tree);

        //prev
        String actualCommit2Prev = getLine(commit2, 2);
        assertEquals(expectedCommit1SHA, actualCommit2Prev);

        //next
        String actualCommit2Next =  getLine(commit2, 3);
        assertEquals(expectedCommit3SHA, actualCommit2Next);

        //tree
        String actualCommit3Tree = getLine(commit3, 1);
        assertEquals(expectedTree3SHA, actualCommit3Tree);

        //prev
        String actualCommit3Prev = getLine(commit3, 2);
        assertEquals(expectedCommit2SHA, actualCommit3Prev);

        //next
        String actualCommit3Next = getLine(commit3, 3);
        assertEquals(expectedCommit4SHA, actualCommit3Next);

        //tree
        String actualCommit4Tree = getLine(commit4, 1);
        assertEquals(expectedTree4SHA, actualCommit4Tree);

        //prev
        String actualCommit4Prev = getLine(commit4, 2);
        assertEquals(expectedCommit3SHA, actualCommit4Prev);

        //next
        String actualCommit4Next =  getLine(commit4, 3);
        assertEquals("", actualCommit4Next);

    }

    @Test
    void testGetDate() throws Exception 
    {
        assertEquals(Commit.getDate(), "Oct 11, 2023");
    }

    public String getLine(File file, int numberOfLine) throws IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(file));
        int count = 1;
        String result = "";
        while(br.ready())
        {
            if(count == numberOfLine)
            {
                result = br.readLine();
                br.close();
                return result;
            }
            br.readLine();
            count++;
        }
        br.close();
        return result;
    }

    public String getBasicContent(String treeSHA, String previousCommit, String nextCommit, int numOfCommit) throws Exception
    {
        String result = treeSHA + "\n"
        + previousCommit + "\n" 
        + nextCommit + "\n" 
        + "claire" + "\n"
        + Commit.getDate() + "\n"
        + "testing commit" + " " + numOfCommit;
        return result;
    }

}
