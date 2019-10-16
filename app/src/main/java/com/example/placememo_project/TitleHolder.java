package com.example.placememo_project;



import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;


import com.example.placememo_project.databinding.ListItemTitleBinding;
import com.xwray.groupie.databinding.BindableItem;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.example.placememo_project.MainActivity.mainContext;
import static com.example.placememo_project.MainActivity.sort;
import static com.example.placememo_project.MainActivity.titlename;


public class TitleHolder extends BindableItem<ListItemTitleBinding> implements View.OnClickListener {
    Data_alam mItem;
    int colorNum;
    int color[] = new int[]{0xFFE8EE9C, 0xFFE4B786, 0xFF97E486, 0xFF86E4D1, 0xFFE48694};  //-- 저장된 메모 메뉴에 표시할 색깔 등록해두기
    View mViewContent1;
    View mActionContainer1;
    ListItemTitleBinding itemTitleBinding;
    Context mContext;
    Realm myRealm;

    TitleHolder(Data_alam item, int num, Context context) {
        mItem = item;
        colorNum = num;
        mContext = context;
        Realm.init(mContext);
        myRealm = Realm.getDefaultInstance();
    }



    @Override
    public void bind(@NonNull ListItemTitleBinding viewBinding, int position) {
        viewBinding.getRoot().setTag(this);
        itemTitleBinding = viewBinding;
        viewBinding.title.tvTitle.setText(mItem.getName());
        viewBinding.title.titleImage.setImageResource(mItem.getIcon());
        viewBinding.title.viewListTitleContent.setBackgroundColor(color[colorNum]);
        viewBinding.viewListTitleActionContainer.setBackgroundColor(color[colorNum]);
        mViewContent1 = viewBinding.title.viewListTitleContent;
        mActionContainer1 = viewBinding.viewListTitleActionContainer;

        viewBinding.viewListTitleActionDelete.setOnClickListener(this);

    }

    @Override
    public int getLayout() {
        return R.layout.list_item_title;
    }


    @Override
    public void onClick(View view) {
        if(view == itemTitleBinding.viewListTitleActionDelete){
            if(mViewContent1.getTranslationX() == -480f) {
                RealmResults<Data_alam> data_alams = myRealm.where(Data_alam.class).equalTo("name", mItem.getName()).findAll();
                titlename.remove(mItem.getName());
                myRealm.beginTransaction();
                data_alams.deleteAllFromRealm();
                myRealm.commitTransaction();
                ((MainActivity) mainContext).ShowAlamUi(sort);
                mViewContent1.setTranslationX(0f);
            }
        }
    }
}
