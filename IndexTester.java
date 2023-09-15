
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
        new Index();

        assertTrue(Util.exists("objects"));
        assertTrue(Util.exists("index"));
    }

    @Test
    @DisplayName("Verify adding and removing Blobs with Index works")
    void testAdd() throws Exception {
        Index index = new Index();
        assertEquals("", Util.readFile("index"));
        index.addBlob("testerFile.txt");
        assertEquals("testerFile.txt : 2aae6c35c94fcfb415dbe95f408b9ce91ee846ed\n", Util.readFile("index"));
        index.addBlob("testerFile2.txt");
        assertEquals(
                "testerFile.txt : 2aae6c35c94fcfb415dbe95f408b9ce91ee846ed\ntesterFile2.txt : 7e4fb4975ec34b65826fd95e60e628110dbef839\n",
                Util.readFile("index"));
        index.removeBlob("testerFile.txt");
        assertEquals(
                "testerFile2.txt : 7e4fb4975ec34b65826fd95e60e628110dbef839\n",
                Util.readFile("index"));
        assertTrue(Util.exists("objects/7e4fb4975ec34b65826fd95e60e628110dbef839"));
    }
}
