package com.example.placememo_project.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.placememo_project.adapter.AlamAdapter;
import com.example.placememo_project.databinding.ActivityAlamBinding;
import com.example.placememo_project.dbData.Data_alam;
import com.example.placememo_project.R;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.example.placememo_project.activity.MainActivity.mainContext;
import static com.example.placememo_project.activity.MainActivity.sort;

public class AlamActivity extends BaseActivity {
    ActivityAlamBinding alamBinding;
    RecyclerView.LayoutManager layoutManager;
    AlamAdapter alamAdapter;
    Realm myrealm;
    String title;
    Animation allanim,fadein;
    RealmResults<Data_alam> data_alams;
    Vibrator vibrator;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        alamBinding = DataBindingUtil.setContentView(this, R.layout.activity_alam);
        allanim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.all_anim);
        fadein =  AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fadein2);
        alamBinding.set.startAnimation(fadein);
        alamBinding.set.startAnimation(allanim);
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        Realm.init(this);
        myrealm = Realm.getDefaultInstance();
        long[] pattern = {800,300,800,300};
        vibrator.vibrate(pattern,0);
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
                if((seekBar.getProgress() > 250  && seekBar.getProgress() < 500 )|| (seekBar.getProgress() < 750 && seekBar.getProgress() > 500)){
                    seekBar.setProgress(500);
                    seekBar.setAlpha(1f);
                    alamBinding.set.setAlpha(1f);
                    alamBinding.set.startAnimation(fadein);
                    alamBinding.set.startAnimation(allanim);
                }else if(seekBar.getProgress() < 250){
                    seekBar.setProgress(0);
                    seekBar.setAlpha(0f);
                    myrealm.beginTransaction();
                    data_alams.deleteAllFromRealm();
                    myrealm.commitTransaction();
                    pause = false;
                    finish();
                    vibrator.cancel();
                    try {
                        ((MainActivity)mainContext).ShowAlamUi(sort);
                    }catch (Exception e){}

                }else if(seekBar.getProgress() >750){
                    seekBar.setProgress(1000);
                    seekBar.setAlpha(0f);
                    myrealm.beginTransaction();
                    for(Data_alam data_alam : data_alams){
                        data_alam.setAlamOn(false);
                    }
                    myrealm.commitTransaction();
                    pause = false;
                    finish();
                    vibrator.cancel();
                    try {
                        ((MainActivity)mainContext).ShowAlamUi(sort);
                    }catch (Exception e){}
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
