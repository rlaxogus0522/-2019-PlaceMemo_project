package com.example.placememo_project;

import androidx.annotation.NonNull;

import com.example.placememo_project.databinding.ItemToItemBinding;
import com.xwray.groupie.databinding.BindableItem;

public
class BetweenHolder extends BindableItem<ItemToItemBinding> {

    @Override
    public void bind(@NonNull ItemToItemBinding viewBinding, int position) {
    }

    @Override
    public int getLayout() {
        return R.layout.item_to_item;
    }
}