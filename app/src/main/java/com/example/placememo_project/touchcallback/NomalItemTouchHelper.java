package com.example.placememo_project.touchcallback;

import android.content.Context;
import android.graphics.Canvas;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.placememo_project.dbData.Data_nomal;
import com.example.placememo_project.activity.MainActivity;
import com.example.placememo_project.adapter.NomalMemoAdapter;
import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;


import io.realm.Realm;

import static com.example.placememo_project.activity.MainActivity.mainContext;

public  class NomalItemTouchHelper extends ItemTouchHelperExtension.Callback{
    Realm myRealm;
    public NomalItemTouchHelper(Context context){
        Realm.init(context);
        myRealm = Realm.getDefaultInstance();
    }
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlag = ItemTouchHelper.LEFT;
        return makeMovementFlags(dragFlag, swipeFlag);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder1) {
        int fromPosition, toPostition; // 드래그하여 위치 를 이동시기면 Db에 oreder 순서를 그에 맞춰 변경하여 다음에 재부팅및 데이터 복원 시에도 순서가 유지되게하기위함
        fromPosition = viewHolder.getAdapterPosition();
        toPostition = viewHolder1.getAdapterPosition();

        Data_nomal from = myRealm.where(Data_nomal.class).equalTo("order",fromPosition).findFirst();
        Data_nomal to = myRealm.where(Data_nomal.class).equalTo("order",toPostition).findFirst();

        myRealm.beginTransaction();
        from.setOrder(toPostition);
        to.setOrder(fromPosition);
        myRealm.commitTransaction();

        ((MainActivity)mainContext).nomaladapters.notifyItemMoved(viewHolder.getPosition(),viewHolder1.getPosition());
        return true;
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {

    }
    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if(dX == 0 && dY != 0){
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
        NomalMemoAdapter.NomalHolder holder1 = (NomalMemoAdapter.NomalHolder) viewHolder;
        if (viewHolder instanceof NomalMemoAdapter.ItemSwipeWithActionWidthNomalHolder) {
            holder1.mViewContent1.setTranslationX(dX);
            return;
        }


    }
}