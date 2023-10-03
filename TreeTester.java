
import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TreeTester {
    @BeforeAll
    static void setupBeforeClass() throws Exception {
        Util.deleteDirectory("objects");
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        Util.deleteDirectory("objects");
    }

    @Test
    @DisplayName("Verify tree add method throws exceptions correctly")
    void testTreeAdd() throws Exception {
        Tree tree = new Tree();

        // Confirming Tree throws an exception when adding an improperly formatted
        // string
        assertThrows(Exception.class, () -> {
            tree.add("hello");
        });

        // Confirming Tree throws an exception when adding a blob without a filename
        assertThrows(Exception.class, () -> {
            tree.add("blob : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b");
        });

        // Confirming Tree throws an exception when adding a tree with a filename
        assertThrows(Exception.class, () -> {
            tree.add("tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b : file.txt");
        });

        tree.add("blob : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b : file.txt");

        // Confirming Tree throws an exception when adding a duplicate file
        assertThrows(Exception.class, () -> {
            tree.add("blob : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b : file.txt");
        });

        tree.add("blob : a64e2a4adcc4ae20e6e35babd2a181619cb8e224 : file2.txt");

        tree.add("tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b");

        // Confirming Tree throws an exception when adding a duplicate tree
        assertThrows(Exception.class, () -> {
            tree.add("tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b");
        });

        tree.add("tree : a1f239cbcd40f722555acfc7d23be06dee9d815e");
    }

    @Test
    @DisplayName("Verify tree remove method works")
    void testTreeRemove() throws Exception {
        Tree tree = new Tree();
        tree.add("tree : a1f239cbcd40f722555acfc7d23be06dee9d815e");
        tree.add("blob : a64e2a4adcc4ae20e6e35babd2a181619cb8e224 : file.txt");

        // Confirming tree can remove a tree which has been added
        assertTrue(tree.remove("a1f239cbcd40f722555acfc7d23be06dee9d815e"));

        // Confirming tree cannot remove a tree which has already been removed
        assertFalse(tree.remove("a1f239cbcd40f722555acfc7d23be06dee9d815e"));

        // Confirming tree cannot remove a tree which has not been added
        assertFalse(tree.remove("a64e2a4adcc4ae20e6e35babd2a181619cb8e224"));

        // Confirming tree can remove a blob which has already been added
        assertTrue(tree.remove("file.txt"));
    }

    @Test
    @DisplayName("Verify tree saving works")
    void testTreeSaveToFile() throws Exception {
        Tree tree = new Tree();
        tree.add("tree : a1f239cbcd40f722555acfc7d23be06dee9d815e");
        tree.add("tree : 039c501ac8dfcac91c6f05601cee876e1cc07e17");
        tree.add("blob : a64e2a4adcc4ae20e6e35babd2a181619cb8e224 : file1.txt");
        tree.add("blob : a64e2a4adcc4ae20e6e35babd2a181619cb8e224 : file2.txt");
        tree.add("blob : a64e2a4adcc4ae20e6e35babd2a181619cb8e224 : file3.txt");

        tree.writeToFile();

        // Confirming the tree file has been saved correctly
        assertTrue(Util.exists("objects/ba5fd54a37de3742d2e76dc2acf88e3d9bea8b0b"));

        // Confirming the tree file's contents are correct (Order of blobs and trees is
        // random due to hashmap)
        assertEquals(Util.readFile("objects/ba5fd54a37de3742d2e76dc2acf88e3d9bea8b0b"),
                "blob : a64e2a4adcc4ae20e6e35babd2a181619cb8e224 : file1.txt\n" + //
                        "blob : a64e2a4adcc4ae20e6e35babd2a181619cb8e224 : file3.txt\n" + //
                        "blob : a64e2a4adcc4ae20e6e35babd2a181619cb8e224 : file2.txt\n" + //
                        "tree : 039c501ac8dfcac91c6f05601cee876e1cc07e17\n" + //
                        "tree : a1f239cbcd40f722555acfc7d23be06dee9d815e");
    }

    @Test
    @DisplayName("Verify add directory works")
    //empty folder
    void testAddDirectoryCase1() throws Exception{
        Tree tree = new Tree();
        File path = new File("textFolder1");
        path.mkdirs();
        
        //actual SHA1
        String actualSHA = tree.addDirectory("textFolder1");
        
        //expected SHA1
        String expectedSHA = "da39a3ee5e6b4b0d3255bfef95601890afd80709";

        //if file exist

        File file = new File("objects/da39a3ee5e6b4b0d3255bfef95601890afd80709");

        assertEquals(actualSHA, expectedSHA);
        assertTrue(file.exists());
    }

        @Test
    @DisplayName("Verify add directory works")
    //folder with two files
    void testAddDirectoryCase2() throws Exception{
        File path = new File("textFolder1");
        path.mkdirs();
        File file1 = new File("textFolder1/" + "testCase1");
        Util.writeFile(file1.getName(), "this is the content for testCase1");
        File file2 = new File("textFolder1/" + "testCase2");
        Util.writeFile(file2.getName(), "this is the content for testCase2");
        
        
    }
        @Test
    @DisplayName("Verify add directory works")
    //folder with two files + one empty folder
    void testAddDirectoryCase3() throws Exception{
        File path = new File("textFolder1");
        path.mkdirs();
        File file1 = new File("textFolder1/" + "testCase1");
        Util.writeFile(file1.getName(), "this is the content for testCase1");
        File file2 = new File("textFolder1/" + "testCase2");
        Util.writeFile(file2.getName(), "this is the content for testCase2");

        File path1 = new File("textFolder1/textInsideFolder1");
        path1.mkdirs();
        
    }
        @Test
    @DisplayName("Verify add directory works")
    //folder with two files + one folder with two files
    void testAddDirectoryCase4() throws Exception{
       File path = new File("textFolder1");
        path.mkdirs();
        File file1 = new File("textFolder1/" + "testCase1");
        Util.writeFile(file1.getName(), "this is the content for testCase1");
        File file2 = new File("textFolder1/" + "testCase2");
        Util.writeFile(file2.getName(), "this is the content for testCase2");

        File path1 = new File("textFolder1/textInsideFolder1");
        path1.mkdirs();
        File file3 = new File("textFolder1/textInsideFolder1" + "testCase3");
        Util.writeFile(file3.getName(), "this is the content for testCase3");
        File file4 = new File("textFolder1/textInsideFolder1" + "testCase4");
        Util.writeFile(file4.getName(), "this is the content for testCase4");
    }
}
