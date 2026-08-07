public class ServiceRequest {
    private final int id;
    private final String requestType;
    private final String location;
    private final int requesterLocationId;
    private final String priority;
    private final String status;
    private final String dateCreated;
    private final String deadline;
    private final String assignedTeam;
    private final String requiredResourceType;
    private final double estimatedDurationHours;
    private final String description;

    public ServiceRequest(int id, String requestType, String location) {
        this(id, requestType, location, 0, "Low", "Pending", currentTimestamp(), currentTimestamp(), "Unassigned", "None", 0.0, location);
    }

    public ServiceRequest(int id, String requestType, int requesterLocationId, String priority, String status,
                          String dateCreated, String deadline, String assignedTeam, String requiredResourceType,
                          double estimatedDurationHours, String description) {
        this(id, requestType, null, requesterLocationId, priority, status, dateCreated, deadline, assignedTeam, requiredResourceType, estimatedDurationHours, description);
    }

    public ServiceRequest(int id, String requestType, String location, int requesterLocationId, String priority,
                          String status, String dateCreated, String deadline, String assignedTeam,
                          String requiredResourceType, double estimatedDurationHours, String description) {
        this.id = id;
        this.requestType = requestType;
        this.location = location;
        this.requesterLocationId = requesterLocationId;
        this.priority = priority;
        this.status = status;
        this.dateCreated = dateCreated;
        this.deadline = deadline;
        this.assignedTeam = assignedTeam;
        this.requiredResourceType = requiredResourceType;
        this.estimatedDurationHours = estimatedDurationHours;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getRequestType() {
        return requestType;
    }

    public String getLocation() {
        return location;
    }

    public int getRequesterLocationId() {
        return requesterLocationId;
    }

    public String getPriority() {
        return priority;
    }

    public String getStatus() {
        return status;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getDeadline() {
        return deadline;
    }

    public String getAssignedTeam() {
        return assignedTeam;
    }

    public String getRequiredResourceType() {
        return requiredResourceType;
    }

    public double getEstimatedDurationHours() {
        return estimatedDurationHours;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "ServiceRequest[ID=" + id + ", Type='" + requestType + "', Location='" + (location != null ? location : description) + "']";
    }

    private static String currentTimestamp() {
        return java.time.LocalDateTime.now().toString().replace('T', ' ');
    }
}