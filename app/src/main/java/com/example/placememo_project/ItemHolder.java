package com.example.placememo_project;

import android.content.Context;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.example.placememo_project.databinding.ListItemMainBinding;
import com.xwray.groupie.databinding.BindableItem;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.example.placememo_project.MainActivity.mainContext;


public class ItemHolder extends BindableItem<ListItemMainBinding> implements View.OnClickListener {
    Data_alam mItem;
    View mViewContent2;
    View mActionContainer2;
    ListItemMainBinding mainBinding;
    Context mContext;
    Realm myRealm;


    ItemHolder(Data_alam item,Context context) {
        mItem = item;
        mContext = context;
        Realm.init(mContext);
        myRealm = Realm.getDefaultInstance();
    }


    @Override
    public void bind(@NonNull ListItemMainBinding viewBinding, int position) {
        viewBinding.getRoot().setTag(this);
        mainBinding = viewBinding;
        viewBinding.item.textListMainTitle.setText(mItem.getMemo());
        mViewContent2 = viewBinding.item.viewListMainContent;
        mActionContainer2 = viewBinding.viewListRepoActionContainer;

        viewBinding.viewListRepoActionDelete.setOnClickListener(this);
    }


    @Override
    public int getLayout() {
        return R.layout.list_item_main;
    }

    @Override
    public void onClick(View view) {
        if(view == mainBinding.viewListRepoActionDelete ){
            Toast.makeText(mContext,"삭제누름",Toast.LENGTH_LONG).show();
            RealmResults<Data_alam> data_alams = myRealm.where(Data_alam.class).equalTo("memo",mItem.getMemo()).findAll();
            myRealm.beginTransaction();
            data_alams.deleteAllFromRealm();
            myRealm.commitTransaction();
            ((MainActivity) mainContext).remove();
            ((MainActivity) mainContext).ShowAlamUi();
            mViewContent2.setTranslationX(0f);
        }

    }
//


}