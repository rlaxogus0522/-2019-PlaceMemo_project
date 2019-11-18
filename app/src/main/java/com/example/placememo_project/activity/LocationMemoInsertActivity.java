package com.example.placememo_project.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


import com.example.placememo_project.databinding.ActivityInsertLocationMemoBinding;
import com.example.placememo_project.dbData.Data_Icon;
import com.example.placememo_project.dbData.Data_alam;
import com.example.placememo_project.R;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.example.placememo_project.activity.MainActivity.mainContext;
import static com.example.placememo_project.activity.MainActivity.sort;

public class LocationMemoInsertActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {   //-- 메모를 추가하는 액티비티
    private final static String TAG = "LocationMemoInsertActivity-------";
    private boolean isEditMode = false;   //--  사용자가 등록한 아이콘을 삭제할때 롱클릭시 에디트모드로 변경
    private ArrayList<Integer> locationButton = new ArrayList<>(); // -- 클릭 되기 전 버튼 이미지
    private ArrayList<Integer> locationButtonClick = new ArrayList<>(); // -- 클릭 된 이후 버튼 이미지
    private ImageButton btnlocation[] = new ImageButton[5]; // -- 이미지버튼의 객체를 배열형태로 담아둠
    private ImageButton btndelete[] = new ImageButton[5];   //-- 이미지삭제 버튼의 객체를 배열형대로 담아둠
    private ArrayList<Double> latitude = new ArrayList<>();
    private ArrayList<Double> longitutde = new ArrayList<>();
    static ArrayList<String> locationName = new ArrayList<>();   //--  위치이름 저장
    private Vibrator vibrator;   //-- 삭제를 위한 롱클릭시 진동으로 사용자에게 알려주기위해
    private int nicon;   //--  현재 선택된 위치의 아이콘 저장용
    private String nName;   //--  현재 선택된 위치의 이름 저장용
    private double nlat, nlong;   //--  현재 선택된 위치의 위 경도 저장용
    ActivityInsertLocationMemoBinding imbinding;
    LocationManager locationManager;
    private boolean isLocationCheck = false;   //-- 메모 추가시 알림 받을 위치를 선택했는지 체크용
    int colors[] = new int[]{0xFFE8EE9C, 0xFFE4B786, 0xFF97E486, 0xFF86E4D1, 0xFFE48694};  //-- 저장된 메모 메뉴에 표시할 색깔 등록해두기
    int setColor;
    int clickNum;
    Realm myRealm;
    AlertDialog alamreset,location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imbinding = DataBindingUtil.setContentView(this, R.layout.activity_insert_location_memo);
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
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

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // 제목셋팅
        alertDialogBuilder.setTitle("모든 알람 초기화");

        // AlertDialog 셋팅
        alertDialogBuilder
                .setMessage("해당위치에 등록된 메모가 있습니다. \n 해당위치와 메모를 모두 지우시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("삭제",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                    deletLocationAndDB(clickNum);

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

        AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(this);

        // 제목셋팅
        alertDialogBuilder2.setTitle("위치 기능 활성화 설정");

        // AlertDialog 셋팅
        alertDialogBuilder2
                .setMessage("위치기능이 꺼져있습니다. \n비활성화시 이용이 제한됩니다. \n\n 설정창으로 이동하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("이동",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                intent.addCategory(Intent.CATEGORY_DEFAULT);
                                startActivity(intent);
                            }
                        })
                .setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        location = alertDialogBuilder2.create();

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        imbinding.btnSave.setOnClickListener(this);
        imbinding.btnAddlocation.setOnClickListener(this);
        imbinding.btnBack.setOnClickListener(this);
        imbinding.insertLayout.setOnClickListener(this);

        btnlocation[0] = imbinding.btnLocation1;   //-- 위치 버튼
        btnlocation[1] = imbinding.btnLocation2;
        btnlocation[2] = imbinding.btnLocation3;
        btnlocation[3] = imbinding.btnLocation4;
        btnlocation[4] = imbinding.btnLocation5;


        btndelete[0] = imbinding.btnLocation1Delete;   //-- 위치 삭제 버튼
        btndelete[1] = imbinding.btnLocation2Delete;
        btndelete[2] = imbinding.btnLocation3Delete;
        btndelete[3] = imbinding.btnLocation4Delete;
        btndelete[4] = imbinding.btnLocation5Delete;

        dataUpdate();   //-- 데이터베이스에 저장된 사용자가 등록해둔 위치정보들 가져오기
        refreshButtonImage();   //--  이미지 설정 초기화
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {   //-- 팝업으로 뜨는 메모추가 액티비티 외 구역을 터치했을때
        if (isEditMode) {
            Rect diaalogBounds = new Rect();
            getWindow().getDecorView().getHitRect(diaalogBounds);
            if (!diaalogBounds.contains((int) ev.getX(), (int) ev.getY())) {
                for (int i = 0; i < locationButton.size(); i++) {   //--  삭제버튼을 안보이게 설정하고 기본버튼을 보이게설정
                    btndelete[i].setAlpha((float) 0.0);
                    btnlocation[i].setAlpha((float) 1.0);
                    isEditMode = false;   //--  이후에 에디트모드 false
                }
                return false;
            }
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
            btnlocation[i].setOnLongClickListener(this);   //-- 추가된 버튼이 롱클릭 리스너 등록
            btndelete[i].setOnClickListener(this);   //--  추가된 버튼에대한 삭제버튼 리스너 등록
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
        isEditMode = false;
    }

    @Override
    public void onClick(View view) {
        if (!isEditMode) {   //-- 만약 에디트모드가 아니라면
            if (view == imbinding.btnAddlocation) { // 위치 추가 버튼이 클릭되었다면
                if (locationButton.size() < 5) {   //-- 위치 버튼이 5개 이하라면
                    checkPermissions();
                } else {
                    Toast.makeText(this, "위치는 최대 5개까지 등록 가능합니다.", Toast.LENGTH_LONG).show();   //-- 5개가 이미 등록되어있다면 불가능하다는 메시지
                }
            } else if (view == imbinding.btnSave) { // 최종 저장버튼 을 클릭한다면
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

        } else {   //-- 에디트 모드 상태라면
            if (view == imbinding.insertLayout) {   //-- 그냥 엑티비티를 클릭하면
                isEditMode = false;  //--  에디트 모드 해제
                for (int i = 0; i < locationButton.size(); i++) {
                    btndelete[i].setAlpha((float) 0.0);
                    btnlocation[i].setAlpha((float) 1.0);
                }
            } else if (view == imbinding.btnAddlocation) {   //-- 위치추가 버튼을 클릭하면
                isEditMode = false;   //-- 에디트 모드를 해제하고
                for (int i = 0; i < locationButton.size(); i++) {
                    btndelete[i].setAlpha((float) 0.0);
                    btnlocation[i].setAlpha((float) 1.0);
                }
                if (locationButton.size() < 5) {   //--  5개 이하라면 위치추가 진행
                    checkPermissions();
                } else {   //-- 5개를 넘었다면 불가능 메시지 출력
                    Toast.makeText(this, "위치는 최대 5개까지 등록 가능합니다.", Toast.LENGTH_LONG).show();
                }
            } else if (view == imbinding.btnSave) {   //--  저장 버튼을 눌렀다면
                isEditMode = false;   //-- 에디트 모드를 해제하고
                for (int i = 0; i < locationButton.size(); i++) {
                    btndelete[i].setAlpha((float) 0.0);
                    btnlocation[i].setAlpha((float) 1.0);
                }

                if (isLocationCheck && !imbinding.EditMemo.getText().toString().equals("")) {   //--  위치추가 기능 실행
                    try {
                        myRealm.beginTransaction();
                        Data_alam dataalam = myRealm.createObject(Data_alam.class);
                        dataalam.setName(nName);
                        dataalam.setMemo(imbinding.EditMemo.getText().toString());
                        dataalam.setIcon(nicon);
                        dataalam.setLatitude(nlat);
                        dataalam.setLongitude(nlong);
                        dataalam.setColor(setColor);
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
                    if (!isLocationCheck)
                        Toast.makeText(this, "알람을 받을 장소를 선택해주세요.", Toast.LENGTH_LONG).show();
                    if (imbinding.EditMemo.getText().toString().equals(""))
                        Toast.makeText(this, "메모를 설정해주세요.", Toast.LENGTH_LONG).show();
                }
            } else {   //-- 사용자의 다른 특별한동작이아니고 에디트모드에서 버튼을 클릭한다면 해당 버튼 삭제
                for (int i = 0; i < locationButton.size(); i++) {
                    if (view == btnlocation[i]) {
                        RealmResults<Data_alam> data_alams = myRealm.where(Data_alam.class).equalTo("name",locationName.get(i)).findAll();
                        if(data_alams.size()>0){
                            clickNum = i;
                            alamreset.show();
                        }else{
                            deletLocation(i);
                        }
                    }
                }
            }
        }
    }

    private void AddLocation() {
        Intent in = new Intent(LocationMemoInsertActivity.this, GoogleMapActivity.class);
        startActivityForResult(in, 0522);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    private void deletLocation(int i) {
        locationButton.remove(i);
        locationButtonClick.remove(i);
        locationName.remove(i);
        latitude.remove(i);
        longitutde.remove(i);
        btndelete[locationButton.size()].setAlpha((float) 0.0);
        btnlocation[locationButton.size()].setAlpha((float) 1.0);
        btnlocation[locationButton.size()].setBackgroundResource(android.R.color.transparent);
        refreshButtonImage();
    }
    private void deletLocationAndDB(int i) {
        RealmResults<Data_alam> data_alams = myRealm.where(Data_alam.class).equalTo("name",locationName.get(i)).findAll();
        for(Data_alam data_alam : data_alams){
            myRealm.beginTransaction();
            data_alam.deleteFromRealm();
            myRealm.commitTransaction();
        }
        locationButton.remove(i);
        locationButtonClick.remove(i);
        locationName.remove(i);
        latitude.remove(i);
        longitutde.remove(i);
        btndelete[locationButton.size()].setAlpha((float) 0.0);
        btnlocation[locationButton.size()].setAlpha((float) 1.0);
        btnlocation[locationButton.size()].setBackgroundResource(android.R.color.transparent);
        refreshButtonImage();
        ((MainActivity)mainContext).ShowAlamUi(sort);
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

    @Override
    public boolean onLongClick(View view) {   //-- 롱클릭을 했다면
        if (view == imbinding.btnAddlocation || view == imbinding.btnSave) {   //-- 저장 또는 위치추가에 대한 롱클릭 무시
            return true;
        } else {
            vibrator.vibrate(100);   //-- 진동을 주고
            isEditMode = true;   //--  에디트모드 실행

            for (int i = 0; i < locationButton.size(); i++) {   //-- 버튼이미지 삭제 가능 이미지로 교체
                btndelete[i].setAlpha((float) 1.0);
                btnlocation[i].setAlpha((float) 0.4);
            }
            return true;

        }
    }

    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            mOnGPSClick();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(LocationMemoInsertActivity.this, "권한 거부시 서비스이용이 제한됩니다.", Toast.LENGTH_SHORT).show();
        }
    };
    public void checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23){ // 마시멜로(안드로이드 6.0) 이상 권한 체크
            TedPermission.with(this)
                    .setPermissionListener(permissionListener)
                    .setRationaleMessage("위치 추가를 위해서는 접근 권한이 필요합니다")
                    .setDeniedMessage("앱에서 요구하는 권한설정이 필요합니다...\n [설정] > [권한] 에서 사용으로 활성화해주세요.")
                    .setPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
                    .check();

        } else {
            mOnGPSClick();
        }
    }

    public void mOnGPSClick(){
        //GPS가 켜져있는지 체크
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            //GPS 설정화면으로 이동
            location.show();

        }else{
            AddLocation();
        }
    }


}
