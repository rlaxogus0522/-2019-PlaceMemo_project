
package com.example.placememo_project.touchcallback;

import android.content.Context;
import android.graphics.Canvas;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.placememo_project.adapter.LocationMemoAdapter;
import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;

public class LocationItemTouchHelper extends ItemTouchHelperExtension.Callback {
    Context context;
    public LocationItemTouchHelper(Context context){
        this.context = context;
    }
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int swipeFlag = ItemTouchHelper.LEFT;
        return makeMovementFlags(0, swipeFlag);

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

        if(viewHolder instanceof LocationMemoAdapter.TitleHolder) {   //-- 저장된 알람 메뉴에 대한 스와이프 기능에 필요한 x 위치 설정
            LocationMemoAdapter.TitleHolder holder1 = (LocationMemoAdapter.TitleHolder) viewHolder;
                holder1.mViewContent1.setTranslationX(dX);

        }else if (viewHolder instanceof LocationMemoAdapter.MemoHolder){ //-- 저장된 알람 메모에 대한 스와이프 기능에 필요한 x 위치 설정
            LocationMemoAdapter.MemoHolder holder2 = (LocationMemoAdapter.MemoHolder) viewHolder;
                holder2.mViewContent2.setTranslationX(dX);

        }




    }

    /*------------------------------------------------------------------------------------------------------------------------------------------*/
}