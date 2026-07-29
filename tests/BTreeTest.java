
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BTreeTest {

    @Test
    void testInsertAndSearch() {
        BTree<Integer, String> tree = new BTree<>();
        tree.insert(10, "ten");
        tree.insert(20, "twenty");
        tree.insert(5, "five");
        tree.insert(15, "fifteen");
        tree.insert(25, "twenty-five");

        assertEquals(5, tree.size());
        assertEquals("twenty", tree.search(20));
        assertEquals("five", tree.search(5));
        assertNull(tree.search(99));
    }

    @Test
    void testSplitTriggeringInsertion() {
        BTree<Integer, String> tree = new BTree<>(2);
        for (int i = 1; i <= 10; i++) {
            tree.insert(i, "value" + i);
        }

        assertEquals(10, tree.size());
        assertEquals("value" + 6, tree.search(6));
        assertNull(tree.search(11));
        assertTrue(tree.traverse().contains("6=value6"));
    }
}
