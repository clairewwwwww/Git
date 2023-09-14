
import static org.junit.Assert.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class IndexTester {

    @BeforeAll
    static void setupBeforeClass() throws Exception {
        FileUtil.deleteDirectory("objects");
        FileUtil.deleteFile("index");
        FileUtil.writeFile("testerFile.txt", "hello world");
        FileUtil.writeFile("testerFile2.txt", "test file 2");
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        FileUtil.deleteFile("testerFile.txt");
        FileUtil.deleteFile("testerFile2.txt");
        FileUtil.deleteFile("index");
        FileUtil.deleteDirectory("objects");
    }

    @Test
    @DisplayName("Verify init has created objects and index")
    void testInit() throws Exception {
        new Index();

        assertTrue(FileUtil.exists("objects"));
        assertTrue(FileUtil.exists("index"));
    }

    @Test
    @DisplayName("Verify adding and removing Blobs with Index works")
    void testAdd() throws Exception {
        Index index = new Index();
        assertEquals("", FileUtil.readFile("index"));
        index.addBlob("testerFile.txt");
        assertEquals("testerFile.txt : 2aae6c35c94fcfb415dbe95f408b9ce91ee846ed\n", FileUtil.readFile("index"));
        index.addBlob("testerFile2.txt");
        assertEquals(
                "testerFile.txt : 2aae6c35c94fcfb415dbe95f408b9ce91ee846ed\ntesterFile2.txt : 7e4fb4975ec34b65826fd95e60e628110dbef839\n",
                FileUtil.readFile("index"));
        index.removeBlob("testerFile.txt");
        assertEquals(
                "testerFile2.txt : 7e4fb4975ec34b65826fd95e60e628110dbef839\n",
                FileUtil.readFile("index"));
        assertTrue(FileUtil.exists("objects/7e4fb4975ec34b65826fd95e60e628110dbef839"));
    }
}
