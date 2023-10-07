
import static org.junit.Assert.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class IndexTester {

    @BeforeAll
    static void setupBeforeClass() throws Exception {
        Util.deleteDirectory("objects");
        Util.deleteFile("index");
        Util.writeFile("testerFile.txt", "hello world");
        Util.writeFile("testerFile2.txt", "test file 2");
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        Util.deleteFile("testerFile.txt");
        Util.deleteFile("testerFile2.txt");
        Util.deleteFile("index");
        Util.deleteDirectory("objects");
    }

    @Test
    @DisplayName("Verify init has created objects and index")
    void testInit() throws Exception {
        // Init is called in the constructor
        new Index();

        // Confirming init created the objects folder
        assertTrue(Util.exists("objects"));

        // Confirming init created the index file
        assertTrue(Util.exists("index"));
    }

    @Test
    @DisplayName("Verify adding Blobs with Index works")
    void testAdd() throws Exception {
        Index index = new Index();

        // Confirming index is empty
        assertEquals("", Util.readFile("index"));

        index.addBlob("testerFile.txt");

        // Confirming testerFile.txt was added to index
        assertEquals("blob : 2aae6c35c94fcfb415dbe95f408b9ce91ee846ed : testerFile.txt\n", Util.readFile("index"));

        index.addBlob("testerFile2.txt");

        // Confirming testerFile2.txt was added to index
        assertEquals(
                "blob : 2aae6c35c94fcfb415dbe95f408b9ce91ee846ed : testerFile.txt\n" +
                        "blob : 7e4fb4975ec34b65826fd95e60e628110dbef839 : testerFile2.txt\n",
                Util.readFile("index"));
    }

    @Test
    @DisplayName("Verify removing Blobs with Index works")
    void testRemove() throws Exception {
        Util.deleteFile("index");
        Index index = new Index();

        // Confirming index is empty
        assertEquals("", Util.readFile("index"));

        index.addBlob("testerFile.txt");

        // Confirming testerFile.txt was added to index
        assertEquals("testerFile.txt : 2aae6c35c94fcfb415dbe95f408b9ce91ee846ed\n", Util.readFile("index"));

        index.addBlob("testerFile2.txt");

        // Confirming testerFile2.txt was added to index
        assertEquals(
                "testerFile.txt : 2aae6c35c94fcfb415dbe95f408b9ce91ee846ed\ntesterFile2.txt : 7e4fb4975ec34b65826fd95e60e628110dbef839\n",
                Util.readFile("index"));

        index.removeBlob("testerFile.txt");

        // Confirming testerFile.txt was removed from index
        assertEquals(
                "testerFile2.txt : 7e4fb4975ec34b65826fd95e60e628110dbef839\n",
                Util.readFile("index"));

        // Confirming the blob file was not deleted
        assertTrue(Util.exists("objects/7e4fb4975ec34b65826fd95e60e628110dbef839"));
    }
}
