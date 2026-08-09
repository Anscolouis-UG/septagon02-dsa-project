package tests;

import datastructures.CustomDisjointSet;

/**
 * CustomDisjointSetTest.java
 * Author: Nana Ekow Gyakye-Smith (22379633)
 *
 * Elements 0-5 represent six sample campus locations:
 *   0 = Main Gate, 1 = Balme Library, 2 = Great Hall,
 *   3 = Department of Computer Science, 4 = Night Market, 5 = Sports Stadium
 */
public class CustomDisjointSetTest {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  CustomDisjointSet - Unit Tests (10 tests)");
        System.out.println("==================================================\n");

        testEachElementStartsInItsOwnSet();
        testUnionMergesTwoSets();
        testUnionOfAlreadyConnectedElementsReturnsFalse();
        testConnectedIsFalseForDifferentSets();
        testTransitiveConnection();
        testFindAppliesPathCompression();
        testMultipleDisjointZonesRemainSeparate();
        testAllElementsMergeIntoOneNetwork();
        testSizeReturnsTotalElementCount();
        testFindOutOfRangeThrows();

        System.out.println("==================================================");
        System.out.println("RESULTS: " + passed + " passed, " + failed + " failed");
        System.out.println("==================================================");
    }

    static void testEachElementStartsInItsOwnSet() {
        CustomDisjointSet ds = new CustomDisjointSet(6);
        assertEq("testInitial_setCount", 6, ds.getSetCount());
        for (int i = 0; i < 6; i++) {
            assertEq("testInitial_find_" + i, i, ds.find(i));
        }
    }

    static void testUnionMergesTwoSets() {
        CustomDisjointSet ds = new CustomDisjointSet(6);
        ds.union(0, 1); // Main Gate + Balme Library
        assertEq("testUnion_setCount", 5, ds.getSetCount());
        assertTrue("testUnion_connected", ds.connected(0, 1), "expected 0 and 1 to be connected");
    }

    static void testUnionOfAlreadyConnectedElementsReturnsFalse() {
        CustomDisjointSet ds = new CustomDisjointSet(6);
        ds.union(0, 1);
        boolean result = ds.union(0, 1); // no-op
        assertTrue("testRedundantUnion_returnsFalse", !result,
                "expected false when unioning already-connected elements");
        assertEq("testRedundantUnion_setCount", 5, ds.getSetCount());
    }

    static void testConnectedIsFalseForDifferentSets() {
        CustomDisjointSet ds = new CustomDisjointSet(6);
        assertTrue("testNotConnected", !ds.connected(2, 3),
                "expected 2 and 3 to not be connected initially");
    }

    static void testTransitiveConnection() {
        CustomDisjointSet ds = new CustomDisjointSet(6);
        ds.union(0, 1); // Main Gate - Balme
        ds.union(1, 2); // Balme - Great Hall
        assertTrue("testTransitive", ds.connected(0, 2),
                "expected Main Gate and Great Hall to be in same zone");
    }

    static void testFindAppliesPathCompression() {
        CustomDisjointSet ds = new CustomDisjointSet(6);
        ds.union(0, 1); ds.union(1, 2); ds.union(2, 3);
        int root = ds.find(0);
        assertEq("testPathCompression_sameRoot", root, ds.find(3));
    }

    static void testMultipleDisjointZonesRemainSeparate() {
        CustomDisjointSet ds = new CustomDisjointSet(6);
        ds.union(0, 1);
        ds.union(4, 5);
        assertTrue("testDisjoint_zones", !ds.connected(0, 4),
                "expected zone {0,1} and zone {4,5} to remain separate");
        assertEq("testDisjoint_setCount", 4, ds.getSetCount()); // {0,1},{2},{3},{4,5}
    }

    static void testAllElementsMergeIntoOneNetwork() {
        CustomDisjointSet ds = new CustomDisjointSet(6);
        ds.union(0, 1); ds.union(1, 2); ds.union(2, 3);
        ds.union(3, 4); ds.union(4, 5);
        assertEq("testAllMerged_setCount", 1, ds.getSetCount());
        assertTrue("testAllMerged_connected", ds.connected(0, 5),
                "expected all elements to be in one network");
    }

    static void testSizeReturnsTotalElementCount() {
        CustomDisjointSet ds = new CustomDisjointSet(6);
        assertEq("testSize", 6, ds.size());
    }

    static void testFindOutOfRangeThrows() {
        CustomDisjointSet ds = new CustomDisjointSet(6);
        boolean threw = false;
        try {
            ds.find(99);
        } catch (IndexOutOfBoundsException e) {
            threw = true;
        }
        assertTrue("testOutOfRange_throws", threw,
                "expected IndexOutOfBoundsException for index 99");
    }

    // ── Helpers ──────────────────────────────────────────────────────
    private static void assertEq(String name, int expected, int actual) {
        if (expected == actual) { System.out.println("  [PASS] " + name); passed++; }
        else { System.out.println("  [FAIL] " + name + " -- expected " + expected + ", got " + actual); failed++; }
    }
    private static void assertTrue(String name, boolean cond, String detail) {
        if (cond) { System.out.println("  [PASS] " + name); passed++; }
        else { System.out.println("  [FAIL] " + name + " -- " + detail); failed++; }
    }
}