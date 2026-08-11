package datastructures;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CustomRedBlackTree {

    public static class Location {
        private String id;
        private String name;
        private String category;
        private double latitude;
        private double longitude;

        public Location(String id, String name, String category, double latitude, double longitude) {
            this.id = id;
            this.name = name;
            this.category = category;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        @Override
        public String toString() {
            return String.format("[%s | ID: %s | Category: %s]", name, id, category);
        }
    }

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private class Node {
        String key;
        Location value;
        Node left, right;
        boolean color;

        Node(String key, Location value, boolean color) {
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

    public void insert(String key, Location value) {
        if (key == null) return;
        root = insert(root, key, value);
        root.color = BLACK;
    }

    private Node insert(Node h, String key, Location value) {
        if (h == null) {
            size++;
            return new Node(key, value, RED);
        }

        int cmp = key.compareTo(h.key);
        if (cmp < 0) h.left = insert(h.left, key, value);
        else if (cmp > 0) h.right = insert(h.right, key, value);
        else h.value = value;

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

    public Location search(String key) {
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

    public static void main(String[] args) throws IOException {
        CustomRedBlackTree tree = new CustomRedBlackTree();

        java.util.List<String> rows = Files.readAllLines(Path.of("data_base", "nodes.csv"));
        for (int index = 1; index < rows.size(); index++) {
            String[] columns = rows.get(index).split(",", -1);
            if (columns.length < 5) continue;
            tree.insert(columns[0].trim(), new Location(
                    columns[0].trim(), columns[1].trim(), columns[2].trim(),
                    Double.parseDouble(columns[3].trim()), Double.parseDouble(columns[4].trim())));
        }

        System.out.println("=== Location Lookups (CustomRedBlackTree) ===");
        System.out.println("Location ID balme-library: " + tree.search("balme-library"));
        System.out.println("Total Locations Indexed: " + tree.size());
    }
}