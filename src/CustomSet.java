

/**
 * A set implementation backed by a custom hash table using a dummy placeholder object.
 * Each element is stored once, which guarantees uniqueness.
 *
 * @param <E> the comparable element type
 */
public class CustomSet<E extends Comparable<E>> {

    /** Dummy object used as the value for every stored element. */
    private static final Object PRESENT = new Object();

    /** Underlying hash table used to store the set contents. */
    private final HashTable<E, Object> table;

    /**
     * Constructs an empty custom set.
     *
     * Time Complexity: Best O(1), Average O(1), Worst O(1).
     * Space Complexity: O(M), where M is the initial bucket count of the backing table.
     */
    public CustomSet() {
        this.table = new HashTable<>();
    }

    /**
     * Adds an element to the set if it is not already present.
     *
     * @param element the element to add
     * @return {@code true} if the element was inserted, otherwise {@code false}
     *
     * Time Complexity: Best O(1), Average O(1), Worst O(N).
     * Space Complexity: O(1) additional space, excluding any resize overhead.
     */
    public boolean add(E element) {
        if (element == null) {
            throw new IllegalArgumentException("Element must not be null");
        }
        if (table.containsKey(element)) {
            return false;
        }
        table.put(element, PRESENT);
        return true;
    }

    /**
     * Removes an element from the set if it exists.
     *
     * @param element the element to remove
     * @return {@code true} if the element was removed, otherwise {@code false}
     *
     * Time Complexity: Best O(1), Average O(1), Worst O(N).
     * Space Complexity: O(1).
     */
    public boolean remove(E element) {
        if (element == null) {
            throw new IllegalArgumentException("Element must not be null");
        }
        return table.remove(element) != null;
    }

    /**
     * Checks whether the set contains a specific element.
     *
     * @param element the element to check
     * @return {@code true} if the element is present, otherwise {@code false}
     *
     * Time Complexity: Best O(1), Average O(1), Worst O(N).
     * Space Complexity: O(1).
     */
    public boolean contains(E element) {
        if (element == null) {
            throw new IllegalArgumentException("Element must not be null");
        }
        return table.containsKey(element);
    }

    /**
     * Returns the number of elements in the set.
     *
     * @return the current size of the set
     *
     * Time Complexity: Best O(1), Average O(1), Worst O(1).
     * Space Complexity: O(1).
     */
    public int size() {
        return table.size();
    }

    /**
     * Checks whether the set is empty.
     *
     * @return {@code true} if there are no elements, otherwise {@code false}
     *
     * Time Complexity: Best O(1), Average O(1), Worst O(1).
     * Space Complexity: O(1).
     */
    public boolean isEmpty() {
        return table.isEmpty();
    }
}
