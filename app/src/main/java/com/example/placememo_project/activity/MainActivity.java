package com.example.placememo_project.activity;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;

import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;


import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import android.util.LruCache;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.placememo_project.dbData.Data_Icon;
import com.example.placememo_project.dbData.Data_Icon_firebase;
import com.example.placememo_project.dbData.Data_alam;
import com.example.placememo_project.dbData.Data_alam_firebase;
import com.example.placememo_project.dbData.Data_nomal;
import com.example.placememo_project.dbData.Data_nomal_firebase;
import com.example.placememo_project.touchcallback.ItemTouchHelperCallback;
import com.example.placememo_project.touchcallback.ItemTouchHelperCallback2;
import com.example.placememo_project.adapter.LocationAdapter;
import com.example.placememo_project.adapter.item.LocationMemo_item;
import com.example.placememo_project.R;
import com.example.placememo_project.adapter.RecyclerAdapter;
import com.example.placememo_project.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import io.realm.Realm;
import io.realm.RealmResults;
import static com.example.placememo_project.activity.LoginActivity.RC_SIGN_OUT;



public class MainActivity extends BaseActivity implements View.OnClickListener {
    ActivityMainBinding mainBinding;
    public static Context mainContext;
    private final static String TAG = "MainActivity======";
    private DrawerLayout drawerLayout;  //-- 옵션창 레이아웃
    private View drawView;
    private DatabaseReference mDatabase;
    private boolean isdrawer = false;
    static public String sort = "sort_update";
    static public ArrayList<String> titlename = new ArrayList<>();  //-- 등록된 알람이있는지 체크하기위한 변수( 메뉴용 )
    boolean isSuccess = true;
    public Realm myRealm;
    public AlertDialog alamreset, alamreset2;  //-- 설정창에서 모든 알람 초기화시 경고 메시지 용
    public ItemTouchHelperExtension mitemTouchHelper;
    public ItemTouchHelperExtension.Callback mCallback;
    public boolean checkAlam = false;
    public View locationView, nomalView;
    public TextView TextViewNoMemo, TextViewNoMemo_nomal;
    public RecyclerView recycleerView, recyclerView_nomal;
    public FragmentManager fragmentManager;
    private Fragment locaion;
    public LocationAdapter locationadapter;
    public RecyclerAdapter nomaladapters;
    public ItemTouchHelperExtension mItemTouchHelper_nomal;
    public ItemTouchHelperExtension.Callback mCallback_nomal;
    public Animation animOpen, animClose, animation3, animation4, animOpen2, animClose2;
    public Bitmap bitmap;
    private String user, UID;
    public String url, url2, goalPath;
    long backKeyPressedTime;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Realm.init(this);
        try {
            myRealm = Realm.getDefaultInstance();

        } catch (Exception e) {
            Log.d(TAG, "myRealm = null");
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        fragmentManager = getSupportFragmentManager();
        locaion = new LocationMemoActivity();
        fragmentManager.beginTransaction().replace(R.id.frame, locaion).commit();
        mainContext = this;
        locationView = getLayoutInflater().inflate(R.layout.location_framelatout, null, false);
        nomalView = getLayoutInflater().inflate(R.layout.nomal_framelayout, null, false);
        recycleerView = locationView.findViewById(R.id.recycleerView);
        recyclerView_nomal = nomalView.findViewById(R.id.nomal_recyclerview);
        TextViewNoMemo = locationView.findViewById(R.id.TextView_no_memo);
        TextViewNoMemo_nomal = nomalView.findViewById(R.id.TextView_no_memo);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainBinding.menu.googleImage.setBackground(new ShapeDrawable(new OvalShape()));
        mainBinding.menu.googleImage.setClipToOutline(true);
        Intent intent = getIntent();
        user = intent.getStringExtra("user");
        UID = intent.getStringExtra("UID");
        if (user.equals("google")) {
            mainBinding.menu.googleId.setText(intent.getStringExtra("name"));
            mainBinding.menu.googleEmail.setText(intent.getStringExtra("email"));
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Intent intent1 = getIntent();
                        URL url = new URL(intent1.getStringExtra("photo"));
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true);
                        conn.connect();
                        InputStream is = conn.getInputStream();
                        bitmap = BitmapFactory.decodeStream(is);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
            try {
                thread.join();
                mainBinding.menu.googleImage.setImageBitmap(bitmap);
            } catch (Exception e) {
            }


        } else if (user.equals("guest")) {
            mainBinding.menu.googleLogout.setText("로그인");
            mainBinding.menu.googleLogout.setBackgroundColor(0xff43BD57);
        }



        /*------------------------------------------------------------*/
        mainBinding.btnSetting.setOnClickListener(this);
        mainBinding.menu.sortName.setOnClickListener(this);
        mainBinding.menu.sortUpdate.setOnClickListener(this);
        mainBinding.menu.sortAlams.setOnClickListener(this);
        mainBinding.menu.btnReset.setOnClickListener(this);
        mainBinding.locationTab.setOnClickListener(this);
        mainBinding.nomalTab.setOnClickListener(this);
        mainBinding.expandButton.setOnClickListener(this);
        mainBinding.hideMenu.setOnClickListener(this);
        mainBinding.hideMenu2.setOnClickListener(this);
        mainBinding.menu.googleLogout.setOnClickListener(this);
        mainBinding.menu.btnBackUp.setOnClickListener(this);
        mainBinding.menu.btnLoad.setOnClickListener(this);
        mainBinding.kakaoButton.setOnClickListener(this);
        mainBinding.menu.btnShare.setOnClickListener(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawlayout);
        drawView = (View) findViewById(R.id.drawer);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(this);
        animOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate);
        animClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate2);
        animOpen2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_1);
        animClose2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate2_1);
        animation3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        animation4 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate2);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("잠시 기다려주세요...");
        progressDialog.setCancelable(true);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal);


        mCallback_nomal = new ItemTouchHelperCallback2(this);
        mItemTouchHelper_nomal = new ItemTouchHelperExtension(mCallback_nomal);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(), new LinearLayoutManager(this).getOrientation());
        recyclerView_nomal.addItemDecoration(dividerItemDecoration);
        nomaladapters = new RecyclerAdapter(this);
        recyclerView_nomal.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_nomal.setAdapter(nomaladapters);
        mItemTouchHelper_nomal.attachToRecyclerView(recyclerView_nomal);

        mCallback = new ItemTouchHelperCallback(this);
        mitemTouchHelper = new ItemTouchHelperExtension(mCallback);


        locationadapter = new LocationAdapter(this);
        recycleerView.setLayoutManager(new LinearLayoutManager(this));
        recycleerView.setAdapter(locationadapter);
        mitemTouchHelper.attachToRecyclerView(recycleerView);

        // 제목셋팅
        alertDialogBuilder.setTitle("위치 메모 초기화");
        alertDialogBuilder2.setTitle("일반 메모 초기화");

        // AlertDialog 셋팅
        alertDialogBuilder
                .setMessage("위치 메모를 초기화 하시겠습니까? \n (단, 등록된 알람위치는 지워지지 않습니다.)")
                .setCancelable(false)
                .setPositiveButton("초기화",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                // 초기화
                                locationAlamReset();

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

        alertDialogBuilder2
                .setMessage("일반 메모를 초기화 하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("초기화",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                // 초기화
                                nomalMemoReset();

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
        alamreset2 = alertDialogBuilder2.create();


        dataUpdate();   //-- DB에 정보 가져오기
        checkNoImage();   //-- 처음에 저장된 메모가 있는지 없는지 여부에 따라 메모 없다고 표시

//        getHashKey();
    }

    private void getHashKey() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null)
            Log.e("KeyHash", "KeyHash:null");

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        myRealm.close();
    }

    private void dataUpdate() {   //-- DB에 있는 정보 가져오기
        ShowAlamUi(sort);
    }


    public void checkNoImage() {  //-- 등록된 알람이 없는지 체크
        if (titlename.size() == 0) {
            checkAlam = false;
            TextViewNoMemo.setVisibility(View.VISIBLE);
            if (sender != null) {
                am.cancel(sender);
                sender = null;
            }
        } else {
            checkAlam = true;
            TextViewNoMemo.setVisibility(View.GONE);
            if (sender == null ) {
                locationSerch(this);
                Log.d("==","1번째");
            }//-- 내위치 검색 알람매니저 실행
        }
    }


    public void checkNoImage_nomal() {
        RealmResults<Data_nomal> results = myRealm.where(Data_nomal.class).findAll();
        if (results.size() == 0) {
            TextViewNoMemo_nomal.setVisibility(View.VISIBLE);
        } else {
            TextViewNoMemo_nomal.setVisibility(View.GONE);
        }
    }

    public void startEdit(String memo, View view, String type) {
        view.setTranslationX(0f);
        Intent intent = new Intent(this, EditMemoActivity.class);
        if (type.equals("memo")) {
            intent.putExtra("memo", memo);
            intent.putExtra("type", "memo");
        } else if (type.equals("title")) {
            intent.putExtra("memo", memo);
            intent.putExtra("type", "title");
        }
        startActivity(intent);
    }

    public void startNomalEdit(String memo, View view, int position) {
        view.setTranslationX(0f);
        Intent intent = new Intent(this, EditNomalMemoActivity.class);
        intent.putExtra("memo", memo);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    public void startTitleAddItem(String name) {
        Intent intent = new Intent(this, TitleAddItemActivity.class);
        intent.putExtra("titlename", name);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mainBinding.hideMenu.getVisibility() == View.VISIBLE) {
            mainBinding.expandButton.startAnimation(animation4);
            mainBinding.hideMenu.startAnimation(animClose);
            mainBinding.hideMenu2.startAnimation(animClose2);
            mainBinding.hideMenu.setVisibility(View.GONE);
            mainBinding.hideMenu2.setVisibility(View.GONE);
        }
    }

    private void locationAlamReset() {  //-- 알람 리셋을 누른다면
        titlename.clear();
        RealmResults<Data_alam> data_alams = myRealm.where(Data_alam.class).findAll();
        myRealm.beginTransaction();
        data_alams.deleteAllFromRealm();
        myRealm.commitTransaction();
        locationadapter.clear();
        checkNoImage();  //-- 저장된 알람 없다는것을 체크하여 No Memo 이미지를 띄우고
    }

    private void nomalMemoReset() {  //-- 알람 리셋을 누른다면
        nomaladapters.clear();
        RealmResults<Data_nomal> results = myRealm.where(Data_nomal.class).findAll();
        myRealm.beginTransaction();
        results.deleteAllFromRealm();
        myRealm.commitTransaction();
        checkNoImage_nomal();
    }


    @Override
    public void onBackPressed() {  //-- 옵션창이 켜져있는상태에서 사용자가 백키를 눌렀을때
        if (isdrawer) {
            drawerLayout.closeDrawers();
            isdrawer = false;
            return;
        } else {

            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis();
                Toast.makeText(this, "두번 눌러 앱 종료", Toast.LENGTH_SHORT).show();
            }
            //2번째 백버튼 클릭 (종료)
            else {
                finishAffinity();
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { //--  사용자가 메모를 추가가 성공적이었다면
            ShowAlamUi(sort);
            locationSerch(this);
            Log.d("==","2번째");
        }
    }


    public void ShowAlamUi(String sort) {
        int count = 0;
        boolean isSmame = true;
        locationadapter.clear();
        titlename.clear();
        if (sort.equals("sort_name")) {
            RealmResults<Data_alam> results = myRealm.where(Data_alam.class).findAll().sort("name");
            for (Data_alam data_alam : results) {
                if (!titlename.contains(data_alam.getName())) {
                    titlename.add(data_alam.getName());
                }
            }


        } else if (sort.equals("sort_update")) {
            RealmResults<Data_alam> results = myRealm.where(Data_alam.class).findAll();
            for (Data_alam data_alam : results) {
                if (!titlename.contains(data_alam.getName())) {
                    titlename.add(data_alam.getName());
                }
            }
        } else if (sort.equals("sort_alams")) {
            RealmResults<Data_alam> results = myRealm.where(Data_alam.class).findAll().sort("name");
//            RealmResults<Data_alam> data_alam2 = results;
            for (Data_alam data_alam : results) {
                if (!titlename.contains(data_alam.getName())) {
                    titlename.add(data_alam.getName());
                }
            }
            for (int i = 0; i < titlename.size(); i++) {
                if (i == 0) {
                    RealmResults<Data_alam> data_alams = myRealm.where(Data_alam.class).equalTo("name", titlename.get(i)).findAll();
                    count = data_alams.size();
                } else {
                    RealmResults<Data_alam> data_alams = myRealm.where(Data_alam.class).equalTo("name", titlename.get(i)).findAll();
                    if (count != data_alams.size()) isSmame = false;
                }
            }
            if (!isSmame) {
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.addAll(titlename);
                titlename.clear();
                for (int j = 0; j < arrayList.size(); j++) {
                    int check = 0;
                    for (int i = 0; i < arrayList.size(); i++) {
                        RealmResults<Data_alam> results2 = myRealm.where(Data_alam.class).equalTo("name", arrayList.get(i)).findAll();
                        if (check < results2.size()) {
                            if (!titlename.contains(results2.first().getName())) {
                                titlename.add(j, results2.first().getName());
                                check = results2.size();
                            }
                        }
                    }
                }
            }

        }
        if (titlename.size() != 0) {
            for (int i = 0; i < titlename.size(); i++) {
                RealmResults<Data_alam> results2 = myRealm.where(Data_alam.class).equalTo("name", titlename.get(i)).findAll();
                locationadapter.addItem(new LocationMemo_item(results2.first().getName(), results2.first().getColor(), "Pin"));
                locationadapter.addItem(new LocationMemo_item(results2.first().getIcon(), results2.first().getColor(), results2.first().getName(), "Title"));
                for (Data_alam data_alam : results2) {
                    locationadapter.addItem(new LocationMemo_item(data_alam.getName(), data_alam.getMemo(), "Memo"));
                }

            }
        }
        checkNoImage();
    }


    /*------------------------------------------------------------------------------------------------------------------------------------------*/
    /*------------------------------------------------------------------------------------------------------------------------------------------*/
    /*------------------------------------------------------------------------------------------------------------------------------------------*/


    @Override
    public void onClick(View view) {
        if (view == mainBinding.btnSetting) {  //-- 옵션을 클릭한다면
            mainBinding.drawlayout.openDrawer(mainBinding.menu.drawer);
            isdrawer = true;  //-- 드로어가 열린것으로 변경
        } else if (view == mainBinding.hideMenu) {  //-- 메모추가를 누른다면
            Intent in = new Intent(MainActivity.this, InsertActivity.class);
            startActivityForResult(in, 0522);  //-- 메모추가 액티비티로 이동
        } else if (view == mainBinding.menu.btnReset) {
            if (mainBinding.locationTab.getAlpha() == 1.0f) {
                alamreset.show();  //-- 모든 알람 초기화를 누른다면 알람리셋 팝업창 보여주기
            } else {
                alamreset2.show();
            }
        } else if (view == mainBinding.locationTab) {
            mainBinding.kakaoButton.setText("   위치메모\n    공유하기 ");
            mainBinding.menu.btnReset.setText("   위치 메모 초기화");
            mainBinding.locationTab.setAlpha(1.0f);
            mainBinding.nomalTab.setAlpha(0.3f);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            LocationMemoActivity location_memo_activity = new LocationMemoActivity();
            transaction.replace(R.id.frame, location_memo_activity);
            transaction.commit();
            ShowAlamUi(sort);
        } else if (view == mainBinding.nomalTab) {
            mainBinding.kakaoButton.setText("   일반메모\n    공유하기 ");
            mainBinding.menu.btnReset.setText("   일반 메모 초기화");
            mainBinding.locationTab.setAlpha(0.3f);
            mainBinding.nomalTab.setAlpha(1.0f);
            nomaladapters.clear();
            RealmResults<Data_nomal> results = myRealm.where(Data_nomal.class).findAll().sort("order");
            for (Data_nomal data_nomals : results) {
                nomaladapters.addItem(data_nomals.getMemo(), data_nomals.getColor());
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            NomalMemoActivity nomal_memo_activity = new NomalMemoActivity();
            transaction.replace(R.id.frame, nomal_memo_activity);
            transaction.commit();
            checkNoImage_nomal();
        } else if (view == mainBinding.hideMenu2) {
            Intent intent = new Intent(this, NomalMemoInsetActivity.class);
            startActivity(intent);
        } else if (view == mainBinding.expandButton) {
            if (mainBinding.hideMenu.getVisibility() == View.VISIBLE) {
                mainBinding.expandButton.startAnimation(animation4);
                mainBinding.hideMenu.startAnimation(animClose);
                mainBinding.hideMenu2.startAnimation(animClose2);
                mainBinding.hideMenu.setVisibility(View.GONE);
                mainBinding.hideMenu2.setVisibility(View.GONE);
            } else {
                mainBinding.expandButton.startAnimation(animation3);
                mainBinding.hideMenu.startAnimation(animOpen);
                mainBinding.hideMenu2.startAnimation(animOpen2);
                mainBinding.hideMenu.setVisibility(View.VISIBLE);
                mainBinding.hideMenu2.setVisibility(View.VISIBLE);
            }
        } else if (view == mainBinding.menu.googleLogout) {
            if (user.equals("google")) {
                Intent intent = new Intent();
                setResult(RC_SIGN_OUT, intent);
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            } else if (user.equals("guest")) {
                Intent intent = new Intent();
                setResult(RC_SIGN_OUT, intent);
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        } else if (view == mainBinding.menu.btnShare) {

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=net.daum.android.daum");
            intent.setPackage("com.kakao.talk");
            startActivity(intent);

//            TextTemplate params = TextTemplate.newBuilder("다다다", LinkObject.newBuilder().setWebUrl("https://developers.kakao.com").setMobileWebUrl("https://developers.kakao.com").build()).build();
//
//            Map<String, String> serverCallbackArgs = new HashMap<String, String>();
//            serverCallbackArgs.put("user_id", "${current_user_id}");
//            serverCallbackArgs.put("product_id", "${shared_product_id}");
//
//            KakaoLinkService.getInstance().sendDefault(this, params, serverCallbackArgs, new ResponseCallback<KakaoLinkResponse>() {
//                @Override
//                public void onFailure(ErrorResult errorResult) {
//                    Logger.e(errorResult.toString());
//                }
//
//                @Override
//                public void onSuccess(KakaoLinkResponse result) {
//                    // 템플릿 밸리데이션과 쿼터 체크가 성공적으로 끝남. 톡에서 정상적으로 보내졌는지 보장은 할 수 없다. 전송 성공 유무는 서버콜백 기능을 이용하여야 한다.
//                }
//            });

        } else if (view == mainBinding.kakaoButton) {

            checkPermissions();

        } else if (view == mainBinding.menu.btnBackUp) {
            if (user.equals("google")) {

                progressDialog.show();
                mDatabase.child(UID).removeValue();

                RealmResults<Data_alam> data_alams = myRealm.where(Data_alam.class).findAll();
                RealmResults<Data_nomal> data_nomals = myRealm.where(Data_nomal.class).findAll();
                RealmResults<Data_Icon> data_icons = myRealm.where(Data_Icon.class).findAll();

                for (Data_alam data_alam : data_alams) {            // 위치 알람 메모
                    Data_alam_firebase dataAlamFirebase = new Data_alam_firebase();
                    dataAlamFirebase.setAlamOn(data_alam.getisAlamOn());
                    dataAlamFirebase.setColor(data_alam.getColor());
                    dataAlamFirebase.setIcon(data_alam.getIcon());
                    dataAlamFirebase.setLatitude(data_alam.getLatitude());
                    dataAlamFirebase.setLongitude(data_alam.getLongitude());
                    dataAlamFirebase.setMemo(data_alam.getMemo());
                    dataAlamFirebase.setName(data_alam.getName());
                    mDatabase.child(UID).child("Location_Memo").child(EncodeString(data_alam.getMemo())).setValue(dataAlamFirebase).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Data_alam", "백업 성공");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            isSuccess = false;
                            Log.d("Data_alam", "백업 실패");
                        }
                    });
                }
                for (Data_nomal data_nomal : data_nomals) {            // 일반 메모
                    Data_nomal_firebase dataNomalFirebase = new Data_nomal_firebase();
                    dataNomalFirebase.setColor(data_nomal.getColor());
                    dataNomalFirebase.setFrag(data_nomal.getFrag());
                    dataNomalFirebase.setMemo(EncodeString(data_nomal.getMemo()));
                    dataNomalFirebase.setOrder(data_nomal.getOrder());
                    mDatabase.child(UID).child("Nomal_Memo").child(String.valueOf(data_nomal.getOrder())).setValue(dataNomalFirebase).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Data_nomal", "백업 성공");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            isSuccess = false;
                            Log.d("Data_nomal", "백업 실패");
                        }
                    });
                }
                for (Data_Icon data_icon : data_icons) {            // 위치 아이콘
                    Data_Icon_firebase dataIconFirebase = new Data_Icon_firebase();
                    dataIconFirebase.setButton(data_icon.getButton());
                    dataIconFirebase.setButtonclick(data_icon.getButtonclick());
                    dataIconFirebase.setLatitude(data_icon.getLatitude());
                    dataIconFirebase.setLongitude(data_icon.getLongitude());
                    dataIconFirebase.setName(data_icon.getName());
                    mDatabase.child(UID).child("Location_Icon").child(EncodeString(data_icon.getName())).setValue(dataIconFirebase).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Data_Icon", "백업 성공");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            isSuccess = false;
                            Log.d("Data_Icon", "백업 실패");
                        }
                    });
                }
                progressDialog.dismiss();
                if (isSuccess) {
                    Toast.makeText(mainContext, "백업 완료.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mainContext, "백업 실패.", Toast.LENGTH_LONG).show();
                    isSuccess = true;
                }
            } else if (user.equals("guest")) {
                Toast.makeText(mainContext, "백업 기능은 로그인 후 이용하실수 있습니다.", Toast.LENGTH_LONG).show();
            }
        } else if (view == mainBinding.menu.btnLoad) {
            if (user.equals("google")) {
                progressDialog.show();
                mDatabase.child(UID).child("Location_Icon").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        RealmResults<Data_Icon> data_icons = myRealm.where(Data_Icon.class).findAll();
                        myRealm.beginTransaction();
                        data_icons.deleteAllFromRealm();
                        myRealm.commitTransaction();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Data_Icon_firebase dataIconFirebase = postSnapshot.getValue(Data_Icon_firebase.class);
                            myRealm.beginTransaction();
                            Data_Icon data_icon = myRealm.createObject(Data_Icon.class);
                            data_icon.setButton(dataIconFirebase.getButton());
                            data_icon.setButtonclick(dataIconFirebase.getButtonclick());
                            data_icon.setLatitude(dataIconFirebase.getLatitude());
                            data_icon.setLongitude(dataIconFirebase.getLongitude());
                            data_icon.setName(DecodeString(dataIconFirebase.getName()));
                            myRealm.commitTransaction();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        isSuccess = false;
                    }
                });
                mDatabase.child(UID).child("Nomal_Memo").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        RealmResults<Data_nomal> data_nomals = myRealm.where(Data_nomal.class).findAll();
                        myRealm.beginTransaction();
                        data_nomals.deleteAllFromRealm();
                        myRealm.commitTransaction();
                        int i = 0;
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Data_nomal_firebase dataNomalFirebase = postSnapshot.getValue(Data_nomal_firebase.class);
                            myRealm.beginTransaction();
                            Data_nomal data_nomal = myRealm.createObject(Data_nomal.class);
                            data_nomal.setOrder(i);
                            data_nomal.setColor(dataNomalFirebase.getColor());
                            data_nomal.setFrag(dataNomalFirebase.getFrag());
                            data_nomal.setMemo(DecodeString(dataNomalFirebase.getMemo()));
                            myRealm.commitTransaction();
                            i++;
                        }
                        nomaladapters.clear();
                        RealmResults<Data_nomal> results = myRealm.where(Data_nomal.class).findAll().sort("order");
                        for (Data_nomal data_nomal : results) {
                            nomaladapters.addItem(data_nomal.getMemo(), data_nomal.getColor());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        isSuccess = false;

                    }
                });


                mDatabase.child(UID).child("Location_Memo").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        RealmResults<Data_alam> data_alams = myRealm.where(Data_alam.class).findAll();
                        myRealm.beginTransaction();
                        data_alams.deleteAllFromRealm();
                        myRealm.commitTransaction();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Data_alam_firebase dataAlamFirebase = postSnapshot.getValue(Data_alam_firebase.class);
//                                Log.d("==",dataAlamFirebase.getName());
                            myRealm.beginTransaction();
                            Data_alam dataalam = myRealm.createObject(Data_alam.class);
                            dataalam.setName(dataAlamFirebase.getName());
                            dataalam.setMemo(DecodeString(dataAlamFirebase.getMemo()));
                            dataalam.setIcon(dataAlamFirebase.getIcon());
                            dataalam.setLatitude(dataAlamFirebase.getLatitude());
                            dataalam.setLongitude(dataAlamFirebase.getLongitude());
                            dataalam.setColor(dataAlamFirebase.getColor());
                            dataalam.setAlamOn(dataAlamFirebase.getisAlamOn());
                            myRealm.commitTransaction();
                            ShowAlamUi(sort);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        isSuccess = false;
                    }
                });
                progressDialog.dismiss();
                if (isSuccess) {
                    Toast.makeText(mainContext, "복원 완료.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mainContext, "복원 실패.", Toast.LENGTH_SHORT).show();
                }
            } else if (user.equals("guest")) {
                Toast.makeText(mainContext, "백업 기능은 로그인 후 이용하실수 있습니다.", Toast.LENGTH_LONG).show();
            }
        }

        settingToggleButton(view);  //-- 옵션창에 버튼설정
    }

    private void Kakao() {
        if (mainBinding.locationTab.getAlpha() == 1.0f) {
            if (getScreenshotFromRecyclerView(recycleerView) != null) {
                saveToInternalStorage(getScreenshotFromRecyclerView(recycleerView));
                File imageFile = new File(goalPath);
                Uri uri = FileProvider.getUriForFile(this, getPackageName(), imageFile);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.setPackage("com.kakao.talk");
                startActivity(intent);
            } else
                Toast.makeText(getApplicationContext(), "전송할 메모가 없습니다.", Toast.LENGTH_SHORT).show();
        } else {
            if (getScreenshotFromRecyclerView(recyclerView_nomal) != null) {
                saveToInternalStorage(getScreenshotFromRecyclerView(recyclerView_nomal));
                File imageFile = new File(goalPath);
                Uri uri = FileProvider.getUriForFile(this, getPackageName(), imageFile);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.setPackage("com.kakao.talk");
                startActivity(intent);
            } else
                Toast.makeText(getApplicationContext(), "전송할 메모가 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }


    /*------------------------------------------------------------------------------------------------------------------------------------------*/
    /*------------------------------------------------------------------------------------------------------------------------------------------*/
    /*--------------------------------------------------------------------------------------------------------------*/
    private void settingToggleButton(View view) { // seletor Item
        if (view.getId() == R.id.sort_name) {
            sort = "sort_name";
            mainBinding.menu.sortAlams.setTextColor(Color.rgb(0, 0, 0));
            mainBinding.menu.sortUpdate.setTextColor(Color.rgb(0, 0, 0));
            mainBinding.menu.sortName.setTextColor(Color.rgb(70, 160, 220));
            ShowAlamUi(sort);
        } else if (view.getId() == R.id.sort_alams) {
            sort = "sort_alams";
            mainBinding.menu.sortAlams.setTextColor(Color.rgb(70, 160, 220));
            mainBinding.menu.sortUpdate.setTextColor(Color.rgb(0, 0, 0));
            mainBinding.menu.sortName.setTextColor(Color.rgb(0, 0, 0));
            ShowAlamUi(sort);
        } else if (view.getId() == R.id.sort_update) {
            sort = "sort_update";
            mainBinding.menu.sortAlams.setTextColor(Color.rgb(0, 0, 0));
            mainBinding.menu.sortUpdate.setTextColor(Color.rgb(70, 160, 220));
            mainBinding.menu.sortName.setTextColor(Color.rgb(0, 0, 0));
            ShowAlamUi(sort);
        }
    }

    public Bitmap getScreenshotFromRecyclerView(RecyclerView view) {
        RecyclerView.Adapter adapter = view.getAdapter();
        Bitmap bigBitmap = null;
        if (adapter != null && adapter.getItemCount() != 0) {
            int size = adapter.getItemCount();
            int height = 0;
            Paint paint = new Paint();
            int iHeight = 0;
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

            // Use 1/8th of the available memory for this memory cache.
            final int cacheSize = maxMemory / 8;
            LruCache<String, Bitmap> bitmaCache = new LruCache<>(cacheSize);
            for (int i = 0; i < size; i++) {
                RecyclerView.ViewHolder holder = adapter.createViewHolder(view, adapter.getItemViewType(i));
                adapter.onBindViewHolder(holder, i);
                holder.itemView.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(), holder.itemView.getMeasuredHeight());
                holder.itemView.setDrawingCacheEnabled(true);
                holder.itemView.buildDrawingCache();
                Bitmap drawingCache = holder.itemView.getDrawingCache();
                if (drawingCache != null) {

                    bitmaCache.put(String.valueOf(i), drawingCache);
                }
                height += holder.itemView.getMeasuredHeight();
            }

            bigBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), height, Bitmap.Config.ARGB_8888);
            Canvas bigCanvas = new Canvas(bigBitmap);
            bigCanvas.drawColor(0xFF9E9B9B);

            for (int i = 0; i < size; i++) {
                Bitmap bitmap = bitmaCache.get(String.valueOf(i));
                bigCanvas.drawBitmap(bitmap, 0f, iHeight, paint);
                iHeight += bitmap.getHeight();
                bitmap.recycle();
            }


        }
        return bigBitmap;
    }


//    public String returnImage(String path){
//        File imageFile = new File(path);
//
//        KakaoLinkService.getInstance().uploadImage(this, false, imageFile, new ResponseCallback<ImageUploadResponse>() {
//            @Override
//            public void onFailure(ErrorResult errorResult) {
//                Logger.e(errorResult.toString());
//            }
//
//            @Override
//            public void onSuccess(ImageUploadResponse result) {
//               url = result.getOriginal().getUrl();
//
//            }
//        });
//        return url;
//    }
//
//
//    public String returnImage2(String path){
//        KakaoLinkService.getInstance().scrapImage(this, false, path, new ResponseCallback<ImageUploadResponse>() {
//            @Override
//            public void onFailure(ErrorResult errorResult) {
//                Logger.e(errorResult.toString());
//            }
//
//            @Override
//            public void onSuccess(ImageUploadResponse result) {
//                Logger.d(result.getOriginal().getUrl());
//                url2 = result.getOriginal().getUrl();
//            }
//        });
//        return url2;
//    }


    private void saveToInternalStorage(Bitmap bitmapImage) {
        String ex_storage = Environment.getExternalStorageDirectory().getAbsolutePath();
        String foler_name = "/" + "immmmmage" + "/";
        String file_name = "KaKao_Image" + ".jpg";
        String string_path = ex_storage + foler_name;

        File file_path;
        try {
            file_path = new File(string_path);
            if (!file_path.isDirectory()) {
                file_path.mkdirs();
            }
            goalPath = string_path + file_name;
            FileOutputStream out = new FileOutputStream(string_path + file_name);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
            Toast.makeText(mainContext, "저장완료", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mainContext, "에러 잼" + e, Toast.LENGTH_SHORT).show();
        }
    }


    private String EncodeString(String string) {
        String encodingString = string.replace(".", "-001");
        String encodingString2 = encodingString.replace("#", "-002");
        String encodingString3 = encodingString2.replace("$", "-003");
        String encodingString4 = encodingString3.replace("[", "-004");
        String encodingString5 = encodingString4.replace("]", "-005");
        return encodingString5;
    }

    private String DecodeString(String string) {
        String encodingString = string.replace("-001", ".");
        String encodingString2 = encodingString.replace("-002", "#");
        String encodingString3 = encodingString2.replace("-003", "$");
        String encodingString4 = encodingString3.replace("-004", "[");
        String encodingString5 = encodingString4.replace("-005", "]");
        return encodingString5;
    }

    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Kakao();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(getApplicationContext(), "권한 거부시 서비스이용이 제한됩니다.", Toast.LENGTH_SHORT).show();
        }
    };

    public void checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) { // 마시멜로(안드로이드 6.0) 이상 권한 체크
            TedPermission.with(this)
                    .setPermissionListener(permissionListener)
                    .setRationaleMessage("카카오톡 메모사진 공유를 위해서는 권한이 필요합니다")
                    .setDeniedMessage("앱에서 요구하는 권한설정이 필요합니다...\n [설정] > [권한] 에서 사용으로 활성화해주세요.")
                    .setPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE})
                    .check();

        } else {
            Kakao();
        }
    }


}