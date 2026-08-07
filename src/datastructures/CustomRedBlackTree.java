package datastructures;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CustomRedBlackTree {

    public static class Location {
        private int locationId;
        private String name;
        private String district;
        private String areaType;
        private double latitude;
        private double longitude;
        private String operatingHours;
        private String buildingCode;

        public Location(int locationId, String name, String district, String areaType,
                        double latitude, double longitude, String operatingHours, String buildingCode) {
            this.locationId = locationId;
            this.name = name;
            this.district = district;
            this.areaType = areaType;
            this.latitude = latitude;
            this.longitude = longitude;
            this.operatingHours = operatingHours;
            this.buildingCode = buildingCode;
        }

        @Override
        public String toString() {
            return String.format("[%s | Code: %s | District: %s | Type: %s]", name, buildingCode, district, areaType);
        }
    }

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private class Node {
        Integer key;
        Location value;
        Node left, right;
        boolean color;

        Node(Integer key, Location value, boolean color) {
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

    public void insert(Integer key, Location value) {
        if (key == null) return;
        root = insert(root, key, value);
        root.color = BLACK;
    }

    private Node insert(Node h, Integer key, Location value) {
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

    public Location search(Integer key) {
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

        java.util.List<String> rows = Files.readAllLines(Path.of("data_base", "locations.csv"));
        for (int index = 1; index < rows.size(); index++) {
            String[] columns = rows.get(index).split(",", -1);
            tree.insert(Integer.parseInt(columns[0]), new Location(
                    Integer.parseInt(columns[0]), columns[1], columns[2], columns[3],
                    Double.parseDouble(columns[4]), Double.parseDouble(columns[5]),
                    columns[6], columns[7]));
        }

        System.out.println("=== Location Lookups (CustomRedBlackTree) ===");
        System.out.println("Location ID 3: " + tree.search(3));
        System.out.println("Total Locations Indexed: " + tree.size());
    }
}