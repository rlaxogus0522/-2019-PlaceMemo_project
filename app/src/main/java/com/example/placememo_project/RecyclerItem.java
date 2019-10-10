package com.example.placememo_project;

public class RecyclerItem {  //-- 메모에 추가되는 아이템
    String type,title,memo;    //-- 메뉴인지,메모인지 구분하는 type 변수 ,  위치에 이름 , 메모
    int icon;  //-- 위치에 대한 Icon
    int color;  //-- 메뉴판에 색깔

    public int getColor() {
        return color;
    }


    public int getIcon() {
        return icon;
    }

    public RecyclerItem(int icon, String title, int color, String type) {
        this.icon = icon;
        this.title = title;
        this.type = type;
        this.color = color;
    }
    public RecyclerItem(String memo, String type) {
        this.memo = memo;
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
}
