package com.example.placememo_project;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import io.realm.Realm;
import io.realm.RealmResults;

import static android.content.Context.ALARM_SERVICE;

/*---------------------------------------------------------*/


public class LocationReceiver extends BroadcastReceiver {
    double latitude;
    double longitude;
    double minDistance = 0f;  //-- 최소거리 확인용
  
    LocationManager manager;
    Context rContext;
    private final static String TAG = "LocationReceiver : ";
    long startTime;  //-- 현재시간 저장용
    int alamCycle;  //-- 초단위 저장용
    @Override
    public void onReceive(Context context, Intent intent) {
        Realm.init(context);
        this.rContext = context;
        Log.d(TAG,"locationSerch Broadcast Receiver시작");
        doBackgroundWork();  //-- 백그라운드에서 실행될 Task 메소드
    }

    private void doBackgroundWork() {
        startLocation();  //-- 내 위치 정보 가져오기
        new Thread(new Runnable() {
            @Override
            public void run() {
                Realm myRealm = null;
                try {
                    myRealm = Realm.getDefaultInstance();
                }catch (Exception e){
                    Log.d(TAG, String.valueOf(e));
                }
                while (true) {
                    if (latitude == 0.0 && longitude == 0.0) {  //-- 위치 정보를 가져오지 못했다면
                        try {
                            Log.d(TAG,"위치 받아오는중");
                            Thread.sleep(1000);  //-- 1초대기후 반복 대기
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else break;  //-- 위치 정보를 가져왔다면 탈출
                }
                distanceMain(myRealm);  //-- 거리확인 메소드 실행
            }
        }).start();
    }

    private void distanceMain(Realm myRealm) {
        try {
            RealmResults<Data_alam> data_alams = myRealm.where(Data_alam.class).findAll();
            for (Data_alam data_alam : data_alams) {  //-- DB에 저장된 알람을 원하는 위치와 현재 위치를 비교
                Log.d(data_alam.getName(),getDistance(data_alam.getLatitude(),data_alam.getLongitude(),this.latitude,this.longitude)+"Km");  //-- 저장된 메모에따른 거리 보여주기위한 Log
                if(minDistance == 0f){  //-- 만약 최소거리가 0이라면 첫번째임을 알수있음
                    minDistance = getDistance(data_alam.getLatitude(), data_alam.getLongitude(), this.latitude, this.longitude);  //-- 최소거리에 첫번째 가져온 위치에 대한 거리를 저장
                }else if(minDistance > getDistance(data_alam.getLatitude(),data_alam.getLongitude(),this.latitude,this.longitude)) {  //-- 최소거리가 0이아니라면 그다음에 가져오는 위치에대한 거리를 저장되어있던 최소거리와 비교후 더 가까운 거리를 저장
                   minDistance = getDistance(data_alam.getLatitude(), data_alam.getLongitude(), this.latitude, this.longitude);
               }
            }
        } catch (NullPointerException e) {
            Log.d(TAG,"NullPointerException");
        }
         if (minDistance > 1000){  //-- 거리에따른 알람 시간을 재설정 ( 세부조정 및 확인 필요  (예시))
            alamCycle = 10000;
        }else if( minDistance > 500){
            alamCycle = 5000;
        }else if ( minDistance >100 ){
            alamCycle = 2000;
        }else if(minDistance >10){
             alamCycle = 500;
        }else if(minDistance > 1){
             alamCycle = 100;
        }else if(minDistance >0.5){
             alamCycle = 60;
        }else if (minDistance > 0.1){
             alamCycle = 10;
        }

        startTime = SystemClock.elapsedRealtime() + alamCycle * 1000;  //-- 알람받을 시간 설정
        locationSerch();  //--  내위치 찾기 알람매니저 재실행 설정
        myRealm.close();  //-- 사용끝난 Realm DB close
    }

    public void locationSerch(){
        Intent intent = new Intent("AlarmService");
        PendingIntent sender = PendingIntent.getBroadcast(rContext, 0, intent, 0);
        AlarmManager am = (AlarmManager) rContext.getSystemService(ALARM_SERVICE);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                //API 19 이상 API 23미만
                am.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, startTime, sender) ;
            } else {
                //API 19미만
                am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, startTime, sender);
            }
        } else {
            //API 23 이상
            am.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, startTime, sender);
        }
    }

    public double getDistance(double lat1 , double lng1 , double lat2 , double lng2 ){
        double distance;

        Location locationA = new Location("point A");
        locationA.setLatitude(lat1);
        locationA.setLongitude(lng1);

        Location locationB = new Location("point B");
        locationB.setLatitude(lat2);
        locationB.setLongitude(lng2);

        distance = locationA.distanceTo(locationB);

        return distance/1000;  //-- Km 단위로 환산
    }


    public void startLocation() {
        manager = (LocationManager) rContext.getSystemService(Context.LOCATION_SERVICE);
        long minTime = 0;
        float minDistance = 0;

        if (ActivityCompat.checkSelfPermission(rContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(rContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, mLocation);
        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, mLocation);
    }

    private void stopLocation() {
        if (ActivityCompat.checkSelfPermission(rContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(rContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.removeUpdates(mLocation);

    }

    LocationListener mLocation = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            stopLocation();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

}
