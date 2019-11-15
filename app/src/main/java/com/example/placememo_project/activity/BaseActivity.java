package com.example.placememo_project.activity;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.placememo_project.receiverNoti.LocationReceiver;

import java.util.ArrayList;

public class BaseActivity extends AppCompatActivity {
    public static boolean pause = false;
    AlarmManager am;
    PendingIntent sender = null ;
    public void locationSerch(Context context) {
        Log.d("==","locationSerch 실행됨");
        Intent intent = new Intent(this, LocationReceiver.class);
        sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long firstTime = System.currentTimeMillis();
        firstTime += 2 * 1000; //10초 후 알람 이벤트 발생
        am = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                //API 19 이상 API 23미만
                Log.d("==","api19-23 실행됨");
                am.setExact(AlarmManager.RTC_WAKEUP, firstTime, sender);
            } else {
                Log.d("==","api19미만 실행됨");
                //API 19미만
                am.set(AlarmManager.RTC_WAKEUP, firstTime, sender);
            }
        } else {
            //API 23 이상
            Log.d("==","api23 실행됨");
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, firstTime, sender);
        }
    }
}
