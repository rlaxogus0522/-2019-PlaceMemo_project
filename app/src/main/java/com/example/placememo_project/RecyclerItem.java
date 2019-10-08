package com.example.placememo_project;

public class RecyclerItem {
    String type,title,memo;
    int icon;
    int color;

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