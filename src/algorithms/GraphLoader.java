package algorithms;

import datastructures.CustomArrayList;
import datastructures.CustomGraph;
import datastructures.CustomMap;

/**
 * GraphLoader.java
 *
 * Loads the UG Legon campus graph from:
 *   data_base/nodes.csv      — vertices (id, name, category, latitude, longitude)
 *   data_base/edges_knn_sparse.csv — edges (source, target, distance_km, distance_m)
 *
 * The graph uses 0-based integer indices internally.
 * We dynamically map string 'id' to an integer index.
 */
public class GraphLoader {

    public static final int MAX_LOCATIONS = 150;

    /** Loads locations and roads into a CustomGraph.
     *  @param locationNames OUT param: index -> name mapping for display
     *  @param idToIndex     OUT param: string id -> index mapping */
    public static CustomGraph load(CustomMap<Integer, String> locationNames,
                                   CustomMap<String, Integer> idToIndex,
                                   String locPath, String roadPath) {
        CustomGraph graph = new CustomGraph(MAX_LOCATIONS, false);

        int currentIndex = 0;

        // ── Step 1: load locations (build id -> index map) ───────────────────
        try (java.util.Scanner sc = new java.util.Scanner(new java.io.File(locPath))) {
            if (sc.hasNextLine()) sc.nextLine(); // skip header
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] cols = line.split(",", -1);
                if (cols.length < 2) continue;
                
                String id = cols[0].trim();
                String name = cols[1].trim();
                
                idToIndex.put(id, currentIndex);
                locationNames.put(currentIndex, name);
                currentIndex++;
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
                if (cols.length < 4) continue;
                
                String sourceId = cols[0].trim();
                String targetId = cols[1].trim();
                double distM    = Double.parseDouble(cols[3].trim()); // column 3 is distance_m

                Integer startIdx = idToIndex.get(sourceId);
                Integer endIdx   = idToIndex.get(targetId);

                if (startIdx == null || endIdx == null) continue; // skip unmatched
                
                graph.addEdge(startIdx, endIdx, distM);
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Loading edges: " + e.getMessage());
        }

        return graph;
    }
}
