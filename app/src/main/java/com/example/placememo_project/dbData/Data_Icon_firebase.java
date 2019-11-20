package com.example.placememo_project.dbData;



public class Data_Icon_firebase{  //-- Google FireBase 연동용
    public int button;
    public int buttonclick;
    public String name;
    public double longitude;
    public  double latitude;
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getButton() {
        return button;
    }

    public void setButton(int button) {
        this.button = button;
    }

    public int getButtonclick() {
        return buttonclick;
    }

    public void setButtonclick(int buttonclick) {
        this.buttonclick = buttonclick;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
