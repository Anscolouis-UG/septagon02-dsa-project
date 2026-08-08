package algorithms;

import datastructures.CustomGraph;
import datastructures.CustomHeap;


public class Dijkstra {

    public static final double INFINITY = Double.POSITIVE_INFINITY;

    private static class MinEntry implements Comparable<MinEntry> {
        final int node;
        final double distance;

        MinEntry(int node, double distance) {
            this.node = node;
            this.distance = distance;
        }

        @Override
        public int compareTo(MinEntry other) {
            return Double.compare(other.distance, this.distance);
        }
    }

    public static double[] run(CustomGraph graph, int numVertices, int source) {
        double[] dist = new double[numVertices];
        boolean[] visited = new boolean[numVertices];
        java.util.Arrays.fill(dist, INFINITY);
        dist[source] = 0;

        CustomHeap<MinEntry> minHeap = new CustomHeap<>();
        minHeap.insert(new MinEntry(source, 0));

        while (!minHeap.isEmpty()) {
            MinEntry current = minHeap.extractMax();
            int u = current.node;

            if (visited[u]) continue;
            visited[u] = true;

            for (CustomGraph.Edge edge : graph.getEdges(u)) {
                int v = edge.target;
                if (visited[v]) continue;

                double candidateDist = dist[u] + edge.weight;
                if (candidateDist < dist[v]) {
                    dist[v] = candidateDist;
                    minHeap.insert(new MinEntry(v, candidateDist));
                }
            }
        }

        return dist;
    }

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  Ghana Smart Service Operations Optimizer");
        System.out.println("  Testing Dijkstra's Shortest Path Algorithm");
        System.out.println("==================================================\n");

        int n = 5;
        CustomGraph graph = new CustomGraph(n, false);

        graph.addEdge(0, 1, 4);
        graph.addEdge(0, 2, 1);
        graph.addEdge(2, 1, 2);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 3, 5);
        graph.addEdge(3, 4, 3);

        int source = 0;
        double[] distances = run(graph, n, source);

        System.out.println("Shortest distances from location " + source + ":");
        for (int i = 0; i < n; i++) {
            String label = (distances[i] == INFINITY) ? "unreachable" : distances[i] + "m";
            System.out.println("  to " + i + ": " + label);
        }

        System.out.println("\nExecution & compilation successful!");
    }
}