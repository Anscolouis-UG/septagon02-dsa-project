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
        CustomMap<Integer, String> locationDetailsMap = new CustomMap<>();
        CustomMap<Integer, String> resourceMap = new CustomMap<>();
        CustomMap<Integer, String> roadsMap = new CustomMap<>();

        System.out.println("==================================================");
        System.out.println("            FULL INTEGRATION DEMO   ");
        System.out.println("==================================================\n");

        System.out.println(" Loading datasets into custom structures...");
        int locationCount = loadLocations(visitedLocations, locationNamesMap, locationDetailsMap);
        int requestCount = loadRequests(requestBTree, requestHashTable, locationNamesMap);
        int resourceCount = loadResources(resourceMap, locationNamesMap);
        int roadCount = loadRoads(roadsMap);

        System.out.println("\n Datasets loaded into memory:");
        System.out.printf("   • Service Requests (BTree & HashTable): %d records%n", requestCount);
        System.out.printf("   • Campus Locations (CustomSet & Map):  %d records%n", locationCount);
        System.out.printf("   • Resource Map (CustomMap):             %d records%n", resourceCount);
        System.out.printf("   • Campus Roads (CustomMap):             %d records%n", roadCount);

        try (Scanner userScanner = new Scanner(System.in)) {
            boolean running = true;

            while (running) {
             System.out.println("\n-------------------------------------------------------------------");
             System.out.println("\n-------------------------------------------------------------------");
             System.out.println("   LEGON CAMPUS FACILITIES MANAGEMENT UNIT  OPERATIONS DEMO   ");
             System.out.println("-------------------------------------------------------------------");
             System.out.println("1. Search Service Request Ticket by ID        [HashTable & BTree]");
             System.out.println("2. Verify Campus Building & Location   [CustomSet & Map]");
             System.out.println("3. Inspect Maintenance Team & Resource Status [CustomMap]");
             System.out.println("4. Inspect Dispatch Route & Road Segment     [CustomMap]");
             System.out.println("5. Generate Sorted Index Report of Requests  [B-Tree Traversal]");
             System.out.println("6. Exit  Management System");
             System.out.print("\nSelect an option (1-6): ");
                String input = userScanner.nextLine().trim();

                switch (input) {
                    case "1":
                        System.out.print("\nEnter Request ID to query: ");
                        String reqInput = userScanner.nextLine().trim();
                        if (reqInput.isEmpty()) {
                            System.out.println("   No ID entered. Returning to menu.");
                            break;
                        }
                        try {
                            int id = Integer.parseInt(reqInput);
                            ServiceRequest hashReq = requestHashTable.get(id);
                            ServiceRequest btreeReq = requestBTree.search(id);

                            System.out.println("\n--------------------------------------------------");
                            if (hashReq != null && btreeReq != null) {
                                System.out.println("  QUERY RESULTS FOR REQUEST ID #" + id);
                                System.out.println("--------------------------------------------------");
                                System.out.println("  HashTable O(1) Match: " + hashReq);
                                System.out.println("  BTree O(log n) Match: " + btreeReq);
                            } else {
                                System.out.println("   Request ID #" + id + " was not found in the index.");
                            }
                            System.out.println("--------------------------------------------------");
                        } catch (NumberFormatException e) {
                            System.out.println("   Please enter a valid numerical ID.");
                        }
                        break;

                    case "2":
                        System.out.print("\nEnter Location ID to check: ");
                        String locInput = userScanner.nextLine().trim();
                        if (locInput.isEmpty()) {
                            System.out.println("   No ID entered. Returning to menu.");
                            break;
                        }
                        try {
                            int locId = Integer.parseInt(locInput);
                            boolean isVisited = visitedLocations.contains(locId);
                            String locDetail = locationDetailsMap.get(locId);

                            System.out.println("\n--------------------------------------------------");
                            if (locDetail != null) {
                                System.out.printf("   Location #%d [%s] -> %s%n", 
                                                  locId, 
                                                  (isVisited ? "Registered in Visited Set" : "Not Visited"), 
                                                  locDetail);
                            } else {
                                System.out.println("   Location #" + locId + " was not found in locations dataset.");
                            }
                            System.out.println("--------------------------------------------------");
                        } catch (NumberFormatException e) {
                            System.out.println("   Please enter a valid numerical ID.");
                        }
                        break;

                    case "3":
                        System.out.print("\nEnter Resource ID to lookup: ");
                        String resInput = userScanner.nextLine().trim();
                        if (resInput.isEmpty()) {
                            System.out.println("   No ID entered. Returning to menu.");
                            break;
                        }
                        try {
                            int resId = Integer.parseInt(resInput);
                            String resDetail = resourceMap.get(resId);

                            System.out.println("\n--------------------------------------------------");
                            if (resDetail != null) {
                                System.out.printf("   Resource ID #%-2d -> %s%n", resId, resDetail);
                            } else {
                                System.out.println("   Resource ID #" + resId + " not found in CustomMap.");
                            }
                            System.out.println("--------------------------------------------------");
                        } catch (NumberFormatException e) {
                            System.out.println("   Please enter a valid numerical ID.");
                        }
                        break;

                    case "4":
                        System.out.print("\nEnter Road ID to lookup: ");
                        String roadInput = userScanner.nextLine().trim();
                        if (roadInput.isEmpty()) {
                            System.out.println("   No ID entered. Returning to menu.");
                            break;
                        }
                        try {
                            int roadId = Integer.parseInt(roadInput);
                            String roadDetail = roadsMap.get(roadId);

                            System.out.println("\n--------------------------------------------------");
                            if (roadDetail != null) {
                                System.out.printf("   Road ID #%-2d -> %s%n", roadId, roadDetail);
                            } else {
                                System.out.println("   Road ID #" + roadId + " not found in Roads Map.");
                            }
                            System.out.println("--------------------------------------------------");
                        } catch (NumberFormatException e) {
                            System.out.println("   Please enter a valid numerical ID.");
                        }
                        break;

                    case "5":
                        System.out.println("\n--------------------------------------------------");
                        System.out.println("  B-TREE IN-ORDER TRAVERSAL (SORTED SERVICE INDEX)");
                        System.out.println("--------------------------------------------------");
                        System.out.println(requestBTree.traverse());
                        break;

                    case "6":
                        running = false;
                        System.out.println("\nExiting Structure Demo. All components loaded cleanly!");
                        break;

                    default:
                        System.out.println("   Invalid selection. Please select an option between 1 and 6.");
                        break;
                }
            }
        }
    }

    private static File getExistingFile(String relativePath) {
        File file = new File(relativePath);
        if (!file.exists()) {
            file = new File("septagon02-dsa-project/" + relativePath);
        }
        return file;
    }

    // Ingestion Methods
    private static int loadLocations(CustomSet<Integer> set, 
                                     CustomMap<Integer, String> namesMap, 
                                     CustomMap<Integer, String> detailsMap) {
        int count = 0;
        File file = getExistingFile("data_base/locations.csv");
        try (Scanner sc = new Scanner(file)) {
            if (sc.hasNextLine()) sc.nextLine(); // Skip header line
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                
                if (parts.length >= 8) {
                    int locId = Integer.parseInt(parts[0].trim().replace("\"", ""));
                    String name = parts[1].trim().replace("\"", "");
                    String district = parts[2].trim().replace("\"", "");
                    String areaType = parts[3].trim().replace("\"", "");
                    String lat = parts[4].trim().replace("\"", "");
                    String lon = parts[5].trim().replace("\"", "");
                    String operatingHours = parts[6].trim().replace("\"", "");
                    String buildingCode = parts[7].trim().replace("\"", "");

                    set.add(locId);
                    namesMap.put(locId, name);

                    String formattedDetail = String.format(
                        "Name: %s | District: %s | Area Type: %s | Lat: %s | Long: %s | Hours: %s | Code: %s",
                        name, district, areaType, lat, lon, operatingHours, buildingCode
                    );
                    detailsMap.put(locId, formattedDetail);
                    count++;
                } else if (parts.length >= 2) {
                    int locId = Integer.parseInt(parts[0].trim().replace("\"", ""));
                    String name = parts[1].trim().replace("\"", "");

                    set.add(locId);
                    namesMap.put(locId, name);
                    detailsMap.put(locId, "Name: " + name);
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
        File file = getExistingFile("data_base/service_requests.csv");
        try (Scanner sc = new Scanner(file)) {
            if (sc.hasNextLine()) sc.nextLine();
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                
                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                
                if (parts.length >= 11) {
                    int id = Integer.parseInt(parts[0].trim().replace("\"", ""));
                    String type = parts[1].trim().replace("\"", "");
                    int locId = Integer.parseInt(parts[2].trim().replace("\"", ""));

                    String realLocationName = locationNamesMap.get(locId);
                    if (realLocationName == null) {
                        realLocationName = "Location #" + locId;
                    }

                    String priority = parts[3].trim().replace("\"", "");
                    String status = parts[4].trim().replace("\"", "");
                    String assignedTeam = parts[7].trim().replace("\"", "");
                    String description = parts[10].trim().replace("\"", "");

                    ServiceRequest request = new ServiceRequest(id, type, realLocationName, priority, status, assignedTeam, description);

                    btree.insert(id, request);
                    hash.put(id, request);
                    count++;
                } else if (parts.length >= 8) {
                    int id = Integer.parseInt(parts[0].trim().replace("\"", ""));
                    String type = parts[1].trim().replace("\"", "");
                    int locId = Integer.parseInt(parts[2].trim().replace("\"", ""));

                    String realLocationName = locationNamesMap.get(locId);
                    if (realLocationName == null) {
                        realLocationName = "Location #" + locId;
                    }

                    String priority = parts[3].trim().replace("\"", "");
                    String status = parts[4].trim().replace("\"", "");
                    String assignedTeam = parts[7].trim().replace("\"", "");

                    ServiceRequest request = new ServiceRequest(id, type, realLocationName, priority, status, assignedTeam);

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

    private static int loadResources(CustomMap<Integer, String> map, CustomMap<Integer, String> locationNamesMap) {
        int count = 0;
        File file = getExistingFile("data_base/resources.csv");
        try (Scanner sc = new Scanner(file)) {
            if (sc.hasNextLine()) sc.nextLine();

            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                if (parts.length >= 6) {
                    int resId = Integer.parseInt(parts[0].trim().replace("\"", ""));
                    String resName = parts[1].trim().replace("\"", "");
                    String resType = parts[2].trim().replace("\"", "");
                    String capacity = parts[3].trim().replace("\"", "");
                    
                    int locId = Integer.parseInt(parts[4].trim().replace("\"", ""));
                    String locName = locationNamesMap.get(locId);
                    if (locName == null) {
                        locName = "Location #" + locId;
                    }

                    String status = parts[5].trim().replace("\"", "");

                    String formattedDetail = String.format(
                        "Name: %s | Type: %s | Capacity: %s | Current Location: %s | Status: %s",
                        resName, resType, capacity, locName, status
                    );

                    map.put(resId, formattedDetail);
                    count++;
                }
            }
        } catch (Exception e) {
            System.out.println(" Error loading resources.csv: " + e.getMessage());
        }
        return count;
    }

    private static int loadRoads(CustomMap<Integer, String> map) {
        int count = 0;
        File file = getExistingFile("data_base/roads.csv");
        try (Scanner sc = new Scanner(file)) {
            if (sc.hasNextLine()) sc.nextLine();

            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                if (parts.length >= 6) {
                    int roadId = Integer.parseInt(parts[0].trim().replace("\"", ""));
                    String roadName = parts[1].trim().replace("\"", "");
                    String roadType = parts[2].trim().replace("\"", "");
                    String startLoc = parts[3].trim().replace("\"", "");
                    String endLoc = parts[4].trim().replace("\"", "");
                    String distance = parts[5].trim().replace("\"", "");

                    String formattedDetail = String.format(
                        "Name: %s | Type: %s | Start Location: %s | End Location: %s | Distance: %sm",
                        roadName, roadType, startLoc, endLoc, distance
                    );

                    map.put(roadId, formattedDetail);
                    count++;
                }
            }
        } catch (Exception e) {
            System.out.println(" Error loading roads.csv: " + e.getMessage());
        }
        return count;
    }
}