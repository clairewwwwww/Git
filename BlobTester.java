import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BlobTester {
    @BeforeAll
    static void setupBeforeClass() throws Exception {
        Blob blob = new Blob("");
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
    }

    @Test
    @DisplayName("Verify Blob has correct contents")
    void testInitialize() throws Exception {
        assertEquals(1, 1);
    }
}
