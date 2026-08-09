package tests;

import datastructures.CustomQueue;

public class CustomQueueTest {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  CustomQueue - Unit Tests (7 tests)");
        System.out.println("==================================================\n");

        testEnqueueAndDequeue();
        testPeek();
        testIsEmpty();
        testSize();
        testDequeueFromEmpty();
        testPeekFromEmpty();
        testFIFOOrder();

        System.out.println("==================================================");
        System.out.println("RESULTS: " + passed + " passed, " + failed + " failed");
        System.out.println("==================================================");
    }

    static void testEnqueueAndDequeue() {
        CustomQueue<Integer> q = new CustomQueue<>();
        q.enqueue(10);
        q.enqueue(20);
        assertEq("testEnqueueDequeue_first", 10, (int) q.dequeue());
        assertEq("testEnqueueDequeue_second", 20, (int) q.dequeue());
    }

    static void testPeek() {
        CustomQueue<Integer> q = new CustomQueue<>();
        q.enqueue(5);
        q.enqueue(15);
        assertEq("testPeek_front", 5, (int) q.peek());
        assertEq("testPeek_sizeUnchanged", 2, q.size());
    }

    static void testIsEmpty() {
        CustomQueue<String> q = new CustomQueue<>();
        assertTrue("testIsEmpty_true", q.isEmpty(), "expected empty on new queue");
        q.enqueue("x");
        assertTrue("testIsEmpty_false", !q.isEmpty(), "expected not empty after enqueue");
    }

    static void testSize() {
        CustomQueue<Integer> q = new CustomQueue<>();
        assertEq("testSize_zero", 0, q.size());
        q.enqueue(1); q.enqueue(2);
        assertEq("testSize_two", 2, q.size());
        q.dequeue();
        assertEq("testSize_afterDequeue", 1, q.size());
    }

    static void testDequeueFromEmpty() {
        CustomQueue<Integer> q = new CustomQueue<>();
        boolean threw = false;
        try {
            q.dequeue();
        } catch (RuntimeException e) {
            threw = true;
        }
        assertTrue("testDequeueFromEmpty_throws", threw,
                "expected exception when dequeuing empty queue");
    }

    static void testPeekFromEmpty() {
        CustomQueue<Integer> q = new CustomQueue<>();
        boolean threw = false;
        try {
            q.peek();
        } catch (RuntimeException e) {
            threw = true;
        }
        assertTrue("testPeekFromEmpty_throws", threw,
                "expected exception when peeking empty queue");
    }

    static void testFIFOOrder() {
        CustomQueue<Integer> q = new CustomQueue<>();
        for (int i = 1; i <= 5; i++) q.enqueue(i);
        for (int i = 1; i <= 5; i++) {
            assertEq("testFIFO_" + i, i, (int) q.dequeue());
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