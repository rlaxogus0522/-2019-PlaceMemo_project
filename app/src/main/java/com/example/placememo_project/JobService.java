package com.example.placememo_project;

import android.Manifest;
import android.app.job.JobParameters;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.SupportMapFragment;

import static com.example.placememo_project.LocationActivity.lContext;

public class JobService extends android.app.job.JobService {
    double latitude, longitude;
    LocationManager manager;
    @Override
    public boolean onStartJob(JobParameters param) {
        doBackgroundWork(param);
        return true;
    }

    private void doBackgroundWork(final JobParameters param) {
        startLocation();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (latitude == 0.0 && longitude == 0.0) {
                        try {
                            Log.d("**","위치 받아오는중");
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else break;
                }
                Log.d("**latitude :",String.valueOf(latitude));
                Log.d("**longitude :",String.valueOf(longitude));
            }
        }).start();
    }
    public void startLocation() {
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        long minTime = 0;
        float minDistance = 0;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, mLocation);
        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, mLocation);
    }

    private void stopLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.removeUpdates(mLocation);

    }

    LocationListener mLocation = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
//            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//            mapFragment.getMapAsync(LocationActivity.this);
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
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
