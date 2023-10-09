
import static org.junit.Assert.*;

import java.io.File;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BlobTester {
    @BeforeAll
    static void setupBeforeClass() throws Exception {
        Util.deleteDirectory("objects");
        Util.writeFile("testerFile.txt", "hello world");
        Blob blob = new Blob("testerFile.txt");
        blob.createFile();
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        Util.deleteFile("testerFile.txt");
        Util.deleteDirectory("objects");
    }

    @Test
    @DisplayName("Verify Blob file has been created")
    void testBlobCreated() throws Exception {
        // Confirm the Blob creates a file in the correct location with the correct hash
        assertTrue(Util.exists("objects/2aae6c35c94fcfb415dbe95f408b9ce91ee846ed"));
    }

    @Test
    @DisplayName("Verify Blob has correct contents")
    void testBlobContents() throws Exception {
        String originalFileContent = Util.readFile("testerFile.txt");
        String blobFileContent = Util.readFile("objects/2aae6c35c94fcfb415dbe95f408b9ce91ee846ed");

        // Confirm the Blob's contents match the original file's contents
        assertEquals(originalFileContent, blobFileContent);
    }
}
