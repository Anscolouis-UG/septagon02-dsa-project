package datastructures;

import java.util.ArrayList;
import java.util.List;

public class CustomBST<K extends Comparable<K>, V> {
    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> left, right;
        Node(K key, V value) { this.key = key; this.value = value; }
    }

    private Node<K, V> root;
    private int size;

    public void insert(K key, V value) {
        if (key == null) return;
        root = insertRecursive(root, key, value);
    }

    private Node<K, V> insertRecursive(Node<K, V> current, K key, V value) {
        if (current == null) {
            size++;
            return new Node<>(key, value);
        }
        int cmp = key.compareTo(current.key);
        if (cmp < 0) current.left = insertRecursive(current.left, key, value);
        else if (cmp > 0) current.right = insertRecursive(current.right, key, value);
        else current.value = value;
        return current;
    }

    public V search(K key) {
        Node<K, V> curr = root;
        while (curr != null) {
            int cmp = key.compareTo(curr.key);
            if (cmp < 0) curr = curr.left;
            else if (cmp > 0) curr = curr.right;
            else return curr.value;
        }
        return null;
    }

    public List<V> inorder() {
        List<V> list = new ArrayList<>();
        inorderRecursive(root, list);
        return list;
    }

    private void inorderRecursive(Node<K, V> node, List<V> list) {
        if (node != null) {
            inorderRecursive(node.left, list);
            list.add(node.value);
            inorderRecursive(node.right, list);
        }
    }

    public int size() { return size; }
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  Ghana Smart Service Operations Optimizer");
        System.out.println("  Testing CustomBST (Core Developer 2)");
        System.out.println("==================================================\n");

        CustomBST<Integer, String> bst = new CustomBST<>();

        System.out.println("--- Inserting Nodes ---");
        bst.insert(50, "Central Server");
        bst.insert(30, "Substation A");
        bst.insert(70, "Substation B");
        bst.insert(20, "Relay Station 1");
        bst.insert(40, "Relay Station 2");

        System.out.println("\n--- Search Tests ---");
        System.out.println("Searching Key 30: " + bst.search(30));
        System.out.println("Searching Key 50: " + bst.search(50));
        System.out.println("Searching Key 99: " + bst.search(99));

        System.out.println("\nExecution successful!");
    }
}