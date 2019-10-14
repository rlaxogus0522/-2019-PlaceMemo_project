
package com.example.placememo_project;

import android.graphics.Canvas;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;


public class ItemTouchHelperCallback extends ItemTouchHelperExtension.Callback{


    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.START);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
     return true;

    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        Object holderItem = viewHolder.itemView.getTag();
        if (holderItem != null) {
            ItemHolder holder = (ItemHolder) holderItem;
            if (holderItem instanceof ItemHolder.ItemSwipeWithActionWidthNoSpringViewHolder2) {
                if (dX < -holder.mActionContainer2.getWidth()) {
                    dX = -holder.mActionContainer2.getWidth();
                }
                holder.mViewContent2.setTranslationX(dX);
                return;
            }

            if (holderItem instanceof ItemHolder) {
                holder.mViewContent2.setTranslationX(dX);
            }
        }

    }
}