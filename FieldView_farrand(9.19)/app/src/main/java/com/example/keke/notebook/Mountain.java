package com.example.keke.notebook;

// CMD + N
public class Mountain {

    /**
     * peak : Mt. Elbert
     * longitude : -106.4454
     * latitude : 39.1178
     * elevation : 14433
     * ID : 0
     * IsNew :
     */

    private String peak;
    private String longitude;
    private String latitude;
    private String elevation;
    private String ID;
    private String IsNew;

    public String getPeak() {
        return peak;
    }

    public void setPeak(String peak) {
        this.peak = peak;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getElevation() {
        return elevation;
    }

    public void setElevation(String elevation) {
        this.elevation = elevation;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getIsNew() {
        return IsNew;
    }

    public void setIsNew(String IsNew) {
        this.IsNew = IsNew;
    }
}
