

import algorithms.GraphEdge;
import algorithms.Kruskal;
import algorithms.Prim;
import datastructures.CustomDisjointSet;
import datastructures.CustomGraph;

import java.util.*;

public class KruskalTest {
    private static int passed = 0;
    private static int failed = 0;
    private static final double EPSILON = 1e-9;

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  Kruskal.java - Unit Tests & Traces (6 tests)");
        System.out.println("==================================================\n");

        test1_totalWeightMatchesKnownMST();
        test2_edgeCountIsVMinusOne();
        test3_resultFormsSingleConnectedTree();
        test4_totalWeightMatchesPrimsResult();
        test5_disconnectedGraphProducesForest();
        test6_edgeListOverloadMatchesGraphOverload();

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
        List<GraphEdge> mst = Kruskal.run(sampleGraph(), 5);
        double total = totalWeight(mst);
        System.out.println("  Trace: MST edges = " + mst + ", total = " + total);
        assertEquals("test1_totalMSTWeight", 7.0, total);
        System.out.println();
    }

    private static void test2_edgeCountIsVMinusOne() {
        System.out.println("Test 2: Edge count is V-1 for a connected graph");
        List<GraphEdge> mst = Kruskal.run(sampleGraph(), 5);
        System.out.println("  Trace: MST edge count = " + mst.size());
        assertEquals("test2_edgeCountIsFour", 4, mst.size());
        System.out.println();
    }

    private static void test3_resultFormsSingleConnectedTree() {
        System.out.println("Test 3: MST forms a single connected component (checked via CustomDisjointSet)");
        List<GraphEdge> mst = Kruskal.run(sampleGraph(), 5);
        System.out.println("  Trace: MST edges = " + mst);

        CustomDisjointSet dsu = new CustomDisjointSet(5);
        for (GraphEdge e : mst) dsu.union(e.source, e.destination);
        assertEquals("test3_singleConnectedComponent", 1, dsu.getSetCount());
        System.out.println();
    }

    private static void test4_totalWeightMatchesPrimsResult() {
        System.out.println("Test 4: Kruskal's total weight matches Prim's on the same graph");
        double kruskalTotal = totalWeight(Kruskal.run(sampleGraph(), 5));
        double primTotal = totalWeight(Prim.run(sampleGraph(), 5, 0));
        System.out.println("  Trace: Kruskal total = " + kruskalTotal + ", Prim total = " + primTotal);
        assertEquals("test4_kruskalMatchesPrim", primTotal, kruskalTotal);
        System.out.println();
    }

    private static void test5_disconnectedGraphProducesForest() {
        System.out.println("Test 5: Disconnected graph — minimum spanning forest");
        CustomGraph graph = new CustomGraph(5, false);
        graph.addEdge(0, 1, 2);
        graph.addEdge(1, 2, 3);

        graph.addEdge(3, 4, 1);

        List<GraphEdge> mst = Kruskal.run(graph, 5);
        System.out.println("  Trace: forest edges = " + mst);
        assertEquals("test5_forestHasThreeEdges", 3, mst.size());

        CustomDisjointSet dsu = new CustomDisjointSet(5);
        for (GraphEdge e : mst) dsu.union(e.source, e.destination);
        assertEquals("test5_twoComponentsRemain", 2, dsu.getSetCount());
        System.out.println();
    }

    private static void test6_edgeListOverloadMatchesGraphOverload() {
        System.out.println("Test 6: List<GraphEdge> overload matches CustomGraph overload");
        List<GraphEdge> edges = new ArrayList<>();
        edges.add(new GraphEdge(0, 1, 4));
        edges.add(new GraphEdge(0, 2, 1));
        edges.add(new GraphEdge(2, 1, 2));
        edges.add(new GraphEdge(1, 3, 1));
        edges.add(new GraphEdge(2, 3, 5));
        edges.add(new GraphEdge(3, 4, 3));

        double totalFromEdgeList = totalWeight(Kruskal.run(edges, 5));
        double totalFromGraph = totalWeight(Kruskal.run(sampleGraph(), 5));
        System.out.println("  Trace: edge-list total = " + totalFromEdgeList + ", graph total = " + totalFromGraph);
        assertEquals("test6_bothOverloadsAgree", totalFromGraph, totalFromEdgeList);
        System.out.println();
    }

    private static double totalWeight(List<GraphEdge> edges) {
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
}