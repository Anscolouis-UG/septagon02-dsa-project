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

        // Min-heap: smaller distance = higher priority, so reverse the comparison
        @Override
        public int compareTo(MinEntry other) {
            return Double.compare(other.distance, this.distance);
        }
    }

    public static double[] run(CustomGraph graph, int numVertices, int source) {
        double[] dist = new double[numVertices];
        boolean[] visited = new boolean[numVertices];
        for (int i = 0; i < numVertices; i++) dist[i] = INFINITY;
        dist[source] = 0;

        CustomHeap<MinEntry> minHeap = new CustomHeap<>();
        minHeap.insert(new MinEntry(source, 0));

        while (!minHeap.isEmpty()) {
            MinEntry current = minHeap.extractMax(); // max = smallest dist due to reversed compareTo
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
        System.out.println("  Dijkstra — UG Legon Campus  (data_base/roads.csv)");
        System.out.println("==================================================\n");

        datastructures.CustomMap<Integer, String> names = new datastructures.CustomMap<>();
        datastructures.CustomGraph graph = GraphLoader.load(
            names, "data_base/locations.csv", "data_base/roads.csv");

        int source = 0;  // Main Gate (location_id=1)
        System.out.println("Source: " + names.get(source) + " (ID=" + (source+1) + ")\n");

        double[] distances = run(graph, GraphLoader.NUM_LOCATIONS + 1, source);

        System.out.println("Shortest distances from Main Gate to all reachable locations:");
        for (int i = 0; i < GraphLoader.NUM_LOCATIONS; i++) {
            if (distances[i] != INFINITY) {
                System.out.printf("  to [%2d] %-45s : %.0fm%n", i+1, names.get(i), distances[i]);
            }
        }

        // Highlight route to University Hospital (location_id=19, index=18)
        int hospital = 18;
        System.out.printf("%nHighlight: Main Gate -> University Hospital = %.0fm%n",
            distances[hospital]);

        System.out.println("\nExecution & compilation successful!");
    }
}