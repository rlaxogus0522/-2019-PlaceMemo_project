package com.example.placememo_project.dbData;

import io.realm.RealmObject;

public class Data_Icon extends RealmObject {
    public  int button; //-- 사용자가 메모입력시 선택하는 버튼
    public int buttonclick;  //-- 사용자가  메모입력시 선택하는 버튼에 클릭된 모양
    public String name;  //--  사용자가 메모 입력시 미리 작성해둔 위치이름
    public double longitude;  //-- 최종적으로 메모를 저장할때 넘겨주는 경도
    public double latitude;   //-- 최종적으로 메모를 저장할때 넘겨주는 위도
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
