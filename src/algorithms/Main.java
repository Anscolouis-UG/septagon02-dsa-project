import java.time.LocalDateTime;

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
        java.util.List<ServiceRequest> requests = new java.util.ArrayList<>();
        requests.add(new ServiceRequest(103, "Maintenance", LocalDateTime.now().plusHours(5)));
        requests.add(new ServiceRequest(101, "Delivery", LocalDateTime.now().plusHours(2)));
        requests.add(new ServiceRequest(102, "Security", LocalDateTime.now().plusHours(1)));

        System.out.println("=== INITIAL UNORDERED LIST ===");
        requests.forEach(req -> System.out.println(req));

        // Test Quicksort
        SortAlgorithms.quickSort(requests, java.util.Comparator.comparing(r -> r.deadline));
        System.out.println("\n=== AFTER QUICKSORT (By Deadline) ===");
        requests.forEach(req -> System.out.println(req));

        // Test Linear Search
        int maintenanceIdx = SearchAlgorithms.linearSearch(requests, r -> r.type.equals("Maintenance"));
        System.out.println("\n=== LINEAR SEARCH ===");
        System.out.println("First 'Maintenance' request found at index: " + maintenanceIdx);

        // Test Binary Search (Requires sorting by ID first)
        SortAlgorithms.mergeSort(requests, java.util.Comparator.comparingInt(r -> r.id));
        ServiceRequest key = new ServiceRequest(102, null, null);
        int binaryIdx = SearchAlgorithms.binarySearch(requests, key, java.util.Comparator.comparingInt(r -> r.id));
        System.out.println("\n=== BINARY SEARCH ===");
        System.out.println("Request #102 found at index: " + binaryIdx);
    }
}