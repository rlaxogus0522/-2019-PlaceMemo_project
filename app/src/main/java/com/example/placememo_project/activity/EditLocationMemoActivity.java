package com.example.placememo_project.activity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;


import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.placememo_project.databinding.ActivityEditLocationMemoBinding;
import com.example.placememo_project.dbData.Data_Icon;
import com.example.placememo_project.dbData.Data_alam;
import com.example.placememo_project.R;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.example.placememo_project.activity.MainActivity.mainContext;
import static com.example.placememo_project.activity.MainActivity.sort;

public class EditLocationMemoActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityEditLocationMemoBinding editMemoBinding;
    Realm myRealm;
    Data_alam data_alams1;
    RealmResults<Data_alam> data_alams;
    Data_Icon data_icon;
    String type;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Realm.init(this);
        myRealm =  Realm.getDefaultInstance();
        editMemoBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_location_memo);
        Intent intent  = getIntent();
        type = intent.getStringExtra("type");
        editMemoBinding.EditMemo.setText(intent.getExtras().getString("memo"));
        if(type.equals("memo")) {
            data_alams1 = myRealm.where(Data_alam.class).equalTo("memo", intent.getExtras().getString("memo")).findFirst();
        }else if(type.equals("title")){
            data_alams = myRealm.where(Data_alam.class).equalTo("name", intent.getExtras().getString("memo")).findAll();
            data_icon = myRealm.where(Data_Icon.class).equalTo("name", intent.getExtras().getString("memo")).findFirst();

        }
        editMemoBinding.btnSave.setOnClickListener(this);
        editMemoBinding.btnBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view == editMemoBinding.btnBack){
            finish();
        }else if(view == editMemoBinding.btnSave){
            myRealm.beginTransaction();
            if(type.equals("memo")){
            data_alams1.setMemo(editMemoBinding.EditMemo.getText().toString());
            }else if (type.equals("title")){
                for(Data_alam data_alam : data_alams){
                    data_alam.setName(editMemoBinding.EditMemo.getText().toString());
                }
                data_icon.setName(editMemoBinding.EditMemo.getText().toString());
            }
            myRealm.commitTransaction();
            finish();
            ((MainActivity)mainContext).ShowAlamUi(sort);
        }
    }
}
