package tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import datastructures.CustomGraph;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CustomGraphTest.java
 * Author: Nana Ekow Gyakye-Smith (22379633)
 *
 * Uses a small sample of real Legon campus locations (from locations.csv)
 * so trace tables in the report can reference real building names:
 *   0 = Main Gate, 1 = Balme Library, 2 = Great Hall,
 *   3 = Department of Computer Science, 4 = Night Market, 5 = Sports Stadium
 */
public class CustomGraphTest {

    private CustomGraph graph;

    private static final int MAIN_GATE = 0;
    private static final int BALME_LIBRARY = 1;
    private static final int GREAT_HALL = 2;
    private static final int DCS = 3;
    private static final int NIGHT_MARKET = 4;
    private static final int SPORTS_STADIUM = 5;

    @BeforeEach
    void setUp() {
        graph = new CustomGraph(6, false); // undirected: pedestrian roads are two-way
        graph.addEdge(MAIN_GATE, BALME_LIBRARY, 450.0);
        graph.addEdge(BALME_LIBRARY, GREAT_HALL, 160.0);
        graph.addEdge(GREAT_HALL, DCS, 300.0);
        graph.addEdge(DCS, NIGHT_MARKET, 700.0);
        // SPORTS_STADIUM (5) is deliberately left unconnected for isolation tests
    }

    @Test
    void testAddVertexIncreasesVertexCount() {
        CustomGraph g = new CustomGraph(10, false);
        g.addVertex(MAIN_GATE);
        g.addVertex(BALME_LIBRARY);
        assertEquals(2, g.vertexCount());
    }

    @Test
    void testAddEdgeCreatesUndirectedConnection() {
        assertTrue(graph.hasEdge(MAIN_GATE, BALME_LIBRARY));
        assertTrue(graph.hasEdge(BALME_LIBRARY, MAIN_GATE)); // reverse exists too
    }

    @Test
    void testDirectedGraphDoesNotAddReverseEdge() {
        CustomGraph oneWay = new CustomGraph(4, true);
        oneWay.addEdge(0, 1, 100.0);
        assertTrue(oneWay.hasEdge(0, 1));
        assertFalse(oneWay.hasEdge(1, 0));
    }

    @Test
    void testEdgeWeightMatchesDistance() {
        assertEquals(450.0, graph.getEdgeWeight(MAIN_GATE, BALME_LIBRARY), 0.001);
    }

    @Test
    void testGetNeighborsReturnsDirectConnections() {
        java.util.List<Integer> neighbors = graph.getNeighbors(BALME_LIBRARY);
        assertTrue(neighbors.contains(MAIN_GATE));
        assertTrue(neighbors.contains(GREAT_HALL));
        assertEquals(2, neighbors.size());
    }

    @Test
    void testBfsVisitsAllReachableLocationsFromMainGate() {
        java.util.List<Integer> order = graph.bfs(MAIN_GATE);
        // Reachable set: Main Gate, Balme Library, Great Hall, DCS, Night Market
        assertEquals(5, order.size());
        assertTrue(order.contains(MAIN_GATE));
        assertTrue(order.contains(NIGHT_MARKET));
        assertFalse(order.contains(SPORTS_STADIUM)); // unreachable, not connected
    }

    @Test
    void testDfsVisitsAllReachableLocationsFromMainGate() {
        java.util.List<Integer> order = graph.dfs(MAIN_GATE);
        assertEquals(order.size(), graph.bfs(MAIN_GATE).size()); // same reachable set
        assertEquals(MAIN_GATE, (int) order.get(0)); // DFS always starts at the source
    }

    @Test
    void testIsolatedVertexHasNoNeighbors() {
        graph.addVertex(SPORTS_STADIUM);
        assertTrue(graph.getNeighbors(SPORTS_STADIUM).isEmpty());
    }

    @Test
    void testHasEdgeFalseForNonExistentRoad() {
        assertFalse(graph.hasEdge(MAIN_GATE, SPORTS_STADIUM));
    }

    @Test
    void testNegativeWeightRejected() {
        assertThrows(IllegalArgumentException.class,
            () -> graph.addEdge(MAIN_GATE, GREAT_HALL, -50.0));
    }

    @Test
    void testOutOfRangeVertexRejected() {
        assertThrows(IndexOutOfBoundsException.class,
            () -> graph.getNeighbors(99));
    }
}