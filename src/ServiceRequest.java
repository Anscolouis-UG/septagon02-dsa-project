public class ServiceRequest {
    private int id;
    private String type;
    private String location;
    private String priority;
    private String status;
    private String assignedTeam;
    private String description;

    // 11-column full constructor
    public ServiceRequest(int id, String type, String location, String priority, 
                          String status, String assignedTeam, String description) {
        this.id = id;
        this.type = type;
        this.location = location;
        this.priority = priority;
        this.status = status;
        this.assignedTeam = assignedTeam;
        this.description = description;
    }

    // 8-column constructor fallback
    public ServiceRequest(int id, String type, String location, String priority, 
                          String status, String assignedTeam) {
        this(id, type, location, priority, status, assignedTeam, "No description available");
    }

    // 3-column minimal constructor fallback
    public ServiceRequest(int id, String type, String location) {
        this(id, type, location, "Medium", "Open", "Unassigned", "No description available");
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getLocation() {
        return location;
    }

    public String getPriority() {
        return priority;
    }

    public String getStatus() {
        return status;
    }

    public String getAssignedTeam() {
        return assignedTeam;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return String.format(
            "ServiceRequest [ID=%d, Type=%s, Location=%s, Priority=%s, Status=%s, Team=%s, Description=\"%s\"]",
            id, type, location, priority, status, assignedTeam, description
        );
    }
}