import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CommitTest 
{
    @Test
    @DisplayName("creates 1 commit")
    void testCase1() throws Exception
    {
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

    @Test
    @DisplayName("creates 2 commit")
    void testCase2() throws Exception
    {

        //add two files
        File file1 = new File("testCase1");
        Util.writeFile("testCase1", "this is the content for testCase1");
        File file2 = new File("testCase2");
        Util.writeFile("testCase2", "this is the content for testCase2");

        Index index1 = new Index();
        index1.addBlob("testCase1");
        index1.addBlob("testCase2");

        Commit commit1 = new Commit(null, "claire", "testing commit 1");


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


        Commit commit2 = new Commit(null, "claire", "testing commit 2");

    }

    @Test
    @DisplayName("creates 4 commit")
    void testCase3() throws Exception
    {

    }

    @Test
    void testGetDate() throws Exception 
    {
        Commit commit = new Commit(null, "claire", "testing commit 1");
        assertEquals(commit.getDate(), "Oct 07, 2023");
    }
}
