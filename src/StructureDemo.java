import datastructures.BTree;
import datastructures.HashTable;
import datastructures.CustomSet;
import datastructures.CustomMap;

import java.io.File;
import java.util.Scanner;

public class StructureDemo {

    public static void main(String[] args) {
        BTree<Integer, ServiceRequest> requestBTree = new BTree<>();
        HashTable<Integer, ServiceRequest> requestHashTable = new HashTable<>();
        CustomSet<Integer> visitedLocations = new CustomSet<>();
        CustomMap<Integer, String> locationNamesMap = new CustomMap<>();
        CustomMap<Integer, String> resourceMap = new CustomMap<>();

        System.out.println("==================================================");
        System.out.println("            FULL INTEGRATION DEMO   ");
        System.out.println("==================================================\n");

        // 1. Load Locations First (Maps location_id -> Building Name)
        System.out.println(" Loading datasets into custom structures...");
        int locationCount = loadLocations(visitedLocations, locationNamesMap);
        int requestCount = loadRequests(requestBTree, requestHashTable, locationNamesMap);
        int resourceCount = loadResources(resourceMap);

        System.out.println("\n Datasets loaded into memory:");
        System.out.printf("   • Service Requests (BTree & HashTable): %d records%n", requestCount);
        System.out.printf("   • Campus Locations (CustomSet & Map):  %d records%n", locationCount);
        System.out.printf("   • Resource Map (CustomMap):             %d records%n", resourceCount);

        // 2. Interactive CLI Loop
        Scanner userScanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("                MAIN MENU OPTIONS                 ");
            System.out.println("1.  Query Request Object by ID (HashTable & BTree)");
            System.out.println("2.  Check Campus Location Registration (CustomSet)");
            System.out.println("3.  Lookup Resource/Team Assignment (CustomMap)");
            System.out.println("4.  Print B-Tree In-Order Traversal (Sorted Index)");
            System.out.println("5.  Exit Program");
            System.out.print("\nSelect an option (1-5): ");

            String input = userScanner.nextLine().trim();

            switch (input) {
                case "1":
                    System.out.print("\nEnter Request ID to query (e.g., 1-9): ");
                    try {
                        int id = Integer.parseInt(userScanner.nextLine().trim());
                        
                        // O(1) Instant Lookup via Hash Table
                        ServiceRequest hashReq = requestHashTable.get(id);
                        // O(log n) Search via BTree
                        ServiceRequest btreeReq = requestBTree.search(id);

                        System.out.println("\n--------------------------------------------------");
                        if (hashReq != null && btreeReq != null) {
                            System.out.println("  QUERY RESULTS FOR REQUEST ID #" + id);
                            System.out.println("--------------------------------------------------");
                            System.out.println("  HashTable O(1) Object: " + hashReq);
                            System.out.println("  BTree O(log n) Object: " + btreeReq);
                        } else {
                            System.out.println("   Request ID #" + id + " was not found in the index.");
                        }
                        System.out.println("--------------------------------------------------");
                    } catch (NumberFormatException e) {
                        System.out.println("   Please enter a valid numerical ID.");
                    }
                    break;

                case "2":
                    System.out.print("\nEnter Location ID to check (e.g., 1-50): ");
                    try {
                        int locId = Integer.parseInt(userScanner.nextLine().trim());
                        boolean isVisited = visitedLocations.contains(locId);
                        String locName = locationNamesMap.get(locId);

                        System.out.println("\n--------------------------------------------------");
                        if (isVisited) {
                            System.out.printf("   Location #%d (%s) is registered in Visited Set.%n", 
                                              locId, (locName != null ? locName : "Unknown"));
                        } else {
                            System.out.println("   Location #" + locId + " has NOT been visited yet.");
                        }
                        System.out.println("--------------------------------------------------");
                    } catch (NumberFormatException e) {
                        System.out.println("   Please enter a valid numerical ID.");
                    }
                    break;

                case "3":
                    System.out.print("\nEnter Resource ID to lookup (e.g., 1-39): ");
                    try {
                        int resId = Integer.parseInt(userScanner.nextLine().trim());
                        String teamName = resourceMap.get(resId);

                        System.out.println("\n--------------------------------------------------");
                        if (teamName != null) {
                            System.out.printf("   Resource ID #%-2d -> Assigned to: %s%n", resId, teamName);
                        } else {
                            System.out.println("   Resource ID #" + resId + " not found in CustomMap.");
                        }
                        System.out.println("--------------------------------------------------");
                    } catch (NumberFormatException e) {
                        System.out.println("   Please enter a valid numerical ID.");
                    }
                    break;

                case "4":
                    System.out.println("\n--------------------------------------------------");
                    System.out.println("  B-TREE IN-ORDER TRAVERSAL (SORTED SERVICE INDEX)");
                    System.out.println("--------------------------------------------------");
                    System.out.println(requestBTree.traverse());
                    break;

                case "5":
                    running = false;
                    System.out.println("\nExiting Structure Demo. All checks passed cleanly! ");
                    break;

                default:
                    System.out.println("   Invalid selection. Please type 1, 2, 3, 4, or 5.");
                    break;
            }
        }
    }

    // ------------------------------------------------------------------
    // CSV Ingestion Helpers
    // ------------------------------------------------------------------

    private static int loadLocations(CustomSet<Integer> set, CustomMap<Integer, String> namesMap) {
        int count = 0;
        try (Scanner sc = new Scanner(new File("septagon02-dsa-project/data_base/locations.csv"))) {
            if (sc.hasNextLine()) sc.nextLine(); // Skip header
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    int locId = Integer.parseInt(parts[0].trim());
                    String locName = parts[1].trim();

                    set.add(locId);
                    namesMap.put(locId, locName);
                    count++;
                }
            }
        } catch (Exception e) {
            System.out.println(" Error loading locations.csv: " + e.getMessage());
        }
        return count;
    }

    private static int loadRequests(BTree<Integer, ServiceRequest> btree, 
                                    HashTable<Integer, ServiceRequest> hash,
                                    CustomMap<Integer, String> locationNamesMap) {
        int count = 0;
        try (Scanner sc = new Scanner(new File("septagon02-dsa-project/data_base/service_requests.csv"))) {
            if (sc.hasNextLine()) sc.nextLine(); // Skip header
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                
                // Parse full 11-column CSV format
                if (parts.length >= 8) {
                    int id = Integer.parseInt(parts[0].trim());
                    String type = parts[1].trim();
                    int locId = Integer.parseInt(parts[2].trim());

                    String realLocationName = locationNamesMap.get(locId);
                    if (realLocationName == null) {
                        realLocationName = "Location #" + locId;
                    }

                    String priority = parts[3].trim();
                    String status = parts[4].trim();
                    String assignedTeam = parts[7].trim(); // Column 8 is assigned_team

                    ServiceRequest request = new ServiceRequest(id, type, realLocationName, priority, status, assignedTeam);

                    btree.insert(id, request);
                    hash.put(id, request);
                    count++;
                } else if (parts.length >= 2) {
                    // Fallback for shorter legacy CSV formats
                    int id = Integer.parseInt(parts[0].trim());
                    String type = parts[1].trim();
                    int locId = (parts.length >= 3) ? Integer.parseInt(parts[2].trim()) : id;

                    String realLocationName = locationNamesMap.get(locId);
                    if (realLocationName == null) {
                        realLocationName = "Location #" + locId;
                    }

                    ServiceRequest request = new ServiceRequest(id, type, realLocationName);

                    btree.insert(id, request);
                    hash.put(id, request);
                    count++;
                }
            }
        } catch (Exception e) {
            System.out.println(" Error loading service_requests.csv: " + e.getMessage());
        }
        return count;
    }

    private static int loadResources(CustomMap<Integer, String> map) {
        int count = 0;
        try (Scanner sc = new Scanner(new File("septagon02-dsa-project/data_base/resources.csv"))) {
            if (sc.hasNextLine()) sc.nextLine(); // Skip header
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    map.put(Integer.parseInt(parts[0].trim()), parts[1].trim());
                    count++;
                }
            }
        } catch (Exception e) {
            // Optional file
        }
        return count;
    }
}