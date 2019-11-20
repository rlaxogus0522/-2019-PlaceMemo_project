package com.example.placememo_project.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.placememo_project.receiver_service.LocationReceiver;

public class BaseActivity extends AppCompatActivity {
    public static boolean pause = false;
    public AlarmManager am;
    public PendingIntent sender = null ;
    public void locationSerch(Context context) { //최초 알림매니저 등록
        Intent intent = new Intent(this, LocationReceiver.class);
        intent.setAction("alam");
        sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        long firstTime = SystemClock.elapsedRealtime();
        firstTime += 10 * 1000; //10초 후 알람 이벤트 발생
        am = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                //API 19 이상 API 23미만
                am.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, sender);
            } else {
                //API 19미만
                am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, sender);
            }
        } else {
            //API 23 이상
            am.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, sender);
        }
    }
}
