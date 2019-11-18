package com.example.placememo_project.dbData;

import io.realm.RealmObject;

public class Data_LastLocation_LastTime extends RealmObject {
    double latitude;
    double longitude;
    double mindistance = 0f;
    double mintime = 0f;

    public double getMintime() {
        return mintime;
    }

    public void setMintime(double mintime) {
        this.mintime = mintime;
    }

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

    public double getMindistance() {
        return mindistance;
    }

    public void setMindistance(double mindistance) {
        this.mindistance = mindistance;
    }
}
