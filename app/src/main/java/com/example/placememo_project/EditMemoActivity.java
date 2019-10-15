package com.example.placememo_project;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;


import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.placememo_project.databinding.ActivityEditMemoBinding;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.example.placememo_project.MainActivity.mainContext;

public class EditMemoActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityEditMemoBinding editMemoBinding;
    Realm myRealm;
    RealmResults<Data_alam> data_alams;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Realm.init(this);
        myRealm =  Realm.getDefaultInstance();
        editMemoBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_memo);
        Intent intent  = getIntent();
        editMemoBinding.EditMemo.setText(intent.getExtras().getString("memo"));
        data_alams = myRealm.where(Data_alam.class).equalTo("memo",intent.getExtras().getString("memo")).findAll();

        editMemoBinding.btnSave.setOnClickListener(this);
        editMemoBinding.btnBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view == editMemoBinding.btnBack){
            finish();
        }else if(view == editMemoBinding.btnSave){
            myRealm.beginTransaction();
            data_alams.first().setMemo(editMemoBinding.EditMemo.getText().toString());
            myRealm.commitTransaction();
            finish();
            ((MainActivity)mainContext).ShowAlamUi();
        }
    }
}
