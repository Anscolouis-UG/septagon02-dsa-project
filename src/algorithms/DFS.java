package algorithms;

import datastructures.CustomArrayList;
import datastructures.CustomSet;
import datastructures.CustomGraph;

public class DFS {

    public static CustomArrayList<Integer> run(CustomGraph graph, int start) {
        CustomArrayList<Integer> visitOrder = new CustomArrayList<>();
        CustomSet<Integer> visited = new CustomSet<>();
        dfsHelper(graph, start, visited, visitOrder);
        return visitOrder;
    }

    private static void dfsHelper(CustomGraph graph, int current, CustomSet<Integer> visited, CustomArrayList<Integer> visitOrder) {
        if (visited.contains(current)) return;
        visited.add(current);
        visitOrder.add(current);

        for (CustomGraph.Edge edge : graph.getEdges(current)) {
            if (!visited.contains(edge.target)) {
                dfsHelper(graph, edge.target, visited, visitOrder);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  Ghana Smart Service Operations Optimizer");
        System.out.println("  DFS — UG Legon Campus  (data_base/roads.csv)");
        System.out.println("==================================================\n");

        datastructures.CustomMap<Integer, String> names = new datastructures.CustomMap<>();
        datastructures.CustomGraph graph = GraphLoader.load(
            names, "data_base/locations.csv", "data_base/roads.csv");

        int start = 0; // Main Gate
        System.out.println("Starting DFS from: " + names.get(start) + " (index " + start + ")\n");

        CustomArrayList<Integer> order = run(graph, start);
        System.out.println("DFS visit order (" + order.size() + " locations reached):");
        for (int i = 0; i < order.size(); i++) {
            int v = order.get(i);
            System.out.printf("  [%2d] %s%n", v + 1, names.get(v));
        }
        System.out.println("\nExecution & compilation successful!");
    }
}