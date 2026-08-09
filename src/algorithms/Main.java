import java.time.LocalDateTime;
import algorithms.SortAlgorithms;
import algorithms.SearchAlgorithms;
import datastructures.CustomArrayList;

public class Main {

    static class ServiceRequest {
        int id;
        String type;
        LocalDateTime deadline;

        public ServiceRequest(int id, String type, LocalDateTime deadline) {
            this.id = id;
            this.type = type;
            this.deadline = deadline;
        }

        @Override
        public String toString() {
            return "Request #" + id + " [" + type + "] Deadline: " + deadline;
        }
    }

    public static void main(String[] args) {
        CustomArrayList<ServiceRequest> requests = new CustomArrayList<>();
        requests.add(new ServiceRequest(103, "Maintenance", LocalDateTime.now().plusHours(5)));
        requests.add(new ServiceRequest(101, "Delivery",    LocalDateTime.now().plusHours(2)));
        requests.add(new ServiceRequest(102, "Security",    LocalDateTime.now().plusHours(1)));

        System.out.println("=== INITIAL UNORDERED LIST ===");
        for (int i = 0; i < requests.size(); i++) System.out.println(requests.get(i));

        // Sort by deadline (ascending) using QuickSort
        SortAlgorithms.quickSort(requests, (a, b) -> a.deadline.compareTo(b.deadline));
        System.out.println("\n=== AFTER QUICKSORT (By Deadline) ===");
        for (int i = 0; i < requests.size(); i++) System.out.println(requests.get(i));

        // Linear Search: find first "Maintenance" request
        int maintenanceIdx = SearchAlgorithms.linearSearch(requests, r -> r.type.equals("Maintenance"));
        System.out.println("\n=== LINEAR SEARCH ===");
        System.out.println("First 'Maintenance' request found at index: " + maintenanceIdx);

        // Binary Search requires sorting by ID first
        SortAlgorithms.mergeSort(requests, (a, b) -> Integer.compare(a.id, b.id));
        ServiceRequest key = new ServiceRequest(102, null, null);
        int binaryIdx = SearchAlgorithms.binarySearch(requests, key, (a, b) -> Integer.compare(a.id, b.id));
        System.out.println("\n=== BINARY SEARCH ===");
        System.out.println("Request #102 found at index: " + binaryIdx);
    }
}