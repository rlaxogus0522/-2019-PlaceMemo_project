package com.example.placememo_project.receiver_service;



/*---------------------------------------------------------*/


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class LocationReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {


        if(intent.getAction().equals("alam")) {
            Intent intent1 = new Intent(context, ForegroundService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent1);
            } else {
                Intent intent2 = new Intent(context, BackgroundService.class);
                context.startService(intent2);
            }
        }
    }
}
