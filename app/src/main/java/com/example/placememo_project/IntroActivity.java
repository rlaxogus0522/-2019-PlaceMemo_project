package com.example.placememo_project;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;


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

import static com.example.placememo_project.MainActivity.titlename;


public class IntroActivity extends AppCompatActivity {
    static boolean permission = false;
    private GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        checkPermission(); // --  권한 체크 시작
        titlename.clear();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(newRunnable);
    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {    // -- 모든 권한 허용 시 위치기능 켤수있도록 유도
            start();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {  // --  필수 권한 거부 시 앱 종료
            Toast.makeText(IntroActivity.this, "설정을 통한 권한 허가\n 또는 앱을 재실행해주세요", Toast.LENGTH_LONG).show();
            finish();
        }
    };


    private void checkPermission() {
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("필수권한 허용 거부시 앱사용이 불가능합니다.\n")
                .setDeniedMessage("")
                .setPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
                .check();
    }




    Runnable newRunnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    };

    private void start() {
        final LocationManager manager = (LocationManager) IntroActivity.this.getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(IntroActivity.this)) {
            Toast.makeText(IntroActivity.this, "위치기능이 꺼져있습니다.\n위치기능이 꺼져있어도 작동하는데 문제는없지만\n위치기능이 켜져있을시 더 정확한 알림을 받으실 수 있습니다.!", Toast.LENGTH_SHORT).show();
            enableLoc(); // --  위치기능이 꺼져있다면 기능을 킬수있도록 알려주는 팝업
        } else {
            Toast.makeText(IntroActivity.this, "위치기능이 켜져있습니다..!", Toast.LENGTH_SHORT).show(); // --  기능이 이미 켜져있다면 인트로 후 다음 진행
            handler.postDelayed(newRunnable, 3000);
            permission = true; // --  현재위치 위,경도 를 받아오기 위해 위치기능을 킨사람과 키지않은사람을 구분
        }

    }

    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    private void enableLoc() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(IntroActivity.this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {
                        }
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    if (status.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                        try {
                            status.startResolutionForResult(IntroActivity.this, REQUEST_LOCATION);

                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                    }


                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Toast.makeText(IntroActivity.this, "성공적으로 위치기능을 켰습니다.", Toast.LENGTH_LONG).show();
                        handler.postDelayed(newRunnable, 3000);
                        permission = true; // --  위치기능을 켯다면 true 로 킨 유저임을 인식
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(IntroActivity.this, "위치기능을 거부하셔습니다.\n지도기능사용이 불가능합니다.", Toast.LENGTH_SHORT).show();
                        handler.postDelayed(newRunnable, 3000);
                        permission = false; // --  위치기능을 안켯다면 false 로 키지않은 유저임을 인식
                        break;
                    default:
                        break;
                }
                return;
        }
    }
}
