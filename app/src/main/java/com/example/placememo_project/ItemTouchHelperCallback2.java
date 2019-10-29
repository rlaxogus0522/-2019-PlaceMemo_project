
package com.example.placememo_project;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;

import java.util.Collection;
import java.util.Collections;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.example.placememo_project.MainActivity.mainContext;

public  class ItemTouchHelperCallback2 extends ItemTouchHelperExtension.Callback{
    Realm myRealm;
    public ItemTouchHelperCallback2(Context context){
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
        int fromPosition, toPostition;
        fromPosition = viewHolder.getAdapterPosition();
        toPostition = viewHolder1.getAdapterPosition();
        RealmResults<Data_nomal> from1 = myRealm.where(Data_nomal.class).findAll().sort("order");


        Data_nomal from = myRealm.where(Data_nomal.class).equalTo("order",fromPosition).findFirst();
        Data_nomal to = myRealm.where(Data_nomal.class).equalTo("order",toPostition).findFirst();


        myRealm.beginTransaction();
        from.setOrder(toPostition);
        to.setOrder(fromPosition);
        myRealm.commitTransaction();


        RealmResults<Data_nomal> from2 = myRealm.where(Data_nomal.class).findAll().sort("order");
        for (Data_nomal data_nomal : from2) {
        }


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
            RecyclerAdapter.ViewHolder1 holder1 = (RecyclerAdapter.ViewHolder1) viewHolder;
            if (viewHolder instanceof RecyclerAdapter.ItemSwipeWithActionWidthViewHolder1) {
                holder1.mViewContent1.setTranslationX(dX);
                Log.d("mViewContent1",holder1.mViewContent1.getTranslationX()+"");
                Log.d("mActionContainer1",holder1.mActionContainer1.getWidth()+"");
                return;
            }


    }
}