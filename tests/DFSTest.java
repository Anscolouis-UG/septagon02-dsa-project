package tests;

import algorithms.DFS;
import datastructures.CustomArrayList;
import datastructures.CustomGraph;

public class DFSTest {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  DFS.java - Unit Tests & Traces (6 tests)");
        System.out.println("==================================================\n");

        test1_linearChainVisitsInOrder();
        test2_branchingGraphGoesDeepBeforeWide();
        test3_disconnectedComponentIsUnreachable();
        test4_singleIsolatedVertex();
        test5_cycleVisitsEachVertexExactlyOnce();
        test6_invalidStartVertexThrows();

        System.out.println("==================================================");
        System.out.println("RESULTS: " + passed + " passed, " + failed + " failed");
        System.out.println("==================================================");
    }

    private static void test1_linearChainVisitsInOrder() {
        System.out.println("Test 1: Linear chain 0-1-2-3");
        CustomGraph graph = new CustomGraph(4, false);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 3, 1);

        CustomArrayList<Integer> order = DFS.run(graph, 0);
        System.out.println("  Trace: DFSAlgorithm.run(0) = " + order);
        boolean ok = order.size() == 4
                && order.get(0) == 0
                && order.get(1) == 1
                && order.get(2) == 2
                && order.get(3) == 3;
        assertEquals("test1_linearChainVisitsInOrder", ok, "expected [0,1,2,3], got " + order);
        System.out.println();
    }

    private static void test2_branchingGraphGoesDeepBeforeWide() {
        System.out.println("Test 2: Branching graph — depth-first property");
        CustomGraph graph = new CustomGraph(6, false);
        graph.addEdge(0, 1, 1);
        graph.addEdge(0, 2, 1);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 4, 1);
        graph.addEdge(4, 5, 1);

        CustomArrayList<Integer> order = DFS.run(graph, 0);
        System.out.println("  Trace: DFSAlgorithm.run(0) = " + order);
        boolean depthFirst = order.size() == 6
                && order.get(0) == 0
                && order.get(1) == 1
                && order.get(2) == 3
                && order.get(3) == 2;
        assertEquals("test2_depthFirstOrder", depthFirst, "expected depth-first order [0,1,3,2,...], got " + order);
        System.out.println();
    }

    private static void test3_disconnectedComponentIsUnreachable() {
        System.out.println("Test 3: Disconnected graph — unreachable vertices skipped");
        CustomGraph graph = new CustomGraph(5, false);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(3, 4, 1);

        CustomArrayList<Integer> order = DFS.run(graph, 0);
        System.out.println("  Trace: DFSAlgorithm.run(0) = " + order);
        boolean ok = !containsVal(order, 3) && !containsVal(order, 4);
        assertTrue("test3_disconnectedComponentIsUnreachable", ok,
                "expected 3 and 4 to be unreachable from 0, got " + order);
        System.out.println();
    }

    private static void test4_singleIsolatedVertex() {
        System.out.println("Test 4: Isolated vertex, no edges");
        CustomGraph graph = new CustomGraph(3, false);
        graph.addVertex(0);

        CustomArrayList<Integer> order = DFS.run(graph, 0);
        System.out.println("  Trace: DFSAlgorithm.run(0) = " + order);
        boolean ok = order.size() == 1 && order.get(0) == 0;
        assertEquals("test4_singleIsolatedVertex", ok, "expected [0], got " + order);
        System.out.println();
    }

    private static void test5_cycleVisitsEachVertexExactlyOnce() {
        System.out.println("Test 5: Cyclic graph 0-1-2-3-0");
        CustomGraph graph = new CustomGraph(4, false);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 0, 1);

        CustomArrayList<Integer> order = DFS.run(graph, 0);
        System.out.println("  Trace: DFSAlgorithm.run(0) = " + order);
        // Count unique values
        int uniqueCount = 0;
        boolean[] seen = new boolean[4];
        for (int i = 0; i < order.size(); i++) {
            int v = order.get(i);
            if (!seen[v]) { seen[v] = true; uniqueCount++; }
        }
        assertEquals("test5_cycleVisitsEachVertexOnce", uniqueCount == 4,
                "expected 4 unique vertices, got " + uniqueCount);
        System.out.println();
    }

    private static void test6_invalidStartVertexThrows() {
        System.out.println("Test 6: Invalid start vertex (out of range)");
        CustomGraph graph = new CustomGraph(3, false);
        graph.addEdge(0, 1, 1);

        boolean threw = false;
        try {
            DFS.run(graph, 10);
        } catch (IndexOutOfBoundsException e) {
            threw = true;
            System.out.println("  Trace: caught expected exception -> " + e.getMessage());
        }
        assertTrue("test6_invalidStartVertexThrows", threw,
                "expected IndexOutOfBoundsException for vertex 10 on a 3-vertex graph");
        System.out.println();
    }

    private static void assertEquals(String testName, boolean condition, String detail) {
        if (condition) {
            System.out.println("  [PASS] " + testName);
            passed++;
        } else {
            System.out.println("  [FAIL] " + testName + " -- " + detail);
            failed++;
        }
    }

    private static boolean containsVal(CustomArrayList<Integer> list, int value) {
        for (int i = 0; i < list.size(); i++) if (list.get(i) == value) return true;
        return false;
    }

    private static void assertTrue(String testName, boolean condition, String detail) {
        if (condition) {
            System.out.println("  [PASS] " + testName);
            passed++;
        } else {
            System.out.println("  [FAIL] " + testName + " -- " + detail);
            failed++;
        }
    }
}