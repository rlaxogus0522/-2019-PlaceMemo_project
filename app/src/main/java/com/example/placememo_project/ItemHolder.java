package com.example.placememo_project;

import android.util.Log;
import android.view.View;
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


    ItemHolder(Data_alam item) {
        mItem = item;

    }

    public ItemHolder(ListItemMainBinding viewBinding) {
        viewBinding.item.textListMainTitle.setText(mItem.getMemo());
        mViewContent2 = viewBinding.item.viewListMainContent;
        mActionContainer2 = viewBinding.viewListRepoActionContainer;
    }


    @Override
    public void bind(@NonNull ListItemMainBinding viewBinding, int position) {
        viewBinding.getRoot().setTag(this);

        viewBinding.item.textListMainTitle.setText(mItem.getMemo());
        mViewContent2 = viewBinding.item.viewListMainContent;
        mActionContainer2 = viewBinding.viewListRepoActionContainer;

    }

    class ItemSwipeWithActionWidthViewHolder2 extends ItemHolder implements Extension {

        View mActionViewDelete2;
        View mActionViewEdit2;

        public ItemSwipeWithActionWidthViewHolder2(ListItemMainBinding viewBinding) {
            super(viewBinding);
            Log.d("ㅎㅇ","ㅎㅇ");
//            viewBinding.getRoot().setTag(this);
            mActionViewDelete2 = viewBinding.viewListRepoActionDelete;
            mActionViewEdit2 = viewBinding.viewListRepoActionEdit;
        }

        @Override
        public float getActionWidth() {
            return mActionContainer2.getWidth();
        }
    }
    class ItemSwipeWithActionWidthNoSpringViewHolder2 extends ItemSwipeWithActionWidthViewHolder2 implements Extension {

        public ItemSwipeWithActionWidthNoSpringViewHolder2(ListItemMainBinding viewBinding) {
            super(viewBinding);
//            viewBinding.getRoot().setTag(this);
        }

        @Override
        public float getActionWidth() {
            return mActionContainer2.getWidth();
        }
    }

    @Override
    public int getLayout() {
        return R.layout.list_item_main;
    }


//    @NonNull
//    @Override
//    public GroupieViewHolder<ListItemMainBinding> createViewHolder(@NonNull View itemView) {
//
//        return super.createViewHolder(itemView);
//    }
}