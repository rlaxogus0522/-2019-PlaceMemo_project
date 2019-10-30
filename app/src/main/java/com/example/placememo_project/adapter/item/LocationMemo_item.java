package com.example.placememo_project.adapter.item;

public class LocationMemo_item {
    String type,title,memo;    //-- 메뉴인지,메모인지,여백인지 구분하는 type 변수 ,  위치에 이름 , 메모
    int icon;  //-- 위치에 대한 Icon
    int color = 0;  //-- 메뉴판에 색깔

    public LocationMemo_item(int icon,int color, String title, String type) {
        this.icon = icon;
        this.title = title;
        this.color = color;
        this.type = type;
    }
    public LocationMemo_item(String title, String memo, String type) {
        this.title = title;
        this.memo = memo;
        this.type = type;
    }
    public LocationMemo_item(String title,int color, String type) {
        this.title = title;
        this.color = color;
        this.type = type;
    }

    public LocationMemo_item(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
