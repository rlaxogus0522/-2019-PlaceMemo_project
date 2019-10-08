package com.example.placememo_project;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


import com.example.placememo_project.databinding.ActivityInsertmemoBinding;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class InsertActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    private final static String TAG = "InsertActivity-------";
    private boolean editMode = false;
    private ArrayList<Integer> locationButton = new ArrayList<>(); // -- 클릭 되기 전 버튼 이미지
    private ArrayList<Integer> locationButtonClick = new ArrayList<>(); // -- 클릭 된 이후 버튼 이미지
    private ImageButton btnlocation[] = new ImageButton[5]; // -- 이미지버튼의 객체를 배열형태로 담아둠
    private ImageButton btndelete[] = new ImageButton[5];
    private ArrayList<Double> latitude = new ArrayList<>();
    private ArrayList<Double> longitutde = new ArrayList<>();
    static ArrayList<String> locationName = new ArrayList<>();
    private Vibrator vibrator;
    private int nicon;
    private String nName;
    private double nlat, nlong;
    ActivityInsertmemoBinding imbinding;
    private boolean checkLocation = false;
    Realm myRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imbinding = DataBindingUtil.setContentView(this, R.layout.activity_insertmemo);
        Realm.init(this);

        imbinding.EditMemo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    imbinding.EditMemo.setText("");
                }
            }
        });


        try {
            myRealm = Realm.getDefaultInstance();

        } catch (Exception e) {
            Log.d(TAG, "myRealm = null");
        }
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        imbinding.btnSave.setOnClickListener(this);
        imbinding.btnAddlocation.setOnClickListener(this);
        imbinding.btnBack.setOnClickListener(this);
        imbinding.insertLayout.setOnClickListener(this);
        btnlocation[0] = imbinding.btnLocation1;
        btnlocation[1] = imbinding.btnLocation2;
        btnlocation[2] = imbinding.btnLocation3;
        btnlocation[3] = imbinding.btnLocation4;
        btnlocation[4] = imbinding.btnLocation5;


        btndelete[0] = imbinding.btnLocation1Delete;
        btndelete[1] = imbinding.btnLocation2Delete;
        btndelete[2] = imbinding.btnLocation3Delete;
        btndelete[3] = imbinding.btnLocation4Delete;
        btndelete[4] = imbinding.btnLocation5Delete;

        dataUpdate();
        refreshButtonImage();
        for (int i = 0; i < locationName.size(); i++) {
            Log.d(TAG, locationName.get(i));
        }
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(editMode) {
            Rect diaalogBounds = new Rect();
            getWindow().getDecorView().getHitRect(diaalogBounds);
            if (!diaalogBounds.contains((int) ev.getX(), (int) ev.getY())) {
                for (int i = 0; i < locationButton.size(); i++) {
                    btndelete[i].setAlpha((float) 0.0);
                    btnlocation[i].setAlpha((float) 1.0);
                    editMode = false;
                }
                return false;
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    private void dataUpdate() {
        try {
            RealmResults<Data_Icon> results = myRealm.where(Data_Icon.class).findAll();
            for (Data_Icon data_icon : results) {
                locationButton.add(data_icon.getButton());
                locationButtonClick.add(data_icon.getButtonclick());
                if(!locationName.contains(data_icon.getName())){
                locationName.add(data_icon.getName());}
                latitude.add(data_icon.getLatitude());
                longitutde.add(data_icon.getLongitude());
                Log.d("////", String.valueOf(locationButton));

            }
        } catch (NullPointerException e) {
            Log.d(TAG, String.valueOf(e));
        }
    }

    private void dataBackup() {
        try {
            for (int i = 0; i < locationButton.size(); i++) {
                myRealm.beginTransaction();
                Data_Icon dataIcon = myRealm.createObject(Data_Icon.class);
                dataIcon.setButton(locationButton.get(i));
                dataIcon.setButtonclick(locationButtonClick.get(i));
                dataIcon.setName(locationName.get(i));
                dataIcon.setLatitude(latitude.get(i));
                dataIcon.setLongitude(longitutde.get(i));
                myRealm.commitTransaction();
            }
        } catch (NullPointerException e) {
            Log.d(TAG, "NullPointerException" + locationButton);
            Log.d(TAG, "NullPointerException" + locationButtonClick);
        }

    }

    private void refreshButtonImage() {
        for (int i = 0; i < locationButton.size(); i++) {
            btnlocation[i].setOnClickListener(this);
            btnlocation[i].setOnLongClickListener(this);
            btndelete[i].setOnClickListener(this);
            btnlocation[i].setBackgroundResource(locationButton.get(i)); // -- 할때마다 새로 리스너를 달아주는 문제점 추후 수정
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RealmResults<Data_Icon> realmDataBases = myRealm.where(Data_Icon.class).findAll();
        myRealm.beginTransaction();
        realmDataBases.deleteAllFromRealm(); // 데이터베이스에 내용 전부 제거
        myRealm.commitTransaction();
        dataBackup();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLocation = false;
    }

    @Override
    public void onClick(View view) {
        if (!editMode) {
            if (view == imbinding.btnAddlocation) { // 위치 추가
                if (locationButton.size() < 5) {
                    Intent in = new Intent(InsertActivity.this, LocationActivity.class);
                    startActivityForResult(in, 0522);
                } else {
                    Toast.makeText(this, "위치는 최대 5개까지 등록 가능합니다.", Toast.LENGTH_LONG).show();
                }
            } else if (view == imbinding.btnSave) { // 최종 저장
                if (checkLocation && !imbinding.EditMemo.getText().toString().equals("")) {
                    try {
                        /*-----------------------------------------------------*/
                        myRealm.beginTransaction();
                        Data_alam dataalam = myRealm.createObject(Data_alam.class);
                        dataalam.setName(nName);
                        dataalam.setMemo(imbinding.EditMemo.getText().toString());
                        dataalam.setIcon(nicon);
                        dataalam.setLatitude(nlat);
                        dataalam.setLongitude(nlong);
                        myRealm.commitTransaction();
                        /*----------------------------------------------------*/
                        Intent intent = new Intent();
                        intent.putExtra("nicon", nicon);
                        intent.putExtra("nName", nName);
                        intent.putExtra("memo", imbinding.EditMemo.getText().toString());
                        setResult(RESULT_OK, intent);
                        Log.d("522 ","저장 누르자마자 : "+locationName);
                        finish();
                    } catch (NullPointerException e) {
                        Log.d(TAG, "NullPointerException");
                    }
                } else {
                    if (!checkLocation)
                        Toast.makeText(this, "알람을 받을 장소를 선택해주세요.", Toast.LENGTH_LONG).show();
                    if (imbinding.EditMemo.getText().toString().equals(""))
                        Toast.makeText(this, "메모를 설정해주세요.", Toast.LENGTH_LONG).show();
                }
            } else if (view == imbinding.btnBack) {
                finish();
            } else {
                imageChange(view);
            }

        } else {
            if (view == imbinding.insertLayout) {
                editMode = false;
                for (int i = 0; i < locationButton.size(); i++) {
                    btndelete[i].setAlpha((float) 0.0);
                    btnlocation[i].setAlpha((float) 1.0);
                }
            }else if(view == imbinding.btnAddlocation){
                editMode = false;
                for (int i = 0; i < locationButton.size(); i++) {
                    btndelete[i].setAlpha((float) 0.0);
                    btnlocation[i].setAlpha((float) 1.0);
                }
                if (locationButton.size() < 5) {
                    Intent in = new Intent(InsertActivity.this, LocationActivity.class);
                    startActivityForResult(in, 0522);
                } else {
                    Toast.makeText(this, "위치는 최대 5개까지 등록 가능합니다.", Toast.LENGTH_LONG).show();
                }
            }else if(view == imbinding.btnSave){
                editMode = false;
                for (int i = 0; i < locationButton.size(); i++) {
                    btndelete[i].setAlpha((float) 0.0);
                    btnlocation[i].setAlpha((float) 1.0);
                }

                if (checkLocation && !imbinding.EditMemo.getText().toString().equals("")) {
                    try {
                        myRealm.beginTransaction();
                        Data_alam dataalam = myRealm.createObject(Data_alam.class);
                        dataalam.setName(nName);
                        dataalam.setMemo(imbinding.EditMemo.getText().toString());
                        dataalam.setIcon(nicon);
                        dataalam.setLatitude(nlat);
                        dataalam.setLongitude(nlong);
                        myRealm.commitTransaction();
                        Intent intent = new Intent();
                        intent.putExtra("nicon", nicon);
                        intent.putExtra("nName", nName);
                        intent.putExtra("memo", imbinding.EditMemo.getText().toString());
                        setResult(RESULT_OK, intent);
                        finish();
                    } catch (NullPointerException e) {
                        Log.d(TAG, "NullPointerException");
                    }
                } else {
                    if (!checkLocation)
                        Toast.makeText(this, "알람을 받을 장소를 선택해주세요.", Toast.LENGTH_LONG).show();
                    if (imbinding.EditMemo.getText().toString().equals(""))
                        Toast.makeText(this, "메모를 설정해주세요.", Toast.LENGTH_LONG).show();
                }
            }
            else {
                for (int i = 0; i < locationButton.size(); i++) {
                    if (view == btnlocation[i]) {
                       //     RealmResults<Data_Icon> delete = myRealm.where(Data_Icon.class).equalTo("nicon",locationButton.get(i)).findAll();
                        //    myRealm.beginTransaction();
                        //    delete.deleteAllFromRealm();
                        //    myRealm.commitTransaction();

                        locationButton.remove(i);
                        locationButtonClick.remove(i);
                        locationName.remove(i);
                        btndelete[locationButton.size()].setAlpha((float) 0.0);
                        btnlocation[locationButton.size()].setAlpha((float) 1.0);
                        btnlocation[locationButton.size()].setBackgroundResource(android.R.color.transparent);
                        refreshButtonImage();
                    } else {

                    }
                }
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            locationButton.add(data.getIntExtra("icon", -1));
            locationButtonClick.add(data.getIntExtra("clickicon", -1));
            locationName.add(data.getStringExtra("name"));
            latitude.add(data.getDoubleExtra("latitude", -1));
            longitutde.add(data.getDoubleExtra("longitude", -1));

            refreshButtonImage();
            Log.d("522 ","onActivityResult : "+locationName);
        } else {
            Log.d(TAG, String.valueOf(resultCode));
        }
    }

    private void imageChange(View view) {
        for (int i = 0; i < locationButton.size(); i++) {
            btnlocation[i].setBackgroundResource(locationButton.get(i));
            if (btnlocation[i] == view) {
                btnlocation[i].setBackgroundResource(locationButtonClick.get(i));
                checkLocation = true;
                nicon = locationButton.get(i);
                nlat = latitude.get(i);
                nlong = longitutde.get(i);
                nName = locationName.get(i);
            }
            Log.d(TAG, String.valueOf(i));
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if (view == imbinding.btnAddlocation || view == imbinding.btnSave) {
            return true;
        } else {
            vibrator.vibrate(100);
            editMode = true;

            for (int i = 0; i < locationButton.size(); i++) {
                btndelete[i].setAlpha((float) 1.0);
                btnlocation[i].setAlpha((float) 0.4);
            }
            return true;

        }
    }
}
