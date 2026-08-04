public class ServiceRequest {
    private final int id;
    private final String requestType;
    private final String location;

    public ServiceRequest(int id, String requestType, String location) {
        this.id = id;
        this.requestType = requestType;
        this.location = location;
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

    @Override
    public String toString() {
        return "ServiceRequest[ID=" + id + ", Type='" + requestType + "', Location='" + location + "']";
    }
}