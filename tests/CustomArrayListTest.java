package tests;

import datastructures.CustomArrayList;

public class CustomArrayListTest {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  CustomArrayList - Unit Tests (8 tests)");
        System.out.println("==================================================\n");

        testAdd();
        testInsert();
        testSet();
        testRemove();
        testClear();
        testInvalidIndex();
        testResizeKeepsAllElements();
        testInsertAtEnd();

        System.out.println("==================================================");
        System.out.println("RESULTS: " + passed + " passed, " + failed + " failed");
        System.out.println("==================================================");
    }

    static void testAdd() {
        CustomArrayList<Integer> list = new CustomArrayList<>();
        list.add(10); list.add(20); list.add(30);
        assertEq("testAdd_size", 3, list.size());
        assertEq("testAdd_get0", 10, (int) list.get(0));
        assertEq("testAdd_get1", 20, (int) list.get(1));
        assertEq("testAdd_get2", 30, (int) list.get(2));
    }

    static void testInsert() {
        CustomArrayList<Integer> list = new CustomArrayList<>();
        list.add(10); list.add(30);
        list.insert(1, 20);
        assertEq("testInsert_size", 3, list.size());
        assertEq("testInsert_middle", 20, (int) list.get(1));
    }

    static void testSet() {
        CustomArrayList<String> list = new CustomArrayList<>();
        list.add("Java");
        list.set(0, "Python");
        assertEq("testSet_value", "Python", list.get(0));
    }

    static void testRemove() {
        CustomArrayList<Integer> list = new CustomArrayList<>();
        list.add(10); list.add(20); list.add(30);
        int removed = list.remove(1);
        assertEq("testRemove_returnValue", 20, removed);
        assertEq("testRemove_size", 2, list.size());
        assertEq("testRemove_shifted", 30, (int) list.get(1));
    }

    static void testClear() {
        CustomArrayList<Integer> list = new CustomArrayList<>();
        list.add(1); list.add(2);
        list.clear();
        assertTrue("testClear_isEmpty", list.isEmpty(), "expected isEmpty() after clear()");
    }

    static void testInvalidIndex() {
        CustomArrayList<Integer> list = new CustomArrayList<>();
        boolean threw = false;
        try {
            list.get(0);
        } catch (IndexOutOfBoundsException e) {
            threw = true;
        }
        assertTrue("testInvalidIndex_throws", threw,
                "expected IndexOutOfBoundsException on empty list get(0)");
    }

    static void testResizeKeepsAllElements() {
        CustomArrayList<Integer> list = new CustomArrayList<>();
        for (int i = 0; i < 11; i++) list.add(i);
        assertEq("testResize_size", 11, list.size());
        assertEq("testResize_first", 0, (int) list.get(0));
        assertEq("testResize_last", 10, (int) list.get(10));
    }

    static void testInsertAtEnd() {
        CustomArrayList<Integer> list = new CustomArrayList<>();
        list.add(10);
        list.insert(list.size(), 20);
        assertEq("testInsertAtEnd_size", 2, list.size());
        assertEq("testInsertAtEnd_last", 20, (int) list.get(1));
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