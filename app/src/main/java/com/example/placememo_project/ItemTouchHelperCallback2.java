
package com.example.placememo_project;

import android.graphics.Canvas;
import android.util.Log;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;

import static com.example.placememo_project.MainActivity.mainContext;

public  class ItemTouchHelperCallback2 extends ItemTouchHelperExtension.Callback{

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlag = ItemTouchHelper.LEFT;
        return makeMovementFlags(dragFlag, swipeFlag);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder1) {
        ((MainActivity)mainContext).nomaladapters.notifyItemMoved(viewHolder.getPosition(),viewHolder1.getPosition());
//                onItemMoved(adapter.getGroup(0),holder.getPosition(),holder1.getPosition());
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