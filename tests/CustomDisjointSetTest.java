package tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import datastructures.CustomDisjointSet;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CustomDisjointSetTest.java
 * Author: Nana Ekow Gyakye-Smith (22379633)
 *
 * Elements 0-5 represent six sample campus locations:
 *   0 = Main Gate, 1 = Balme Library, 2 = Great Hall,
 *   3 = Department of Computer Science, 4 = Night Market, 5 = Sports Stadium
 */
public class CustomDisjointSetTest {

    private CustomDisjointSet ds;

    @BeforeEach
    void setUp() {
        ds = new CustomDisjointSet(6);
    }

    @Test
    void testEachElementStartsInItsOwnSet() {
        assertEquals(6, ds.getSetCount());
        for (int i = 0; i < 6; i++) {
            assertEquals(i, ds.find(i));
        }
    }

    @Test
    void testUnionMergesTwoSets() {
        ds.union(0, 1); // Main Gate + Balme Library now one zone
        assertEquals(5, ds.getSetCount());
        assertTrue(ds.connected(0, 1));
    }

    @Test
    void testUnionOfAlreadyConnectedElementsReturnsFalse() {
        ds.union(0, 1);
        assertFalse(ds.union(0, 1)); // no-op, already same set
        assertEquals(5, ds.getSetCount());
    }

    @Test
    void testConnectedIsFalseForDifferentSets() {
        assertFalse(ds.connected(2, 3));
    }

    @Test
    void testTransitiveConnection() {
        ds.union(0, 1); // Main Gate - Balme
        ds.union(1, 2); // Balme - Great Hall
        assertTrue(ds.connected(0, 2)); // Main Gate and Great Hall now same zone
    }

    @Test
    void testFindAppliesPathCompression() {
        ds.union(0, 1);
        ds.union(1, 2);
        ds.union(2, 3);
        int root = ds.find(0);
        assertEquals(root, ds.find(3));
    }

    @Test
    void testMultipleDisjointZonesRemainSeparate() {
        ds.union(0, 1);
        ds.union(4, 5);
        assertFalse(ds.connected(0, 4));
        assertEquals(4, ds.getSetCount()); // {0,1}, {2}, {3}, {4,5}
    }

    @Test
    void testAllElementsMergeIntoOneNetwork() {
        ds.union(0, 1);
        ds.union(1, 2);
        ds.union(2, 3);
        ds.union(3, 4);
        ds.union(4, 5);
        assertEquals(1, ds.getSetCount());
        assertTrue(ds.connected(0, 5));
    }

    @Test
    void testSizeReturnsTotalElementCount() {
        assertEquals(6, ds.size());
    }

    @Test
    void testFindOutOfRangeThrows() {
        assertThrows(IndexOutOfBoundsException.class, () -> ds.find(99));
    }

    @Test
    void testZeroSizedSetRejected() {
        assertThrows(IllegalArgumentException.class, () -> new CustomDisjointSet(0));
    }
}