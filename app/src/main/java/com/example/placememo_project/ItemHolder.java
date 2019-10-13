package com.example.placememo_project;

import androidx.annotation.NonNull;

import com.example.placememo_project.databinding.ViewListMainContentBinding;
import com.xwray.groupie.databinding.BindableItem;

public class ItemHolder extends BindableItem<ViewListMainContentBinding> {
    Data_alam mItem;


    ItemHolder(Data_alam item) {
        mItem = item;

    }

    @Override
    public void bind(@NonNull ViewListMainContentBinding viewBinding, int position) {
        viewBinding.textListMainTitle.setText(mItem.getMemo());

    }

    @Override
    public int getLayout() {
        return R.layout.view_list_main_content;
    }
}