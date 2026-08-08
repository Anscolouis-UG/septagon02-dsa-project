package datastructures;

/**
 * CustomDisjointSet.java (Union-Find)
 *
 * Module: Graph & Advanced Structures (DCIT 204)
 * Author: Nana Ekow Gyakye-Smith (22379633)
 * Context: University of Ghana, Legon Campus — Facilities & Grounds Management
 *
 * Used to group campus locations into connected zones/districts, and by
 * Kruskal's algorithm (Nana Kwabena's module) to build a minimum-spanning
 * network of roads without forming cycles.
 *
 * Implements union by rank + path compression, giving near O(1) amortized
 * find/union operations (inverse Ackermann function).
 */
public class CustomDisjointSet {

    /** Personal parameter, documented for the report — not used as an array size here
     *  since capacity is driven by the actual number of campus locations (n). */
    public static final double TREE_BALANCE_THRESHOLD = 0.83;

    private final int[] parent;
    private final int[] rank;
    private int setCount;

    /**
     * Creates n disjoint sets, one per element, each initially its own parent.
     * Equivalent to calling makeSet() for every id from 0 to n-1.
     */
    public CustomDisjointSet(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be positive");
        }
        parent = new int[n];
        rank = new int[n];
        setCount = n;
        for (int i = 0; i < n; i++) {
            makeSet(i);
        }
    }

    /** Initializes element x as its own set (parent = itself, rank = 0). */
    public void makeSet(int x) {
        validate(x);
        parent[x] = x;
        rank[x] = 0;
    }

    /**
     * Finds the representative (root) of the set containing x,
     * compressing the path along the way so future lookups are faster.
     */
    public int find(int x) {
        validate(x);
        if (parent[x] != x) {
            parent[x] = find(parent[x]); // path compression
        }
        return parent[x];
    }

    /**
     * Merges the sets containing a and b using union by rank.
     * Returns true if a merge happened, false if a and b were already in the same set.
     */
    public boolean union(int a, int b) {
        int rootA = find(a);
        int rootB = find(b);

        if (rootA == rootB) {
            return false;
        }

        if (rank[rootA] < rank[rootB]) {
            parent[rootA] = rootB;
        } else if (rank[rootA] > rank[rootB]) {
            parent[rootB] = rootA;
        } else {
            parent[rootB] = rootA;
            rank[rootA]++;
        }

        setCount--;
        return true;
    }

    /** True if a and b are currently in the same set (same campus zone/network). */
    public boolean connected(int a, int b) {
        return find(a) == find(b);
    }

    /** Number of distinct sets remaining (e.g. number of disconnected campus zones). */
    public int getSetCount() {
        return setCount;
    }

    public int size() {
        return parent.length;
    }

    private void validate(int x) {
        if (x < 0 || x >= parent.length) {
            throw new IndexOutOfBoundsException(
                "id " + x + " is outside range (0.." + (parent.length - 1) + ")");
        }
    }
}