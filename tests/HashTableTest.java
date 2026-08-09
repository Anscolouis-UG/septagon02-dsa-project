package tests;

import datastructures.HashTable;

public class HashTableTest {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  HashTable - Unit Tests (7 tests)");
        System.out.println("==================================================\n");

        testPutAndGet();
        testUpdateExistingKey();
        testGetMissingReturnsNull();
        testContainsKey();
        testRemove();
        testSize();
        testCollisionHandling();

        System.out.println("==================================================");
        System.out.println("RESULTS: " + passed + " passed, " + failed + " failed");
        System.out.println("==================================================");
    }

    static void testPutAndGet() {
        HashTable<Integer, String> table = new HashTable<>();
        table.put(1, "Electrical");
        table.put(2, "Medical");
        assertEq("testPutGet_1", "Electrical", table.get(1));
        assertEq("testPutGet_2", "Medical", table.get(2));
    }

    static void testUpdateExistingKey() {
        HashTable<Integer, String> table = new HashTable<>();
        table.put(5, "original");
        table.put(5, "updated");
        assertEq("testUpdate_value", "updated", table.get(5));
        assertEq("testUpdate_size", 1, table.size());
    }

    static void testGetMissingReturnsNull() {
        HashTable<Integer, String> table = new HashTable<>();
        assertTrue("testMissing_null", table.get(999) == null,
                "expected null for missing key");
    }

    static void testContainsKey() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("Dijkstra", 7);
        assertTrue("testContains_found", table.containsKey("Dijkstra"),
                "expected containsKey to return true");
        assertTrue("testContains_notFound", !table.containsKey("BFS"),
                "expected containsKey to return false for missing key");
    }

    static void testRemove() {
        HashTable<Integer, String> table = new HashTable<>();
        table.put(10, "Network"); table.put(20, "Plumbing");
        table.remove(10);
        assertTrue("testRemove_gone", !table.containsKey(10),
                "expected key 10 to be removed");
        assertEq("testRemove_size", 1, table.size());
    }

    static void testSize() {
        HashTable<Integer, String> table = new HashTable<>();
        assertEq("testSize_zero", 0, table.size());
        table.put(1, "a"); table.put(2, "b"); table.put(3, "c");
        assertEq("testSize_three", 3, table.size());
    }

    static void testCollisionHandling() {
        // Insert many entries to force chaining / collision handling
        HashTable<Integer, String> table = new HashTable<>();
        for (int i = 0; i < 50; i++) {
            table.put(i, "val" + i);
        }
        boolean allCorrect = true;
        for (int i = 0; i < 50; i++) {
            if (!"val".concat(String.valueOf(i)).equals(table.get(i))) {
                allCorrect = false;
                break;
            }
        }
        assertEq("testCollision_size", 50, table.size());
        assertTrue("testCollision_allValues", allCorrect,
                "expected all 50 values to be retrievable after collision stress");
    }

    // ── Helpers ──────────────────────────────────────────────────────
    private static void assertEq(String name, int expected, int actual) {
        if (expected == actual) { System.out.println("  [PASS] " + name); passed++; }
        else { System.out.println("  [FAIL] " + name + " -- expected " + expected + ", got " + actual); failed++; }
    }
    private static void assertEq(String name, String expected, Object actual) {
        if (expected.equals(actual)) { System.out.println("  [PASS] " + name); passed++; }
        else { System.out.println("  [FAIL] " + name + " -- expected \"" + expected + "\", got \"" + actual + "\""); failed++; }
    }
    private static void assertTrue(String name, boolean cond, String detail) {
        if (cond) { System.out.println("  [PASS] " + name); passed++; }
        else { System.out.println("  [FAIL] " + name + " -- " + detail); failed++; }
    }
}