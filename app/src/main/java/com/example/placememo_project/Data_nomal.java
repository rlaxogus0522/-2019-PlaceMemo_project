package com.example.placememo_project;

import io.realm.Realm;
import io.realm.RealmObject;

public class Data_nomal extends RealmObject {
    String memo;
    int color;
    boolean frag;

    public boolean getFrag() {
        return frag;
    }

    public void setFrag(boolean frag) {
        this.frag = frag;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}