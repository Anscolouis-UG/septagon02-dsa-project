public class Resource {
    private final int resourceId;
    private final String resourceName;
    private final String resourceType;
    private final int capacity;
    private final Integer currentLocationId;
    private final String availabilityStatus;
    private final double costPerHour;

    public Resource(int resourceId, String resourceName, String resourceType,
                    int capacity, Integer currentLocationId, String availabilityStatus,
                    double costPerHour) {
        this.resourceId = resourceId;
        this.resourceName = resourceName;
        this.resourceType = resourceType;
        this.capacity = capacity;
        this.currentLocationId = currentLocationId;
        this.availabilityStatus = availabilityStatus;
        this.costPerHour = costPerHour;
    }

    public int getResourceId() {
        return resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getResourceType() {
        return resourceType;
    }

    public int getCapacity() {
        return capacity;
    }

    public Integer getCurrentLocationId() {
        return currentLocationId;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public double getCostPerHour() {
        return costPerHour;
    }

    @Override
    public String toString() {
        return String.format("Resource[id=%d,name=%s,type=%s,capacity=%d,location=%s,status=%s,cost=%.2f]",
                resourceId, resourceName, resourceType, capacity,
                currentLocationId == null ? "null" : currentLocationId,
                availabilityStatus, costPerHour);
    }
}