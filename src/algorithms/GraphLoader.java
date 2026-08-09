package algorithms;

import datastructures.CustomArrayList;
import datastructures.CustomGraph;
import datastructures.CustomMap;

/**
 * GraphLoader.java
 *
 * Loads the UG Legon campus graph from:
 *   data_base/locations.csv  — vertices (location_id, name, ...)
 *   data_base/roads.csv      — edges (start_location name, end_location name, distance_m)
 *
 * The graph uses location_id (1-based) as vertex indices.
 * Since CustomGraph uses 0-based indices internally, we store (location_id - 1).
 *
 * locationNames maps vertex index -> display name for pretty printing.
 */
public class GraphLoader {

    public static final int NUM_LOCATIONS = 50;

    /** Loads locations and roads into a CustomGraph.
     *  @param locationNames OUT param: index -> name mapping for display */
    public static CustomGraph load(CustomMap<Integer, String> locationNames,
                                   String locPath, String roadPath) {
        CustomGraph graph = new CustomGraph(NUM_LOCATIONS + 1, false);

        // ── Step 1: load locations (build name -> id map) ───────────────────
        // We use a parallel CustomArrayList pair as a name->id lookup
        // (CustomMap<String,Integer> requires String to extend Comparable,
        //  but String does, so we can use CustomMap<String, Integer>)
        CustomMap<String, Integer> nameToId = new CustomMap<>();

        try (java.util.Scanner sc = new java.util.Scanner(new java.io.File(locPath))) {
            if (sc.hasNextLine()) sc.nextLine(); // skip header
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] cols = line.split(",", -1);
                if (cols.length < 2) continue;
                int id = Integer.parseInt(cols[0].trim());
                String name = cols[1].trim();
                nameToId.put(name, id);
                locationNames.put(id - 1, name);  // 0-based index
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Loading locations: " + e.getMessage());
        }

        // ── Step 2: load roads (edges) ───────────────────────────────────────
        try (java.util.Scanner sc = new java.util.Scanner(new java.io.File(roadPath))) {
            if (sc.hasNextLine()) sc.nextLine(); // skip header
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] cols = line.split(",", -1);
                if (cols.length < 6) continue;
                String startName = cols[3].trim();
                String endName   = cols[4].trim();
                double distM     = Double.parseDouble(cols[5].trim());

                Integer startId = nameToId.get(startName);
                Integer endId   = nameToId.get(endName);

                if (startId == null || endId == null) continue; // skip unmatched
                // convert to 0-based
                graph.addEdge(startId - 1, endId - 1, distM);
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Loading roads: " + e.getMessage());
        }

        return graph;
    }
}
