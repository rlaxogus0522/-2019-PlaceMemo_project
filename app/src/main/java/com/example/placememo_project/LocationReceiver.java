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
    double distance = 0f;
  
    LocationManager manager;
    Context rContext;
    private final static String TAG = "LocationReceiver : ";
    long startTime;
    int alamCycle;
    @Override
    public void onReceive(Context context, Intent intent) {
        Realm.init(context);
        this.rContext = context;





        Log.d(TAG,"locationSerch Broadcast Receiver시작");

        doBackgroundWork();
    }

    private void doBackgroundWork() {
        startLocation();
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
                    if (latitude == 0.0 && longitude == 0.0) {
                        try {
                            Log.d(TAG,"위치 받아오는중");
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else break;
                }
                distanceMain(myRealm);
            }
        }).start();
    }

    private void distanceMain(Realm myRealm) {
        try {
            RealmResults<Data_alam> data_alams = myRealm.where(Data_alam.class).findAll();
            Log.d(TAG,""+data_alams);
            for (Data_alam data_alam : data_alams) {
                Log.d(data_alam.getName(),getDistance(data_alam.getLatitude(),data_alam.getLongitude(),this.latitude,this.longitude)+"Km");
                if(distance == 0f){
                    distance = getDistance(data_alam.getLatitude(), data_alam.getLongitude(), this.latitude, this.longitude);
                }else if(distance > getDistance(data_alam.getLatitude(),data_alam.getLongitude(),this.latitude,this.longitude)) {
                   distance = getDistance(data_alam.getLatitude(), data_alam.getLongitude(), this.latitude, this.longitude);
               }
            }
        } catch (NullPointerException e) {
            Log.d(TAG,"NullPointerException");
        }
         if (distance > 1000){
            alamCycle = 10000;
        }else if( distance > 500){
            alamCycle = 5000;
        }else if ( distance >100 ){
            alamCycle = 2000;
        }else if(distance >10){
             alamCycle = 500;
        }else if(distance > 1){
             alamCycle = 100;
        }else if(distance >0.5){
             alamCycle = 60;
        }else if (distance > 0.1){
             alamCycle = 10;
        }

        startTime = SystemClock.elapsedRealtime() + alamCycle * 1000;
        locationSerch();
        myRealm.close();
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

        return distance/1000;
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
}
