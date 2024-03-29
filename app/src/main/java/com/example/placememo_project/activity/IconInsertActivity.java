package com.example.placememo_project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.placememo_project.R;
import com.example.placememo_project.databinding.ActivityInsertIconBinding;

import java.util.ArrayList;

public class IconInsertActivity extends AppCompatActivity implements View.OnClickListener {   //-- 아이콘추가시 실행되는 액티비티
    private final static String TAG = "IconInsertActivity : ";
    private int icon,clickicon;   //-- 클릭되기전 아이콘과 이후 아이콘 구분
    ActivityInsertIconBinding iBinding;
    ArrayList<Integer> locationButton = new ArrayList<>(); // -- 클릭 되기 전 버튼 이미지
    ArrayList<Integer> locationButtonClick = new ArrayList<>(); // -- 클릭 된 이후 버튼 이미지
    ArrayList<ImageButton> btnlocation = new ArrayList<>(); // -- 이미지버튼의 객체를 배열형태로 담아둠
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iBinding = DataBindingUtil.setContentView(this, R.layout.activity_insert_icon);
        iBinding.btnAddiconOk.setOnClickListener(this);

        addButton(); // icon 버튼들 추가하기 실행

    }



    @Override
    public void onClick(View v) {
        if(v==iBinding.btnAddiconOk){
            Intent intent = new Intent();
            intent.putExtra("icon", icon);
            intent.putExtra("clickicon", clickicon);
            setResult(RESULT_OK,intent);
            finish();
        }else {
            imageChange(v);
        }
    }
    private void imageChange(View view) { //사용자가 클릭한 버튼의 색을 변경하고 나머지 버튼은 원래 클리되기 전 색으로 돌림
        for (int i = 0; i < locationButton.size(); i++) {
            btnlocation.get(i).setBackgroundResource(locationButton.get(i));
            if (btnlocation.get(i) == view) {
                btnlocation.get(i).setBackgroundResource(locationButtonClick.get(i));
                icon = locationButton.get(i);
                clickicon = locationButtonClick.get(i);
            }
        }
    }

    private void addButton() {

        //-- 기본으로 사용자에게 제공될 버튼이미지
        btnlocation.add(iBinding.btnLocation1);
        btnlocation.add(iBinding.btnLocation2);
        btnlocation.add(iBinding.btnLocation3);
        btnlocation.add(iBinding.btnLocation4);
        btnlocation.add(iBinding.btnLocation5);
        btnlocation.add(iBinding.btnLocation6);
        btnlocation.add(iBinding.btnLocation7);
        btnlocation.add(iBinding.btnLocation8);
        btnlocation.add(iBinding.btnLocation9);
        btnlocation.add(iBinding.btnLocation10);

        locationButton.add(R.drawable.home);
        locationButton.add(R.drawable.work);
        locationButton.add(R.drawable.subway);
        locationButton.add(R.drawable.school);
        locationButton.add(R.drawable.cafe);
        locationButton.add(R.drawable.car);
        locationButton.add(R.drawable.car_wash);
        locationButton.add(R.drawable.create);
        locationButton.add(R.drawable.computer);
        locationButton.add(R.drawable.eco);

        locationButtonClick.add(R.drawable.home_click);
        locationButtonClick.add(R.drawable.work_click);
        locationButtonClick.add(R.drawable.subway_click);
        locationButtonClick.add(R.drawable.school_click);
        locationButtonClick.add(R.drawable.cafe_click);
        locationButtonClick.add(R.drawable.car_click);
        locationButtonClick.add(R.drawable.car_wash_click);
        locationButtonClick.add(R.drawable.create_click);
        locationButtonClick.add(R.drawable.computer_click);
        locationButtonClick.add(R.drawable.eco_click);

        for(int i=0;i<btnlocation.size();i++){
            btnlocation.get(i).setOnClickListener(this);
            btnlocation.get(i).setBackgroundResource(locationButton.get(i));
        }
    }
}
