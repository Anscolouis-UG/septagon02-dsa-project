package tests;

import datastructures.CustomMap;

public class CustomMapTest {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  CustomMap - Unit Tests (7 tests)");
        System.out.println("==================================================\n");

        testPutAndGet();
        testUpdateExistingKey();
        testGetMissingKeyReturnsNull();
        testContainsKey();
        testRemove();
        testSize();
        testIsEmpty();

        System.out.println("==================================================");
        System.out.println("RESULTS: " + passed + " passed, " + failed + " failed");
        System.out.println("==================================================");
    }

    static void testPutAndGet() {
        CustomMap<String, Integer> map = new CustomMap<>();
        map.put("Main Gate", 1);
        map.put("Balme Library", 3);
        assertEq("testPutGet_mainGate", 1, (int) map.get("Main Gate"));
        assertEq("testPutGet_balme", 3, (int) map.get("Balme Library"));
    }

    static void testUpdateExistingKey() {
        CustomMap<String, Integer> map = new CustomMap<>();
        map.put("Great Hall", 4);
        map.put("Great Hall", 99);
        assertEq("testUpdate_value", 99, (int) map.get("Great Hall"));
        assertEq("testUpdate_size", 1, map.size());
    }

    static void testGetMissingKeyReturnsNull() {
        CustomMap<String, Integer> map = new CustomMap<>();
        assertTrue("testMissingKey_null", map.get("NonExistent") == null,
                "expected null for missing key");
    }

    static void testContainsKey() {
        CustomMap<Integer, String> map = new CustomMap<>();
        map.put(1, "one");
        assertTrue("testContainsKey_found", map.containsKey(1), "expected key 1 to be present");
        assertTrue("testContainsKey_notFound", !map.containsKey(99), "expected key 99 to be absent");
    }

    static void testRemove() {
        CustomMap<Integer, String> map = new CustomMap<>();
        map.put(1, "one"); map.put(2, "two");
        map.remove(1);
        assertTrue("testRemove_gone", !map.containsKey(1), "expected key 1 to be removed");
        assertEq("testRemove_size", 1, map.size());
    }

    static void testSize() {
        CustomMap<Integer, String> map = new CustomMap<>();
        assertEq("testSize_zero", 0, map.size());
        map.put(1, "a"); map.put(2, "b");
        assertEq("testSize_two", 2, map.size());
    }

    static void testIsEmpty() {
        CustomMap<Integer, String> map = new CustomMap<>();
        assertTrue("testIsEmpty_true", map.isEmpty(), "expected empty on new map");
        map.put(1, "x");
        assertTrue("testIsEmpty_false", !map.isEmpty(), "expected not empty after put");
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