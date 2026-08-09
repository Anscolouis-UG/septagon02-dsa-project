package algorithms;

import datastructures.CustomArrayList;

/**
 * SearchSortDemo.java
 *
 * Demonstrates all searching and sorting algorithms on REAL data
 * loaded from data_base/service_requests.csv and data_base/resources.csv.
 *
 * Covers:
 *   - Linear search  (find request by type)
 *   - Binary search  (find request by ID after sorting)
 *   - Selection sort (sort requests by estimated duration)
 *   - Insertion sort (sort requests by priority score)
 *   - Merge sort     (sort requests by deadline)
 *   - Quick sort     (sort resources by cost per hour)
 */
public class SearchSortDemo {

    // ─── Domain record: ServiceRequest ──────────────────────────────────────
    static class ServiceRequest {
        int    id;
        String type;
        int    locationId;
        String priority;
        int    priorityScore;
        String status;
        String dateCreated;
        String deadline;
        String assignedTeam;
        String resourceType;
        int    durationHours;
        String description;

        ServiceRequest(String[] cols) {
            this.id           = Integer.parseInt(cols[0].trim());
            this.type         = cols[1].trim();
            this.locationId   = Integer.parseInt(cols[2].trim());
            this.priority     = cols[3].trim();
            this.priorityScore = parsePriority(cols[3].trim());
            this.status       = cols[4].trim();
            this.dateCreated  = cols[5].trim();
            this.deadline     = cols[6].trim();
            this.assignedTeam = cols[7].trim();
            this.resourceType = cols[8].trim();
            this.durationHours = Integer.parseInt(cols[9].trim());
            this.description  = cols[10].trim();
        }

        private static int parsePriority(String p) {
            switch (p.toLowerCase()) {
                case "critical": return 4;
                case "high":     return 3;
                case "medium":   return 2;
                case "low":      return 1;
                default:         return 0;
            }
        }

        public String toRow() {
            return String.format("  ID:%-2d | %-12s | %-8s | %dh | %s",
                id, type, priority, durationHours, description);
        }

        @Override public String toString() { return toRow(); }
    }

    // ─── Domain record: Resource ─────────────────────────────────────────────
    static class Resource {
        int    id;
        String name;
        String type;
        int    capacity;
        int    locationId;
        String status;
        double costPerHour;

        Resource(String[] cols) {
            this.id          = Integer.parseInt(cols[0].trim());
            this.name        = cols[1].trim();
            this.type        = cols[2].trim();
            this.capacity    = Integer.parseInt(cols[3].trim());
            this.locationId  = Integer.parseInt(cols[4].trim());
            this.status      = cols[5].trim();
            this.costPerHour = Double.parseDouble(cols[6].trim());
        }

        public String toRow() {
            return String.format("  ID:%-2d | %-35s | %-9s | GH¢%.0f/hr | %s",
                id, name, type, costPerHour, status);
        }

        @Override public String toString() { return toRow(); }
    }

    // ─── CSV loaders ─────────────────────────────────────────────────────────

    static CustomArrayList<ServiceRequest> loadRequests(String path) {
        CustomArrayList<ServiceRequest> list = new CustomArrayList<>();
        try (java.util.Scanner sc = new java.util.Scanner(new java.io.File(path))) {
            if (sc.hasNextLine()) sc.nextLine(); // skip header
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] cols = line.split(",", -1);
                if (cols.length >= 11) list.add(new ServiceRequest(cols));
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Could not read " + path + ": " + e.getMessage());
        }
        return list;
    }

    static CustomArrayList<Resource> loadResources(String path) {
        CustomArrayList<Resource> list = new CustomArrayList<>();
        try (java.util.Scanner sc = new java.util.Scanner(new java.io.File(path))) {
            if (sc.hasNextLine()) sc.nextLine(); // skip header
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] cols = line.split(",", -1);
                if (cols.length >= 7) list.add(new Resource(cols));
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Could not read " + path + ": " + e.getMessage());
        }
        return list;
    }

    // ─── Print helpers ───────────────────────────────────────────────────────

    static void printRequests(CustomArrayList<ServiceRequest> list) {
        for (int i = 0; i < list.size(); i++) System.out.println(list.get(i).toRow());
    }

    static void printResources(CustomArrayList<Resource> list) {
        for (int i = 0; i < list.size(); i++) System.out.println(list.get(i).toRow());
    }

    // ─── Copy helpers ────────────────────────────────────────────────────────

    static CustomArrayList<ServiceRequest> copyRequests(CustomArrayList<ServiceRequest> src) {
        CustomArrayList<ServiceRequest> copy = new CustomArrayList<>();
        for (int i = 0; i < src.size(); i++) copy.add(src.get(i));
        return copy;
    }

    static CustomArrayList<Resource> copyResources(CustomArrayList<Resource> src) {
        CustomArrayList<Resource> copy = new CustomArrayList<>();
        for (int i = 0; i < src.size(); i++) copy.add(src.get(i));
        return copy;
    }

    // ─── Main ────────────────────────────────────────────────────────────────

    public static void main(String[] args) {
        final String REQ_PATH = "data_base/service_requests.csv";
        final String RES_PATH = "data_base/resources.csv";

        System.out.println("================================================================");
        System.out.println("  Ghana Smart Service Operations — Search & Sort Demo");
        System.out.println("  University of Ghana, Legon Campus");
        System.out.println("================================================================\n");

        // Load data
        CustomArrayList<ServiceRequest> requests = loadRequests(REQ_PATH);
        CustomArrayList<Resource>       resources = loadResources(RES_PATH);

        System.out.printf("Loaded: %d service requests | %d resources%n%n",
            requests.size(), resources.size());

        // ── ORIGINAL DATA ──────────────────────────────────────────────────
        System.out.println("── ORIGINAL SERVICE REQUESTS (as loaded from CSV) ─────────────");
        printRequests(requests);

        // ══════════════════════════════════════════════════════════════════════
        //  SEARCHING
        // ══════════════════════════════════════════════════════════════════════
        System.out.println("\n================================================================");
        System.out.println("  PART 1 — SEARCHING");
        System.out.println("================================================================");

        // ── Linear Search: find first "Critical" request ───────────────────
        System.out.println("\n── Linear Search: find first CRITICAL priority request ─────────");
        int idx = SearchAlgorithms.linearSearch(requests, r -> r.priority.equalsIgnoreCase("Critical"));
        if (idx >= 0) {
            System.out.println("  Found at index " + idx + ":");
            System.out.println(requests.get(idx).toRow());
        } else {
            System.out.println("  No Critical request found.");
        }

        // ── Linear Search: find all "Pending" requests ─────────────────────
        System.out.println("\n── Linear Search: find ALL PENDING requests ────────────────────");
        for (int i = 0; i < requests.size(); i++) {
            if (requests.get(i).status.equalsIgnoreCase("Pending")) {
                System.out.println("  [index " + i + "] " + requests.get(i).toRow());
            }
        }

        // ── Binary Search: sort by ID then search for ID=5 ────────────────
        System.out.println("\n── Binary Search: search for Request ID = 5 ────────────────────");
        System.out.println("  (Precondition: list must be sorted by ID — applying Insertion Sort first)");
        CustomArrayList<ServiceRequest> sortedById = copyRequests(requests);
        SortAlgorithms.insertionSort(sortedById, (a, b) -> Integer.compare(a.id, b.id));
        System.out.println("  List sorted by ID: ");
        for (int i = 0; i < sortedById.size(); i++) {
            System.out.printf("    [%d] ID=%d%n", i, sortedById.get(i).id);
        }
        ServiceRequest target = new ServiceRequest(new String[]{"5","","0","Low","","","","","","0",""});
        int binaryIdx = SearchAlgorithms.binarySearch(sortedById, target, (a, b) -> Integer.compare(a.id, b.id));
        if (binaryIdx >= 0) {
            System.out.println("  Found Request ID=5 at sorted index " + binaryIdx + ":");
            System.out.println(sortedById.get(binaryIdx).toRow());
        } else {
            System.out.println("  Request ID=5 not found.");
        }

        // ══════════════════════════════════════════════════════════════════════
        //  SORTING
        // ══════════════════════════════════════════════════════════════════════
        System.out.println("\n================================================================");
        System.out.println("  PART 2 — SORTING");
        System.out.println("================================================================");

        // ── Selection Sort: by estimated duration (ascending) ─────────────
        System.out.println("\n── Selection Sort: service requests by DURATION (ascending) ────");
        CustomArrayList<ServiceRequest> byDuration = copyRequests(requests);
        SortAlgorithms.selectionSort(byDuration, (a, b) -> Integer.compare(a.durationHours, b.durationHours));
        printRequests(byDuration);

        // ── Insertion Sort: by priority score (highest first) ─────────────
        System.out.println("\n── Insertion Sort: service requests by PRIORITY (Critical first) ─");
        CustomArrayList<ServiceRequest> byPriority = copyRequests(requests);
        SortAlgorithms.insertionSort(byPriority, (a, b) -> Integer.compare(b.priorityScore, a.priorityScore));
        printRequests(byPriority);

        // ── Merge Sort: by deadline (earliest first) ──────────────────────
        System.out.println("\n── Merge Sort: service requests by DEADLINE (earliest first) ───");
        CustomArrayList<ServiceRequest> byDeadline = copyRequests(requests);
        SortAlgorithms.mergeSort(byDeadline, (a, b) -> a.deadline.compareTo(b.deadline));
        printRequests(byDeadline);

        // ── Quick Sort: resources by cost per hour (cheapest first) ───────
        System.out.println("\n── Quick Sort: resources by COST PER HOUR (cheapest first) ────");
        CustomArrayList<Resource> byCost = copyResources(resources);
        SortAlgorithms.quickSort(byCost, (a, b) -> Double.compare(a.costPerHour, b.costPerHour));
        printResources(byCost);

        System.out.println("\n================================================================");
        System.out.println("  All search & sort demos completed on real CSV data!");
        System.out.println("================================================================");
    }
}
