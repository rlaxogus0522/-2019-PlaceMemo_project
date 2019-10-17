package com.example.placememo_project;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;

import androidx.annotation.NonNull;

import com.example.placememo_project.databinding.ItemToItemBinding;
import com.example.placememo_project.databinding.PinBinding;
import com.xwray.groupie.databinding.BindableItem;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.example.placememo_project.MainActivity.mainContext;

public class PinHolder extends BindableItem<PinBinding> {
    Data_alam mItem;
   Context context;
   Realm myRealm;
    PinHolder(Data_alam item){
        mItem = item;
        Realm.init(mainContext);
        myRealm = Realm.getDefaultInstance();
    }
        @Override
        public void bind(@NonNull PinBinding viewBinding, int position) {
            RealmResults<Data_alam> data_alams = myRealm.where(Data_alam.class).equalTo("name",mItem.getName()).findAll();
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setColor(data_alams.first().getColor());
            gradientDrawable.setCornerRadii(new float[ ]{ 200, 200, 200, 200, 0, 0, 0, 0 });
            viewBinding.pinLayout.setBackgroundDrawable(gradientDrawable);
        }

        @Override
        public int getLayout() {
            return R.layout.pin;
        }
    }

