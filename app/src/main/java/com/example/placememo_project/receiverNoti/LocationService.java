package com.example.placememo_project.receiverNoti;

import android.Manifest;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.placememo_project.R;
import com.example.placememo_project.activity.AlamActivity;
import com.example.placememo_project.activity.IntroActivity;
import com.example.placememo_project.activity.MainActivity;
import com.example.placememo_project.dbData.Data_LastLocation_LastTime;
import com.example.placememo_project.dbData.Data_alam;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.example.placememo_project.activity.BaseActivity.pause;
import static com.example.placememo_project.activity.MainActivity.mainContext;

public class LocationService extends Service {
    double latitude;
    double longitude;
    double minDistance = 0f;  //-- 최소거리 확인용

    LocationManager manager;
    Context rContext = this;
    private final static String TAG = "LocationReceiver : ";
    long startTime;  //-- 현재시간 저장용
    int alamCycle = 15;  //-- 초단위 저장용
    Realm myRealm2;
    NotificationCompat.Builder builder;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        PendingIntent pendingIntent = PendingIntent.getActivity(rContext, 0, new Intent(rContext, IntroActivity.class), 0);
        builder = new NotificationCompat.Builder(rContext, "default")
                .setSmallIcon(R.drawable.nomemo)
                .setContentTitle("ff")
                .setContentText("ff")
                .setFullScreenIntent(pendingIntent, true);

        builder.setColor(Color.RED);
        // 사용자가 탭을 클릭하면 자동 제거
        builder.setAutoCancel(true);


        // 알림 표시
        NotificationManager notificationManager = (NotificationManager) rContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
        }




        startForeground(9,builder.build());


        Log.d("==Service","받음");
        Realm.init(this);
        myRealm2 = Realm.getDefaultInstance();
        RealmResults<Data_alam> data_alams = myRealm2.where(Data_alam.class).equalTo("isAlamOn",true).findAll();
        if(data_alams.size()>0) doBackgroundWork();  //-- 백그라운드에서 실행될 Task 메소드
        myRealm2.close();



        stopForeground(true);
        stopSelf();


        return START_STICKY;
    }

    private void doBackgroundWork() {
        startLocation();  //-- 내 위치 정보 가져오기
        new Thread(new Runnable() {
            @Override
            public void run() {
                Realm myRealm = null;
                myRealm = Realm.getDefaultInstance();
                while (true) {
                    if (latitude == 0.0 && longitude == 0.0) {  //-- 위치 정보를 가져오지 못했다면
                        try {
                            Log.d(TAG,"==위치 받아오는중");
                            Thread.sleep(1000);  //-- 1초대기후 반복 대기
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else break;  //-- 위치 정보를 가져왔다면 탈출
                }
                RealmResults<Data_LastLocation_LastTime> data_lastLocation_lastTimes = myRealm.where(Data_LastLocation_LastTime.class).findAll();
                if(data_lastLocation_lastTimes.size()==0) {
                    myRealm.beginTransaction();
                    Data_LastLocation_LastTime data_lastLocation_lastTime = myRealm.createObject(Data_LastLocation_LastTime.class);
                    data_lastLocation_lastTime.setLatitude(latitude);
                    data_lastLocation_lastTime.setLongitude(longitude);
                    data_lastLocation_lastTime.setMinTime(alamCycle);
                    myRealm.commitTransaction();
                }else{

                }
                distanceMain(myRealm);  //-- 거리확인 메소드 실행
            }
        }).start();
    }

    private void distanceMain(Realm myRealm) {
        double distance;
        boolean onetime = false;
        int notiNum=1;
        try {
            RealmResults<Data_alam> data_alams = myRealm.where(Data_alam.class).equalTo("isAlamOn",true).findAll();
            for (Data_alam data_alam : data_alams) {  //-- DB에 저장된 알람을 원하는 위치와 현재 위치를 비교
                distance = getDistance(data_alam.getLatitude(),data_alam.getLongitude(),this.latitude,this.longitude);
                Log.d(data_alam.getName(),distance +" Km");  //-- 저장된 메모에따른 거리 보여주기위한 Log
                if(minDistance == 0f){  //-- 만약 최소거리가 0이라면 첫번째임을 알수있음
                    minDistance = distance;  //-- 최소거리에 첫번째 가져온 위치에 대한 거리를 저장
                }else if(minDistance > getDistance(data_alam.getLatitude(),data_alam.getLongitude(),this.latitude,this.longitude)) {  //-- 최소거리가 0이아니라면 그다음에 가져오는 위치에대한 거리를 저장되어있던 최소거리와 비교후 더 가까운 거리를 저장
                    minDistance = distance;
                }
                if(distance<0.3) {
                    if (!pause) {
                        KeyguardManager km = (KeyguardManager) rContext.getSystemService(Context.KEYGUARD_SERVICE);
                        if (km.inKeyguardRestrictedInputMode()) {
                            if (!onetime) {
                                Intent intent = new Intent(rContext, AlamActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                                intent.putExtra("title", data_alam.getName());
                                rContext.startActivity(intent);


                                PowerManager pm = (PowerManager) rContext.getSystemService(Context.POWER_SERVICE);
                                PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, TAG);
                                wl.acquire(3000);
                                wl.release();

                                onetime = true;
                            }
                        } else if (!km.inKeyguardRestrictedInputMode()) {
                            new Notification(data_alam.getName(), data_alam.getMemo(), rContext, notiNum, data_alam.getisAlamOn());
                            notiNum++;
                            RealmResults<Data_alam> data_alams1 = myRealm.where(Data_alam.class).equalTo("isAlamOn", true).findAll();
                            if (data_alams1.size() == 0)
                                ((MainActivity) mainContext).checkAlam = false;
                            myRealm.beginTransaction();
                            data_alam.setAlamOn(false);
                            myRealm.commitTransaction();
                        }
                    }
                }
            }
        } catch (NullPointerException e) {
        }

        /*-----------------------------------------------------------------------------------------------------------------------------------------------*/

        if (minDistance > 1000){
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
        }else if (minDistance > 0.2){
            alamCycle = 15;
        }
        Data_LastLocation_LastTime data_lastLocation_lastTimes = myRealm.where(Data_LastLocation_LastTime.class).findFirst();
        double dis = getDistance(data_lastLocation_lastTimes.getLatitude(),data_lastLocation_lastTimes.getLongitude(),latitude,longitude);
        int time = (int)(minDistance/(dis/data_lastLocation_lastTimes.getMinTime()));
        myRealm.beginTransaction();
        data_lastLocation_lastTimes.setLongitude(longitude);
        data_lastLocation_lastTimes.setLatitude(latitude);
        data_lastLocation_lastTimes.setMinTime(time);
        myRealm.commitTransaction();
        if(alamCycle>time){
            alamCycle = time;
        }
        Log.d("==alamCycle",alamCycle+"초");
        Log.d("==time",time+"초");
        /*-----------------------------------------------------------------------------------------------------------------------------------------------*/

        startTime = SystemClock.elapsedRealtime() + alamCycle * 1000;  //-- 알람받을 시간 설정
        RealmResults<Data_alam> data_alams = myRealm.where(Data_alam.class).equalTo("isAlamOn",true).findAll();
        locationSerch();  //--  내위치 찾기 알람매니저 재실행 설정
        myRealm.close();  //-- 사용끝난 Realm DB close
    }

    public void locationSerch(){

        Log.d("==location","실행됨");
        Intent intent = new Intent(this,LocationReceiver.class);
        intent.setAction("alam");
        PendingIntent sender = PendingIntent.getBroadcast(rContext, 0, intent,  PendingIntent.FLAG_CANCEL_CURRENT); //flag 종류
        AlarmManager am = (AlarmManager) rContext.getSystemService(ALARM_SERVICE);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                //API 19 이상 API 23미만
                am.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, startTime, sender) ;
                Log.d("==버전","19이상 23미만");
            } else {
                //API 19미만
                am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, startTime, sender);
                Log.d("==버전","19미만");
            }
        } else {
            //API 23 이상
            am.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, startTime, sender);
            Log.d("==startTime",startTime+"");
            Log.d("==버전","23이상");
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
        manager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, minTime, minDistance, mLocation);
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

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
