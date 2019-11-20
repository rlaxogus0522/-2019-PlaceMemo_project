package com.example.placememo_project.dbData;

import io.realm.RealmObject;

public class Data_LastLocation_LastTime extends RealmObject { // 위치 조회 시 가장 최근에 정보를 가지고 있기 위한 DB
    double latitude;
    double longitude;
    double mindistance = 0f; // 현재 자신의 위치로부터 가장 가까운 목적지까지의 거리
    double mintime = 0f; // 방금 조회하는 데 소요된 시간

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
