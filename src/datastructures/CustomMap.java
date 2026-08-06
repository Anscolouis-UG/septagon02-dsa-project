package datastructures;

/**
 * A simple map abstraction implemented on top of a custom hash table.
 * The structure stores unique keys and associates each with one value.
 *
 * @param <K> the comparable key type
 * @param <V> the stored value type
 */
public class CustomMap<K extends Comparable<K>, V> {

    /** Underlying hash table used to store the mapping. */
    private final HashTable<K, V> table;

    /**
     * Constructs an empty custom map.
     *
     * Time Complexity: Best O(1), Average O(1), Worst O(1).
     * Space Complexity: O(M), where M is the initial bucket count of the backing table.
     */
    public CustomMap() {
        this.table = new HashTable<>();
    }

    /**
     * Inserts or updates a key-value pair in the map.
     *
     * @param key the key to insert or update
     * @param value the value to associate with the key
     *
     * Time Complexity: Best O(1), Average O(1), Worst O(N) due to hashing and chaining.
     * Space Complexity: O(1) additional space, excluding any resize overhead.
     */
    public void put(K key, V value) {
        table.put(key, value);
    }

    /**
     * Retrieves the value associated with a key.
     *
     * @param key the key to search for
     * @return the stored value or {@code null} if the key is absent
     *
     * Time Complexity: Best O(1), Average O(1), Worst O(N).
     * Space Complexity: O(1).
     */
    public V get(K key) {
        return table.get(key);
    }

    /**
     * Removes the entry for the specified key.
     *
     * @param key the key to remove
     * @return the removed value or {@code null} if the key was absent
     *
     * Time Complexity: Best O(1), Average O(1), Worst O(N).
     * Space Complexity: O(1).
     */
    public V remove(K key) {
        return table.remove(key);
    }

    /**
     * Checks whether the map contains a specific key.
     *
     * @param key the key to check
     * @return {@code true} if the key exists, otherwise {@code false}
     *
     * Time Complexity: Best O(1), Average O(1), Worst O(N).
     * Space Complexity: O(1).
     */
    public boolean containsKey(K key) {
        return table.containsKey(key);
    }

    /**
     * Returns the number of entries stored in the map.
     *
     * @return the current size of the map
     *
     * Time Complexity: Best O(1), Average O(1), Worst O(1).
     * Space Complexity: O(1).
     */
    public int size() {
        return table.size();
    }

    /**
     * Checks whether the map has no entries.
     *
     * @return {@code true} if the map is empty, otherwise {@code false}
     *
     * Time Complexity: Best O(1), Average O(1), Worst O(1).
     * Space Complexity: O(1).
     */
    public boolean isEmpty() {
        return table.isEmpty();
    }
}
