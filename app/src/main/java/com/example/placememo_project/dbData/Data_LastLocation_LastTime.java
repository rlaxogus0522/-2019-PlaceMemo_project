package com.example.placememo_project.dbData;

import io.realm.RealmObject;

public class Data_LastLocation_LastTime extends RealmObject {
    double latitude;
    double longitude;
    double minTime = 0f;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getMinTime() {
        return minTime;
    }

    public void setMinTime(double minTime) {
        this.minTime = minTime;
    }
}
