package algorithms;

import datastructures.CustomArrayList;

public class UserInterface {
    public static void main(String[] args) {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        boolean running = true;

        System.out.println("=============================================");
        System.out.println("  Java Console Searching & Sorting Program   ");
        System.out.println("  University of Ghana, Legon Campus          ");
        System.out.println("=============================================");

        // Load data on startup
        String REQ_PATH = "data_base/service_requests.csv";
        CustomArrayList<SearchSortDemo.ServiceRequest> list = SearchSortDemo.loadRequests(REQ_PATH);
        
        System.out.println("Loaded " + list.size() + " service requests from database.\n");

        while (running) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Perform Searching");
            System.out.println("2. Perform Sorting");
            System.out.println("3. Exit Program");
            System.out.print("Enter your choice (1-3): ");

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input! Please enter a number.");
                scanner.next(); // consume
                continue;
            }

            int mainChoice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            if (mainChoice == 3) {
                System.out.println("\nThank you for using the program. Goodbye!");
                running = false;
                continue;
            }

            if (mainChoice < 1 || mainChoice > 3) {
                System.out.println("Invalid choice! Please choose a number between 1 and 3.");
                continue;
            }

            if (mainChoice == 1) {
                handleSearching(scanner, list);
            } else {
                handleSorting(scanner, list);
            }
        }
        scanner.close();
    }

    private static void handleSearching(java.util.Scanner scanner, CustomArrayList<SearchSortDemo.ServiceRequest> list) {
        System.out.println("\n--- SEARCHING MENU ---");
        System.out.println("1. Linear Search (by Request Type, e.g., 'Maintenance')");
        System.out.println("2. Binary Search (by Request ID, e.g., 5)");
        System.out.print("Choose a search algorithm (1-2): ");
        
        if (!scanner.hasNextInt()) {
            System.out.println("Invalid choice.");
            scanner.nextLine();
            return;
        }
        
        int searchChoice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        int resultIndex = -1;

        if (searchChoice == 1) {
            System.out.print("Enter the Request Type to search for (e.g., 'Maintenance', 'Delivery', 'Waste', 'Security', 'Grounds'): ");
            String target = scanner.nextLine().trim();
            resultIndex = SearchAlgorithms.linearSearch(list, r -> r.type.equalsIgnoreCase(target));
            
            if (resultIndex != -1) {
                System.out.println("\nFound first instance of '" + target + "' at index: " + resultIndex);
                System.out.println(list.get(resultIndex).toRow());
            } else {
                System.out.println("Target type '" + target + "' was not found in the list.");
            }
        } else if (searchChoice == 2) {
            System.out.print("Enter the Request ID to search for (e.g., 1-150): ");
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid ID.");
                scanner.nextLine();
                return;
            }
            int targetId = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Note: Sorting list by ID first (required for Binary Search)...");
            
            // Copy list
            CustomArrayList<SearchSortDemo.ServiceRequest> sorted = new CustomArrayList<>();
            for (int i = 0; i < list.size(); i++) sorted.add(list.get(i));
            
            // Sort by ID
            SortAlgorithms.insertionSort(sorted, (a, b) -> Integer.compare(a.id, b.id));
            
            SearchSortDemo.ServiceRequest dummy = new SearchSortDemo.ServiceRequest(new String[]{String.valueOf(targetId), "", "0", "", "", "", "", "", "", "0", ""});
            resultIndex = SearchAlgorithms.binarySearch(sorted, dummy, (a, b) -> Integer.compare(a.id, b.id));
            
            if (resultIndex != -1) {
                System.out.println("\nTarget ID " + targetId + " found at sorted index: " + resultIndex);
                System.out.println(sorted.get(resultIndex).toRow());
            } else {
                System.out.println("Target ID " + targetId + " was not found in the list.");
            }
        } else {
            System.out.println("Invalid search choice.");
        }
    }

    private static void handleSorting(java.util.Scanner scanner, CustomArrayList<SearchSortDemo.ServiceRequest> list) {
        System.out.println("\n--- SORTING MENU ---");
        System.out.println("1. Bubble Sort (by Estimated Duration)");
        System.out.println("2. Selection Sort (by Request Type alphabetically)");
        System.out.println("3. Insertion Sort (by Priority Score, Highest First)");
        System.out.println("4. Merge Sort (by Location ID alphabetically)");
        System.out.println("5. Quick Sort (by Request ID)");
        System.out.print("Choose a sorting algorithm (1-5): ");
        
        if (!scanner.hasNextInt()) {
            System.out.println("Invalid choice.");
            scanner.nextLine();
            return;
        }
        
        int sortChoice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        // Copy list
        CustomArrayList<SearchSortDemo.ServiceRequest> copy = new CustomArrayList<>();
        for (int i = 0; i < list.size(); i++) copy.add(list.get(i));

        SortAlgorithms.Comparator<SearchSortDemo.ServiceRequest> cmp;

        switch (sortChoice) {
            case 1:
                cmp = (a, b) -> Integer.compare(a.durationHours, b.durationHours);
                // Bubble sort implemented inline since SortingAlgorithms doesn't have a generic one
                for (int i = 0; i < copy.size() - 1; i++) {
                    for (int j = 0; j < copy.size() - 1 - i; j++) {
                        if (cmp.compare(copy.get(j), copy.get(j + 1)) > 0) {
                            SearchSortDemo.ServiceRequest tmp = copy.get(j);
                            copy.set(j, copy.get(j + 1));
                            copy.set(j + 1, tmp);
                        }
                    }
                }
                System.out.println("\nSorted by Estimated Duration (Bubble Sort):");
                break;
            case 2:
                cmp = (a, b) -> a.type.compareToIgnoreCase(b.type);
                SortAlgorithms.selectionSort(copy, cmp); 
                System.out.println("\nSorted by Request Type (Selection Sort):");
                break;
            case 3:
                // Descending by priority score
                cmp = (a, b) -> Integer.compare(b.priorityScore, a.priorityScore);
                SortAlgorithms.insertionSort(copy, cmp);
                System.out.println("\nSorted by Priority Score Descending (Insertion Sort):");
                break;
            case 4:
                cmp = (a, b) -> a.locationId.compareToIgnoreCase(b.locationId);
                SortAlgorithms.mergeSort(copy, cmp);
                System.out.println("\nSorted by Location ID (Merge Sort):");
                break;
            case 5:
                cmp = (a, b) -> Integer.compare(a.id, b.id);
                SortAlgorithms.quickSort(copy, cmp);
                System.out.println("\nSorted by Request ID (Quick Sort):");
                break;
            default:
                System.out.println("Invalid sorting choice.");
                return;
        }

        // Print top 10 results to not flood the console
        System.out.println("Showing top 15 results:");
        for (int i = 0; i < Math.min(15, copy.size()); i++) {
            System.out.println(copy.get(i).toRow());
        }
        if (copy.size() > 15) {
            System.out.println("... and " + (copy.size() - 15) + " more.");
        }
    }
}