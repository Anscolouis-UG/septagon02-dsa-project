import datastructures.CustomMap;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CustomMapTest {

    @Test
    void testPutAndGet() {
        CustomMap<String, Integer> map = new CustomMap<>();
        map.put("alpha", 1);
        map.put("beta", 2);

        assertEquals(2, map.size());
        assertTrue(map.containsKey("alpha"));
        assertEquals(Integer.valueOf(1), map.get("alpha"));
        assertEquals(Integer.valueOf(2), map.get("beta"));
    }

    @Test
    void testUpdateExistingKey() {
        CustomMap<String, Integer> map = new CustomMap<>();
        map.put("x", 10);
        map.put("x", 20);

        assertEquals(1, map.size());
        assertEquals(Integer.valueOf(20), map.get("x"));
    }

    @Test
    void testContainsKeyAndRemove() {
        CustomMap<Integer, String> map = new CustomMap<>();
        map.put(1, "one");
        map.put(2, "two");

        assertTrue(map.containsKey(2));
        assertFalse(map.containsKey(3));

        assertEquals("two", map.remove(2));
        assertNull(map.get(2));
        assertEquals(1, map.size());
    }
}
