package algorithms;

import datastructures.CustomArrayList;
import datastructures.CustomSet;
import datastructures.CustomQueue;
import datastructures.CustomGraph;

public class BFS {

    public static CustomArrayList<Integer> run(CustomGraph graph, int start) {
        CustomArrayList<Integer> visitOrder = new CustomArrayList<>();
        CustomSet<Integer> visited = new CustomSet<>();
        CustomQueue<Integer> queue = new CustomQueue<>();

        visited.add(start);
        queue.enqueue(start);

        while (!queue.isEmpty()) {
            int current = queue.dequeue();
            visitOrder.add(current);
            for (CustomGraph.Edge edge : graph.getEdges(current)) {
                if (!visited.contains(edge.target)) {
                    visited.add(edge.target);
                    queue.enqueue(edge.target);
                }
            }
        }
        return visitOrder;
    }

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  Ghana Smart Service Operations Optimizer");
        System.out.println("  BFS — UG Legon Campus  (data_base/edges_knn_sparse.csv)");
        System.out.println("==================================================\n");

        datastructures.CustomMap<Integer, String> names = new datastructures.CustomMap<>();
        datastructures.CustomMap<String, Integer> idToIndex = new datastructures.CustomMap<>();
        datastructures.CustomGraph graph = GraphLoader.load(
            names, idToIndex, "data_base/nodes.csv", "data_base/edges_knn_sparse.csv");

        int start = 0; // Main Gate (location_id=1, 0-based index=0)
        System.out.println("Starting BFS from: " + names.get(start) + " (index " + start + ")\n");

        CustomArrayList<Integer> order = run(graph, start);
        System.out.println("BFS visit order (" + order.size() + " locations reached):");
        for (int i = 0; i < order.size(); i++) {
            int v = order.get(i);
            System.out.printf("  [%2d] %s%n", v + 1, names.get(v));
        }
        System.out.println("\nExecution & compilation successful!");
    }
}