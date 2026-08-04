import datastructures.CustomSet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CustomSetTest {

    @Test
    void testAddAndContains() {
        CustomSet<String> set = new CustomSet<>();
        assertTrue(set.add("apple"));
        assertTrue(set.contains("apple"));
        assertFalse(set.add("apple"));
        assertEquals(1, set.size());
    }

    @Test
    void testRemove() {
        CustomSet<Integer> set = new CustomSet<>();
        set.add(10);
        set.add(20);

        assertTrue(set.remove(10));
        assertFalse(set.contains(10));
        assertEquals(1, set.size());
        assertFalse(set.remove(10));
    }
}
