package datastructures;

import java.util.*;

/**
 * CustomGraph.java
 *
 * Module: Graph & Advanced Structures (DCIT 204)
 * Author: Nana Ekow Gyakye-Smith (22379633)
 * Context: University of Ghana, Legon Campus — Facilities & Grounds Management
 *
 * Vertices represent campus locations (location_id from locations.csv).
 * Edges represent roads/pathways (roads.csv) with a real-world weight
 * (distance in metres, from the ROADS table's distance_m column).
 *
 * Supports BOTH representations at once, kept in sync on every edge insert:
 *   - Adjacency list  -> fast for BFS/DFS/getNeighbors, used by routing algorithms
 *   - Adjacency matrix -> fast O(1) edge lookups, easier to trace by hand for the report
 *
 * Personal parameters (derived from student ID 22379633):
 *   - Hash Table Size: 233  -> used as the default initial capacity of the
 *     adjacency list's backing map, so the internal hashing behaviour is
 *     unique to this submission.
 *   - Tree Balance Threshold: 0.83 (not used directly in Graph — reserved
 *     for cross-referencing with Benjamin's BST/Red-Black Tree module).
 */
public class CustomGraph {

    /** Personal parameter — initial capacity for the adjacency-list map. */
    public static final int HASH_TABLE_SIZE = 233;

    private final int numVertices;
    private final boolean directed;

    // Adjacency list: vertex -> list of (neighbor, weight) edges
    private final Map<Integer, List<Edge>> adjList;

    // Adjacency matrix: matrix[i][j] = weight of edge i->j, or POSITIVE_INFINITY if none
    private final double[][] adjMatrix;

    private final Set<Integer> vertices;

    /** Represents one edge in the adjacency list. */
    public static class Edge {
        public final int target;
        public final double weight;

        public Edge(int target, double weight) {
            this.target = target;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "-> " + target + " (" + weight + "m)";
        }
    }

    /**
     * @param numVertices maximum number of vertices (e.g. 50 for the 50 campus buildings)
     * @param directed    true for one-way roads, false for two-way roads
     */
    public CustomGraph(int numVertices, boolean directed) {
        if (numVertices <= 0) {
            throw new IllegalArgumentException("numVertices must be positive");
        }
        this.numVertices = numVertices;
        this.directed = directed;
        this.adjList = new HashMap<>(HASH_TABLE_SIZE);
        this.adjMatrix = new double[numVertices][numVertices];
        this.vertices = new LinkedHashSet<>();

        for (double[] row : adjMatrix) {
            Arrays.fill(row, Double.POSITIVE_INFINITY);
        }
    }

    /** Adds a vertex (campus location) to the graph if it isn't already present. */
    public void addVertex(int id) {
        validateId(id);
        vertices.add(id);
        adjList.putIfAbsent(id, new ArrayList<>());
    }

    /**
     * Adds a road connecting two locations.
     * If the graph is undirected, the reverse edge is added automatically.
     */
    public void addEdge(int from, int to, double weight) {
        validateId(from);
        validateId(to);
        if (weight < 0) {
            throw new IllegalArgumentException("weight (distance_m) cannot be negative");
        }

        addVertex(from);
        addVertex(to);

        adjList.get(from).add(new Edge(to, weight));
        adjMatrix[from][to] = weight;

        if (!directed) {
            adjList.get(to).add(new Edge(from, weight));
            adjMatrix[to][from] = weight;
        }
    }

    /** Returns the ids of all locations directly reachable from the given vertex. */
    public List<Integer> getNeighbors(int vertex) {
        validateId(vertex);
        List<Integer> neighbors = new ArrayList<>();
        for (Edge e : adjList.getOrDefault(vertex, Collections.emptyList())) {
            neighbors.add(e.target);
        }
        return neighbors;
    }

    /** Returns the raw edge list (target + distance) for a vertex — used by routing algorithms. */
    public List<Edge> getEdges(int vertex) {
        validateId(vertex);
        return new ArrayList<>(adjList.getOrDefault(vertex, Collections.emptyList()));
    }

    /** Depth-First Search, returns visit order starting from `start`. */
    public List<Integer> dfs(int start) {
        validateId(start);
        List<Integer> visitOrder = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        dfsHelper(start, visited, visitOrder);
        return visitOrder;
    }

    private void dfsHelper(int current, Set<Integer> visited, List<Integer> visitOrder) {
        if (visited.contains(current)) return;
        visited.add(current);
        visitOrder.add(current);
        for (Edge e : adjList.getOrDefault(current, Collections.emptyList())) {
            if (!visited.contains(e.target)) {
                dfsHelper(e.target, visited, visitOrder);
            }
        }
    }

    /** Breadth-First Search, returns visit order starting from `start`. */
    public List<Integer> bfs(int start) {
        validateId(start);
        List<Integer> visitOrder = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();

        visited.add(start);
        queue.add(start);

        while (!queue.isEmpty()) {
            int current = queue.poll();
            visitOrder.add(current);
            for (Edge e : adjList.getOrDefault(current, Collections.emptyList())) {
                if (!visited.contains(e.target)) {
                    visited.add(e.target);
                    queue.add(e.target);
                }
            }
        }
        return visitOrder;
    }

    /** Returns true if there is a direct edge from `from` to `to` — O(1) via the matrix. */
    public boolean hasEdge(int from, int to) {
        validateId(from);
        validateId(to);
        return adjMatrix[from][to] != Double.POSITIVE_INFINITY;
    }

    public double getEdgeWeight(int from, int to) {
        validateId(from);
        validateId(to);
        return adjMatrix[from][to];
    }

    public int vertexCount() {
        return vertices.size();
    }

    public boolean isDirected() {
        return directed;
    }

    private void validateId(int id) {
        if (id < 0 || id >= numVertices) {
            throw new IndexOutOfBoundsException(
                "location_id " + id + " is outside the graph's capacity (0.." + (numVertices - 1) + ")");
        }
    }
}
