import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CSVLoader {
    private final AuditLogger auditLogger;
    private final String userId;

    public CSVLoader(AuditLogger auditLogger) {
        this(auditLogger, "Heartfield-22414593");
    }

    public CSVLoader(AuditLogger auditLogger, String userId) {
        this.auditLogger = auditLogger;
        this.userId = userId;
    }

    public int loadLocations(String csvPath, Database db) throws IOException, java.sql.SQLException {
        return importCsv(csvPath, 8, "LOCATIONS", columns -> {
            db.executeUpdate(
                    "INSERT INTO LOCATIONS(location_id, name, district, area_type, latitude, longitude, operating_hours, building_code) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                    Integer.parseInt(columns[0].trim()),
                    columns[1].trim(),
                    columns[2].trim(),
                    columns[3].trim(),
                    Double.parseDouble(columns[4].trim()),
                    Double.parseDouble(columns[5].trim()),
                    columns[6].trim(),
                    columns[7].trim().isEmpty() ? null : columns[7].trim());
            auditLogger.logInsert("LOCATIONS", String.join(",", java.util.Arrays.copyOf(columns, 8)), userId);
        });
    }

    public int loadRoads(String csvPath, Database db) throws IOException, java.sql.SQLException {
        return importCsv(csvPath, 9, "ROADS", columns -> {
            db.executeUpdate(
                    "INSERT INTO ROADS(road_id, road_name, road_type, start_location, end_location, distance_m, speed_limit_kmh, one_way, surface) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    Integer.parseInt(columns[0].trim()),
                    columns[1].trim(),
                    columns[2].trim(),
                    columns[3].trim(),
                    columns[4].trim(),
                    Double.parseDouble(columns[5].trim()),
                    Integer.parseInt(columns[6].trim()),
                    columns[7].trim(),
                    columns[8].trim());
            auditLogger.logInsert("ROADS", String.join(",", java.util.Arrays.copyOf(columns, 9)), userId);
        });
    }

    public int loadRequests(String csvPath, Database db) throws IOException, java.sql.SQLException {
        return importCsv(csvPath, 11, "SERVICE_REQUESTS", columns -> {
            db.executeUpdate(
                    "INSERT INTO SERVICE_REQUESTS(request_id, request_type, requester_location_id, priority, status, date_created, deadline, assigned_team, required_resource_type, estimated_duration_hours, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    Integer.parseInt(columns[0].trim()),
                    columns[1].trim(),
                    Integer.parseInt(columns[2].trim()),
                    columns[3].trim(),
                    columns[4].trim(),
                    columns[5].trim(),
                    columns[6].trim(),
                    columns[7].trim(),
                    columns[8].trim(),
                    Double.parseDouble(columns[9].trim()),
                    columns[10].trim());
            auditLogger.logInsert("SERVICE_REQUESTS", String.join(",", java.util.Arrays.copyOf(columns, 11)), userId);
        });
    }

    public int loadResources(String csvPath, Database db) throws IOException, java.sql.SQLException {
        return importCsv(csvPath, 7, "RESOURCES", columns -> {
            db.executeUpdate(
                    "INSERT INTO RESOURCES(resource_id, resource_name, resource_type, capacity, current_location_id, availability_status, cost_per_hour) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    Integer.parseInt(columns[0].trim()),
                    columns[1].trim(),
                    columns[2].trim(),
                    Integer.parseInt(columns[3].trim()),
                    Integer.parseInt(columns[4].trim()),
                    columns[5].trim(),
                    Double.parseDouble(columns[6].trim()));
            auditLogger.logInsert("RESOURCES", String.join(",", java.util.Arrays.copyOf(columns, 7)), userId);
        });
    }

    private int importCsv(String csvPath, int expectedColumns, String tableName, RowParser parser) throws IOException, java.sql.SQLException {
        Path path = Path.of(csvPath);
        if (!Files.exists(path)) {
            throw new IOException("CSV file not found: " + csvPath);
        }

        int count = 0;
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String header = reader.readLine();
            if (header == null) {
                return 0;
            }

            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                String trimmed = line.trim();
                if (trimmed.isEmpty()) {
                    continue;
                }

                String[] columns = splitCsvLine(trimmed);
                if (columns.length > expectedColumns) {
                    if (columns.length == expectedColumns + 1 && columns[0].equals(columns[columns.length - 1])) {
                        columns = java.util.Arrays.copyOf(columns, expectedColumns);
                    }
                }

                if (columns.length < expectedColumns) {
                    throw new IllegalArgumentException(String.format(
                            "Malformed row in %s at line %d: expected at least %d columns, found %d",
                            tableName, lineNumber, expectedColumns, columns.length));
                }

                String[] row = java.util.Arrays.copyOf(columns, expectedColumns);
                parser.parse(row);
                count++;
            }
        }
        return count;
    }

    private static String[] splitCsvLine(String line) {
        java.util.List<String> fields = new java.util.ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                fields.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        fields.add(current.toString());
        return fields.toArray(new String[0]);
    }

    @FunctionalInterface
    private interface RowParser {
        void parse(String[] columns) throws java.sql.SQLException;
    }

    public static void main(String[] args) {
        String schemaPath = "data_base/schema.sql";
        String dbPath = "data_base/george.db";
        String locationCsv = "data_base/locations.csv";
        String roadsCsv = "data_base/roads.csv";
        String resourcesCsv = "data_base/resources.csv";
        String requestsCsv = "data_base/service_requests.csv";

        AuditLogger auditLogger = new AuditLogger();
        CSVLoader loader = new CSVLoader(auditLogger);

        try (Database db = Database.connectFile(dbPath)) {
            System.out.println("Initializing database schema...");
            db.initializeSchema(schemaPath);

            System.out.println("Loading locations...");
            int locations = loader.loadLocations(locationCsv, db);
            System.out.println("Loaded locations: " + locations);

            System.out.println("Loading roads...");
            int roads = loader.loadRoads(roadsCsv, db);
            System.out.println("Loaded roads: " + roads);

            System.out.println("Loading resources...");
            int resources = loader.loadResources(resourcesCsv, db);
            System.out.println("Loaded resources: " + resources);

            System.out.println("Loading service requests...");
            int requests = loader.loadRequests(requestsCsv, db);
            System.out.println("Loaded service requests: " + requests);

            System.out.println("Audit log entries:");
            auditLogger.getEntries().forEach(System.out::println);
            System.out.println("Done.");
        } catch (Exception ex) {
            System.err.println("Error during CSV load: " + ex.getClass().getSimpleName() + " - " + ex.getMessage());
            ex.printStackTrace(System.err);
        }
    }
}