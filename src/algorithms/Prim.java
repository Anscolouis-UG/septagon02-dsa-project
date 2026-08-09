package algorithms;

import datastructures.CustomArrayList;
import datastructures.CustomGraph;
import datastructures.CustomHeap;

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

        // Min-heap: smaller weight = higher priority, so reverse the comparison
        @Override
        public int compareTo(MinEntry other) {
            return Double.compare(other.weight, this.weight);
        }
    }

    public static CustomArrayList<GraphEdge> run(CustomGraph graph, int numVertices, int startNode) {
        boolean[] inMST = new boolean[numVertices];
        CustomArrayList<GraphEdge> mstEdges = new CustomArrayList<>();
        double totalWeight = 0;

        CustomHeap<MinEntry> minHeap = new CustomHeap<>();
        minHeap.insert(new MinEntry(startNode, 0, -1));

        while (!minHeap.isEmpty() && mstEdges.size() < numVertices - 1) {
            MinEntry current = minHeap.extractMax(); // max = smallest weight due to reversed compareTo
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
        System.out.println("  Prim's MST — UG Legon Campus  (data_base/roads.csv)");
        System.out.println("==================================================\n");

        datastructures.CustomMap<Integer, String> names = new datastructures.CustomMap<>();
        datastructures.CustomGraph graph = GraphLoader.load(
            names, "data_base/locations.csv", "data_base/roads.csv");

        int start = 0; // Main Gate
        System.out.println("Starting Prim's MST from: " + names.get(start) + "\n");

        CustomArrayList<GraphEdge> mst = run(graph, GraphLoader.NUM_LOCATIONS + 1, start);

        System.out.println("MST edges (" + mst.size() + " roads to connect campus):");
        for (int i = 0; i < mst.size(); i++) {
            GraphEdge e = mst.get(i);
            System.out.printf("  %-45s -- %-45s  (%.0fm)%n",
                names.get(e.source), names.get(e.destination), e.weight);
        }
        System.out.println("\nExecution & compilation successful!");
    }
}