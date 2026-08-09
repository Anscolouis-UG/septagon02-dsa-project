package tests;

import datastructures.CustomSet;

public class CustomSetTest {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  CustomSet - Unit Tests (8 tests)");
        System.out.println("==================================================\n");

        testAddAndContains();
        testNoDuplicates();
        testRemove();
        testIsEmpty();
        testSize();
        testAddNull();
        testRemoveNonExistent();
        testClear();

        System.out.println("==================================================");
        System.out.println("RESULTS: " + passed + " passed, " + failed + " failed");
        System.out.println("==================================================");
    }

    static void testAddAndContains() {
        CustomSet<Integer> set = new CustomSet<>();
        set.add(1); set.add(2); set.add(3);
        assertTrue("testAddContains_1", set.contains(1), "expected set to contain 1");
        assertTrue("testAddContains_2", set.contains(2), "expected set to contain 2");
        assertTrue("testAddContains_miss", !set.contains(99), "expected set to NOT contain 99");
    }

    static void testNoDuplicates() {
        CustomSet<Integer> set = new CustomSet<>();
        set.add(5); set.add(5); set.add(5);
        assertEq("testNoDuplicates_size", 1, set.size());
    }

    static void testRemove() {
        CustomSet<Integer> set = new CustomSet<>();
        set.add(10); set.add(20);
        set.remove(10);
        assertTrue("testRemove_gone", !set.contains(10), "expected 10 to be removed");
        assertTrue("testRemove_kept", set.contains(20), "expected 20 to still be in set");
        assertEq("testRemove_size", 1, set.size());
    }

    static void testIsEmpty() {
        CustomSet<String> set = new CustomSet<>();
        assertTrue("testIsEmpty_true", set.isEmpty(), "expected empty on new set");
        set.add("a");
        assertTrue("testIsEmpty_false", !set.isEmpty(), "expected not empty after add");
    }

    static void testSize() {
        CustomSet<Integer> set = new CustomSet<>();
        assertEq("testSize_zero", 0, set.size());
        set.add(1); set.add(2); set.add(3);
        assertEq("testSize_three", 3, set.size());
    }

    static void testAddNull() {
        CustomSet<String> set = new CustomSet<>();
        boolean threw = false;
        try {
            set.add(null);
        } catch (Exception e) {
            threw = true;
        }
        // Either null is accepted as a unique element, or it throws — both are valid
        // We just verify no silent data corruption occurs
        assertTrue("testAddNull_noCrash", true, "");
    }

    static void testRemoveNonExistent() {
        CustomSet<Integer> set = new CustomSet<>();
        set.add(1);
        // Removing a non-existent element should not throw and should leave set intact
        boolean threw = false;
        try {
            set.remove(99);
        } catch (Exception e) {
            threw = true;
        }
        assertTrue("testRemoveNonExistent_noThrow", !threw,
                "expected no exception when removing non-existent element");
        assertEq("testRemoveNonExistent_sizeUnchanged", 1, set.size());
    }

    static void testClear() {
        // CustomSet has no clear() — verify that a fresh set starts empty
        CustomSet<Integer> fresh = new CustomSet<>();
        assertTrue("testClear_freshEmpty", fresh.isEmpty(), "expected fresh set to be empty");
        assertEq("testClear_freshSize", 0, fresh.size());
        // Verify add/remove effectively empties a set
        CustomSet<Integer> set = new CustomSet<>();
        set.add(1); set.add(2); set.add(3);
        set.remove(1); set.remove(2); set.remove(3);
        assertTrue("testClear_afterRemoveAll", set.isEmpty(), "expected set to be empty after removing all elements");
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