package datastructures;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A generic max-heap (priority queue) backed by a resizable array.
 * Elements must implement Comparable so the heap can order them.
 *
 * The included ServiceRequest inner class is the domain type used by the
 * Ghana Smart Service Operations demos and reads directly from
 * data_base/service_requests.csv.
 *
 * @param <T> any Comparable element type
 */
public class CustomHeap<T extends Comparable<T>> {

    // ─── inner domain class (kept for CSV demo) ─────────────────────────────
    public static class ServiceRequest implements Comparable<ServiceRequest> {
        private int requestId;
        private String requestType;
        private int locationId;
        private String priorityStr;
        private int priorityScore;
        private String status;
        private String dateCreated;
        private String deadline;
        private String assignedTeam;
        private String requiredResourceType;
        private int estimatedDurationHours;
        private String description;

        public ServiceRequest(int requestId, String requestType, int locationId, String priorityStr,
                              String status, String dateCreated, String deadline, String assignedTeam,
                              String requiredResourceType, int estimatedDurationHours, String description) {
            this.requestId = requestId;
            this.requestType = requestType;
            this.locationId = locationId;
            this.priorityStr = priorityStr;
            this.status = status;
            this.dateCreated = dateCreated;
            this.deadline = deadline;
            this.assignedTeam = assignedTeam;
            this.requiredResourceType = requiredResourceType;
            this.estimatedDurationHours = estimatedDurationHours;
            this.description = description;
            this.priorityScore = parsePriority(priorityStr);
        }

        private int parsePriority(String priority) {
            if (priority == null) return 0;
            switch (priority.trim().toLowerCase()) {
                case "critical": return 4;
                case "high":     return 3;
                case "medium":   return 2;
                case "low":      return 1;
                default:         return 0;
            }
        }

        @Override
        public int compareTo(ServiceRequest other) {
            return Integer.compare(this.priorityScore, other.priorityScore);
        }

        @Override
        public String toString() {
            return String.format("Request #%d [%-8s] - %s (Location ID: %d, Team: %s)",
                    requestId, priorityStr, description, locationId, assignedTeam);
        }
    }
    // ────────────────────────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    private T[] heap = (T[]) new Comparable[16];
    private int size;

    public CustomHeap() {
        this.size = 0;
    }

    /**
     * Inserts an element into the heap.
     * Time Complexity: O(log n) average/worst.
     */
    public void insert(T item) {
        if (item == null) throw new IllegalArgumentException("Cannot insert null");
        if (size == heap.length) {
            @SuppressWarnings("unchecked")
            T[] bigger = (T[]) new Comparable[heap.length * 2];
            for (int i = 0; i < size; i++) bigger[i] = heap[i];
            heap = bigger;
        }
        heap[size] = item;
        siftUp(size);
        size++;
    }

    /**
     * Removes and returns the maximum (highest-priority) element.
     * Time Complexity: O(log n) average/worst.
     */
    public T extractMax() {
        if (isEmpty()) throw new java.util.NoSuchElementException("Heap is empty");
        T max = heap[0];
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        if (size > 0) siftDown(0);
        return max;
    }

    /** Returns the maximum element without removing it. */
    public T peek() {
        if (isEmpty()) throw new java.util.NoSuchElementException("Heap is empty");
        return heap[0];
    }

    public boolean isEmpty() { return size == 0; }
    public int size()       { return size; }

    // ── Internal helpers ─────────────────────────────────────────────────────

    private void siftUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (heap[index].compareTo(heap[parent]) > 0) {
                swap(index, parent);
                index = parent;
            } else break;
        }
    }

    private void siftDown(int index) {
        while (2 * index + 1 < size) {
            int left  = 2 * index + 1;
            int right = 2 * index + 2;
            int largest = left;
            if (right < size && heap[right].compareTo(heap[left]) > 0) largest = right;
            if (heap[largest].compareTo(heap[index]) > 0) {
                swap(index, largest);
                index = largest;
            } else break;
        }
    }

    private void swap(int i, int j) {
        T tmp = heap[i];
        heap[i] = heap[j];
        heap[j] = tmp;
    }

    // ── CSV demo entry point (ServiceRequest) ────────────────────────────────
    public static void main(String[] args) throws IOException {
        CustomHeap<ServiceRequest> priorityQueue = new CustomHeap<>();

        java.util.List<String> rows = Files.readAllLines(Path.of("data_base", "service_requests.csv"));
        for (int index = 1; index < rows.size(); index++) {
            String[] columns = rows.get(index).split(",", -1);
            priorityQueue.insert(new ServiceRequest(
                    Integer.parseInt(columns[0]), columns[1], Integer.parseInt(columns[2]), columns[3],
                    columns[4], columns[5], columns[6], columns[7], columns[8],
                    Integer.parseInt(columns[9]), columns[10]));
        }

        System.out.println("=== Service Requests Priority Extraction (CustomHeap) ===");
        while (!priorityQueue.isEmpty()) {
            System.out.println(priorityQueue.extractMax());
        }
    }
}