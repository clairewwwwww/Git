
import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;

//import org.jcp.xml.dsig.internal.dom.Utils;
//import org.jcp.xml.dsig.internal.dom.Utils;
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
    
    /*
    @Test
    @DisplayName("Verify tree add method throws exceptions correctly")
    void testTreeAdd() throws Exception {
        Tree tree = new Tree();
        // Confirming Tree throws an exception when adding an improperly formatted
        // string
        /* 
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
    }*/

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

    //Test Case 1: In JUnit create a single folder (directory) with 3 files in it and test addDirectory
    @Test
    @DisplayName("Verify add directory works")
    void testCase1() throws Exception
    {
        Util.deleteDirectory("objects");
        Util.deleteDirectory("testFolder1");
        Tree tree = new Tree();
        File path = new File("testFolder1");
        path.mkdirs();

        File file1 = new File("testFolder1/testCase1");
        Util.writeFile("testFolder1/testCase1", "this is the content for testCase1");
        File file2 = new File("testFolder1/testCase2");
        Util.writeFile("testFolder1/testCase2", "this is the content for testCase2");
        File file3 = new File("testFolder1/testCase3");
        Util.writeFile("testFolder1/testCase3", "this is the content for testCase3");

        String actualSHA = tree.addDirectory("testFolder1");
        tree.writeToFile();
        
        //expected SHA1
        String expectedSHA = "a3ff9da1b55bdebae84d2f893ac92220c56b9ca2";

        //if file exist
        File file = new File("objects", "a3ff9da1b55bdebae84d2f893ac92220c56b9ca2");
        
        assertEquals(expectedSHA, actualSHA);
        assertTrue(file.exists());
        
    }
    //Test Case 2: In JUnit create a single folder (directory) with 3 files and 2 folders in it and test addDirectory
    @Test
    @DisplayName("Verify add directory works")
    void testCase2() throws Exception
    {
        Util.deleteDirectory("objects");
        Util.deleteDirectory("testFolder1");
        Tree tree = new Tree();
        File path = new File("testFolder1");
        path.mkdirs();

        File file1 = new File("testFolder1/testCase1");
        Util.writeFile("testFolder1/testCase1", "this is the content for testCase1");
        File file2 = new File("testFolder1/testCase2");
        Util.writeFile("testFolder1/testCase2", "this is the content for testCase2");
        File file3 = new File("testFolder1/testCase3");
        Util.writeFile("testFolder1/testCase3", "this is the content for testCase3");

        File path1 = new File("testFolder1/testInsideFolder1");
        path1.mkdirs();
        File path2 = new File("testFolder1/testInsideFolder2");
        path2.mkdirs();

        String actualSHA = tree.addDirectory("testFolder1");
        tree.writeToFile();
        /* sha: e326ca009314ec2e3878ff0b761239973e514009
         *  blob : 85d25f2ccd2fed2f8368498fd8f52ebefdeedb4f : testFolder1/testCase3
            blob : 475b6b0f321c21893de9d2a828d399f22e341fec : testFolder1/testCase2
            tree : da39a3ee5e6b4b0d3255bfef95601890afd80709 : testFolder1/testInsideFolder2
            tree : da39a3ee5e6b4b0d3255bfef95601890afd80709 : testFolder1/testInsideFolder1
            blob : b43170f3ce583d5aa4cf796bcba18bf4dfb47a84 : testFolder1/testCase1
         */
        
        //expected SHA1
        String expectedSHA = "e326ca009314ec2e3878ff0b761239973e514009";

        //if file exist
        File file = new File("objects", "8cd2bf287b79935999338a79f7f251cd2e4009b3");
        
        assertEquals(expectedSHA, actualSHA);
        assertTrue(file.exists());
    }

    @Test
    @DisplayName("Verify add directory works")
    //empty folder
    void testAddDirectoryCase1() throws Exception{
        Util.deleteDirectory("objects");
        Util.deleteDirectory("testFolder1");
        Util.deleteFile("index");
        Tree tree = new Tree();
        File path = new File("testFolder1");
        path.mkdirs();
        File folder = new File("testFolder1", "testInsideFolder1");
        folder.mkdirs();
        
        String actualSHA = tree.addDirectory("testFolder1");
        tree.writeToFile();
        
        //expected SHA1
        String expectedSHA = "ccd4729c9f22b7a794be54833a94404580089010";

        //if file exist
        File file = new File("objects", "ccd4729c9f22b7a794be54833a94404580089010");
        
        assertEquals(expectedSHA, actualSHA);
        assertTrue(file.exists());
        

    }
    @Test
    @DisplayName("Verify add directory works")
    //folder with two files
    void testAddDirectoryCase2() throws Exception{
        Util.deleteDirectory("objects");
        Util.deleteDirectory("testFolder1");
        Util.deleteFile("index");
        Tree tree = new Tree();
        File path = new File("testFolder1");
        path.mkdirs();
        
        File file1 = new File("testFolder1/testCase1");
        Util.writeFile("testFolder1/testCase1", "this is the content for testCase1");
        File file2 = new File("testFolder1/testCase2");
        Util.writeFile("testFolder1/testCase2", "this is the content for testCase2");
        
        //actual SHA1
        String actualSHA = tree.addDirectory("testFolder1");
        tree.writeToFile();
        
        //expected SHA1
        String expectedSHA = "ba3b51ea28f8ec6f46f7b625dbbb9265f0d6214c";

        //if file exist
        File file = new File("objects", "ba3b51ea28f8ec6f46f7b625dbbb9265f0d6214c");
        
        assertEquals(expectedSHA, actualSHA);
        assertTrue(file.exists());
    }

        @Test
    @DisplayName("Verify add directory works")
    //folder with two files + one empty folder
    void testAddDirectoryCase3() throws Exception{
        Util.deleteDirectory("objects");
        Util.deleteDirectory("testFolder1");
        Util.deleteFile("index");
        Tree tree = new Tree();
        File path = new File("testFolder1");
        path.mkdirs();
        
        File file1 = new File("testFolder1/testCase1");
        Util.writeFile("testFolder1/testCase1", "this is the content for testCase1");
        File file2 = new File("testFolder1/testCase2");
        Util.writeFile("testFolder1/testCase2", "this is the content for testCase2");
        
        File folder = new File("testFolder1", "testInsideFolder1");
        folder.mkdirs();

        //actual SHA1
        String actualSHA = tree.addDirectory("testFolder1");
        tree.writeToFile();
        
        //expected SHA1
        String expectedSHA = "b438bcecef0d451f17a29b025755c65ca92b05ed";
        //Sha of
        // blob : 475b6b0f321c21893de9d2a828d399f22e341fec : testFolder1/testCase2
        // tree : da39a3ee5e6b4b0d3255bfef95601890afd80709 : testFolder1/testInsideFolder1
        // blob : b43170f3ce583d5aa4cf796bcba18bf4dfb47a84 : testFolder1/testCase1

        //if file exist
        File file = new File("objects", "3d6ef5f7b8deb35daa6b4e8ba36d78d551026c2e");
        
        assertEquals(expectedSHA, actualSHA);
        assertTrue(file.exists());
    }
        @Test
    @DisplayName("Verify add directory works")
    //folder with two files + one folder with two files
    void testAddDirectoryCase4() throws Exception{
       Util.deleteDirectory("objects");
        Util.deleteDirectory("testFolder1");
        Util.deleteFile("index");

        Tree tree = new Tree();
        File path = new File("testFolder1");
        path.mkdirs();

        String name1 = "testFolder1/testCase1";
        File file1 = new File(name1);
        Util.writeFile(name1, "this is the content for testCase1");
        //blob : b43170f3ce583d5aa4cf796bcba18bf4dfb47a84 : testFolder1/testCase1

        String name2 = "testFolder1/testCase2";
        File file2 = new File(name2);
        Util.writeFile(name2, "this is the content for testCase2");
        //blob : 475b6b0f321c21893de9d2a828d399f22e341fec : testFolder1/testCase2

        String insideFolder = "testFolder1/testInsideFolder1";
        File path1 = new File(insideFolder);
        path1.mkdirs();
        //tree : 77b44a6a3c547328cb87eadda65a35d4c60a5418 : testFolder1/testInsideFolder1

        String name3 = insideFolder + "/testCase3";
        File file3 = new File(name3);
        Util.writeFile(name3, "this is the content for testCase3");
        //blob : 85d25f2ccd2fed2f8368498fd8f52ebefdeedb4f : testFolder1/testInsideFolder1/testCase3

        String name4 = insideFolder + "/testCase4";
        File file4 = new File(name4);
        Util.writeFile(name4, "this is the content for testCase4");
        //blob : 4d75b3656cfb85c3fb1d56d459b63e0d2862639a : testFolder1/testInsideFolder1/testCase4


        //actual SHA1
        String actualSHA = tree.addDirectory("testFolder1");
        tree.writeToFile();
        
        //expected SHA1
        String expectedSHA = "ae8c5e8963e1508cf739b0854cb503464d68ba95";

        //if file exist
        File file = new File("objects", "09a7ba25f3cc2b0252a4d35863d238da5db62e15");
        
        assertEquals(expectedSHA, actualSHA);
        assertTrue(file.exists());
    }
}
