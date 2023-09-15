
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TreeTester {
    @BeforeAll
    static void setupBeforeClass() throws Exception {
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

        tree.add("blob : a64e2a4adcc4ae20e6e35babd2a181619cb8e224 : file.txt");

        tree.add("tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b");

        assertThrows(Exception.class, () -> {
            tree.add("tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b");
        });

        tree.add("tree : a1f239cbcd40f722555acfc7d23be06dee9d815e");
    }
}
