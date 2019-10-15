package com.example.placememo_project;


import android.app.AlarmManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;

import android.os.Bundle;

import android.util.Log;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.placememo_project.databinding.ActivityMainBinding;
import com.example.placememo_project.databinding.ItemToItemBinding;

import com.loopeer.itemtouchhelperextension.Extension;
import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.Section;
import com.xwray.groupie.databinding.BindableItem;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    ActivityMainBinding mainBinding;
    public static Context mainContext;
    private final static String TAG = "MainActivity======";
    private DrawerLayout drawerLayout;  //-- 옵션창 레이아웃
    private View drawView;
    private boolean isdrawer = false;
    static public ArrayList<String> titlename = new ArrayList<>();  //-- 등록된 알람이있는지 체크하기위한 변수( 메뉴용 )
    GroupAdapter<GroupieViewHolder> adapter = new GroupAdapter<>();
    Realm myRealm;
    AlertDialog alamreset;  //-- 설정창에서 모든 알람 초기화시 경고 메시지 용
    ItemTouchHelperExtension mitemTouchHelper;
    ItemTouchHelperExtension.Callback mCallback;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Realm.init(this);
        try {
            myRealm = Realm.getDefaultInstance();

        } catch (Exception e) {
            Log.d(TAG, "myRealm = null");
        }
        mainContext = this;
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainBinding.recycleerView.setAdapter(adapter);
        mainBinding.recycleerView.setLayoutManager(new LinearLayoutManager(this));
        /*------------------------------------------------------------*/
        mainBinding.btnSetting.setOnClickListener(this);
        mainBinding.menu.sortName.setOnClickListener(this);
        mainBinding.menu.sortUpdate.setOnClickListener(this);
        mainBinding.menu.sortAlams.setOnClickListener(this);
        mainBinding.menu.loof1.setOnClickListener(this);
        mainBinding.menu.loof3.setOnClickListener(this);
        mainBinding.menu.loofInfinity.setOnClickListener(this);
        mainBinding.menu.wigetOn.setOnClickListener(this);
        mainBinding.menu.wigetOff.setOnClickListener(this);
        mainBinding.menu.btnClose.setOnClickListener(this);
        mainBinding.menu.btnReset.setOnClickListener(this);
        mainBinding.btnInsertMemo.setOnClickListener(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawlayout);
        drawView = (View) findViewById(R.id.drawer);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // 제목셋팅
        alertDialogBuilder.setTitle("모든 알람 초기화");

        // AlertDialog 셋팅
        alertDialogBuilder
                .setMessage("모든 알람을초기화 하시겠습니까? \n (단, 등록된 알람위치는 지워지지 않습니다.)")
                .setCancelable(false)
                .setPositiveButton("초기화",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                // 초기화
                                alamReset();

                            }
                        })
                .setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                // 다이얼로그를 취소한다
                                dialog.cancel();
                            }
                        });
        alamreset = alertDialogBuilder.create();

        mCallback = new ItemTouchHelperCallback();
        mitemTouchHelper = new ItemTouchHelperExtension(mCallback);
        mitemTouchHelper.attachToRecyclerView(mainBinding.recycleerView);

        dataUpdate();   //-- DB에 정보 가져오기
        checkNoImage();   //-- 처음에 저장된 메모가 있는지 없는지 여부에 따라 메모 없다고 표시
    }


    private void dataUpdate() {   //-- DB에 있는 정보 가져오기
        ShowAlamUi();
    }


    void checkNoImage() {  //-- 등록된 알람이 없는지 체크
        if (titlename.size() == 0) {
            mainBinding.TextViewNoMemo.setAlpha(1);
            if(sender!=null) {
                am.cancel(sender);
                sender = null;
            }
        } else {
            mainBinding.TextViewNoMemo.setAlpha(0);
            if(sender== null) locationSerch(this);   //-- 내위치 검색 알람매니저 실행
        }
    }

    void startEdit(String memo,View view){
        view.setTranslationX(0f);
        Intent intent = new Intent(this,EditMemoActivity.class);
        intent.putExtra("memo",memo);
        startActivity(intent);
    }
    @Override
    public void onClick(View view) {
        if (view == mainBinding.btnSetting) {  //-- 옵션을 클릭한다면
            mainBinding.drawlayout.openDrawer(mainBinding.menu.drawer);
            isdrawer = true;  //-- 드로어가 열린것으로 변경
        } else if (view.getId() == R.id.btn_close) {  //-- 옵션창 닫기를 클릭하면
            drawerLayout.closeDrawers();  //-- 드로어를 닫고
            isdrawer = false;  //-- 드로어가 닫힌것으로 변경
        } else if (view == mainBinding.btnInsertMemo) {  //-- 메모추가를 누른다면
            Intent in = new Intent(MainActivity.this, InsertActivity.class);
            startActivityForResult(in, 0522);  //-- 메모추가 액티비티로 이동
        } else if (view == mainBinding.menu.btnReset) {
            alamreset.show();  //-- 모든 알람 초기화를 누른다면 알람리셋 팝업창 보여주기
        }

        settingToggleButton(view);  //-- 옵션창에 버튼설정
    }

    private void alamReset() {  //-- 알람 리셋을 누른다면
        titlename.clear();
        RealmResults<Data_alam> data_alams = myRealm.where(Data_alam.class).findAll();
        myRealm.beginTransaction();
        data_alams.deleteAllFromRealm();
        myRealm.commitTransaction();
        adapter.clear();
        checkNoImage();  //-- 저장된 알람 없다는것을 체크하여 No Memo 이미지를 띄우고
    }


    @Override
    public void onBackPressed() {  //-- 옵션창이 켜져있는상태에서 사용자가 백키를 눌렀을때
        if (isdrawer) {
            drawerLayout.closeDrawers();
            isdrawer = false;
            return;
        } else {
            super.onBackPressed();
            finishAffinity();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { //--  사용자가 메모를 추가가 성공적이었다면
            ShowAlamUi();
        }
    }

    public void ShowAlamUi() {
        adapter.clear();
        RealmResults<Data_alam> results = myRealm.where(Data_alam.class).findAll();
        for (Data_alam data_alam : results) {
            if (!titlename.contains(data_alam.getName())) {
                titlename.add(data_alam.getName());
            }
        }
        if(titlename.size()!=0) {
            for (int i = 0; i < titlename.size(); i++) {
                RealmResults<Data_alam> results2 = myRealm.where(Data_alam.class).equalTo("name", titlename.get(i)).findAll();
                Data_alam data_alam_first = results2.first();
                Section section = new Section();
                TitleHolder titleHolder = new TitleHolder(data_alam_first, i , this);
                section.add(titleHolder);
                for (Data_alam data_alam : results2) {
                    ItemHolder itemHolder = new ItemHolder(data_alam, this);
                    section.add(itemHolder);
                }
                BetweenHolder betweenHolder = new BetweenHolder();
                section.add(betweenHolder);
                adapter.add(section);
            }
        }
        checkNoImage();
    }

    public void remove(){
            for (int i = 0; i < titlename.size(); i++) {
                RealmResults<Data_alam> results = myRealm.where(Data_alam.class).equalTo("name", titlename.get(i)).findAll();

                if (results.size() == 0) {
                    titlename.remove(i);
                    myRealm.beginTransaction();
                    results.deleteAllFromRealm();
                    myRealm.commitTransaction();
                }
            }

    }


    /*--------------------------------------------------------------------------------------------------------------*/


    class BetweenHolder extends BindableItem<ItemToItemBinding> {

        @Override
        public void bind(@NonNull ItemToItemBinding viewBinding, int position) {
        }

        @Override
        public int getLayout() {
            return R.layout.item_to_item;
        }
    }



    /*------------------------------------------------------------------------------------------------------------------------------------------*/
    /*------------------------------------------------------------------------------------------------------------------------------------------*/
    /*------------------------------------------------------------------------------------------------------------------------------------------*/
    public  class ItemTouchHelperCallback extends ItemTouchHelperExtension.Callback{

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return makeMovementFlags(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder1) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {

        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            Object holderItem = viewHolder.itemView.getTag();
            try {
                if(holderItem.getClass()==ItemHolder.class){
                    ItemHolder holder = (ItemHolder) holderItem;
                    if( dX < 0)
                    {
                        Log.d("==+1", "down : " + holder.down + "   " + dX);
                        if (dX < -holder.mActionContainer2.getWidth()) {
                            dX = -holder.mActionContainer2.getWidth();
                        }
                        holder.mViewContent2.setTranslationX(dX);
                        Log.d("==+2", dX + "");
                    }
                    else
                    {
                        Log.d("==+1", "down : " + holder.down + "   " + dX);
                        if (dX > 0) {
                            dX = 0;
                        }
//                        holder.mViewContent2.getTranslationX();
                        holder.mViewContent2.setTranslationX(dX);
                        Log.d("==+2", dX + "");
                    }
                }else if(holderItem.getClass() == TitleHolder.class){
                    TitleHolder holder = (TitleHolder) holderItem;
                    if (dX < -holder.mActionContainer1.getWidth()) {
                        dX = -holder.mActionContainer1.getWidth();
                    }
                    holder.mViewContent1.setTranslationX(dX);
                }
            }catch (NullPointerException e){ }
        }

        /*------------------------------------------------------------------------------------------------------------------------------------------*/
    }
    /*------------------------------------------------------------------------------------------------------------------------------------------*/
    /*------------------------------------------------------------------------------------------------------------------------------------------*/
    /*--------------------------------------------------------------------------------------------------------------*/
    private void settingToggleButton(View view) { // seletor Item
        if (view.getId() == R.id.sort_name) {
            mainBinding.menu.sortAlams.setTextColor(Color.rgb(0, 0, 0));
            mainBinding.menu.sortUpdate.setTextColor(Color.rgb(0, 0, 0));
            mainBinding.menu.sortName.setTextColor(Color.rgb(70, 160, 220));
        } else if (view.getId() == R.id.sort_alams) {
            mainBinding.menu.sortAlams.setTextColor(Color.rgb(70, 160, 220));
            mainBinding.menu.sortUpdate.setTextColor(Color.rgb(0, 0, 0));
            mainBinding.menu.sortName.setTextColor(Color.rgb(0, 0, 0));
        } else if (view.getId() == R.id.sort_update) {
            mainBinding.menu.sortAlams.setTextColor(Color.rgb(0, 0, 0));
            mainBinding.menu.sortUpdate.setTextColor(Color.rgb(70, 160, 220));
            mainBinding.menu.sortName.setTextColor(Color.rgb(0, 0, 0));
        } else if (view.getId() == R.id.loof_1) {

            mainBinding.menu.loof1.setTextColor(Color.rgb(70, 160, 220));
            mainBinding.menu.loof3.setTextColor(Color.rgb(0, 0, 0));
            mainBinding.menu.loofInfinity.setTextColor(Color.rgb(0, 0, 0));
        } else if (view.getId() == R.id.loof_3) {
            mainBinding.menu.loof1.setTextColor(Color.rgb(0, 0, 0));
            mainBinding.menu.loof1.setTextColor(Color.rgb(70, 160, 220));
            mainBinding.menu.loofInfinity.setTextColor(Color.rgb(0, 0, 0));
        } else if (view.getId() == R.id.loof_infinity) {
            mainBinding.menu.loof1.setTextColor(Color.rgb(0, 0, 0));
            mainBinding.menu.loof1.setTextColor(Color.rgb(0, 0, 0));
            mainBinding.menu.loofInfinity.setTextColor(Color.rgb(70, 160, 220));
        } else if (view.getId() == R.id.wiget_on) {
            mainBinding.menu.wigetOn.setTextColor(Color.rgb(70, 160, 220));
            mainBinding.menu.wigetOff.setTextColor(Color.rgb(0, 0, 0));
        } else if (view.getId() == R.id.wiget_off) {
            mainBinding.menu.wigetOn.setTextColor(Color.rgb(0, 0, 0));
            mainBinding.menu.wigetOff.setTextColor(Color.rgb(70, 160, 220));
        }
    }

}