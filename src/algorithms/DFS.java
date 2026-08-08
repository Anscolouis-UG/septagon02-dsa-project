package algorithms;

import datastructures.CustomGraph;


public class DFS {

    public static java.util.List<Integer> run(CustomGraph graph, int start) {
        java.util.List<Integer> visitOrder = new java.util.ArrayList<>();
        java.util.Set<Integer> visited = new java.util.HashSet<>();
        dfsHelper(graph, start, visited, visitOrder);
        return visitOrder;
    }

    private static void dfsHelper(CustomGraph graph, int current, java.util.Set<Integer> visited, java.util.List<Integer> visitOrder) {
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
        System.out.println("  Testing Depth-First Search");
        System.out.println("==================================================\n");

        int n = 6;
        CustomGraph graph = new CustomGraph(n, false);

        graph.addEdge(0, 1, 1);
        graph.addEdge(0, 2, 1);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 4, 1);
        graph.addEdge(4, 5, 1);

        int start = 0;
        java.util.List<Integer> order = run(graph, start);

        System.out.println("DFS visit order from location " + start + ":");
        System.out.println("  " + order);

        System.out.println("\nExecution & compilation successful!");
    }
}