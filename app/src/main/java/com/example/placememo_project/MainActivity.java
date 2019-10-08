package com.example.placememo_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.placememo_project.databinding.ActivityMainBinding;
import com.google.gson.Gson;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityMainBinding mainBinding;
    private final static String TAG = "MainActivity======";
    private DrawerLayout drawerLayout;
    private View drawView;
    private boolean isdrawer = false;
    int color[] = new int[]{0xFFE8EE9C,0xFFE4B786,0xFF97E486,0xFF86E4D1,0xFFE48694};
    private ArrayList<String> titlename = new ArrayList<>();
    RecyclerView[] recyclerViews;
    RecyclerAdapter[] adapters;
    Realm myRealm;
    AlertDialog alamreset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Realm.init(this);
        try {
            myRealm = Realm.getDefaultInstance();

        } catch (Exception e) {
            Log.d(TAG, "myRealm = null");
        }
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
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
        recyclerViews = new RecyclerView[5];
        recyclerViews[0] = findViewById(R.id.recycleerView);
        recyclerViews[1] = findViewById(R.id.recycleerView2);
        recyclerViews[2] = findViewById(R.id.recycleerView3);
        recyclerViews[3] = findViewById(R.id.recycleerView4);
        recyclerViews[4] = findViewById(R.id.recycleerView5);
        adapters = new RecyclerAdapter[5];
        for (int i = 0; i < recyclerViews.length; i++) {
            adapters[i] = new RecyclerAdapter();
            recyclerViews[i].setLayoutManager(new LinearLayoutManager(this));
            recyclerViews[i].setAdapter(adapters[i]);
        }

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


        dataUpdate();
        checkNoImage();



    }





    private void dataUpdate() {
        Log.d("dataUpdate 실행됨", "");
        try {
            RealmResults<Data_alam> results = myRealm.where(Data_alam.class).findAll();
            for (Data_alam data_alam : results) {
//                new Gson().toJson(myRealm.copyFromRealm(data_alam));
//                data_alam.toString();
                String jsonData = new Gson().toJson(myRealm.copyFromRealm(data_alam));
                Log.d("====", jsonData);
//                Log.d("data_alam.getName()",data_alam.getName());
//                Log.d("data_alam.getIcon()",String.valueOf(data_alam.getIcon()));
//                Log.d("data_alam.getMemo()",data_alam.getMemo());
//                Log.d("data_alam.getMemo()",data_alam.getMemo());

                /*----------------------------------------------------------------------------------------------------*/

                Log.d("===","titlename"+titlename);
                    if (titlename.contains(data_alam.getName())) { // 있다면
                        for (int i = 0; i < titlename.size(); i++) {
                            if (titlename.get(i).equals(data_alam.getName())) {
                                adapters[i].addItem(new RecyclerItem(data_alam.getMemo(), "B"));
                            }
                        }
                    } else { // 없다면
                        titlename.add(data_alam.getName());
                        for (int i = 0; i < titlename.size(); i++) {
                            try {
                                if (titlename.get(i).equals(data_alam.getName())) {
                                    adapters[i].addItem(new RecyclerItem(data_alam.getIcon(),data_alam.getName(),color[i], "A"));
                                    adapters[i].addItem(new RecyclerItem(data_alam.getMemo(), "B"));
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

    private void checkNoImage() {
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
        if (view == mainBinding.btnSetting) {
            mainBinding.drawlayout.openDrawer(mainBinding.menu.drawer);
            isdrawer = true;
        } else if (view.getId() == R.id.btn_close) {
            drawerLayout.closeDrawers();
            isdrawer = false;
        } else if (view == mainBinding.btnInsertMemo) {
            Intent in = new Intent(MainActivity.this, InsertActivity.class);
            startActivityForResult(in, 0522);
        } else if (view == mainBinding.menu.btnReset) {
            alamreset.show();
        }

        settingToggleButton(view);
    }

    private void alamReset() {
        for (int i = 0; i < titlename.size(); i++) {
            adapters[i].remove(0);
        }
        titlename.clear();
        checkNoImage();
        RealmResults<Data_alam> realmDataBases = myRealm.where(Data_alam.class).findAll();
        myRealm.beginTransaction();
        realmDataBases.deleteAllFromRealm(); // 데이터베이스에 내용 전부 제거
        myRealm.commitTransaction();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if( event.getAction() == KeyEvent.ACTION_DOWN ){ //키 다운 액션 감지
            if( keyCode == KeyEvent.KEYCODE_BACK ){ //BackKey 다운일 경우만 처리
                Log.d("+++",String.valueOf(isdrawer));
                if(isdrawer) {
                    drawerLayout.closeDrawers();
                    isdrawer = false;
                    return false;
                }else{
                    finish();
                    return true;
                }
                 // 리턴이 true인 경우 기존 BackKey의 기본액션이 그대로 행해 지게 됩니다.
                // 리턴을 false로 할 경우 기존 BackKey의 기본액션이 진행 되지 않습니다.
                // 따라서 별도의 종료처리 혹은 다이얼로그 처리를 통한
                //BackKey기본액션을 구현 해주셔야 합니다.
            }
        }
        return super.onKeyDown( keyCode, event );
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (titlename.contains(data.getStringExtra("nName"))) { // 있다면
                for (int i = 0; i < titlename.size(); i++) {
                    if (titlename.get(i).equals(data.getStringExtra("nName"))) {
                        adapters[i].addItem(new RecyclerItem(data.getStringExtra("memo"), "B"));
                    }
                }
            } else { // 없다면
                titlename.add(data.getStringExtra("nName"));
                checkNoImage();
                for (int i = 0; i < titlename.size(); i++) {
                    try {
                        if (titlename.get(i).equals(data.getStringExtra("nName"))) {
                            adapters[i].addItem(new RecyclerItem(data.getIntExtra("nicon", -1), data.getStringExtra("nName"),color[i], "A"));
                            adapters[i].addItem(new RecyclerItem(data.getStringExtra("memo"), "B"));
                        }
                    } catch (NullPointerException e) {
                    }
                    Log.d(TAG, "NullPointerException");

                }

            }

        }
    }


    private void settingToggleButton(View view) {
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
