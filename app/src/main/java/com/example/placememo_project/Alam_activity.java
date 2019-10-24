package com.example.placememo_project;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.placememo_project.databinding.AlamBinding;

import io.realm.Realm;
import io.realm.RealmResults;

public class Alam_activity extends AppCompatActivity {
    AlamBinding alamBinding;
    RecyclerView.LayoutManager layoutManager;
    AlamAdapter alamAdapter;
    Realm myrealm;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        alamBinding = DataBindingUtil.setContentView(this,R.layout.alam);


        Realm.init(this);
        myrealm = Realm.getDefaultInstance();
        Intent intent = getIntent();
        RealmResults<Data_alam> data_alams = myrealm.where(Data_alam.class).equalTo("name",intent.getStringExtra("title")).findAll();
        alamBinding.alamIcon.setImageResource(data_alams.first().getIcon());
        alamBinding.alamName.setText(data_alams.first().getName());
        layoutManager = new LinearLayoutManager(this);
        alamBinding.alamRecycler.setLayoutManager(layoutManager);
        alamAdapter = new AlamAdapter();
        for(Data_alam data_alam : data_alams){
            alamAdapter.addItem(data_alam.getMemo());
        }

        alamBinding.alamRecycler.setAdapter(alamAdapter);
    }
}
