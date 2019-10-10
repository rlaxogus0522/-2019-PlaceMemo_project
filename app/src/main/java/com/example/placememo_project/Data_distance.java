package com.example.placememo_project;

import io.realm.Realm;
import io.realm.RealmObject;

public class Data_distance extends RealmObject {
    double minDistance;

    public double getDistance()  {
        return minDistance;
    }

    public void setDistance(double distance) {
        this.minDistance = distance;
    }
}

