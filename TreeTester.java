
import static org.junit.Assert.*;

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
    }

    @Test
    @DisplayName("Verify tree add method throws exceptions correctly")
    void testTreeAdd() throws Exception {
        Tree tree = new Tree();

        assertThrows(Exception.class, () -> {
            tree.add("hello");
        });

        assertThrows(Exception.class, () -> {
            tree.add("blob : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b");
        });

        assertThrows(Exception.class, () -> {
            tree.add("tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b : file.txt");
        });

        tree.add("blob : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b : file.txt");

        assertThrows(Exception.class, () -> {
            tree.add("blob : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b : file.txt");
        });

        tree.add("blob : a64e2a4adcc4ae20e6e35babd2a181619cb8e224 : file2.txt");

        tree.add("tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b");

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

        assertTrue(tree.remove("a1f239cbcd40f722555acfc7d23be06dee9d815e"));
        assertFalse(tree.remove("a1f239cbcd40f722555acfc7d23be06dee9d815e"));
        assertFalse(tree.remove("a64e2a4adcc4ae20e6e35babd2a181619cb8e224"));
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

        assertTrue(Util.exists("objects/ba5fd54a37de3742d2e76dc2acf88e3d9bea8b0b"));
        assertEquals(Util.readFile("objects/ba5fd54a37de3742d2e76dc2acf88e3d9bea8b0b"),
                "blob : a64e2a4adcc4ae20e6e35babd2a181619cb8e224 : file1.txt\n" + //
                        "blob : a64e2a4adcc4ae20e6e35babd2a181619cb8e224 : file3.txt\n" + //
                        "blob : a64e2a4adcc4ae20e6e35babd2a181619cb8e224 : file2.txt\n" + //
                        "tree : 039c501ac8dfcac91c6f05601cee876e1cc07e17\n" + //
                        "tree : a1f239cbcd40f722555acfc7d23be06dee9d815e");
    }
}
