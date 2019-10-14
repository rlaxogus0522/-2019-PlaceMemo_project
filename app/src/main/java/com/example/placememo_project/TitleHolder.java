package com.example.placememo_project;



import android.view.View;

import androidx.annotation.NonNull;


import com.example.placememo_project.databinding.ListItemTitleBinding;
import com.example.placememo_project.databinding.ViewListMainTitleContentBinding;
import com.xwray.groupie.databinding.BindableItem;

import java.util.Collection;
import java.util.Collections;


public class TitleHolder extends BindableItem<ListItemTitleBinding>  {
    Data_alam mItem;
    int colorNum;
    int color[] = new int[]{0xFFE8EE9C, 0xFFE4B786, 0xFF97E486, 0xFF86E4D1, 0xFFE48694};  //-- 저장된 메모 메뉴에 표시할 색깔 등록해두기
    View mViewContent1;
    View mActionContainer1;

    TitleHolder(Data_alam item, int num) {
        mItem = item;
        colorNum = num;
    }



    @Override
    public void bind(@NonNull ListItemTitleBinding viewBinding, int position) {
        viewBinding.getRoot().setTag(this);
        viewBinding.title.tvTitle.setText(mItem.getName());
        viewBinding.title.titleImage.setImageResource(mItem.getIcon());
        viewBinding.title.viewListTitleContent.setBackgroundColor(color[colorNum]);
        viewBinding.viewListTitleActionContainer.setBackgroundColor(color[colorNum]);
        mViewContent1 = viewBinding.title.viewListTitleContent;
        mActionContainer1 = viewBinding.viewListTitleActionContainer;
    }

    @Override
    public int getLayout() {
        return R.layout.list_item_title;
    }






}
