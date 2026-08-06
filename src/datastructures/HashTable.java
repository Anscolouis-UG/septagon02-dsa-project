package datastructures;

/**
 * A hash table implementation using separate chaining with custom linked-entry nodes.
 * This structure stores key-value pairs and supports dynamic resizing when the load
 * factor exceeds the configured threshold.
 *
 * @param <K> the comparable key type
 * @param <V> the stored value type
 */
public class HashTable<K extends Comparable<K>, V> {

    /** Default initial capacity for the backing bucket array. */
    private static final int DEFAULT_CAPACITY = 16;

    /** Resize threshold for the load factor. */
    private static final double LOAD_FACTOR_THRESHOLD = 0.75;

    /** Bucket array for storing chained entries. */
    private Entry<K, V>[] table;

    /** Number of stored entries. */
    private int size;

    /** Current physical capacity of the bucket array. */
    private int capacity;

    /**
     * Constructs a hash table with the default initial capacity.
     *
     * Time Complexity: Best O(1), Average O(1), Worst O(1) for initialization.
     * Space Complexity: O(M), where M is the initial bucket count.
     */
    public HashTable() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Constructs a hash table with a custom initial capacity.
     *
     * @param initialCapacity the starting bucket count
     *
     * Time Complexity: Best O(1), Average O(1), Worst O(1) for initialization.
     * Space Complexity: O(M), where M is the initial bucket count.
     */
    public HashTable(int initialCapacity) {
        if (initialCapacity < 1) {
            throw new IllegalArgumentException("Initial capacity must be positive");
        }
        this.capacity = initialCapacity;
        this.table = createTable(initialCapacity);
    }

    /**
     * Inserts or updates a key-value pair in the hash table.
     *
     * @param key the key to insert or update
     * @param value the value to associate with the key
     *
     * Time Complexity: Best O(1), Average O(1), Worst O(N) due to chaining.
     * Space Complexity: O(1) additional space, excluding the new entry.
     */
    public void put(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Key and value must not be null");
        }

        if (((double) size / capacity) > LOAD_FACTOR_THRESHOLD) {
            resize();
        }

        int index = compressIndex(key.hashCode());
        Entry<K, V> current = table[index];

        while (current != null) {
            if (current.key.equals(key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        table[index] = new Entry<>(key, value, table[index]);
        size++;
    }

    /**
     * Retrieves the value associated with a key.
     *
     * @param key the key to search for
     * @return the stored value or {@code null} if the key is absent
     *
     * Time Complexity: Best O(1), Average O(1), Worst O(N) due to chaining.
     * Space Complexity: O(1).
     */
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key must not be null");
        }

        int index = compressIndex(key.hashCode());
        Entry<K, V> current = table[index];
        while (current != null) {
            if (current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    /**
     * Removes the entry with the specified key.
     *
     * @param key the key to remove
     * @return the removed value or {@code null} if the key was not present
     *
     * Time Complexity: Best O(1), Average O(1), Worst O(N) due to chaining.
     * Space Complexity: O(1).
     */
    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key must not be null");
        }

        int index = compressIndex(key.hashCode());
        Entry<K, V> current = table[index];
        Entry<K, V> previous = null;

        while (current != null) {
            if (current.key.equals(key)) {
                if (previous == null) {
                    table[index] = current.next;
                } else {
                    previous.next = current.next;
                }
                size--;
                return current.value;
            }
            previous = current;
            current = current.next;
        }

        return null;
    }

    /**
     * Checks whether a key exists in the hash table.
     *
     * @param key the key to check
     * @return {@code true} if the key exists, otherwise {@code false}
     *
     * Time Complexity: Best O(1), Average O(1), Worst O(N) due to chaining.
     * Space Complexity: O(1).
     */
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    /**
     * Returns the number of stored entries.
     *
     * @return the current size of the hash table
     *
     * Time Complexity: Best O(1), Average O(1), Worst O(1).
     * Space Complexity: O(1).
     */
    public int size() {
        return size;
    }

    /**
     * Checks whether the hash table is empty.
     *
     * @return {@code true} if there are no stored entries, otherwise {@code false}
     *
     * Time Complexity: Best O(1), Average O(1), Worst O(1).
     * Space Complexity: O(1).
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Compresses a hash code into a valid bucket index.
     *
     * @param hashCode the raw hash code value
     * @return the bucket index for the given hash code
     *
     * Time Complexity: Best O(1), Average O(1), Worst O(1).
     * Space Complexity: O(1).
     */
    private int compressIndex(int hashCode) {
        return Math.floorMod(hashCode, capacity);
    }

    /**
     * Creates a new bucket array of the requested capacity.
     *
     * @param capacity the size of the new bucket array
     * @return the newly created bucket array
     *
     * Time Complexity: Best O(M), Average O(M), Worst O(M), where M is the capacity.
     * Space Complexity: O(M).
     */
    @SuppressWarnings("unchecked")
    private Entry<K, V>[] createTable(int capacity) {
        return (Entry<K, V>[]) new Entry[capacity];
    }

    /**
     * Doubles the table capacity and rehashes all stored entries.
     *
     * Time Complexity: Best O(M), Average O(M), Worst O(M), where M is the current number of entries.
     * Space Complexity: O(M).
     */
    private void resize() {
        Entry<K, V>[] oldTable = table;
        int newCapacity = capacity * 2;
        table = createTable(newCapacity);
        capacity = newCapacity;
        size = 0;

        for (Entry<K, V> entry : oldTable) {
            while (entry != null) {
                put(entry.key, entry.value);
                entry = entry.next;
            }
        }
    }

    /**
     * A custom linked node that stores one key-value pair in a bucket.
     */
    private static class Entry<K, V> {
        private final K key;
        private V value;
        private Entry<K, V> next;

        private Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
