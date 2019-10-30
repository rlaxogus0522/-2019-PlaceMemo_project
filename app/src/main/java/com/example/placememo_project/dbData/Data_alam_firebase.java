package com.example.placememo_project.dbData;


public class Data_alam_firebase {  //-- 저장된 메모
    public  double latitude,longitude; //-- 저장된 각 위치의 위,경도 위치 ( 사용자에게는 안보이는 부분 )
    public String name; //-- 사용자가 입력한 위치 이름 ( 제목 부분 )
    public  int icon;  //-- 사용자가 설정한 Icon  ( 제목 부분 )
    public  String memo; //-- 사용자가 등록한 메모 ( 내용 부분)
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
