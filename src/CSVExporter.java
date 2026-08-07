import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class CSVExporter {
    public void exportLocations(Database db, String outputPath) throws java.sql.SQLException, IOException {
        String header = "location_id,name,district,area_type,latitude,longitude,operating_hours,building_code";
        try (java.sql.PreparedStatement stmt = db.prepareStatement("SELECT location_id, name, district, area_type, latitude, longitude, operating_hours, building_code FROM LOCATIONS");
             java.sql.ResultSet rs = stmt.executeQuery()) {
            writeCsv(outputPath, header, resultSetToLines(rs, 8));
        }
    }

    public void exportRequests(Database db, String outputPath) throws java.sql.SQLException, IOException {
        String header = "request_id,request_type,requester_location_id,priority,status,date_created,deadline,assigned_team,required_resource_type,estimated_duration_hours,description";
        try (java.sql.PreparedStatement stmt = db.prepareStatement("SELECT request_id, request_type, requester_location_id, priority, status, date_created, deadline, assigned_team, required_resource_type, estimated_duration_hours, description FROM SERVICE_REQUESTS");
             java.sql.ResultSet rs = stmt.executeQuery()) {
            writeCsv(outputPath, header, resultSetToLines(rs, 11));
        }
    }

    public void exportRoads(Database db, String outputPath) throws java.sql.SQLException, IOException {
        String header = "road_id,road_name,road_type,start_location,end_location,distance_m,speed_limit_kmh,one_way,surface";
        try (java.sql.PreparedStatement stmt = db.prepareStatement("SELECT road_id, road_name, road_type, start_location, end_location, distance_m, speed_limit_kmh, one_way, surface FROM ROADS");
             java.sql.ResultSet rs = stmt.executeQuery()) {
            writeCsv(outputPath, header, resultSetToLines(rs, 9));
        }
    }

    public void exportResources(Database db, String outputPath) throws java.sql.SQLException, IOException {
        String header = "resource_id,resource_name,resource_type,capacity,current_location_id,availability_status,cost_per_hour";
        try (java.sql.PreparedStatement stmt = db.prepareStatement("SELECT resource_id, resource_name, resource_type, capacity, current_location_id, availability_status, cost_per_hour FROM RESOURCES");
             java.sql.ResultSet rs = stmt.executeQuery()) {
            writeCsv(outputPath, header, resultSetToLines(rs, 7));
        }
    }

    public void exportBatch(Database db, java.util.List<String> tables, String outputDir) throws java.sql.SQLException, IOException {
        for (String table : tables) {
            String normalized = table.trim().toUpperCase();
            String outputPath = Path.of(outputDir, normalized.toLowerCase() + ".csv").toString();
            switch (normalized) {
                case "LOCATIONS" -> exportLocations(db, outputPath);
                case "SERVICE_REQUESTS" -> exportRequests(db, outputPath);
                case "ROADS" -> exportRoads(db, outputPath);
                case "RESOURCES" -> exportResources(db, outputPath);
                default -> throw new IllegalArgumentException("Unsupported table for batch export: " + table);
            }
        }
    }

    private void writeCsv(String outputPath, String header, java.util.List<String> lines) throws IOException {
        Path path = Path.of(outputPath);
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }
        java.util.List<String> outputLines = new java.util.ArrayList<>();
        outputLines.add(header);
        outputLines.addAll(lines);
        Files.write(path, outputLines, StandardCharsets.UTF_8);
    }

    private java.util.List<String> resultSetToLines(java.sql.ResultSet rs, int columns) throws java.sql.SQLException {
        java.util.List<String> lines = new java.util.ArrayList<>();
        while (rs.next()) {
            StringBuilder row = new StringBuilder();
            for (int i = 1; i <= columns; i++) {
                Object value = rs.getObject(i);
                row.append(value == null ? "" : value.toString());
                if (i < columns) {
                    row.append(',');
                }
            }
            lines.add(row.toString());
        }
        return lines;
    }
}