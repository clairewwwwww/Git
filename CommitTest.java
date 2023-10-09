import static org.junit.jupiter.api.Assertions.assertEquals;

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
        File file1 = new File("testCase1");
        Util.writeFile("testCase1", "this is the content for testCase1");
        File file2 = new File("testCase2");
        Util.writeFile("testCase2", "this is the content for testCase2");

        Index index = new Index();
        index.addBlob("testCase1");
        index.addBlob("testCase2");

        Commit commit = new Commit(null, "claire", "testing commit 1");
        String actual = commit.createTree();
        File file = new File("objects", actual);
        Util.readFile("objects/" + "d881faafc6fa5d4848d6b1423fa441a5a7615d1d");

        //Verify the commit has correct Tree, Prev and Next SHA1s
        String expected = "9644dcc839e35631be3d1d6ea3eed804d513033a";

        BufferedReader read = new BufferedReader(new FileReader(file));
        actual = read.readLine();
        read.close();

        assertEquals(expected, actual);

    }

    @Test
    @DisplayName("creates 2 commit")
    void testCase2() throws Exception
    {

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
