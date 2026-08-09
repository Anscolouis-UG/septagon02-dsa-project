package algorithms;

import datastructures.CustomArrayList;

public class SearchAlgorithms {

    // Local functional interface — replaces java.util.function.Predicate
    public interface Predicate<T> {
        boolean test(T item);
    }

    // Local functional interface — replaces java.util.Comparator
    public interface Comparator<T> {
        int compare(T a, T b);
    }

    /**
     * Linear search — scans every element until the predicate matches.
     * Time Complexity: O(n) best/average/worst.
     *
     * @param list      the list to search
     * @param predicate condition to match
     * @return index of the first match, or -1 if not found
     */
    public static <T> int linearSearch(CustomArrayList<T> list, Predicate<T> predicate) {
        if (list == null || predicate == null) return -1;

        for (int i = 0; i < list.size(); i++) {
            if (predicate.test(list.get(i))) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Binary search — requires the list to be sorted by the same comparator.
     * Time Complexity: O(log n) best/average/worst.
     *
     * @param list       the SORTED list to search
     * @param key        the key to find
     * @param comparator ordering used to compare elements
     * @return index of the match, or -1 if not found
     */
    public static <T> int binarySearch(CustomArrayList<T> list, T key, Comparator<T> comparator) {
        if (list == null || key == null || comparator == null) return -1;

        int low = 0;
        int high = list.size() - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            int cmp = comparator.compare(list.get(mid), key);

            if (cmp == 0) {
                return mid;
            } else if (cmp < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }
}