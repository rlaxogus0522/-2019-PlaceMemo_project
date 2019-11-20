package com.example.placememo_project.dbData;


public class Data_alam_firebase {  //-- Google FireBase 연동용
    public  double latitude,longitude;
    public String name;
    public  int icon;
    public  String memo;
    public  boolean isAlamOn;
    public  int color;


    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
    public boolean getisAlamOn() {
        return isAlamOn;
    }

    public void setAlamOn(boolean alamOn) {
        isAlamOn = alamOn;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
