package com.example.placememo_project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.placememo_project.dbData.Data_alam;
import com.example.placememo_project.R;
import com.example.placememo_project.databinding.ActivityTitleAddItemBinding;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.example.placememo_project.activity.MainActivity.mainContext;
import static com.example.placememo_project.activity.MainActivity.sort;

public class TitleAddItemActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityTitleAddItemBinding titleAddItemBinding;
    Realm myRealm;
    RealmResults<Data_alam> data_alams;
    String title;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Realm.init(this);
        myRealm =  Realm.getDefaultInstance();
        titleAddItemBinding = DataBindingUtil.setContentView(this, R.layout.activity_title_add_item);
        Intent intent  = getIntent();
        title = intent.getExtras().getString("titlename");;

        titleAddItemBinding.btnSave.setOnClickListener(this);
        titleAddItemBinding.btnBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view == titleAddItemBinding.btnBack){
            finish();
        }else if(view == titleAddItemBinding.btnSave) {
            if (!titleAddItemBinding.EditMemo.getText().toString().equals("") && !CheckDBinMemo(title)) {
                RealmResults<Data_alam> data_alams = myRealm.where(Data_alam.class).equalTo("name", title).findAll();
                myRealm.beginTransaction();
                Data_alam dataalam = myRealm.createObject(Data_alam.class);
                dataalam.setName(title);
                dataalam.setMemo(titleAddItemBinding.EditMemo.getText().toString());
                dataalam.setIcon(data_alams.first().getIcon());
                dataalam.setLatitude(data_alams.first().getLatitude());
                dataalam.setLongitude(data_alams.first().getLongitude());
                dataalam.setColor(data_alams.first().getColor());
                dataalam.setAlamOn(true);
                myRealm.commitTransaction();
                finish();
                ((MainActivity) mainContext).ShowAlamUi(sort);
                ((MainActivity) mainContext).checkNoImage();
            }else{
                if (titleAddItemBinding.EditMemo.getText().toString().equals(""))  //--  메모내용이 비어있을때
                    Toast.makeText(this, "메모를 설정해주세요.", Toast.LENGTH_LONG).show();
                if (CheckDBinMemo(title))  //--  메모내용이 이미 있을때
                    Toast.makeText(this, "메모가 이미 존재합니다.", Toast.LENGTH_LONG).show();
            }
        }
    }

    boolean CheckDBinMemo(String title){
        boolean isAlready= false;
        RealmResults<Data_alam> Data_alams = myRealm.where(Data_alam.class).equalTo("name",title).findAll();
        for(Data_alam data_alam : Data_alams){
            if(titleAddItemBinding.EditMemo.getText().toString().equals(data_alam.getMemo())){
                isAlready = true;
                break;
            }
        }
        return isAlready;
    }
}
