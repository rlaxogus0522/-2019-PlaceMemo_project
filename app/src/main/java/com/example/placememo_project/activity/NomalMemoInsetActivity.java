package com.example.placememo_project.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.placememo_project.databinding.ActivityInsertNomalMemoBinding;
import com.example.placememo_project.dbData.Data_nomal;
import com.example.placememo_project.R;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.example.placememo_project.activity.MainActivity.mainContext;

public class NomalMemoInsetActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityInsertNomalMemoBinding nBinding;
    Realm myRealm;
    int color[] = new int[]{0xFFDF8A84, 0xFF8E65D8, 0xFF6CB8DF, 0xFFCBD654, 0xFFE76E97};  //-- 저장된 메모 메뉴에 표시할 색깔 등록해두기
    Button colorButton[] = new Button[5];
    int selectColor = 0xFFFFFFFF;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nBinding = DataBindingUtil.setContentView(this, R.layout.activity_insert_nomal_memo);
        nBinding.EditMemo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {   //--  메모입력창에 포커스를 받으면 메모를 입력해주세요라는 문구 삭제
                if (b) {
                    nBinding.EditMemo.setText("");
                }
            }
        });
        Realm.init(this);
        myRealm = Realm.getDefaultInstance();
        nBinding.btnSave.setOnClickListener(this);
        nBinding.btnBack.setOnClickListener(this);
        nBinding.color1.setOnClickListener(this);
        nBinding.color2.setOnClickListener(this);
        nBinding.color3.setOnClickListener(this);
        nBinding.color4.setOnClickListener(this);
        nBinding.color5.setOnClickListener(this);
        colorButton[0] = nBinding.color1;
        colorButton[1] = nBinding.color2;
        colorButton[2] = nBinding.color3;
        colorButton[3] = nBinding.color4;
        colorButton[4] = nBinding.color5;

    }

    @Override
    public void onClick(View view) {
        if(view == nBinding.btnSave) { // 일반메모에서 저장버튼을 눌렀을때
            boolean isAledymemo = false;
            RealmResults<Data_nomal> data_nomals2 = myRealm.where(Data_nomal.class).findAll();
            for (Data_nomal data_nomal : data_nomals2) {
                if (nBinding.EditMemo.getText().toString().equals(data_nomal.getMemo())) { // 단 한개라고 완벽하게 겹치는 내용의 메모가 이미 있다면
                    Toast.makeText(this, "이미 같은 메모가 저장되어있습니다.", Toast.LENGTH_LONG).show();
                    isAledymemo = true;
                    break;
                }
            }
            if (!isAledymemo){ // 단 한개도 겹치는 메모가 없었다면 메모 저장 실행
                RealmResults<Data_nomal> results = myRealm.where(Data_nomal.class).findAll();
            myRealm.beginTransaction();
            Data_nomal data_nomal = myRealm.createObject(Data_nomal.class);
            data_nomal.setMemo(nBinding.EditMemo.getText().toString());
            data_nomal.setColor(selectColor);
            data_nomal.setOrder(results.size() - 1);
            myRealm.commitTransaction();
            ((MainActivity) mainContext).nomaladapters.clear();
            for (Data_nomal data_nomals : results) {
                ((MainActivity) mainContext).nomaladapters.addItem(data_nomals.getMemo(), data_nomals.getColor());
            }
            ((MainActivity) mainContext).checkNomalNoImage();
            finish();
        }
        }else if(view == nBinding.btnBack){
            finish();
        }else if(view == nBinding.color1){
            selectColor = color[0];
            for (int i = 0; i < colorButton.length ; i++) {
                colorButton[i].setAlpha(0.4f);
            }
            colorButton[0].setAlpha(1.0f);
        }else if(view == nBinding.color2){
            selectColor = color[1];
            for (int i = 0; i < colorButton.length ; i++) {
                colorButton[i].setAlpha(0.4f);
            }
            colorButton[1].setAlpha(1.0f);
        }else if(view == nBinding.color3){
            selectColor = color[2];
            for (int i = 0; i < colorButton.length ; i++) {
                colorButton[i].setAlpha(0.4f);
            }
            colorButton[2].setAlpha(1.0f);
        }else if(view == nBinding.color4){
            selectColor = color[3];
            for (int i = 0; i < colorButton.length ; i++) {
                colorButton[i].setAlpha(0.4f);
            }
            colorButton[3].setAlpha(1.0f);
        }else if(view == nBinding.color5){
            selectColor = color[4];
            for (int i = 0; i < colorButton.length ; i++) {
                colorButton[i].setAlpha(0.4f);
            }
            colorButton[4].setAlpha(1.0f);
        }
    }


}
