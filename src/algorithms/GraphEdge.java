package algorithms;

public class GraphEdge implements Comparable<GraphEdge> {
    public final int source;
    public final int destination;
    public final double weight;

    public GraphEdge(int source, int destination, double weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    @Override
    public int compareTo(GraphEdge other) {
        return Double.compare(this.weight, other.weight);
    }

    @Override
    public String toString() {
        return source + " -- " + destination + " (w=" + weight + "m)";
    }
}