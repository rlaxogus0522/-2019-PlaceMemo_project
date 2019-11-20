package com.example.placememo_project.dbData;

import io.realm.RealmObject;

public class Data_nomal extends RealmObject {
    String memo;
    int color;
    boolean frag; // 밑줄을 그엇는지 아닌지 확인용
    int order; // 메모들의 순서를 저장, 드래그앤 드롭으로 순서가 바뀌어도 백업이나 앱을 껏다켜도 그 순서가 유지되기위해 사용


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
