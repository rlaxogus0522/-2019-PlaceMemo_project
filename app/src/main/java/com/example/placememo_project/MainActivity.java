package com.example.placememo_project;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.placememo_project.databinding.ActivityMainBinding;
import com.example.placememo_project.databinding.ItemTestItemBinding;
import com.example.placememo_project.databinding.ItemTestTitleBinding;
import com.example.placememo_project.databinding.ViewListMainContentBinding;
import com.example.placememo_project.databinding.ViewListMainTitleContentBinding;
import com.google.gson.Gson;
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
    public Context mContext;  //-- 메인엑티비티에 있는 메소드를 다른곳에서 사용하기위해 사용
    private final static String TAG = "MainActivity======";
    private DrawerLayout drawerLayout;  //-- 옵션창 레이아웃
    private View drawView;
    private boolean isdrawer = false;
    int color[] = new int[]{0xFFE8EE9C, 0xFFE4B786, 0xFF97E486, 0xFF86E4D1, 0xFFE48694};  //-- 저장된 메모 메뉴에 표시할 색깔 등록해두기
    static public ArrayList<String> titlename = new ArrayList<>();  //-- 등록된 알람이있는지 체크하기위한 변수( 메뉴용 )
    ArrayList<RecyclerItem> arraryItem = new ArrayList<>();
    GroupAdapter<GroupieViewHolder> adapter = new GroupAdapter<>();
    Realm myRealm;
    AlertDialog alamreset;  //-- 설정창에서 모든 알람 초기화시 경고 메시지 용
    public ItemTouchHelperExtension[] mItemTouchHelper;
    public ItemTouchHelperExtension.Callback[] mCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Realm.init(this);
        try {
            myRealm = Realm.getDefaultInstance();

        } catch (Exception e) {
            Log.d(TAG, "myRealm = null");
        }
        mContext = this;
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
        /*------------------------------------------------------------*/
//        recyclerViews = new RecyclerView[5];
//        recyclerViews[0] = findViewById(R.id.recycleerView);
//        recyclerViews[1] = findViewById(R.id.recycleerView2);
//        recyclerViews[2] = findViewById(R.id.recycleerView3);
//        recyclerViews[3] = findViewById(R.id.recycleerView4);
//        recyclerViews[4] = findViewById(R.id.recycleerView5);
        /*------------------------------------------------------------*/
//        adapters = new RecyclerAdapter[5];
//        mCallback = new ItemTouchHelperCallback[5];
//        mItemTouchHelper = new ItemTouchHelperExtension[5];
        /*------------------------------------------------------------*/
//        for (int i = 0; i < recyclerViews.length; i++) {
//            mCallback[i] = new ItemTouchHelperCallback();
//            mItemTouchHelper[i] = new ItemTouchHelperExtension(mCallback[i]);
//            adapters[i] = new RecyclerAdapter(this);
//            recyclerViews[i].setLayoutManager(new LinearLayoutManager(this));
//            recyclerViews[i].setAdapter(adapters[i]);
//            mItemTouchHelper[i].attachToRecyclerView(recyclerViews[i]);
//        }
        /*------------------------------------------------------------*/
//        drawerLayout = (DrawerLayout) findViewById(R.id.drawlayout);
//        drawView = (View) findViewById(R.id.drawer);
        /*------------------------------------------------------------*/

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

//        dataUpdate();   //-- DB에 정보 가져오기
        checkNoImage();   //-- 처음에 저장된 메모가 있는지 없는지 여부에 따라 메모 없다고 표시
        locationSerch(this);   //-- 내위치 검색 알람매니저 실행
    }

    @Override
    protected void onPause() {
        super.onPause();
    }




    private void dataUpdate() {   //-- DB에 있는 정보 가져오기
        Log.d("dataUpdate 실행됨", "");
        try {
            RealmResults<Data_alam> results = myRealm.where(Data_alam.class).findAll();
            for (Data_alam data_alam : results) {
                String jsonData = new Gson().toJson(myRealm.copyFromRealm(data_alam));
                Log.d("====", jsonData);   //-- 들어있는 정보 확인용 Log

                /*----------------------------------------------------------------------------------------------------*/

                if (titlename.contains(data_alam.getName())) {   //--메뉴제목에 DB에 저장된 알람목록이 이미 저장되어 있다면
                    for (int i = 0; i < titlename.size(); i++) {
                        if (titlename.get(i).equals(data_alam.getName())) {
//                            adapters[i].addItem(new RecyclerItem(data_alam.getMemo(), "B"));   //-- 그 하위에 내용만 추가
                        }
                    }
                } else { //--메뉴제목에 DB에 저장된 알람목록이 저장되어 있지않다면
                    titlename.add(data_alam.getName());  //-- 메뉴제목에 DB 에 저장된 목록을 추가하고
                    for (int i = 0; i < titlename.size(); i++) {
                        try {
                            if (titlename.get(i).equals(data_alam.getName())) {
//                                adapters[i].addItem(new RecyclerItem(data_alam.getIcon(), data_alam.getName(), color[i], "A"));
//                                adapters[i].addItem(new RecyclerItem(data_alam.getMemo(), "B"));  //-- 메뉴 + 내용을 동시에 추가
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            Log.d(TAG, e.toString());
                        }
                    }

                }


                /*-------------------------------------------------------------------------------------------------------------------*/
            }
        } catch (NullPointerException e) {
            Log.d(TAG, String.valueOf(e));
        }
    }


    void checkNoImage() {  //-- 등록된 알람이 없는지 체크
        if (titlename.size() == 0) {
            mainBinding.imageNoMemo.setAlpha(1.0f);
            mainBinding.TextViewNoMemo.setAlpha(1);
        } else {
            mainBinding.imageNoMemo.setAlpha(0.0f);
            mainBinding.TextViewNoMemo.setAlpha(0);
        }
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
        checkNoImage();  //-- 저장된 알람 없다는것을 체크하여 No Memo 이미지를 띄우고
    }

    // - 문제발견 수정필요

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
                RealmResults<Data_alam> results = myRealm.where(Data_alam.class).findAll();
                for (Data_alam data_alam : results) {
                   if(!titlename.contains(data_alam.getName())){
                       titlename.add(data_alam.getName());
                   }
                }
            for (int i = 0; i < titlename.size() ; i++) {
                RealmResults<Data_alam> results2 = myRealm.where(Data_alam.class).equalTo("name",titlename.get(i)).findAll();
                Data_alam data_alam_first = results2.first();
                Section section = new Section();
                TitleHolder titleHolder = new TitleHolder(data_alam_first);
                section.add(titleHolder);
                for(Data_alam data_alam : results2){
                    ItemHolder itemHolder = new ItemHolder(data_alam);
                    section.add(itemHolder);
                }
                adapter.add(section);
            }





//            if (!titlename.contains(data.getStringExtra("nName"))) {//-- 사용자가 추가하는 메모에 해당하는 위치가있다면
//                titlename.add(data.getStringExtra("nName"));
//                for (int i = 0; i < titlename.size(); i++) {
////                    if (titlename.get(i).equals(data.getStringExtra("nName"))) {
//                        Section section = new Section();
//                        TitleHolder titleHolder = new TitleHolder(arraryItem.get(0));
//                        section.add(titleHolder);
//                    for (int j = 0; j <  ; j++) {
//
//                    }
//
//                        adapters[i].addItem(new RecyclerItem(data.getStringExtra("memo"), "B"));  //-- 내용만 추가
//                    }
//                }
//            }
//            else { //-- 사용자가 추가하는 메모에 해당하는 위치가없다면
//                titlename.add(data.getStringExtra("nName"));
//                checkNoImage();
//                for (int i = 0; i < titlename.size(); i++) {
//                    try {
//                        if (titlename.get(i).equals(data.getStringExtra("nName"))) {  //-- 메뉴 + 내용을 추가
////                            adapters[i].addItem(new RecyclerItem(data.getIntExtra("nicon", -1), data.getStringExtra("nName"), color[i], "A"));  //-- 메뉴바에 색깔도 지정
////                            adapters[i].addItem(new RecyclerItem(data.getStringExtra("memo"), "B"));
//                        }
//                    } catch (NullPointerException e) {
//                    }
//                    Log.d(TAG, "NullPointerException");
//
//                }
//
//            }

        }
    }

/*--------------------------------------------------------------------------------------------------------------*/
    class ItemHolder extends BindableItem<ViewListMainContentBinding> {
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



    class TitleHolder extends BindableItem<ViewListMainTitleContentBinding> {
        Data_alam mItem;

        TitleHolder(Data_alam item) {
            mItem = item;
        }

        @Override
        public void bind(@NonNull ViewListMainTitleContentBinding viewBinding, int position) {
            viewBinding.tvTitle.setText(mItem.getName());
            viewBinding.titleImage.setImageResource(mItem.getIcon());
        }

        @Override
        public int getLayout() {
            return R.layout.view_list_main_title_content;
        }
    }

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


//    private String getUserName()
//    {
//        return getIntent().getStringExtra(USER_NAME);
//    }
//
//    static public final String USER_NAME = "user_name";
//    static public void start(Context ct, String arg)
//    {
//        Intent it = new Intent(ct, MainActivity.class);
//        it.putExtra( USER_NAME,it);
//        ct.startActivity(it );
//    }
//
}