public class Location {
    private final int locationId;
    private final String name;
    private final String district;
    private final String areaType;
    private final double latitude;
    private final double longitude;
    private final String operatingHours;
    private final String buildingCode;

    public Location(int locationId, String name, String district, String areaType,
                    double latitude, double longitude, String operatingHours, String buildingCode) {
        this.locationId = locationId;
        this.name = name;
        this.district = district;
        this.areaType = areaType;
        this.latitude = latitude;
        this.longitude = longitude;
        this.operatingHours = operatingHours;
        this.buildingCode = buildingCode;
    }

    public int getLocationId() {
        return locationId;
    }

    public String getName() {
        return name;
    }

    public String getDistrict() {
        return district;
    }

    public String getAreaType() {
        return areaType;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getOperatingHours() {
        return operatingHours;
    }

    public String getBuildingCode() {
        return buildingCode;
    }

    @Override
    public String toString() {
        return String.format("Location[id=%d,name=%s,district=%s,areaType=%s,lat=%.5f,long=%.5f,open=%s,code=%s]",
                locationId, name, district, areaType, latitude, longitude, operatingHours,
                buildingCode == null ? "" : buildingCode);
    }
}