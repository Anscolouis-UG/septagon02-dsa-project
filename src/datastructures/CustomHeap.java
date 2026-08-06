package datastructures;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class CustomHeap {

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

    private ServiceRequest[] heap;
    private int size;

    @SuppressWarnings("unchecked")
    public CustomHeap() {
        this.heap = new ServiceRequest[16];
        this.size = 0;
    }

    public void insert(ServiceRequest item) {
        if (item == null) throw new IllegalArgumentException("Cannot insert null");
        if (size == heap.length) {
            heap = Arrays.copyOf(heap, heap.length * 2);
        }
        heap[size] = item;
        siftUp(size);
        size++;
    }

    public ServiceRequest extractMax() {
        if (isEmpty()) throw new NoSuchElementException("Heap is empty");
        ServiceRequest max = heap[0];
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        siftDown(0);
        return max;
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
        ServiceRequest temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    public boolean isEmpty() { return size == 0; }
    public int size() { return size; }

    public static void main(String[] args) throws IOException {
        CustomHeap priorityQueue = new CustomHeap();

        List<String> rows = Files.readAllLines(Path.of("data_base", "service_requests.csv"));
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
