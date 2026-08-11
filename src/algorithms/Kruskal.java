package algorithms;

import datastructures.CustomArrayList;
import datastructures.CustomDisjointSet;
import datastructures.CustomGraph;

public class Kruskal {

    public static CustomArrayList<GraphEdge> run(CustomGraph graph, int numVertices) {
        return run(extractEdges(graph, numVertices), numVertices);
    }

    public static CustomArrayList<GraphEdge> run(CustomArrayList<GraphEdge> allEdges, int numVertices) {
        // Sort edges by weight (insertion sort — no java.util.Collections)
        for (int i = 1; i < allEdges.size(); i++) {
            GraphEdge key = allEdges.get(i);
            int j = i - 1;
            while (j >= 0 && allEdges.get(j).compareTo(key) > 0) {
                allEdges.set(j + 1, allEdges.get(j));
                j--;
            }
            allEdges.set(j + 1, key);
        }

        CustomDisjointSet dsu = new CustomDisjointSet(numVertices);
        CustomArrayList<GraphEdge> mstEdges = new CustomArrayList<>();
        double totalWeight = 0;

        for (int i = 0; i < allEdges.size(); i++) {
            GraphEdge edge = allEdges.get(i);
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

    private static CustomArrayList<GraphEdge> extractEdges(CustomGraph graph, int numVertices) {
        CustomArrayList<GraphEdge> edges = new CustomArrayList<>();
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
        System.out.println("  Kruskal's MST — UG Legon Campus (data_base/edges_knn_sparse.csv)");
        System.out.println("==================================================\n");

        datastructures.CustomMap<Integer, String> names = new datastructures.CustomMap<>();
        datastructures.CustomMap<String, Integer> idToIndex = new datastructures.CustomMap<>();
        datastructures.CustomGraph graph = GraphLoader.load(
            names, idToIndex, "data_base/nodes.csv", "data_base/edges_knn_sparse.csv");

        System.out.println("Computing Minimum Spanning Tree (Kruskal's)...\n");
        CustomArrayList<GraphEdge> mst = run(graph, GraphLoader.MAX_LOCATIONS);

        System.out.println("MST edges (" + mst.size() + " roads to connect campus):");
        for (int i = 0; i < mst.size(); i++) {
            GraphEdge e = mst.get(i);
            System.out.printf("  %-45s -- %-45s  (%.0fm)%n",
                names.get(e.source), names.get(e.destination), e.weight);
        }
        System.out.println("\nExecution & compilation successful!");
    }
}