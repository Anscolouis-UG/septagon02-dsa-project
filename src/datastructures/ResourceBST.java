package datastructures;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ResourceBST {

    public static class Resource {
        private int resourceId;
        private String resourceName;
        private String resourceType;
        private int capacity;
        private int currentLocationId;
        private String availabilityStatus;
        private double costPerHour;

        public Resource(int resourceId, String resourceName, String resourceType, int capacity,
                        int currentLocationId, String availabilityStatus, double costPerHour) {
            this.resourceId = resourceId;
            this.resourceName = resourceName;
            this.resourceType = resourceType;
            this.capacity = capacity;
            this.currentLocationId = currentLocationId;
            this.availabilityStatus = availabilityStatus;
            this.costPerHour = costPerHour;
        }

        public int getResourceId() { return resourceId; }
        public String getResourceName() { return resourceName; }

        @Override
        public String toString() {
            return String.format("[%s | Type: %s | Status: %s | Loc ID: %d | Cost: $%.2f/hr]",
                    resourceName, resourceType, availabilityStatus, currentLocationId, costPerHour);
        }
    }

    private static class Node {
        Integer key;
        Resource value;
        Node left, right;
        Node(Integer key, Resource value) { this.key = key; this.value = value; }
    }

    private Node root;
    private int size;

    public void insert(Integer key, Resource value) {
        if (key == null) return;
        root = insertRecursive(root, key, value);
    }

    private Node insertRecursive(Node current, Integer key, Resource value) {
        if (current == null) {
            size++;
            return new Node(key, value);
        }
        int cmp = key.compareTo(current.key);
        if (cmp < 0) current.left = insertRecursive(current.left, key, value);
        else if (cmp > 0) current.right = insertRecursive(current.right, key, value);
        else current.value = value;
        return current;
    }

    public Resource search(Integer key) {
        Node curr = root;
        while (curr != null) {
            int cmp = key.compareTo(curr.key);
            if (cmp < 0) curr = curr.left;
            else if (cmp > 0) curr = curr.right;
            else return curr.value;
        }
        return null;
    }

    public java.util.List<Resource> inorder() {
        java.util.List<Resource> list = new java.util.ArrayList<>();
        inorderRecursive(root, list);
        return list;
    }

    private void inorderRecursive(Node node, java.util.List<Resource> list) {
        if (node != null) {
            inorderRecursive(node.left, list);
            list.add(node.value);
            inorderRecursive(node.right, list);
        }
    }

    public int size() { return size; }

    public static void main(String[] args) throws IOException {
        ResourceBST bst = new ResourceBST();

        java.util.List<String> rows = Files.readAllLines(Path.of("data_base", "resources.csv"));
        for (int index = 1; index < rows.size(); index++) {
            String[] columns = rows.get(index).split(",", -1);
            bst.insert(Integer.parseInt(columns[0]), new Resource(
                    Integer.parseInt(columns[0]), columns[1], columns[2],
                    Integer.parseInt(columns[3]), Integer.parseInt(columns[4]),
                    columns[5], Double.parseDouble(columns[6])));
        }

        System.out.println("=== Resource Index Lookup (CustomBST) ===");
        System.out.println("Resource ID 1: " + bst.search(1));
        System.out.println("Resource ID 5: " + bst.search(5));
        System.out.println("Total Resources Indexed: " + bst.size());
    }
}