

import algorithms.Dijkstra;
import datastructures.CustomGraph;


public class DijkstraTest {
    private static int passed = 0;
    private static int failed = 0;
    private static final double EPSILON = 1e-9;

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  Dijkstra.java - Unit Tests & Traces (6 tests)");
        System.out.println("==================================================\n");

        test1_knownShortestDistances();
        test2_sourceDistanceIsAlwaysZero();
        test3_unreachableVertexIsInfinity();
        test4_singleVertexGraph();
        test5_shorterTwoHopPathBeatsDirectEdge();
        test6_parallelEdgesKeepMinimumWeight();

        System.out.println("==================================================");
        System.out.println("RESULTS: " + passed + " passed, " + failed + " failed");
        System.out.println("==================================================");
    }

    private static void test1_knownShortestDistances() {
        System.out.println("Test 1: Known shortest distances on a 5-node graph");
        CustomGraph graph = new CustomGraph(5, false);
        graph.addEdge(0, 1, 4);
        graph.addEdge(0, 2, 1);
        graph.addEdge(2, 1, 2);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 3, 5);
        graph.addEdge(3, 4, 3);

        double[] dist = Dijkstra.run(graph, 5, 0);
        System.out.println("  Trace: distances from 0 = " + java.util.Arrays.toString(dist));
        assertEquals("test1_distTo0", 0.0, dist[0]);
        assertEquals("test1_distTo1", 3.0, dist[1]);
        assertEquals("test1_distTo2", 1.0, dist[2]);
        assertEquals("test1_distTo3", 4.0, dist[3]);
        assertEquals("test1_distTo4", 7.0, dist[4]);
        System.out.println();
    }

    private static void test2_sourceDistanceIsAlwaysZero() {
        System.out.println("Test 2: Source-to-source distance is always 0");
        CustomGraph graph = new CustomGraph(4, false);
        graph.addEdge(0, 1, 10);
        graph.addEdge(1, 2, 20);
        graph.addEdge(2, 3, 30);

        double[] dist = Dijkstra.run(graph, 4, 2);
        System.out.println("  Trace: distances from 2 = " + java.util.Arrays.toString(dist));
        assertEquals("test2_sourceDistanceIsZero", 0.0, dist[2]);
        System.out.println();
    }

    private static void test3_unreachableVertexIsInfinity() {
        System.out.println("Test 3: Unreachable vertex stays INFINITY");
        CustomGraph graph = new CustomGraph(4, false);
        graph.addEdge(0, 1, 5);

        graph.addEdge(2, 3, 5);

        double[] dist = Dijkstra.run(graph, 4, 0);
        System.out.println("  Trace: distances from 0 = " + java.util.Arrays.toString(dist));
        assertTrue("test3_vertex2Unreachable", dist[2] == Dijkstra.INFINITY,
                "expected INFINITY, got " + dist[2]);
        assertTrue("test3_vertex3Unreachable", dist[3] == Dijkstra.INFINITY,
                "expected INFINITY, got " + dist[3]);
        System.out.println();
    }

    private static void test4_singleVertexGraph() {
        System.out.println("Test 4: Single-vertex graph");
        CustomGraph graph = new CustomGraph(1, false);
        graph.addVertex(0);

        double[] dist = Dijkstra.run(graph, 1, 0);
        System.out.println("  Trace: distances from 0 = " + java.util.Arrays.toString(dist));
        assertEquals("test4_singleVertexDistanceIsZero", 0.0, dist[0]);
        System.out.println();
    }

    private static void test5_shorterTwoHopPathBeatsDirectEdge() {
        System.out.println("Test 5: Two-hop path beats an expensive direct edge");
        CustomGraph graph = new CustomGraph(3, false);
        graph.addEdge(0, 2, 10);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);

        double[] dist = Dijkstra.run(graph, 3, 0);
        System.out.println("  Trace: distances from 0 = " + java.util.Arrays.toString(dist));
        assertEquals("test5_twoHopPathWins", 2.0, dist[2]);
        System.out.println();
    }

    private static void test6_parallelEdgesKeepMinimumWeight() {
        System.out.println("Test 6: Parallel edges — minimum weight wins");
        CustomGraph graph = new CustomGraph(2, false);
        graph.addEdge(0, 1, 5);
        graph.addEdge(0, 1, 2);

        double[] dist = Dijkstra.run(graph, 2, 0);
        System.out.println("  Trace: distances from 0 = " + java.util.Arrays.toString(dist));
        assertEquals("test6_parallelEdgesMinimumWins", 2.0, dist[1]);
        System.out.println();
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