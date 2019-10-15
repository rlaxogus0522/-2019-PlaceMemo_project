package com.example.placememo_project;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
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

    boolean down = false;
    float x=0;
    float moveX=0;

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
        viewBinding.viewListRepoActionEdit.setOnClickListener(this);
        viewBinding.item.viewListMainContent.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    moveX = 0;
                    x = motionEvent.getRawX();
                    down = true;
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_MOVE)
                {
                    //moveX = x - motionEvent.getRawX();
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    down = false;
                }
                return false;
            }
        });
    }


    @Override
    public int getLayout() {
        return R.layout.list_item_main;
    }

    @Override
    public void onClick(View view) {
        if(view == mainBinding.viewListRepoActionDelete ){
            if(mViewContent2.getTranslationX() == -mActionContainer2.getWidth()) {
                Toast.makeText(mContext, "삭제누름", Toast.LENGTH_LONG).show();
                RealmResults<Data_alam> data_alams = myRealm.where(Data_alam.class).equalTo("memo", mItem.getMemo()).findAll();
                myRealm.beginTransaction();
                data_alams.deleteAllFromRealm();
                myRealm.commitTransaction();
                ((MainActivity) mainContext).remove();
                ((MainActivity) mainContext).ShowAlamUi();
                mViewContent2.setTranslationX(0f);
            }
        }else if ( view == mainBinding.viewListRepoActionEdit){
            if(mViewContent2.getTranslationX() == -mActionContainer2.getWidth()) {
                ((MainActivity) mainContext).startEdit(mItem.getMemo(), mViewContent2);
                Log.d("==", "실행완료");
                mViewContent2.setTranslationX(0f);
            }
        }

    }
//


}