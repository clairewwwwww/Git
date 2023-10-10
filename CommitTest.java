import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
    
    @Test
    @DisplayName("creates 1 commit")
    void testCase1() throws Exception
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

        //create one commit
        Commit commit = new Commit(null, "claire", "testing commit 1");


        //sha --> 9644dcc839e35631be3d1d6ea3eed804d513033a
        //blob : b43170f3ce583d5aa4cf796bcba18bf4dfb47a84 : testCase1
        //blob : 475b6b0f321c21893de9d2a828d399f22e341fec : testCase2
        File indexFile = new File("objects/9644dcc839e35631be3d1d6ea3eed804d513033a");
        assertTrue(indexFile.exists());
        //sha --> 
        /*3ca52142514636c9dc41e3d65ae79c86183a5dce
        null
        null
        claire
        Oct 09, 2023
        testing commit 1 */
        File commitFile = new File("objects/3ca52142514636c9dc41e3d65ae79c86183a5dce");
        assertTrue(commitFile.exists());

        //Verify the commit has correct Tree
        BufferedReader read = new BufferedReader(new FileReader(commitFile));
        String actualTree = read.readLine();
        String expectedTree = "9644dcc839e35631be3d1d6ea3eed804d513033a";
        assertEquals(expectedTree, actualTree);

        //Verify the commit has correct Prev
        Util.readFile("objects/" + "3ca52142514636c9dc41e3d65ae79c86183a5dce");
        String actualPrev = read.readLine();
        String expectedPrev = "null";
        assertEquals(actualPrev, expectedPrev);

        //Verify the commit has correct SHA1s
        assertTrue(commitFile.exists());

        read.close();
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
        //check commit1 tree
        //sha --> 9644dcc839e35631be3d1d6ea3eed804d513033a
        //blob : b43170f3ce583d5aa4cf796bcba18bf4dfb47a84 : testCase1
        //blob : 475b6b0f321c21893de9d2a828d399f22e341fec : testCase2
        File commit1Tree = new File("objects/9644dcc839e35631be3d1d6ea3eed804d513033a");
        assertTrue(commit1Tree.exists());
        assertEquals("blob : b43170f3ce583d5aa4cf796bcba18bf4dfb47a84 : testCase1\n" + 
        "blob : 475b6b0f321c21893de9d2a828d399f22e341fec : testCase2", Util.readFile(commit1Tree.getAbsolutePath()));


        //check commit2 tree
        //sha --> 4b4933965bdfe1dd29f0de3b4dd74f6e4d756607
        /*
        blob : 85d25f2ccd2fed2f8368498fd8f52ebefdeedb4f : testCase3
        blob : 4d75b3656cfb85c3fb1d56d459b63e0d2862639a : testCase4
        tree : da39a3ee5e6b4b0d3255bfef95601890afd80709 : testFolder1
        tree : 3ca52142514636c9dc41e3d65ae79c86183a5dce 
        */
        File commit2Tree = new File("objects/4b4933965bdfe1dd29f0de3b4dd74f6e4d756607");
        assertTrue(commit2Tree.exists());
        assertEquals("blob : 85d25f2ccd2fed2f8368498fd8f52ebefdeedb4f : testCase3\n" +
        "blob : 4d75b3656cfb85c3fb1d56d459b63e0d2862639a : testCase4\n" +
        "tree : da39a3ee5e6b4b0d3255bfef95601890afd80709 : testFolder1\n" +
        "tree : 3ca52142514636c9dc41e3d65ae79c86183a5dce", Util.readFile(commit2Tree.getAbsolutePath()));

        //check commit1 commit
        //sha --> 3ca52142514636c9dc41e3d65ae79c86183a5dce
        /*
        9644dcc839e35631be3d1d6ea3eed804d513033a
        null
        b784879f83097356bff6a9a08db8bc2ffb3721cc (null)(yes next commit, but sha no change)
        claire
        Oct 09, 2023
        testing commit 1 */
        File commit1File = new File("objects/3ca52142514636c9dc41e3d65ae79c86183a5dce");
        assertTrue(commit1File.exists());
        String actualPrev = getLine(commit1File, 1);
        String expectedPrev = "null";
        assertEquals(actualPrev, expectedPrev);

        String actualNext = getLine(commit1File, 2);
        String expectedNext = "b784879f83097356bff6a9a08db8bc2ffb3721cc";
        assertEquals(actualNext, expectedNext);


        // assertEquals(
        // "9644dcc839e35631be3d1d6ea3eed804d513033a\n" + 
        // "null\n" + 
        // "b784879f83097356bff6a9a08db8bc2ffb3721cc\n" + //(commit 2 sha)
        // "claire\n" + 
        // "Oct 09, 2023\n" + 
        // "testing commit 1", Util.readFile(commit1File.getAbsolutePath()));

        
        //check commit2 commit
        //sha --> b784879f83097356bff6a9a08db8bc2ffb3721cc
        /*
        4b4933965bdfe1dd29f0de3b4dd74f6e4d756607
        3ca52142514636c9dc41e3d65ae79c86183a5dce
        null
        claire
        Oct 09, 2023
        testing commit 2 */

        File commit2File = new File("objects/b784879f83097356bff6a9a08db8bc2ffb3721cc");
        assertTrue(commit2File.exists());
        String actualPrev2 = getLine(commit2File, 1);
        String expectedPrev2 = "3ca52142514636c9dc41e3d65ae79c86183a5dce";
        assertEquals(actualPrev2, expectedPrev2);

        String actualNext2 = getLine(commit2File, 2);
        String expectedNext2 = "null";
        assertEquals(actualNext2, expectedNext2);


        // assertEquals(
        // "4b4933965bdfe1dd29f0de3b4dd74f6e4d756607\n" + 
        // "3ca52142514636c9dc41e3d65ae79c86183a5dce\n" + 
        // "null\n" + 
        // "claire\n" + 
        // "Oct 09, 2023\n" + 
        // "testing commit 2", Util.readFile(commit2File.getAbsolutePath()));
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
        folder1.mkdirs();

        Index index4= new Index();
        index4.addBlob("testCase7");
        index4.addBlob("testCase8");
        index4.addTree("testFolder1");

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
        //content
        // blob : b43170f3ce583d5aa4cf796bcba18bf4dfb47a84 : testCase1
        // blob : 475b6b0f321c21893de9d2a828d399f22e341fec : testCase2
        String tree1SHA = "9644dcc839e35631be3d1d6ea3eed804d513033a";
        File commit1Tree = new File("objects/9644dcc839e35631be3d1d6ea3eed804d513033a");
        assertTrue(commit1Tree.exists());
        assertEquals("blob : b43170f3ce583d5aa4cf796bcba18bf4dfb47a84 : testCase1\n" + 
        "blob : 475b6b0f321c21893de9d2a828d399f22e341fec : testCase2", Util.readFile(commit1Tree.getAbsolutePath()));

        //content
        // blob : 85d25f2ccd2fed2f8368498fd8f52ebefdeedb4f : testCase3
        // blob : 4d75b3656cfb85c3fb1d56d459b63e0d2862639a : testCase4
        // tree : da39a3ee5e6b4b0d3255bfef95601890afd80709 : testFolder1
        // tree : da96b6a03d51e6378e5951470c2726b4893ab6f3
        String tree2SHA = "964f8d3432d2d4f20c5f0c276f845e39d7f7f852";
        File commit2Tree = new File("objects/964f8d3432d2d4f20c5f0c276f845e39d7f7f852");
        assertTrue(commit2Tree.exists());
        assertEquals("blob : 85d25f2ccd2fed2f8368498fd8f52ebefdeedb4f : testCase3\n" +
        "blob : 4d75b3656cfb85c3fb1d56d459b63e0d2862639a : testCase4\n" + 
        "tree : da39a3ee5e6b4b0d3255bfef95601890afd80709 : testFolder1\n" +
        "tree : da96b6a03d51e6378e5951470c2726b4893ab6f3", Util.readFile(commit2Tree.getAbsolutePath()));

        //content 
        // blob : 427cb1dcd72fd646c81c7d71ecb80008ab855003 : testCase5
        // blob : 3619e67c8c8bdb48c096bf767f319c7f2d6e7b6b : testCase6
        // tree : d13808e90ea5f6ae212b088f6dbf439895adb4d1
        String tree3SHA = "1528484ebe724d547a6b0f76e8df760653a2978d";
        File commit3Tree = new File("objects/1528484ebe724d547a6b0f76e8df760653a2978d");
        assertTrue(commit3Tree.exists());
        assertEquals("blob : 427cb1dcd72fd646c81c7d71ecb80008ab855003 : testCase5\n" + 
        "blob : 3619e67c8c8bdb48c096bf767f319c7f2d6e7b6b : testCase6\n" +
        "tree : d13808e90ea5f6ae212b088f6dbf439895adb4d1", Util.readFile(commit3Tree.getAbsolutePath()));

        //content
        //blob : 13df75729db599b9da266be79a431547838a5932 : testCase7
        //blob : f08eada325e3ae1d1bd62c66f5c930e441550f96 : testCase8
        //tree : da39a3ee5e6b4b0d3255bfef95601890afd80709 : testFolder1
        //tree : cd0ee33ea4d8efcb8c7d87891b6d68f32bc04be8
        String tree4SHA = "5fe7bdb7fb8c5a73d29d5bce3b109e23beb9dc9a";
        File commit4Tree = new File("objects/5fe7bdb7fb8c5a73d29d5bce3b109e23beb9dc9a");
        assertTrue(commit4Tree.exists());
        assertEquals("blob : 13df75729db599b9da266be79a431547838a5932 : testCase7\n" +
        "blob : f08eada325e3ae1d1bd62c66f5c930e441550f96 : testCase8\n" +
        "tree : da39a3ee5e6b4b0d3255bfef95601890afd80709 : testFolder1\n" +
        "tree : cd0ee33ea4d8efcb8c7d87891b6d68f32bc04be8", Util.readFile(commit4Tree.getAbsolutePath()));

        /*
        SHA --> da96b6a03d51e6378e5951470c2726b4893ab6f3
        //content
        9644dcc839e35631be3d1d6ea3eed804d513033a
        null
        null
        claire
        Oct 10, 2023
        testing commit 1 */
        String commit1SHA = "da96b6a03d51e6378e5951470c2726b4893ab6f3";
        File commit1 = new File("objects/da96b6a03d51e6378e5951470c2726b4893ab6f3");
        assertTrue(commit1.exists());

        String actualPrev1 = getLine(commit1, 1);
        String expectedPrev1 = "null";
        assertEquals(actualPrev1, expectedPrev1);

        String actualNext = getLine(commit1, 2);
        String expectedNext = "d13808e90ea5f6ae212b088f6dbf439895adb4d1";
        assertEquals(actualNext, expectedNext);

        /*
        SHA --> d13808e90ea5f6ae212b088f6dbf439895adb4d1
        964f8d3432d2d4f20c5f0c276f845e39d7f7f852
        da96b6a03d51e6378e5951470c2726b4893ab6f3
        null
        claire
        Oct 10, 2023
        testing commit 2 */
        String commit2SHA = "d13808e90ea5f6ae212b088f6dbf439895adb4d1";
        File commit2 = new File("objects/d13808e90ea5f6ae212b088f6dbf439895adb4d1");
        assertTrue(commit2.exists());

        String actualPrev2 = getLine(commit2, 1);
        String expectedPrev2 = "da96b6a03d51e6378e5951470c2726b4893ab6f3";
        assertEquals(actualPrev2, expectedPrev2);

        String actualNext2 = getLine(commit2, 2);
        String expectedNext2 = "cd0ee33ea4d8efcb8c7d87891b6d68f32bc04be8";
        assertEquals(actualNext2, expectedNext2);


        /*
        SHA --> cd0ee33ea4d8efcb8c7d87891b6d68f32bc04be8
        1528484ebe724d547a6b0f76e8df760653a2978d
        d13808e90ea5f6ae212b088f6dbf439895adb4d1
        null
        claire
        Oct 10, 2023
        testing commit 3*/
        String commit3SHA = "cd0ee33ea4d8efcb8c7d87891b6d68f32bc04be8";
        File commit3 = new File("objects/cd0ee33ea4d8efcb8c7d87891b6d68f32bc04be8");
        assertTrue(commit3.exists());

        String actualPrev3 = getLine(commit3, 1);
        String expectedPrev3 = "d13808e90ea5f6ae212b088f6dbf439895adb4d1";
        assertEquals(actualPrev3, expectedPrev3);

        String actualNext3 = getLine(commit3, 2);
        String expectedNext3 = "30051167a65ff690ad0fe817bfdfeeeb1de9963c";
        assertEquals(actualNext3, expectedNext3);

        /*
        SHA --> 30051167a65ff690ad0fe817bfdfeeeb1de9963c
        5fe7bdb7fb8c5a73d29d5bce3b109e23beb9dc9a
        cd0ee33ea4d8efcb8c7d87891b6d68f32bc04be8
        null
        claire
        Oct 10, 2023
        testing commit 4 */
        String commit4SHA = "30051167a65ff690ad0fe817bfdfeeeb1de9963c";
        File commit4 = new File("objects/30051167a65ff690ad0fe817bfdfeeeb1de9963c");
        assertTrue(commit4.exists());

        String actualPrev4 = getLine(commit4, 1);
        String expectedPrev4 = "cd0ee33ea4d8efcb8c7d87891b6d68f32bc04be8";
        assertEquals(actualPrev4, expectedPrev4);

        String actualNext4 = getLine(commit4, 2);
        String expectedNext4 = "null";
        assertEquals(actualNext4, expectedNext4);

    }

    @Test
    void testGetDate() throws Exception 
    {
        Commit commit = new Commit(null, "claire", "testing commit 1");
        assertEquals(commit.getDate(), "Oct 10, 2023");
    }

    public String getLine(File file, int numberOfLine) throws IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(file));
        int count = 0;
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
}
