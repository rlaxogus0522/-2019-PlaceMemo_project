package com.example.placememo_project.activity;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Toast;


import com.airbnb.lottie.LottieAnimationView;
import com.example.placememo_project.R;
import com.google.android.gms.location.LocationSettingsStates;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;


import androidx.appcompat.app.AppCompatActivity;

import static com.example.placememo_project.activity.MainActivity.titlename;


public class IntroActivity extends AppCompatActivity {
    private GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;
    Handler handler = new Handler();
    LottieAnimationView animationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro);
        titlename.clear();
        animationView = (LottieAnimationView) findViewById(R.id.animation_view);
        animationView.setAnimation("locations.json");
        animationView.loop(false);

        start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(newRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        animationView.loop(false);
    }


    Runnable newRunnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fadein,R.anim.fadeout);
            finish();
        }
    };

    private void start() {
            animationView.playAnimation();
            handler.postDelayed(newRunnable, 3000);
    }






}
