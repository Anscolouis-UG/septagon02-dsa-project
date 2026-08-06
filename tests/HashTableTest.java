import datastructures.HashTable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HashTableTest {

    @Test
    void testPutAndGet() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", 1);
        table.put("two", 2);

        assertEquals(2, table.size());
        assertTrue(table.containsKey("one"));
        assertEquals(Integer.valueOf(1), table.get("one"));
        assertEquals(Integer.valueOf(2), table.get("two"));
    }

    @Test
    void testCollisionHandling() {
        HashTable<String, Integer> table = new HashTable<>(2);
        table.put("Aa", 1);
        table.put("BB", 2);

        assertEquals(2, table.size());
        assertEquals(Integer.valueOf(1), table.get("Aa"));
        assertEquals(Integer.valueOf(2), table.get("BB"));
    }

    @Test
    void testDynamicResizing() {
        HashTable<Integer, String> table = new HashTable<>(2);
        table.put(1, "A");
        table.put(2, "B");
        table.put(3, "C");

        assertEquals(3, table.size());
        assertEquals("C", table.get(3));
    }

    @Test
    void testRemove() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", 1);
        table.put("two", 2);

        assertEquals(Integer.valueOf(2), table.remove("two"));
        assertNull(table.get("two"));
        assertFalse(table.containsKey("two"));
        assertEquals(1, table.size());
    }
}
