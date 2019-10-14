package com.example.placememo_project;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.placememo_project.databinding.ListItemMainBinding;
import com.loopeer.itemtouchhelperextension.Extension;
import com.xwray.groupie.databinding.BindableItem;
import com.xwray.groupie.databinding.GroupieViewHolder;

public class ItemHolder extends BindableItem<ListItemMainBinding> {
    Data_alam mItem;
    View mViewContent2;
    View mActionContainer2;
    Context context;


    ItemHolder(Data_alam item, Context context) {
        mItem = item;
        this.context = context;
    }


    @Override
    public void bind(@NonNull ListItemMainBinding viewBinding, int position) {
        viewBinding.getRoot().setTag(this);
        viewBinding.item.textListMainTitle.setText(mItem.getMemo());
        mViewContent2 = viewBinding.item.viewListMainContent;
        mActionContainer2 = viewBinding.viewListRepoActionContainer;
    }

    public float getActionWidth(){
        return mActionContainer2.getWidth();
    }



//    class ItemSwipeWithActionWidthViewHolder2 extends ItemHolder implements Extension {
//
//        View mActionViewDelete2;
//        View mActionViewEdit2;
//
//        public ItemSwipeWithActionWidthViewHolder2(View view) {
//            super();
//            Log.d("ㅎㅇ","ㅎㅇ");
//            mActionViewDelete2 = viewBinding.viewListRepoActionDelete;
//            mActionViewEdit2 = viewBinding.viewListRepoActionEdit;
//        }
//
//        @Override
//        public float getActionWidth() {
//            return mActionContainer2.getWidth();
//        }
//    }

    @Override
    public int getLayout() {
        return R.layout.list_item_main;
    }
//


}