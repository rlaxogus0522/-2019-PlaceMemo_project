package com.example.placememo_project.dbData;

import io.realm.RealmObject;

public class Data_nomal extends RealmObject {
    String memo;
    int color;
    boolean frag;
    int order;


    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

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
