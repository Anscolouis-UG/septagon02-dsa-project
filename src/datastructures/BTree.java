

/**
 * A B-tree implementation that stores key-value pairs in a balanced multi-way search tree.
 * The structure maintains sorted order and supports efficient insertion and search operations.
 *
 * @param <K> the comparable key type
 * @param <V> the stored value type
 */
public class BTree<K extends Comparable<K>, V> {

    /** Default minimum degree used for node splitting. */
    private static final int DEFAULT_MIN_DEGREE = 2;

    /** Minimum degree of the tree; each node can hold up to 2T - 1 keys. */
    private final int minDegree;

    /** Root node of the B-tree. */
    private BTreeNode<K, V> root;

    /** Number of stored entries. */
    private int size;

    /**
     * Constructs a B-tree with the default minimum degree.
     *
     * Time Complexity: Best O(1), Average O(1), Worst O(1).
     * Space Complexity: O(1).
     */
    public BTree() {
        this(DEFAULT_MIN_DEGREE);
    }

    /**
     * Constructs a B-tree with a specific minimum degree.
     *
     * @param minDegree the minimum degree for the tree
     *
     * Time Complexity: Best O(1), Average O(1), Worst O(1).
     * Space Complexity: O(1).
     */
    public BTree(int minDegree) {
        if (minDegree < 2) {
            throw new IllegalArgumentException("Minimum degree must be at least 2");
        }
        this.minDegree = minDegree;
        this.root = new BTreeNode<>(true, minDegree);
    }

    /**
     * Inserts a key-value pair into the B-tree.
     *
     * @param key the key to insert
     * @param value the value to associate with the key
     *
     * Time Complexity: Best O(log n), Average O(log n), Worst O(log n) for balanced insertion.
     * Space Complexity: O(1) auxiliary space, excluding node allocations.
     */
    public void insert(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Key and value must not be null");
        }

        if (root == null) {
            root = new BTreeNode<>(true, minDegree);
        }

        if (root.keyCount == (2 * minDegree - 1)) {
            BTreeNode<K, V> newRoot = new BTreeNode<>(false, minDegree);
            newRoot.children[0] = root;
            splitChild(newRoot, 0);
            root = newRoot;
        }

        if (containsKey(root, key)) {
            updateValue(root, key, value);
            return;
        }

        insertNonFull(root, key, value);
        size++;
    }

    /**
     * Searches for a value by key.
     *
     * @param key the key to search for
     * @return the value associated with the key or {@code null} if absent
     *
     * Time Complexity: Best O(log n), Average O(log n), Worst O(log n).
     * Space Complexity: O(1).
     */
    public V search(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key must not be null");
        }
        return search(root, key);
    }

    /**
     * Returns the entries of the tree in sorted in-order form.
     *
     * @return a string containing the key-value pairs in sorted order
     *
     * Time Complexity: Best O(n), Average O(n), Worst O(n).
     * Space Complexity: O(h) for the traversal stack, where h is the tree height.
     */
    public String traverse() {
        StringBuilder builder = new StringBuilder();
        traverse(root, builder);
        if (builder.length() > 2) {
            builder.setLength(builder.length() - 2);
        }
        return builder.toString();
    }

    /**
     * Returns the number of stored entries.
     *
     * @return the current size of the tree
     *
     * Time Complexity: Best O(1), Average O(1), Worst O(1).
     * Space Complexity: O(1).
     */
    public int size() {
        return size;
    }

    /**
     * Checks whether the tree is empty.
     *
     * @return {@code true} if the tree has no entries, otherwise {@code false}
     *
     * Time Complexity: Best O(1), Average O(1), Worst O(1).
     * Space Complexity: O(1).
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Inserts a key into a non-full node.
     *
     * @param node the node that can still accept a key
     * @param key the key to insert
     * @param value the value to associate with the key
     *
     * Time Complexity: Best O(log n), Average O(log n), Worst O(log n).
     * Space Complexity: O(1).
     */
    private void insertNonFull(BTreeNode<K, V> node, K key, V value) {
        int index = node.keyCount - 1;

        if (node.leaf) {
            while (index >= 0 && key.compareTo(node.keys[index]) < 0) {
                node.keys[index + 1] = node.keys[index];
                node.values[index + 1] = node.values[index];
                index--;
            }
            node.keys[index + 1] = key;
            node.values[index + 1] = value;
            node.keyCount++;
            return;
        }

        while (index >= 0 && key.compareTo(node.keys[index]) < 0) {
            index--;
        }
        index++;

        BTreeNode<K, V> child = node.children[index];
        if (child.keyCount == (2 * minDegree - 1)) {
            splitChild(node, index);
            if (key.compareTo(node.keys[index]) > 0) {
                index++;
            }
            child = node.children[index];
        }

        insertNonFull(child, key, value);
    }

    /**
     * Splits a full child node and promotes the median key to the parent.
     *
     * @param parent the parent node
     * @param childIndex the index of the full child node to split
     *
     * Time Complexity: Best O(log n), Average O(log n), Worst O(log n).
     * Space Complexity: O(1).
     */
    private void splitChild(BTreeNode<K, V> parent, int childIndex) {
        BTreeNode<K, V> child = parent.children[childIndex];
        BTreeNode<K, V> sibling = new BTreeNode<>(child.leaf, minDegree);

        sibling.keyCount = minDegree - 1;
        for (int i = 0; i < minDegree - 1; i++) {
            sibling.keys[i] = child.keys[i + minDegree];
            sibling.values[i] = child.values[i + minDegree];
        }

        if (!child.leaf) {
            for (int i = 0; i < minDegree; i++) {
                sibling.children[i] = child.children[i + minDegree];
            }
        }

        child.keyCount = minDegree - 1;

        for (int i = parent.keyCount; i > childIndex; i--) {
            parent.children[i + 1] = parent.children[i];
            parent.keys[i] = parent.keys[i - 1];
            parent.values[i] = parent.values[i - 1];
        }

        parent.keys[childIndex] = child.keys[minDegree - 1];
        parent.values[childIndex] = child.values[minDegree - 1];
        parent.children[childIndex + 1] = sibling;
        parent.keyCount++;
    }

    /**
     * Checks whether the tree contains a key.
     *
     * @param node the current subtree to search
     * @param key the key to locate
     * @return {@code true} if the key exists in the subtree, otherwise {@code false}
     *
     * Time Complexity: Best O(log n), Average O(log n), Worst O(log n).
     * Space Complexity: O(1).
     */
    private boolean containsKey(BTreeNode<K, V> node, K key) {
        if (node == null) {
            return false;
        }

        int index = findInsertionIndex(node, key);
        if (index < node.keyCount && key.compareTo(node.keys[index]) == 0) {
            return true;
        }

        if (node.leaf) {
            return false;
        }
        return containsKey(node.children[index], key);
    }

    /**
     * Updates the value of an existing key in the tree.
     *
     * @param node the current subtree to update
     * @param key the key whose value should be changed
     * @param value the new value
     * @return {@code true} if the key was found and updated, otherwise {@code false}
     *
     * Time Complexity: Best O(log n), Average O(log n), Worst O(log n).
     * Space Complexity: O(1).
     */
    private boolean updateValue(BTreeNode<K, V> node, K key, V value) {
        int index = findInsertionIndex(node, key);
        if (index < node.keyCount && key.compareTo(node.keys[index]) == 0) {
            node.values[index] = value;
            return true;
        }
        if (node.leaf) {
            return false;
        }
        return updateValue(node.children[index], key, value);
    }

    /**
     * Searches for a value in a subtree.
     *
     * @param node the current subtree to inspect
     * @param key the key to search for
     * @return the associated value or {@code null} if absent
     *
     * Time Complexity: Best O(log n), Average O(log n), Worst O(log n).
     * Space Complexity: O(1).
     */
    private V search(BTreeNode<K, V> node, K key) {
        if (node == null) {
            return null;
        }

        int index = findInsertionIndex(node, key);
        if (index < node.keyCount && key.compareTo(node.keys[index]) == 0) {
            return node.values[index];
        }
        if (node.leaf) {
            return null;
        }
        return search(node.children[index], key);
    }

    /**
     * Traverses the tree in sorted order and appends entries to a builder.
     *
     * @param node the current subtree to visit
     * @param builder the output collector
     *
     * Time Complexity: Best O(n), Average O(n), Worst O(n).
     * Space Complexity: O(h) for recursion depth.
     */
    private void traverse(BTreeNode<K, V> node, StringBuilder builder) {
        if (node == null) {
            return;
        }

        for (int i = 0; i < node.keyCount; i++) {
            if (!node.leaf) {
                traverse(node.children[i], builder);
            }
            builder.append(node.keys[i]).append("=").append(node.values[i]).append(", ");
        }

        if (!node.leaf) {
            traverse(node.children[node.keyCount], builder);
        }
    }

    /**
     * Finds the insertion position for a key within a node using binary search.
     *
     * @param node the node to inspect
     * @param key the key to place
     * @return the insertion index for the key
     *
     * Time Complexity: Best O(1), Average O(log t), Worst O(log t), where t is the node key count.
     * Space Complexity: O(1).
     */
    private int findInsertionIndex(BTreeNode<K, V> node, K key) {
        int low = 0;
        int high = node.keyCount - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            int comparison = key.compareTo(node.keys[mid]);
            if (comparison == 0) {
                return mid;
            }
            if (comparison < 0) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }

    /**
     * A node in the B-tree that contains keys, values, and child pointers.
     */
    @SuppressWarnings("unchecked")
    private static class BTreeNode<K, V> {
        private final K[] keys;
        private final V[] values;
        private final BTreeNode<K, V>[] children;
        private int keyCount;
        private final boolean leaf;

        private BTreeNode(boolean leaf, int minDegree) {
            this.leaf = leaf;
            int maxKeys = 2 * minDegree - 1;
            this.keys = (K[]) new Comparable[maxKeys];
            this.values = (V[]) new Object[maxKeys];
            this.children = (BTreeNode<K, V>[]) new BTreeNode[maxKeys + 1];
        }
    }
}
