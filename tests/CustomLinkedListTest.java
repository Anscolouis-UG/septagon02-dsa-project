package tests;

import datastructures.CustomLinkedList;

public class CustomLinkedListTest {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  CustomLinkedList - Unit Tests (8 tests)");
        System.out.println("==================================================\n");

        testAddFirst();
        testAddLast();
        testGet();
        testRemoveFirst();
        testRemoveLast();
        testSize();
        testIsEmpty();
        testGetInvalidIndex();

        System.out.println("==================================================");
        System.out.println("RESULTS: " + passed + " passed, " + failed + " failed");
        System.out.println("==================================================");
    }

    static void testAddFirst() {
        CustomLinkedList<Integer> list = new CustomLinkedList<>();
        list.addFirst(20);
        list.addFirst(10);
        assertEq("testAddFirst_head", 10, (int) list.get(0));
        assertEq("testAddFirst_second", 20, (int) list.get(1));
    }

    static void testAddLast() {
        CustomLinkedList<Integer> list = new CustomLinkedList<>();
        list.addLast(10);
        list.addLast(20);
        assertEq("testAddLast_first", 10, (int) list.get(0));
        assertEq("testAddLast_second", 20, (int) list.get(1));
    }

    static void testGet() {
        CustomLinkedList<String> list = new CustomLinkedList<>();
        list.addLast("a");
        list.addLast("b");
        list.addLast("c");
        assertEq("testGet_0", "a", list.get(0));
        assertEq("testGet_1", "b", list.get(1));
        assertEq("testGet_2", "c", list.get(2));
    }

    static void testRemoveFirst() {
        CustomLinkedList<Integer> list = new CustomLinkedList<>();
        list.addLast(1); list.addLast(2); list.addLast(3);
        int removed = list.remove(1); // remove by value
        assertEq("testRemoveFirst_value", 1, removed);
        assertEq("testRemoveFirst_newHead", 2, (int) list.get(0));
        assertEq("testRemoveFirst_size", 2, list.size());
    }

    static void testRemoveLast() {
        CustomLinkedList<Integer> list = new CustomLinkedList<>();
        list.addLast(1); list.addLast(2); list.addLast(3);
        int removed = list.remove(3); // remove by value
        assertEq("testRemoveLast_value", 3, removed);
        assertEq("testRemoveLast_size", 2, list.size());
    }

    static void testSize() {
        CustomLinkedList<Integer> list = new CustomLinkedList<>();
        assertEq("testSize_empty", 0, list.size());
        list.addLast(1);
        assertEq("testSize_one", 1, list.size());
        list.addLast(2);
        assertEq("testSize_two", 2, list.size());
    }

    static void testIsEmpty() {
        CustomLinkedList<Integer> list = new CustomLinkedList<>();
        assertTrue("testIsEmpty_true", list.isEmpty(), "expected true on empty list");
        list.addLast(1);
        assertTrue("testIsEmpty_false", !list.isEmpty(), "expected false after adding element");
    }

    static void testGetInvalidIndex() {
        CustomLinkedList<Integer> list = new CustomLinkedList<>();
        list.addLast(10);
        boolean threw = false;
        try {
            list.get(1);
        } catch (IndexOutOfBoundsException e) {
            threw = true;
        }
        assertTrue("testGetInvalidIndex_throws", threw,
                "expected IndexOutOfBoundsException for out-of-range index");
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