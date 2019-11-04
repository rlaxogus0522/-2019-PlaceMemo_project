package com.example.placememo_project.activity;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.example.placememo_project.R;
import com.example.placememo_project.databinding.ActivityWidgetInsertmemoBinding;
import com.example.placememo_project.dbData.Data_Icon;
import com.example.placememo_project.dbData.Data_alam;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.example.placememo_project.activity.MainActivity.mainContext;


public class WidgetInsertActivity extends Activity implements View.OnClickListener {   //-- 메모를 추가하는 액티비티
    private final static String TAG = "InsertActivity-------";
    private ArrayList<Integer> locationButton = new ArrayList<>(); // -- 클릭 되기 전 버튼 이미지
    private ArrayList<Integer> locationButtonClick = new ArrayList<>(); // -- 클릭 된 이후 버튼 이미지
    private ImageButton btnlocation[] = new ImageButton[5]; // -- 이미지버튼의 객체를 배열형태로 담아둠
    private ArrayList<Double> latitude = new ArrayList<>();
    private ArrayList<Double> longitutde = new ArrayList<>();
    static ArrayList<String> locationName = new ArrayList<>();   //--  위치이름 저장
    private int nicon;   //--  현재 선택된 위치의 아이콘 저장용
    private String nName;   //--  현재 선택된 위치의 이름 저장용
    private double nlat, nlong;   //--  현재 선택된 위치의 위 경도 저장용
    ActivityWidgetInsertmemoBinding imbinding;
    private boolean isLocationCheck = false;   //-- 메모 추가시 알림 받을 위치를 선택했는지 체크용
    int colors[] = new int[]{0xFFE8EE9C, 0xFFE4B786, 0xFF97E486, 0xFF86E4D1, 0xFFE48694};  //-- 저장된 메모 메뉴에 표시할 색깔 등록해두기
    int setColor;
    int clickNum;
    Realm myRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imbinding = DataBindingUtil.setContentView(this, R.layout.activity_widget_insertmemo);
        Realm.init(this);
        imbinding.EditMemo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {   //--  메모입력창에 포커스를 받으면 메모를 입력해주세요라는 문구 삭제
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


        imbinding.btnSave.setOnClickListener(this);
        imbinding.btnBack.setOnClickListener(this);
        imbinding.insertLayout.setOnClickListener(this);

        btnlocation[0] = imbinding.btnLocation1;   //-- 위치 버튼
        btnlocation[1] = imbinding.btnLocation2;
        btnlocation[2] = imbinding.btnLocation3;
        btnlocation[3] = imbinding.btnLocation4;
        btnlocation[4] = imbinding.btnLocation5;




        dataUpdate();   //-- 데이터베이스에 저장된 사용자가 등록해둔 위치정보들 가져오기
        refreshButtonImage();   //--  이미지 설정 초기화
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {   //-- 팝업으로 뜨는 메모추가 액티비티 외 구역을 터치했을때
            Rect diaalogBounds = new Rect();
            getWindow().getDecorView().getHitRect(diaalogBounds);
            if (!diaalogBounds.contains((int) ev.getX(), (int) ev.getY())) {

                return false;
            }
        return super.dispatchTouchEvent(ev);
    }

    private void dataUpdate() {   //-- 저장되어있는 사용자가 원하는 알림위치에 대한 정보 가져오기
        locationName.clear();
        try {
            RealmResults<Data_Icon> results = myRealm.where(Data_Icon.class).findAll();
            for (Data_Icon data_icon : results) {
                locationButton.add(data_icon.getButton());   //-- 클릭전 이미지
                locationButtonClick.add(data_icon.getButtonclick());   //--  클릭후 이미지
                if (!locationName.contains(data_icon.getName())) {   //-- 만약 위치이름이 리스트에 없다면
                    locationName.add(data_icon.getName());
                }   //--  데이터베이스에서 가져온 위치를 리스트에 추가
                latitude.add(data_icon.getLatitude());   //-- 데이터베이스에서 위도가져와서 추가
                longitutde.add(data_icon.getLongitude());   //--  데이터베이스에서 경도가져와서  추가

            }
        } catch (NullPointerException e) {
            Log.d(TAG, String.valueOf(e));
        }
    }

    private void dataBackup() {   //--  사용자가 입력한 데이터 저장
        try {
            for (int i = 0; i < locationButton.size(); i++) {   //-- 등록되어있는 버튼의 갯수만큼 반복
                myRealm.beginTransaction();
                Data_Icon dataIcon = myRealm.createObject(Data_Icon.class);
                dataIcon.setButton(locationButton.get(i));   //-- 클릭전 이미지
                dataIcon.setButtonclick(locationButtonClick.get(i));   //--  클릭후 이미지
                dataIcon.setName(locationName.get(i));   //-- 위치이름
                dataIcon.setLatitude(latitude.get(i));   //-- 위도
                dataIcon.setLongitude(longitutde.get(i));   //-- 경도
                myRealm.commitTransaction();
            }
        } catch (NullPointerException e) {
            Log.d(TAG, "NullPointerException");
        }

    }

    private void refreshButtonImage() {   //-- 이미지 초기화
        for (int i = 0; i < locationButton.size(); i++) {   //-- 등록되어있는 버튼의 갯수만큼 반복
            btnlocation[i].setOnClickListener(this);   //--  추가된 버튼에 리스너 등록
            btnlocation[i].setBackgroundResource(locationButton.get(i)); // -- Refresh 할때마다 새로 리스너를 달아주는 문제점발견 -  (추후 수정)
        }
    }

    boolean CheckDBinMemo(String title){
        boolean isAlready= false;
            RealmResults<Data_alam> Data_alams = myRealm.where(Data_alam.class).equalTo("name",title).findAll();
            for(Data_alam data_alam : Data_alams){
                if(imbinding.EditMemo.getText().toString().equals(data_alam.getMemo())){
                    isAlready = true;
                    break;
                }
            }
            return isAlready;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        RealmResults<Data_Icon> realmDataBases = myRealm.where(Data_Icon.class).findAll();   //-- 종료 전에 등록된 위치 정보 데이터베이스 초기화후 재저장 ( 오류 및 꼬임현상 방지 )
        myRealm.beginTransaction();
        realmDataBases.deleteAllFromRealm(); // 데이터베이스에 내용 전부 제거
        myRealm.commitTransaction();
        dataBackup();   //--  백업실행
        myRealm.close();
    }

    @Override
    protected void onResume() {   //-- 화면을 받았을때 초기 설정
        super.onResume();
        isLocationCheck = false;
    }

    @Override
    public void onClick(View view) {
           if (view == imbinding.btnSave) { // 최종 저장버튼 을 클릭한다면
                if (isLocationCheck && !imbinding.EditMemo.getText().toString().equals("") && !CheckDBinMemo(nName)) {   //-- 알림받을위치를 설정하였고 메모내용이 비어있지않다면
                    try {
                        /*-----------------------------------------------------*/
                        myRealm.beginTransaction();
                        Data_alam dataalam = myRealm.createObject(Data_alam.class);
                        dataalam.setName(nName);
                        dataalam.setMemo(imbinding.EditMemo.getText().toString());
                        dataalam.setIcon(nicon);
                        dataalam.setLatitude(nlat);
                        dataalam.setLongitude(nlong);
                        dataalam.setColor(setColor);
                        dataalam.setAlamOn(true);
                        myRealm.commitTransaction();
                        /*----------------------------------------------------*/
                        Intent intent = new Intent();   //-- 메인액티비티에 Result 값으로 위치 아이콘과 위치명, 메모내용을 전송
                        setResult(RESULT_OK, intent);
                        finish();
                        ((MainActivity)mainContext).startLocationSerch();
                    } catch (NullPointerException e) {
                        Log.d(TAG, "NullPointerException");
                    }
                } else {   //- -알림받을위치를 설정지않았거나  메모내용이 비어있다면
                    if (!isLocationCheck)   //-- 위치가 설정되어있지않을때,
                        Toast.makeText(this, "알람을 받을 장소를 선택해주세요.", Toast.LENGTH_LONG).show();
                    if (imbinding.EditMemo.getText().toString().equals(""))  //--  메모내용이 비어있을때
                        Toast.makeText(this, "메모를 설정해주세요.", Toast.LENGTH_LONG).show();
                    if (CheckDBinMemo(nName))  //--  메모내용이 이미 있을때
                        Toast.makeText(this, "메모가 이미 존재합니다.", Toast.LENGTH_LONG).show();
                }
            } else if (view == imbinding.btnBack) {   //-- 백버튼 액티비티 종료
                finish();
            } else if (view == imbinding.btnLocation1 || view == imbinding.btnLocation2 || view == imbinding.btnLocation3 || view == imbinding.btnLocation4 || view == imbinding.btnLocation5 ) {   //-- 위치 버튼을 클릭했다면 클릭한 위치 버튼 클릭이미지로 변경
                imageChange(view);   //-- 이미지 변경 메소드
            }

    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {   //-- 위치 추가를 누른후 Location 액티비티에서의 반환값을 반영
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            locationButton.add(data.getIntExtra("icon", -1));
            locationButtonClick.add(data.getIntExtra("clickicon", -1));
            locationName.add(data.getStringExtra("name"));
            latitude.add(data.getDoubleExtra("latitude", -1));
            longitutde.add(data.getDoubleExtra("longitude", -1));

            refreshButtonImage();  //-- 받아온 이미지를 토대로 이미지 재설정
        } else {
            Log.d(TAG, String.valueOf(resultCode));
        }
    }

    private void imageChange(View view) {   //-- 이미지를 클릭했을시 이미지 버튼 클릭된이미지르 변경 다른이미지는 클릭해제된 이미지로 변경/ 해당이미지에대한 정보 임시변수에 저장
        for (int i = 0; i < locationButton.size(); i++) {
            btnlocation[i].setBackgroundResource(locationButton.get(i));
            if (btnlocation[i] == view) {
                btnlocation[i].setBackgroundResource(locationButtonClick.get(i));
                isLocationCheck = true;
                nicon = locationButton.get(i);
                nlat = latitude.get(i);
                nlong = longitutde.get(i);
                nName = locationName.get(i);
                setColor = colors[i];
            }
        }
    }
}
