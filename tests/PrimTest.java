

import algorithms.GraphEdge;
import algorithms.Prim;
import datastructures.CustomDisjointSet;
import datastructures.CustomGraph;


public class PrimTest {
    private static int passed = 0;
    private static int failed = 0;
    private static final double EPSILON = 1e-9;

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  Prim.java - Unit Tests & Traces (6 tests)");
        System.out.println("==================================================\n");

        test1_totalWeightMatchesKnownMST();
        test2_edgeCountIsVMinusOne();
        test3_resultingEdgesFormNoCycle();
        test4_singleVertexGraphHasNoEdges();
        test5_startNodeDoesNotChangeTotalWeight();
        test6_disconnectedGraphReturnsPartialTree();

        System.out.println("==================================================");
        System.out.println("RESULTS: " + passed + " passed, " + failed + " failed");
        System.out.println("==================================================");
    }

    private static CustomGraph sampleGraph() {
        CustomGraph graph = new CustomGraph(5, false);
        graph.addEdge(0, 1, 4);
        graph.addEdge(0, 2, 1);
        graph.addEdge(2, 1, 2);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 3, 5);
        graph.addEdge(3, 4, 3);
        return graph;
    }

    private static void test1_totalWeightMatchesKnownMST() {
        System.out.println("Test 1: Known MST weight on a 5-node graph");
        java.util.List<GraphEdge> mst = Prim.run(sampleGraph(), 5, 0);
        double total = totalWeight(mst);
        System.out.println("  Trace: MST edges = " + mst + ", total = " + total);
        assertEquals("test1_totalMSTWeight", 7.0, total);
        System.out.println();
    }

    private static void test2_edgeCountIsVMinusOne() {
        System.out.println("Test 2: Edge count is V-1 for a connected graph");
        java.util.List<GraphEdge> mst = Prim.run(sampleGraph(), 5, 0);
        System.out.println("  Trace: MST edge count = " + mst.size());
        assertEquals("test2_edgeCountIsFour", 4, mst.size());
        System.out.println();
    }

    private static void test3_resultingEdgesFormNoCycle() {
        System.out.println("Test 3: MST edges contain no cycle (checked via CustomDisjointSet)");
        java.util.List<GraphEdge> mst = Prim.run(sampleGraph(), 5, 0);
        System.out.println("  Trace: MST edges = " + mst);

        CustomDisjointSet dsu = new CustomDisjointSet(5);
        boolean cycleFound = false;
        for (GraphEdge e : mst) {
            if (!dsu.union(e.source, e.destination)) {
                cycleFound = true;
            }
        }
        assertTrue("test3_noCycleInMST", !cycleFound, "found a redundant edge implying a cycle");
        assertEquals("test3_singleConnectedComponent", 1, dsu.getSetCount());
        System.out.println();
    }

    private static void test4_singleVertexGraphHasNoEdges() {
        System.out.println("Test 4: Single-vertex graph has no MST edges");
        CustomGraph graph = new CustomGraph(1, false);
        graph.addVertex(0);

        java.util.List<GraphEdge> mst = Prim.run(graph, 1, 0);
        System.out.println("  Trace: MST edges = " + mst);
        assertEquals("test4_noEdgesForSingleVertex", 0, mst.size());
        System.out.println();
    }

    private static void test5_startNodeDoesNotChangeTotalWeight() {
        System.out.println("Test 5: Total weight is the same regardless of start vertex");
        double totalFrom0 = totalWeight(Prim.run(sampleGraph(), 5, 0));
        double totalFrom4 = totalWeight(Prim.run(sampleGraph(), 5, 4));
        System.out.println("  Trace: total from start=0 -> " + totalFrom0 + ", from start=4 -> " + totalFrom4);
        assertEquals("test5_sameWeightRegardlessOfStart", totalFrom0, totalFrom4);
        System.out.println();
    }

    private static void test6_disconnectedGraphReturnsPartialTree() {
        System.out.println("Test 6: Disconnected graph — MST limited to reachable component");
        CustomGraph graph = new CustomGraph(5, false);
        graph.addEdge(0, 1, 2);
        graph.addEdge(1, 2, 3);

        graph.addEdge(3, 4, 1);

        java.util.List<GraphEdge> mst = Prim.run(graph, 5, 0);
        System.out.println("  Trace: MST edges from start=0 = " + mst);
        assertTrue("test6_partialTreeSmallerThanVMinusOne", mst.size() < 4,
                "expected fewer than 4 edges since the graph is disconnected, got " + mst.size());
        assertEquals("test6_reachedTwoEdgesInComponent", 2, mst.size());
        System.out.println();
    }

    private static double totalWeight(java.util.List<GraphEdge> edges) {
        double total = 0;
        for (GraphEdge e : edges) total += e.weight;
        return total;
    }

    private static void assertEquals(String testName, double expected, double actual) {
        if (Math.abs(expected - actual) < EPSILON) {
            System.out.println("  [PASS] " + testName);
            passed++;
        } else {
            System.out.println("  [FAIL] " + testName + " -- expected: " + expected + ", actual: " + actual);
            failed++;
        }
    }

    private static void assertEquals(String testName, int expected, int actual) {
        if (expected == actual) {
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