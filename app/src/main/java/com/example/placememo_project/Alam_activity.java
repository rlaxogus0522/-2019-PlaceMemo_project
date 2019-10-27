package com.example.placememo_project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.placememo_project.databinding.AlamBinding;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.example.placememo_project.MainActivity.mainContext;

public class Alam_activity extends AppCompatActivity {
    AlamBinding alamBinding;
    RecyclerView.LayoutManager layoutManager;
    AlamAdapter alamAdapter;
    Realm myrealm;
    String title;
    Animation allanim,fadein;
    RealmResults<Data_alam> data_alams;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        alamBinding = DataBindingUtil.setContentView(this,R.layout.alam);
        allanim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.all_anim);
        fadein =  AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fadein2);
        alamBinding.set.startAnimation(fadein);
        alamBinding.set.startAnimation(allanim);

        Realm.init(this);
        myrealm = Realm.getDefaultInstance();
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        data_alams = myrealm.where(Data_alam.class).equalTo("isAlamOn",true).equalTo("name",intent.getStringExtra("title")).findAll();
        alamBinding.alamIcon.setImageResource(data_alams.first().getIcon());
        alamBinding.alamName.setText(data_alams.first().getName());
        alamBinding.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.d("==",seekBar.getProgress()+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                alamBinding.set.clearAnimation();
                alamBinding.set.setAlpha(0f);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if((seekBar.getProgress() > 25  && seekBar.getProgress() < 50 )|| (seekBar.getProgress() < 75 && seekBar.getProgress() > 50)){
                    seekBar.setProgress(50);
                    seekBar.setAlpha(1f);
                    alamBinding.set.setAlpha(1f);
                    alamBinding.set.startAnimation(fadein);
                    alamBinding.set.startAnimation(allanim);
                }else if(seekBar.getProgress() < 25){
                    seekBar.setProgress(0);
                    seekBar.setAlpha(0f);
                    myrealm.beginTransaction();
                    data_alams.deleteAllFromRealm();
                    myrealm.commitTransaction();
                    ((MainActivity)mainContext).pause = false;
                    finishAffinity();
                }else if(seekBar.getProgress() >75){
                    seekBar.setProgress(100);
                    seekBar.setAlpha(0f);
                    myrealm.beginTransaction();
                    for(Data_alam data_alam : data_alams){
                        data_alam.setAlamOn(false);
                    }
                    myrealm.commitTransaction();
                    ((MainActivity)mainContext).pause = false;
                    finishAffinity();
                }

            }
        });





        layoutManager = new LinearLayoutManager(this);
        alamBinding.alamRecycler.setLayoutManager(layoutManager);
        alamAdapter = new AlamAdapter();
        for(Data_alam data_alam : data_alams){
            alamAdapter.addItem(data_alam.getMemo());
        }
        alamBinding.alamRecycler.setAdapter(alamAdapter);
        ((MainActivity)mainContext).pause = true;
//        RealmResults<Data_alam> data_alam = myrealm.where(Data_alam.class).equalTo("isAlamOn",true).equalTo("name",intent.getStringExtra("title")).findAll();
//        for(Data_alam data_alam1 : data_alam){
//            data_alam1.setAlamOn(false);
//        }
    }
}
