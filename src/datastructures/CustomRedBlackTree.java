package datastructures;

public class CustomRedBlackTree<K extends Comparable<K>, V> {
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private class Node {
        K key;
        V value;
        Node left, right;
        boolean color;

        Node(K key, V value, boolean color) {
            this.key = key;
            this.value = value;
            this.color = color;
        }
    }

    private Node root;
    private int size;

    private boolean isRed(Node x) {
        return x != null && x.color == RED;
    }

    public void insert(K key, V value) {
        if (key == null) return;
        root = insert(root, key, value);
        root.color = BLACK;
    }

    private Node insert(Node h, K key, V value) {
        if (h == null) {
            size++;
            return new Node(key, value, RED);
        }

        int cmp = key.compareTo(h.key);
        if (cmp < 0) h.left = insert(h.left, key, value);
        else if (cmp > 0) h.right = insert(h.right, key, value);
        else h.value = value;

        // Balance conditions & rotations
        if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);

        return h;
    }

    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    private void flipColors(Node h) {
        h.color = !h.color;
        if (h.left != null) h.left.color = !h.left.color;
        if (h.right != null) h.right.color = !h.right.color;
    }

    public V search(K key) {
        Node x = root;
        while (x != null) {
            int cmp = key.compareTo(x.key);
            if (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
            else return x.value;
        }
        return null;
    }

    public int size() { return size; }
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  Ghana Smart Service Operations Optimizer");
        System.out.println("  Testing CustomRedBlackTree (Core Developer 2)");
        System.out.println("==================================================\n");

        CustomRedBlackTree<Integer, String> tree = new CustomRedBlackTree<>();

        System.out.println("--- Inserting Nodes & Testing Rotations ---");
        tree.insert(10, "Task #101 - Urgent Maintenance");
        tree.insert(20, "Task #102 - Field Dispatch");
        tree.insert(30, "Task #103 - Network Audit"); // Triggers Left Rotation

        System.out.println("\n--- Search Tests ---");
        System.out.println("Searching Key 20: " + tree.search(20));
        System.out.println("Searching Key 99: " + tree.search(99));

        System.out.println("\nExecution successful!");
    }
}