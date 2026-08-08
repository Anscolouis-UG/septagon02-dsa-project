package algorithms;


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


            int[] userArray = acceptArrayInput(scanner);

            if (mainChoice == 1) {
                handleSearching(scanner, userArray);
            } else if (mainChoice == 2) {
                handleSorting(scanner, userArray);
            }
        }
        scanner.close();
    }

    private static int[] acceptArrayInput(java.util.Scanner scanner) {
        System.out.print("\nHow many integers are in your list? ");
        int size = scanner.nextInt();
        int[] array = new int[size];

        System.out.println("Enter " + size + " integers (press Enter after each):");
        for (int i = 0; i < size; i++) {
            System.out.print("Element " + (i + 1) + ": ");
            array[i] = scanner.nextInt();
        }
        return array;
    }

    private static void handleSearching(java.util.Scanner scanner, int[] array) {
        System.out.println("\n--- SEARCHING MENU ---");
        System.out.println("1. Linear Search");
        System.out.println("2. Binary Search");
        System.out.print("Choose a search algorithm (1-2): ");
        int searchChoice = scanner.nextInt();

        System.out.print("Enter the target value to search for: ");
        int target = scanner.nextInt();

        int resultIndex = -1;

        if (searchChoice == 1) {
            resultIndex = SearchAlgorithms.linearSearch(array, target);
        } else if (searchChoice == 2) {

            System.out.println("Note: Sorting array first (required for Binary Search)...");
            int[] sortedCopy = java.util.Arrays.copyOf(array, array.length);
            java.util.Arrays.sort(sortedCopy);
            System.out.println("Sorted array: " + java.util.Arrays.toString(sortedCopy));

            resultIndex = SearchAlgorithms.binarySearch(sortedCopy, target);
        } else {
            System.out.println("Invalid search choice.");
            return;
        }

        if (resultIndex != -1) {
            System.out.println("Target " + target + " found at index: " + resultIndex);
        } else {
            System.out.println("Target " + target + " was not found in the array.");
        }
    }

    private static void handleSorting(java.util.Scanner scanner, int[] array) {
        System.out.println("\n--- SORTING MENU ---");
        System.out.println("1. Bubble Sort");
        System.out.println("2. Selection Sort");
        System.out.println("3. Insertion Sort");
        System.out.println("4. Merge Sort");
        System.out.println("5. Quick Sort");
        System.out.print("Choose a sorting algorithm (1-5): ");
        int sortChoice = scanner.nextInt();

        int[] sortedArray;
        int[] arrayCopy = java.util.Arrays.copyOf(array, array.length);

        switch (sortChoice) {
            case 1:
                sortedArray = SortingAlgorithms.bubbleSort(arrayCopy);
                break;
            case 2:
                sortedArray = SortingAlgorithms.selectionSort(arrayCopy);
                break;
            case 3:
                sortedArray = SortingAlgorithms.insertionSort(arrayCopy);
                break;
            case 4:
                sortedArray = SortingAlgorithms.mergeSort(arrayCopy);
                break;
            case 5:
                sortedArray = SortingAlgorithms.quickSort(arrayCopy);
                break;
            default:
                System.out.println("Invalid sorting choice.");
                return;
        }

        System.out.println("Original java.util.List: " + java.util.Arrays.toString(array));
        System.out.println("Sorted java.util.List:   " + java.util.Arrays.toString(sortedArray));
    }
}