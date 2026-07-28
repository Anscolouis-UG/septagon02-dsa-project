package datastructures;

import java.util.Arrays;
import java.util.NoSuchElementException;

@SuppressWarnings("unchecked")
public class CustomHeap<T extends Comparable<T>> {
    private T[] heap;
    private int size;

    public CustomHeap() {
        this.heap = (T[]) new Comparable[16];
        this.size = 0;
    }

    public void insert(T item) {
        if (item == null) throw new IllegalArgumentException("Cannot insert null");
        if (size == heap.length) {
            heap = Arrays.copyOf(heap, heap.length * 2);
        }
        heap[size] = item;
        siftUp(size);
        size++;
    }

    public T extractMax() {
        if (isEmpty()) throw new NoSuchElementException("Heap is empty");
        T max = heap[0];
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        siftDown(0);
        return max;
    }

    public T peek() {
        if (isEmpty()) return null;
        return heap[0];
    }

    private void siftUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            if (heap[index].compareTo(heap[parentIndex]) > 0) {
                swap(index, parentIndex);
                index = parentIndex;
            } else break;
        }
    }

    private void siftDown(int index) {
        while (2 * index + 1 < size) {
            int leftChild = 2 * index + 1;
            int rightChild = 2 * index + 2;
            int largest = leftChild;

            if (rightChild < size && heap[rightChild].compareTo(heap[leftChild]) > 0) {
                largest = rightChild;
            }
            if (heap[largest].compareTo(heap[index]) > 0) {
                swap(index, largest);
                index = largest;
            } else break;
        }
    }

    private void swap(int i, int j) {
        T temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    public boolean isEmpty() { return size == 0; }
    public int size() { return size; }
}