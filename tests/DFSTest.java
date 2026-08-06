

import algorithms.DFS;
import datastructures.CustomGraph;

import java.util.*;

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

        List<Integer> order = DFS.run(graph, 0);
        System.out.println("  Trace: DFSAlgorithm.run(0) = " + order);
        assertEquals("test1_linearChainVisitsInOrder", Arrays.asList(0, 1, 2, 3), order);
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

        List<Integer> order = DFS.run(graph, 0);
        System.out.println("  Trace: DFSAlgorithm.run(0) = " + order);

        assertEquals("test2_depthFirstOrder", Arrays.asList(0, 1, 3, 2, 4, 5), order);
        System.out.println();
    }

    private static void test3_disconnectedComponentIsUnreachable() {
        System.out.println("Test 3: Disconnected graph — unreachable vertices skipped");
        CustomGraph graph = new CustomGraph(5, false);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(3, 4, 1);

        List<Integer> order = DFS.run(graph, 0);
        System.out.println("  Trace: DFSAlgorithm.run(0) = " + order);
        assertTrue("test3_disconnectedComponentIsUnreachable",
                !order.contains(3) && !order.contains(4),
                "expected 3 and 4 to be unreachable from 0, got " + order);
        System.out.println();
    }

    private static void test4_singleIsolatedVertex() {
        System.out.println("Test 4: Isolated vertex, no edges");
        CustomGraph graph = new CustomGraph(3, false);
        graph.addVertex(0);

        List<Integer> order = DFS.run(graph, 0);
        System.out.println("  Trace: DFSAlgorithm.run(0) = " + order);
        assertEquals("test4_singleIsolatedVertex", List.of(0), order);
        System.out.println();
    }

    private static void test5_cycleVisitsEachVertexExactlyOnce() {
        System.out.println("Test 5: Cyclic graph 0-1-2-3-0");
        CustomGraph graph = new CustomGraph(4, false);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 0, 1);

        List<Integer> order = DFS.run(graph, 0);
        System.out.println("  Trace: DFSAlgorithm.run(0) = " + order);
        Set<Integer> unique = new HashSet<>(order);
        assertEquals("test5_cycleVisitsEachVertexOnce", 4, unique.size());
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

    private static void assertEquals(String testName, Object expected, Object actual) {
        if (Objects.equals(expected, actual)) {
            System.out.println("  [PASS] " + testName);
            passed++;
        } else {
            System.out.println("  [FAIL] " + testName + " -- expected: " + expected + ", actual: " + actual);
            failed++;
        }
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