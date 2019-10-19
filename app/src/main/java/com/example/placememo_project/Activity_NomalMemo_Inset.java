package com.example.placememo_project;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.placememo_project.databinding.ActivityNomalMemoBinding;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.example.placememo_project.MainActivity.mainContext;

public class Activity_NomalMemo_Inset extends AppCompatActivity implements View.OnClickListener {
    ActivityNomalMemoBinding nBinding;
    Realm myRealm;
    int color[] = new int[]{0xFFE96259, 0xFF8E65D8, 0xFF6CB8DF, 0xFFCBD654, 0xFFE76E97};  //-- 저장된 메모 메뉴에 표시할 색깔 등록해두기
    int selectColor = 0xFFF1E28F;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nBinding = DataBindingUtil.setContentView(this, R.layout.activity_nomal_memo);
        Realm.init(this);
        myRealm = Realm.getDefaultInstance();
        nBinding.btnSave.setOnClickListener(this);
        nBinding.btnBack.setOnClickListener(this);
        nBinding.color1.setOnClickListener(this);
        nBinding.color2.setOnClickListener(this);
        nBinding.color3.setOnClickListener(this);
        nBinding.color4.setOnClickListener(this);
        nBinding.color5.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == nBinding.btnSave){
            myRealm.beginTransaction();
            Data_nomal data_nomal = myRealm.createObject(Data_nomal.class);
            data_nomal.setMemo(nBinding.EditMemo.getText().toString());
            data_nomal.setColor(selectColor);
            myRealm.commitTransaction();
            ((MainActivity)mainContext).nomaladapters.clear();
            RealmResults<Data_nomal> results = myRealm.where(Data_nomal.class).findAll();
            for(Data_nomal data_nomals : results) {
                ((MainActivity) mainContext).nomaladapters.addItem(data_nomals.getMemo(),data_nomals.getColor());
            }
            ((MainActivity)mainContext).checkNoImage_nomal();
            finish();
        }else if(view == nBinding.btnBack){
            finish();
        }else if(view == nBinding.color1){
            selectColor = color[0];
        }else if(view == nBinding.color2){
            selectColor = color[1];
        }else if(view == nBinding.color3){
            selectColor = color[2];
        }else if(view == nBinding.color4){
            selectColor = color[3];
        }else if(view == nBinding.color5){
            selectColor = color[4];
        }
    }


}
