package algorithms;

import datastructures.CustomDisjointSet;
import datastructures.CustomGraph;


public class Kruskal {

    public static java.util.List<GraphEdge> run(CustomGraph graph, int numVertices) {
        return run(extractEdges(graph, numVertices), numVertices);
    }

    public static java.util.List<GraphEdge> run(java.util.List<GraphEdge> allEdges, int numVertices) {
        java.util.List<GraphEdge> sortedEdges = new java.util.ArrayList<>(allEdges);
        java.util.Collections.sort(sortedEdges);

        CustomDisjointSet dsu = new CustomDisjointSet(numVertices);
        java.util.List<GraphEdge> mstEdges = new java.util.ArrayList<>();
        double totalWeight = 0;

        for (GraphEdge edge : sortedEdges) {

            if (dsu.union(edge.source, edge.destination)) {
                mstEdges.add(edge);
                totalWeight += edge.weight;
                if (mstEdges.size() == numVertices - 1) break;
            }
        }

        System.out.println("Total MST weight: " + totalWeight + "m");
        if (mstEdges.size() < numVertices - 1) {
            System.out.println("Warning: graph is disconnected - result is a minimum spanning forest.");
        }
        return mstEdges;
    }

    private static java.util.List<GraphEdge> extractEdges(CustomGraph graph, int numVertices) {
        java.util.List<GraphEdge> edges = new java.util.ArrayList<>();
        for (int u = 0; u < numVertices; u++) {
            for (CustomGraph.Edge e : graph.getEdges(u)) {
                int v = e.target;

                if (graph.isDirected() || v > u) {
                    edges.add(new GraphEdge(u, v, e.weight));
                }
            }
        }
        return edges;
    }

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  Ghana Smart Service Operations Optimizer");
        System.out.println("  Testing Kruskal's Minimum Spanning Tree Algorithm");
        System.out.println("==================================================\n");

        int n = 5;
        CustomGraph graph = new CustomGraph(n, false);

        graph.addEdge(0, 1, 4);
        graph.addEdge(0, 2, 1);
        graph.addEdge(2, 1, 2);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 3, 5);
        graph.addEdge(3, 4, 3);

        System.out.println("Minimum Spanning Tree edges (Kruskal's):");
        java.util.List<GraphEdge> mst = run(graph, n);
        for (GraphEdge e : mst) {
            System.out.println("  " + e);
        }

        System.out.println("\nExecution & compilation successful!");
    }
}