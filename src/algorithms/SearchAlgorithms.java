package algorithms;

public class SearchAlgorithms {

    public static <T> int linearSearch(java.util.List<T> list, java.util.function.Predicate<T> predicate) {
        if (list == null || predicate == null) return -1;

        for (int i = 0; i < list.size(); i++) {
            if (predicate.test(list.get(i))) {
                return i;
            }
        }
        return -1;
    }

    public static <T> int binarySearch(java.util.List<T> list, T key, java.util.Comparator<T> comparator) {
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