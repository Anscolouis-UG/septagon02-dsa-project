public class ServiceRequest implements Comparable<ServiceRequest> {
    private int id;
    private String type;
    private String location;
    private String priority;
    private String status;
    private String assignedTeam;

    // Primary constructor containing tracking fields
    public ServiceRequest(int id, String type, String location, String priority, String status, String assignedTeam) {
        this.id = id;
        this.type = type;
        this.location = location;
        this.priority = priority;
        this.status = status;
        this.assignedTeam = assignedTeam;
    }

    // Overloaded constructor for backward compatibility
    public ServiceRequest(int id, String type, String location) {
        this(id, type, location, "Normal", "Pending", "Unassigned");
    }

    // Getters
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

    // Setters
    public void setStatus(String status) {
        this.status = status;
    }

    public void setAssignedTeam(String assignedTeam) {
        this.assignedTeam = assignedTeam;
    }

    @Override
    public int compareTo(ServiceRequest other) {
        return Integer.compare(this.id, other.id);
    }

    @Override
    public String toString() {
        return "ServiceRequest[ID=" + id + 
               ", Type='" + type + '\'' + 
               ", Location='" + location + '\'' + 
               ", Priority='" + priority + '\'' + 
               ", Status='" + status + '\'' + 
               ", Team='" + assignedTeam + "']";
    }
}