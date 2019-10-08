
package com.example.placememo_project;

import android.graphics.Canvas;
import android.util.Log;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;
public  class ItemTouchHelperCallback extends ItemTouchHelperExtension.Callback{

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        Log.d("지금이니!!","1");
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
        if(viewHolder.getLayoutPosition()==0) {
            RecyclerAdapter.ViewHolder1 holder1 = (RecyclerAdapter.ViewHolder1) viewHolder;
            if (viewHolder instanceof RecyclerAdapter.ItemSwipeWithActionWidthNoSpringViewHolder1) {
                if (dX < -holder1.mActionContainer1.getWidth()) {
                    dX = -holder1.mActionContainer1.getWidth();
                }
                holder1.mViewContent1.setTranslationX(dX);
                return;
            }
            if (viewHolder instanceof RecyclerAdapter.ViewHolder1) {
                holder1.mViewContent1.setTranslationX(dX);

            }
        }else{
            RecyclerAdapter.ViewHolder2 holder2 = (RecyclerAdapter.ViewHolder2) viewHolder;
            if (viewHolder instanceof RecyclerAdapter.ItemSwipeWithActionWidthNoSpringViewHolder2) {
                if (dX < -holder2.mActionContainer2.getWidth()) {
                    dX = -holder2.mActionContainer2.getWidth();
                }
                holder2.mViewContent2.setTranslationX(dX);
                return;
            }

            if (viewHolder instanceof RecyclerAdapter.ViewHolder2) {
                holder2.mViewContent2.setTranslationX(dX);
                Log.d("++-++-",dX+"");

            }
        }




    }
}