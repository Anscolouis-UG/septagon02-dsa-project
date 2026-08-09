package algorithms;

import datastructures.CustomArrayList;

public class UserInterface {
    public static void main(String[] args) {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        boolean running = true;

        System.out.println("=============================================");
        System.out.println("  Java Console Searching & Sorting Program   ");
        System.out.println("=============================================");

        while (running) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Perform Searching");
            System.out.println("2. Perform Sorting");
            System.out.println("3. Exit Program");
            System.out.print("Enter your choice (1-3): ");

            int mainChoice = scanner.nextInt();

            if (mainChoice == 3) {
                System.out.println("\nThank you for using the program. Goodbye!");
                running = false;
                continue;
            }

            if (mainChoice < 1 || mainChoice > 3) {
                System.out.println("Invalid choice! Please choose a number between 1 and 3.");
                continue;
            }

            CustomArrayList<Integer> userList = acceptArrayInput(scanner);

            if (mainChoice == 1) {
                handleSearching(scanner, userList);
            } else {
                handleSorting(scanner, userList);
            }
        }
        scanner.close();
    }

    private static CustomArrayList<Integer> acceptArrayInput(java.util.Scanner scanner) {
        System.out.print("\nHow many integers are in your list? ");
        int size = scanner.nextInt();
        CustomArrayList<Integer> list = new CustomArrayList<>();

        System.out.println("Enter " + size + " integers (press Enter after each):");
        for (int i = 0; i < size; i++) {
            System.out.print("Element " + (i + 1) + ": ");
            list.add(scanner.nextInt());
        }
        return list;
    }

    private static void handleSearching(java.util.Scanner scanner, CustomArrayList<Integer> list) {
        System.out.println("\n--- SEARCHING MENU ---");
        System.out.println("1. Linear Search");
        System.out.println("2. Binary Search");
        System.out.print("Choose a search algorithm (1-2): ");
        int searchChoice = scanner.nextInt();

        System.out.print("Enter the target value to search for: ");
        int target = scanner.nextInt();

        int resultIndex = -1;

        if (searchChoice == 1) {
            resultIndex = SearchAlgorithms.linearSearch(list, x -> x == target);
        } else if (searchChoice == 2) {
            System.out.println("Note: Sorting list first (required for Binary Search)...");
            // Copy list
            CustomArrayList<Integer> sorted = new CustomArrayList<>();
            for (int i = 0; i < list.size(); i++) sorted.add(list.get(i));
            SortAlgorithms.insertionSort(sorted, (a, b) -> Integer.compare(a, b));
            System.out.print("Sorted list: [");
            for (int i = 0; i < sorted.size(); i++) {
                System.out.print(sorted.get(i));
                if (i < sorted.size() - 1) System.out.print(", ");
            }
            System.out.println("]");
            resultIndex = SearchAlgorithms.binarySearch(sorted, target, (a, b) -> Integer.compare(a, b));
        } else {
            System.out.println("Invalid search choice.");
            return;
        }

        if (resultIndex != -1) {
            System.out.println("Target " + target + " found at index: " + resultIndex);
        } else {
            System.out.println("Target " + target + " was not found in the list.");
        }
    }

    private static void handleSorting(java.util.Scanner scanner, CustomArrayList<Integer> list) {
        System.out.println("\n--- SORTING MENU ---");
        System.out.println("1. Bubble Sort");
        System.out.println("2. Selection Sort");
        System.out.println("3. Insertion Sort");
        System.out.println("4. Merge Sort");
        System.out.println("5. Quick Sort");
        System.out.print("Choose a sorting algorithm (1-5): ");
        int sortChoice = scanner.nextInt();

        // Copy list
        CustomArrayList<Integer> copy = new CustomArrayList<>();
        for (int i = 0; i < list.size(); i++) copy.add(list.get(i));

        SortAlgorithms.Comparator<Integer> cmp = (a, b) -> Integer.compare(a, b);

        switch (sortChoice) {
            case 1: // Bubble sort — implemented inline since SortingAlgorithms works on int[]
                for (int i = 0; i < copy.size() - 1; i++) {
                    for (int j = 0; j < copy.size() - 1 - i; j++) {
                        if (copy.get(j) > copy.get(j + 1)) {
                            int tmp = copy.get(j);
                            copy.set(j, copy.get(j + 1));
                            copy.set(j + 1, tmp);
                        }
                    }
                }
                break;
            case 2: SortAlgorithms.selectionSort(copy, cmp); break;
            case 3: SortAlgorithms.insertionSort(copy, cmp); break;
            case 4: SortAlgorithms.mergeSort(copy, cmp);     break;
            case 5: SortAlgorithms.quickSort(copy, cmp);     break;
            default:
                System.out.println("Invalid sorting choice.");
                return;
        }

        System.out.print("Original List: [");
        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i));
            if (i < list.size() - 1) System.out.print(", ");
        }
        System.out.println("]");

        System.out.print("Sorted List:   [");
        for (int i = 0; i < copy.size(); i++) {
            System.out.print(copy.get(i));
            if (i < copy.size() - 1) System.out.print(", ");
        }
        System.out.println("]");
    }
}