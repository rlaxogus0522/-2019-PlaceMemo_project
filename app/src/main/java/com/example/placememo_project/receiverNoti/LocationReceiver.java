package com.example.placememo_project.receiverNoti;



/*---------------------------------------------------------*/


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class LocationReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context,LocationService.class);

        if(intent.getAction().equals("alam")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent1);
                Log.d("==o", "받음");
            } else {
                context.startService(intent1);
                Log.d("==n", "받음");
            }
        }else if(intent.getAction().equals("stop")){
            context.stopService(intent1);
        }
    }
}
