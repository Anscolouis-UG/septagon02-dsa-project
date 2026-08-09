package tests;

import datastructures.CustomGraph;

/**
 * CustomGraphTest.java
 * Author: Nana Ekow Gyakye-Smith (22379633)
 *
 * Uses a small sample of real Legon campus locations (from locations.csv):
 *   0 = Main Gate, 1 = Balme Library, 2 = Great Hall,
 *   3 = Department of Computer Science, 4 = Night Market, 5 = Sports Stadium
 */
public class CustomGraphTest {
    private static int passed = 0;
    private static int failed = 0;

    private static final int MAIN_GATE    = 0;
    private static final int BALME        = 1;
    private static final int GREAT_HALL   = 2;
    private static final int DCS          = 3;
    private static final int NIGHT_MARKET = 4;
    private static final int SPORTS       = 5;

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  CustomGraph - Unit Tests (10 tests)");
        System.out.println("==================================================\n");

        testAddVertexIncreasesVertexCount();
        testAddEdgeCreatesUndirectedConnection();
        testDirectedGraphDoesNotAddReverseEdge();
        testEdgeWeightMatchesDistance();
        testGetNeighborsReturnsDirectConnections();
        testBfsVisitsAllReachableLocations();
        testDfsVisitsAllReachableLocations();
        testIsolatedVertexHasNoNeighbors();
        testHasEdgeFalseForNonExistentRoad();
        testNegativeWeightRejected();

        System.out.println("==================================================");
        System.out.println("RESULTS: " + passed + " passed, " + failed + " failed");
        System.out.println("==================================================");
    }

    // Build: Main Gate-Balme-Great Hall-DCS-Night Market chain; Sports isolated
    private static CustomGraph buildGraph() {
        CustomGraph g = new CustomGraph(6, false);
        g.addEdge(MAIN_GATE, BALME, 450.0);
        g.addEdge(BALME, GREAT_HALL, 160.0);
        g.addEdge(GREAT_HALL, DCS, 300.0);
        g.addEdge(DCS, NIGHT_MARKET, 700.0);
        return g;
    }

    static void testAddVertexIncreasesVertexCount() {
        CustomGraph g = new CustomGraph(10, false);
        g.addVertex(MAIN_GATE);
        g.addVertex(BALME);
        assertEq("testAddVertex_count", 2, g.vertexCount());
    }

    static void testAddEdgeCreatesUndirectedConnection() {
        CustomGraph g = buildGraph();
        assertTrue("testUndirected_forward",  g.hasEdge(MAIN_GATE, BALME),
                "expected edge Main Gate -> Balme");
        assertTrue("testUndirected_reverse",  g.hasEdge(BALME, MAIN_GATE),
                "expected reverse edge Balme -> Main Gate (undirected)");
    }

    static void testDirectedGraphDoesNotAddReverseEdge() {
        CustomGraph oneWay = new CustomGraph(4, true);
        oneWay.addEdge(0, 1, 100.0);
        assertTrue("testDirected_forward",   oneWay.hasEdge(0, 1), "expected 0->1");
        assertTrue("testDirected_noReverse", !oneWay.hasEdge(1, 0), "expected no 1->0 in directed graph");
    }

    static void testEdgeWeightMatchesDistance() {
        CustomGraph g = buildGraph();
        assertDouble("testWeight_mainGateBalme", 450.0, g.getEdgeWeight(MAIN_GATE, BALME), 0.001);
    }

    static void testGetNeighborsReturnsDirectConnections() {
        CustomGraph g = buildGraph();
        java.util.List<Integer> neighbors = g.getNeighbors(BALME);
        assertTrue("testNeighbors_mainGate", neighbors.contains(MAIN_GATE),
                "expected Main Gate to be a neighbor of Balme");
        assertTrue("testNeighbors_greatHall", neighbors.contains(GREAT_HALL),
                "expected Great Hall to be a neighbor of Balme");
        assertEq("testNeighbors_count", 2, neighbors.size());
    }

    static void testBfsVisitsAllReachableLocations() {
        CustomGraph g = buildGraph();
        java.util.List<Integer> order = g.bfs(MAIN_GATE);
        assertEq("testBFS_reachableCount", 5, order.size());
        assertTrue("testBFS_containsMainGate", order.contains(MAIN_GATE), "expected Main Gate");
        assertTrue("testBFS_containsNightMarket", order.contains(NIGHT_MARKET), "expected Night Market");
        assertTrue("testBFS_excludesSports", !order.contains(SPORTS), "Sports should be unreachable");
    }

    static void testDfsVisitsAllReachableLocations() {
        CustomGraph g = buildGraph();
        java.util.List<Integer> order = g.dfs(MAIN_GATE);
        assertEq("testDFS_sameCount", g.bfs(MAIN_GATE).size(), order.size());
        assertEq("testDFS_startsAtSource", MAIN_GATE, (int) order.get(0));
    }

    static void testIsolatedVertexHasNoNeighbors() {
        CustomGraph g = buildGraph();
        g.addVertex(SPORTS);
        assertTrue("testIsolated_noNeighbors", g.getNeighbors(SPORTS).isEmpty(),
                "expected Sports Stadium to have no neighbors");
    }

    static void testHasEdgeFalseForNonExistentRoad() {
        CustomGraph g = buildGraph();
        assertTrue("testNoEdge_mainGateSports", !g.hasEdge(MAIN_GATE, SPORTS),
                "expected no edge between Main Gate and Sports Stadium");
    }

    static void testNegativeWeightRejected() {
        CustomGraph g = buildGraph();
        boolean threw = false;
        try {
            g.addEdge(MAIN_GATE, GREAT_HALL, -50.0);
        } catch (IllegalArgumentException e) {
            threw = true;
        }
        assertTrue("testNegativeWeight_throws", threw,
                "expected IllegalArgumentException for negative weight");
    }

    // ── Helpers ──────────────────────────────────────────────────────
    private static void assertEq(String name, int expected, int actual) {
        if (expected == actual) { System.out.println("  [PASS] " + name); passed++; }
        else { System.out.println("  [FAIL] " + name + " -- expected " + expected + ", got " + actual); failed++; }
    }
    private static void assertDouble(String name, double expected, double actual, double epsilon) {
        if (Math.abs(expected - actual) < epsilon) { System.out.println("  [PASS] " + name); passed++; }
        else { System.out.println("  [FAIL] " + name + " -- expected " + expected + ", got " + actual); failed++; }
    }
    private static void assertTrue(String name, boolean cond, String detail) {
        if (cond) { System.out.println("  [PASS] " + name); passed++; }
        else { System.out.println("  [FAIL] " + name + " -- " + detail); failed++; }
    }
}