import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SortAlgorithms {

    public static <T> void selectionSort(List<T> list, Comparator<T> comparator) {
        if (list == null || list.size() <= 1) return;
        int n = list.size();

        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (comparator.compare(list.get(j), list.get(minIdx)) < 0) {
                    minIdx = j;
                }
            }
            T temp = list.get(minIdx);
            list.set(minIdx, list.get(i));
            list.set(i, temp);
        }
    }

    public static <T> void insertionSort(List<T> list, Comparator<T> comparator) {
        if (list == null || list.size() <= 1) return;
        int n = list.size();

        for (int i = 1; i < n; i++) {
            T key = list.get(i);
            int j = i - 1;

            while (j >= 0 && comparator.compare(list.get(j), key) > 0) {
                list.set(j + 1, list.get(j));
                j = j - 1;
            }
            list.set(j + 1, key);
        }
    }

    public static <T> void mergeSort(List<T> list, Comparator<T> comparator) {
        if (list == null || list.size() <= 1) return;
        mergeSortRecursive(list, 0, list.size() - 1, comparator);
    }

    private static <T> void mergeSortRecursive(List<T> list, int left, int right, Comparator<T> comparator) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSortRecursive(list, left, mid, comparator);
            mergeSortRecursive(list, mid + 1, right, comparator);
            merge(list, left, mid, right, comparator);
        }
    }

    private static <T> void merge(List<T> list, int left, int mid, int right, Comparator<T> comparator) {
        List<T> leftList = new ArrayList<>(list.subList(left, mid + 1));
        List<T> rightList = new ArrayList<>(list.subList(mid + 1, right + 1));

        int i = 0, j = 0, k = left;
        while (i < leftList.size() && j < rightList.size()) {
            if (comparator.compare(leftList.get(i), rightList.get(j)) <= 0) {
                list.set(k++, leftList.get(i++));
            } else {
                list.set(k++, rightList.get(j++));
            }
        }

        while (i < leftList.size()) {
            list.set(k++, leftList.get(i++));
        }
        while (j < rightList.size()) {
            list.set(k++, rightList.get(j++));
        }
    }

    public static <T> void quickSort(List<T> list, Comparator<T> comparator) {
        if (list == null || list.size() <= 1) return;
        quickSortRecursive(list, 0, list.size() - 1, comparator);
    }

    private static <T> void quickSortRecursive(List<T> list, int low, int high, Comparator<T> comparator) {
        if (low < high) {
            int pivotIndex = partition(list, low, high, comparator);
            quickSortRecursive(list, low, pivotIndex - 1, comparator);
            quickSortRecursive(list, pivotIndex + 1, high, comparator);
        }
    }

    private static <T> int partition(List<T> list, int low, int high, Comparator<T> comparator) {
        T pivot = list.get(high);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (comparator.compare(list.get(j), pivot) <= 0) {
                i++;
                T temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
            }
        }

        T temp = list.get(i + 1);
        list.set(i + 1, list.get(high));
        list.set(high, temp);

        return i + 1;
    }
}