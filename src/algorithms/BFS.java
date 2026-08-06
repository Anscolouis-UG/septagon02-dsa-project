package algorithms;

import datastructures.CustomGraph;

import java.util.*;

public class BFS {

    public static List<Integer> run(CustomGraph graph, int start) {
        List<Integer> visitOrder = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();

        visited.add(start);
        queue.add(start);

        while (!queue.isEmpty()) {
            int current = queue.poll();
            visitOrder.add(current);

            for (CustomGraph.Edge edge : graph.getEdges(current)) {
                int neighbor = edge.target;
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }

        return visitOrder;
    }

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  Ghana Smart Service Operations Optimizer");
        System.out.println("  Testing Breadth-First Search");
        System.out.println("==================================================\n");

        int n = 6;
        CustomGraph graph = new CustomGraph(n, false);

        graph.addEdge(0, 1, 1);
        graph.addEdge(0, 2, 1);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 4, 1);
        graph.addEdge(4, 5, 1);

        int start = 0;
        List<Integer> order = run(graph, start);

        System.out.println("BFS visit order from location " + start + ":");
        System.out.println("  " + order);

        System.out.println("\nExecution & compilation successful!");
    }
}