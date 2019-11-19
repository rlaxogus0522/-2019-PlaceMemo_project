package com.example.placememo_project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.airbnb.lottie.L;
import com.example.placememo_project.R;
import com.example.placememo_project.databinding.GuideBinding;
import com.example.placememo_project.fragment.Location_guide1;
import com.example.placememo_project.fragment.Location_guide2;
import com.example.placememo_project.fragment.Location_guide4;
import com.example.placememo_project.fragment.Location_guide6;
import com.example.placememo_project.fragment.Location_guide7;
import com.example.placememo_project.fragment.Location_guide8;
import com.example.placememo_project.fragment.Nomal_guide1;
import com.example.placememo_project.fragment.Nomal_guide2;
import com.example.placememo_project.fragment.Nomal_guide3;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

public class GuideActivity  extends AppCompatActivity implements View.OnClickListener {
    GuideBinding guideBinding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        guideBinding = DataBindingUtil.setContentView(this, R.layout.guide);
        guideBinding.locationGuideBack.setOnClickListener(this);
        MovePagerAdapter adapter = new MovePagerAdapter(getSupportFragmentManager());
        Intent intent = getIntent();

        if(intent.getAction().equals("location")){
            guideBinding.guideIcon.setImageResource(R.drawable.guide_location_custom);
            guideBinding.guideText.setText("위치 메모 가이드");
            guideBinding.guideLayout.setBackgroundColor(0xffDD8D9B);
            guideBinding.guideVp.setOffscreenPageLimit(6);
            Location_guide1 location_guide1 = new Location_guide1();
            adapter.addItem(location_guide1);
            Location_guide2 location_guide2 = new Location_guide2();
            adapter.addItem(location_guide2);
            Location_guide4 location_guide4 = new Location_guide4();
            adapter.addItem(location_guide4);
            Location_guide6 location_guide6 = new Location_guide6();
            adapter.addItem(location_guide6);
            Location_guide7 location_guide7 = new Location_guide7();
            adapter.addItem(location_guide7);
            Location_guide8 location_guide8 = new Location_guide8();
            adapter.addItem(location_guide8);
            guideBinding.guideVp.setAdapter(adapter);
            guideBinding.indicator.setViewPager(guideBinding.guideVp);
        }else if(intent.getAction().equals("nomal")){
            guideBinding.guideIcon.setImageResource(R.drawable.guide_nomal_custom);
            guideBinding.guideText.setText("일반 메모 가이드");
            guideBinding.guideLayout.setBackgroundColor(0xffF7D480);
            guideBinding.guideVp.setOffscreenPageLimit(3);
            Nomal_guide1 nomal_guide1 = new Nomal_guide1();
            adapter.addItem(nomal_guide1);
            Nomal_guide2 nomal_guide2 = new Nomal_guide2();
            adapter.addItem(nomal_guide2);
            Nomal_guide3 nomal_guide3 = new Nomal_guide3();
            adapter.addItem(nomal_guide3);
            guideBinding.guideVp.setAdapter(adapter);
            guideBinding.indicator.setViewPager(guideBinding.guideVp);
        }




    }

    @Override
    public void onClick(View view) {
        if(view == guideBinding.locationGuideBack){
            finish();
        }
    }

    class MovePagerAdapter extends FragmentPagerAdapter{
        ArrayList<Fragment> items = new ArrayList<>();

        MovePagerAdapter(FragmentManager fm){
            super(fm);
        }


        public void addItem(Fragment item){
            items.add(item);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return items.get(position);
        }
        @Override
        public int getCount() {
            return items.size();
        }
    }
}
