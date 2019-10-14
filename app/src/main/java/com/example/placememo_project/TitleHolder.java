package com.example.placememo_project;



import android.view.View;

import androidx.annotation.NonNull;


import com.example.placememo_project.databinding.ViewListMainTitleContentBinding;
import com.xwray.groupie.databinding.BindableItem;

import java.util.Collection;
import java.util.Collections;


public class TitleHolder extends BindableItem<ViewListMainTitleContentBinding>  {
    Data_alam mItem;
    int colorNum;
    int color[] = new int[]{0xFFE8EE9C, 0xFFE4B786, 0xFF97E486, 0xFF86E4D1, 0xFFE48694};  //-- 저장된 메모 메뉴에 표시할 색깔 등록해두기

    TitleHolder(Data_alam item, int num) {
        mItem = item;
        colorNum = num;
    }



    @Override
    public void bind(@NonNull ViewListMainTitleContentBinding viewBinding, int position) {
        viewBinding.tvTitle.setText(mItem.getName());
        viewBinding.titleImage.setImageResource(mItem.getIcon());
        viewBinding.viewListTitleContent.setBackgroundColor(color[colorNum]);
    }

    @Override
    public int getLayout() {
        return R.layout.view_list_main_title_content;
    }






}
