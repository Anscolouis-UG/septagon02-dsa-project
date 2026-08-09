package tests;

import datastructures.CustomStack;

public class CustomStackTest {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  CustomStack - Unit Tests (7 tests)");
        System.out.println("==================================================\n");

        testPushAndPop();
        testPeek();
        testIsEmpty();
        testSize();
        testPopFromEmpty();
        testPeekFromEmpty();
        testMultiplePushPop();

        System.out.println("==================================================");
        System.out.println("RESULTS: " + passed + " passed, " + failed + " failed");
        System.out.println("==================================================");
    }

    static void testPushAndPop() {
        CustomStack<Integer> stack = new CustomStack<>();
        stack.push(10);
        stack.push(20);
        assertEq("testPushAndPop_top", 20, (int) stack.pop());
        assertEq("testPushAndPop_next", 10, (int) stack.pop());
    }

    static void testPeek() {
        CustomStack<Integer> stack = new CustomStack<>();
        stack.push(5);
        stack.push(15);
        assertEq("testPeek_top", 15, (int) stack.peek());
        assertEq("testPeek_sizeUnchanged", 2, stack.size());
    }

    static void testIsEmpty() {
        CustomStack<String> stack = new CustomStack<>();
        assertTrue("testIsEmpty_true", stack.isEmpty(), "expected empty on new stack");
        stack.push("a");
        assertTrue("testIsEmpty_false", !stack.isEmpty(), "expected not empty after push");
    }

    static void testSize() {
        CustomStack<Integer> stack = new CustomStack<>();
        assertEq("testSize_zero", 0, stack.size());
        stack.push(1); stack.push(2);
        assertEq("testSize_two", 2, stack.size());
        stack.pop();
        assertEq("testSize_afterPop", 1, stack.size());
    }

    static void testPopFromEmpty() {
        CustomStack<Integer> stack = new CustomStack<>();
        boolean threw = false;
        try {
            stack.pop();
        } catch (RuntimeException e) {
            threw = true;
        }
        assertTrue("testPopFromEmpty_throws", threw,
                "expected exception when popping empty stack");
    }

    static void testPeekFromEmpty() {
        CustomStack<Integer> stack = new CustomStack<>();
        boolean threw = false;
        try {
            stack.peek();
        } catch (RuntimeException e) {
            threw = true;
        }
        assertTrue("testPeekFromEmpty_throws", threw,
                "expected exception when peeking empty stack");
    }

    static void testMultiplePushPop() {
        CustomStack<Integer> stack = new CustomStack<>();
        for (int i = 1; i <= 5; i++) stack.push(i);
        for (int i = 5; i >= 1; i--) {
            assertEq("testMultiplePushPop_" + i, i, (int) stack.pop());
        }
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