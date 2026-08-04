package algorithms;

import datastructures.CustomGraph;
import datastructures.CustomHeap;

import java.util.List;
import java.util.ArrayList;

public class Prim {

    private static class MinEntry implements Comparable<MinEntry> {
        final int node;
        final double weight;
        final int parent;

        MinEntry(int node, double weight, int parent) {
            this.node = node;
            this.weight = weight;
            this.parent = parent;
        }

        @Override
        public int compareTo(MinEntry other) {
            return Double.compare(other.weight, this.weight);
        }
    }

    public static List<GraphEdge> run(CustomGraph graph, int numVertices, int startNode) {
        boolean[] inMST = new boolean[numVertices];
        List<GraphEdge> mstEdges = new ArrayList<>();
        double totalWeight = 0;

        CustomHeap<MinEntry> minHeap = new CustomHeap<>();
        minHeap.insert(new MinEntry(startNode, 0, -1));

        while (!minHeap.isEmpty() && mstEdges.size() < numVertices - 1) {
            MinEntry current = minHeap.extractMax();
            int u = current.node;

            if (inMST[u]) continue;
            inMST[u] = true;

            if (current.parent != -1) {
                mstEdges.add(new GraphEdge(current.parent, u, current.weight));
                totalWeight += current.weight;
            }

            for (CustomGraph.Edge edge : graph.getEdges(u)) {
                int v = edge.target;
                if (!inMST[v]) {
                    minHeap.insert(new MinEntry(v, edge.weight, u));
                }
            }
        }

        System.out.println("Total MST weight: " + totalWeight + "m");
        return mstEdges;
    }

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  Ghana Smart Service Operations Optimizer");
        System.out.println("  Testing Prim's Minimum Spanning Tree Algorithm");
        System.out.println("==================================================\n");

        int n = 5;
        CustomGraph graph = new CustomGraph(n, false);

        graph.addEdge(0, 1, 4);
        graph.addEdge(0, 2, 1);
        graph.addEdge(2, 1, 2);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 3, 5);
        graph.addEdge(3, 4, 3);

        System.out.println("Minimum Spanning Tree edges (Prim's), starting at 0:");
        List<GraphEdge> mst = run(graph, n, 0);
        for (GraphEdge e : mst) {
            System.out.println("  " + e);
        }

        System.out.println("\nExecution & compilation successful!");
    }
}