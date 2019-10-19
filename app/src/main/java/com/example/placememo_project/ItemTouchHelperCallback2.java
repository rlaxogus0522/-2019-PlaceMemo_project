
package com.example.placememo_project;

import android.graphics.Canvas;
import android.util.Log;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;

public  class ItemTouchHelperCallback2 extends ItemTouchHelperExtension.Callback{

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.START);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder1) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {

    }
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            RecyclerAdapter.ViewHolder1 holder1 = (RecyclerAdapter.ViewHolder1) viewHolder;
            if (viewHolder instanceof RecyclerAdapter.ItemSwipeWithActionWidthViewHolder1) {
                holder1.mViewContent1.setTranslationX(dX);
                Log.d("mViewContent1",holder1.mViewContent1.getTranslationX()+"");
                Log.d("mActionContainer1",holder1.mActionContainer1.getWidth()+"");
                return;
            }


    }
}