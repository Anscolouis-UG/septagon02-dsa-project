public class CRUDOperations {
    private final AuditLogger auditLogger;
    private final String userId;

    public CRUDOperations(AuditLogger auditLogger) {
        this(auditLogger, "Heartfield-22414593");
    }

    public CRUDOperations(AuditLogger auditLogger, String userId) {
        this.auditLogger = auditLogger;
        this.userId = userId;
    }

    public void createServiceRequest(ServiceRequest req, Database db) throws java.sql.SQLException {
        db.executeUpdate(
                "INSERT INTO SERVICE_REQUESTS(request_id, request_type, requester_location_id, priority, status, date_created, deadline, assigned_team, required_resource_type, estimated_duration_hours, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                req.getId(),
                req.getRequestType(),
                req.getRequesterLocationId(),
                req.getPriority(),
                req.getStatus(),
                req.getDateCreated(),
                req.getDeadline(),
                req.getAssignedTeam(),
                req.getRequiredResourceType(),
                req.getEstimatedDurationHours(),
                req.getDescription());
        auditLogger.logInsert("SERVICE_REQUESTS", req, userId);
    }

    public void createResource(Resource res, Database db) throws java.sql.SQLException {
        db.executeUpdate(
                "INSERT INTO RESOURCES(resource_id, resource_name, resource_type, capacity, current_location_id, availability_status, cost_per_hour) VALUES (?, ?, ?, ?, ?, ?, ?)",
                res.getResourceId(),
                res.getResourceName(),
                res.getResourceType(),
                res.getCapacity(),
                res.getCurrentLocationId(),
                res.getAvailabilityStatus(),
                res.getCostPerHour());
        auditLogger.logInsert("RESOURCES", res, userId);
    }

    public void createLocation(Location loc, Database db) throws java.sql.SQLException {
        db.executeUpdate(
                "INSERT INTO LOCATIONS(location_id, name, district, area_type, latitude, longitude, operating_hours, building_code) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                loc.getLocationId(),
                loc.getName(),
                loc.getDistrict(),
                loc.getAreaType(),
                loc.getLatitude(),
                loc.getLongitude(),
                loc.getOperatingHours(),
                loc.getBuildingCode());
        auditLogger.logInsert("LOCATIONS", loc, userId);
    }

    public ServiceRequest getServiceRequest(int requestId, Database db) throws java.sql.SQLException {
        try (java.sql.PreparedStatement stmt = db.prepareStatement(
                "SELECT request_id, request_type, requester_location_id, priority, status, date_created, deadline, assigned_team, required_resource_type, estimated_duration_hours, description FROM SERVICE_REQUESTS WHERE request_id = ?",
                requestId);
             java.sql.ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return mapRequest(rs);
            }
            return null;
        }
    }

    public java.util.List<ServiceRequest> getAllRequests(Database db) throws java.sql.SQLException {
        java.util.List<ServiceRequest> requests = new java.util.ArrayList<>();
        try (java.sql.PreparedStatement stmt = db.prepareStatement(
                "SELECT request_id, request_type, requester_location_id, priority, status, date_created, deadline, assigned_team, required_resource_type, estimated_duration_hours, description FROM SERVICE_REQUESTS");
             java.sql.ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                requests.add(mapRequest(rs));
            }
        }
        return requests;
    }

    public java.util.List<ServiceRequest> getRequestsByType(String type, Database db) throws java.sql.SQLException {
        java.util.List<ServiceRequest> requests = new java.util.ArrayList<>();
        try (java.sql.PreparedStatement stmt = db.prepareStatement(
                "SELECT request_id, request_type, requester_location_id, priority, status, date_created, deadline, assigned_team, required_resource_type, estimated_duration_hours, description FROM SERVICE_REQUESTS WHERE request_type = ?",
                type);
             java.sql.ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                requests.add(mapRequest(rs));
            }
        }
        return requests;
    }

    public void updateRequestStatus(int requestId, String status, Database db) throws java.sql.SQLException {
        ServiceRequest oldRequest = getServiceRequest(requestId, db);
        if (oldRequest == null) {
            return;
        }
        db.executeUpdate("UPDATE SERVICE_REQUESTS SET status = ? WHERE request_id = ?", status, requestId);
        ServiceRequest updatedRequest = new ServiceRequest(
                oldRequest.getId(),
                oldRequest.getRequestType(),
                oldRequest.getLocation(),
                oldRequest.getRequesterLocationId(),
                oldRequest.getPriority(),
                status,
                oldRequest.getDateCreated(),
                oldRequest.getDeadline(),
                oldRequest.getAssignedTeam(),
                oldRequest.getRequiredResourceType(),
                oldRequest.getEstimatedDurationHours(),
                oldRequest.getDescription());
        auditLogger.logUpdate("SERVICE_REQUESTS", oldRequest, updatedRequest, userId);
    }

    public void updateResourceAvailability(int resourceId, String status, Database db) throws java.sql.SQLException {
        Resource oldResource = getResource(resourceId, db);
        if (oldResource == null) {
            return;
        }
        db.executeUpdate("UPDATE RESOURCES SET availability_status = ? WHERE resource_id = ?", status, resourceId);
        Resource updatedResource = new Resource(
                oldResource.getResourceId(),
                oldResource.getResourceName(),
                oldResource.getResourceType(),
                oldResource.getCapacity(),
                oldResource.getCurrentLocationId(),
                status,
                oldResource.getCostPerHour());
        auditLogger.logUpdate("RESOURCES", oldResource, updatedResource, userId);
    }

    public void deleteServiceRequest(int requestId, Database db) throws java.sql.SQLException {
        ServiceRequest oldRequest = getServiceRequest(requestId, db);
        if (oldRequest == null) {
            return;
        }
        db.executeUpdate("DELETE FROM SERVICE_REQUESTS WHERE request_id = ?", requestId);
        auditLogger.logDelete("SERVICE_REQUESTS", oldRequest, userId);
    }

    private ServiceRequest mapRequest(java.sql.ResultSet rs) throws java.sql.SQLException {
        int id = rs.getInt("request_id");
        String requestType = rs.getString("request_type");
        int requesterLocationId = rs.getInt("requester_location_id");
        String priority = rs.getString("priority");
        String status = rs.getString("status");
        String dateCreated = rs.getString("date_created");
        String deadline = rs.getString("deadline");
        String assignedTeam = rs.getString("assigned_team");
        String requiredResourceType = rs.getString("required_resource_type");
        double estimatedDurationHours = rs.getDouble("estimated_duration_hours");
        String description = rs.getString("description");
        return new ServiceRequest(id, requestType, null, requesterLocationId, priority, status, dateCreated, deadline, assignedTeam, requiredResourceType, estimatedDurationHours, description);
    }

    private Resource getResource(int resourceId, Database db) throws java.sql.SQLException {
        try (java.sql.PreparedStatement stmt = db.prepareStatement(
                "SELECT resource_id, resource_name, resource_type, capacity, current_location_id, availability_status, cost_per_hour FROM RESOURCES WHERE resource_id = ?",
                resourceId);
             java.sql.ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                Integer currentLocationId = rs.getObject("current_location_id") == null ? null : rs.getInt("current_location_id");
                return new Resource(
                        rs.getInt("resource_id"),
                        rs.getString("resource_name"),
                        rs.getString("resource_type"),
                        rs.getInt("capacity"),
                        currentLocationId,
                        rs.getString("availability_status"),
                        rs.getDouble("cost_per_hour"));
            }
        }
        return null;
    }
}