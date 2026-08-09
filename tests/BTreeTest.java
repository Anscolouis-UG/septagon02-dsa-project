package tests;

import datastructures.BTree;

public class BTreeTest {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  BTree - Unit Tests (6 tests)");
        System.out.println("==================================================\n");

        testInsertAndSearch();
        testSplitTriggeringInsertion();
        testSearchOnEmptyTree();
        testSizeStartsAtZero();
        testOverwriteExistingKey();
        testTraverseIsInOrder();

        System.out.println("==================================================");
        System.out.println("RESULTS: " + passed + " passed, " + failed + " failed");
        System.out.println("==================================================");
    }

    static void testInsertAndSearch() {
        BTree<Integer, String> tree = new BTree<>();
        tree.insert(10, "ten");
        tree.insert(20, "twenty");
        tree.insert(5, "five");
        tree.insert(15, "fifteen");
        tree.insert(25, "twenty-five");

        assertEq("testInsertSearch_size", 5, tree.size());
        assertEq("testInsertSearch_twenty", "twenty", tree.search(20));
        assertEq("testInsertSearch_five", "five", tree.search(5));
        assertTrue("testInsertSearch_miss", tree.search(99) == null, "expected null for key 99");
    }

    static void testSplitTriggeringInsertion() {
        BTree<Integer, String> tree = new BTree<>(2);
        for (int i = 1; i <= 10; i++) {
            tree.insert(i, "value" + i);
        }
        assertEq("testSplit_size", 10, tree.size());
        assertEq("testSplit_value6", "value6", tree.search(6));
        assertTrue("testSplit_miss", tree.search(11) == null, "expected null for key 11");
        assertTrue("testSplit_traverseContains6", tree.traverse().contains("6=value6"),
                "expected traversal to contain '6=value6'");
    }

    static void testSearchOnEmptyTree() {
        BTree<Integer, String> tree = new BTree<>();
        assertTrue("testEmpty_null", tree.search(1) == null,
                "expected null on empty tree search");
    }

    static void testSizeStartsAtZero() {
        BTree<Integer, String> tree = new BTree<>();
        assertEq("testSizeZero", 0, tree.size());
    }

    static void testOverwriteExistingKey() {
        BTree<Integer, String> tree = new BTree<>();
        tree.insert(1, "original");
        tree.insert(1, "overwritten");
        assertTrue("testOverwrite_notNull", tree.search(1) != null,
                "expected non-null result after overwrite");
    }

    static void testTraverseIsInOrder() {
        BTree<Integer, String> tree = new BTree<>();
        tree.insert(30, "thirty");
        tree.insert(10, "ten");
        tree.insert(20, "twenty");
        String traversal = tree.traverse();
        assertTrue("testTraverse_10before20", traversal.indexOf("10=") < traversal.indexOf("20="),
                "expected 10 before 20 in traversal");
        assertTrue("testTraverse_20before30", traversal.indexOf("20=") < traversal.indexOf("30="),
                "expected 20 before 30 in traversal");
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