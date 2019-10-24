package com.example.placememo_project;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.PowerManager;
import android.view.WindowManager;

import androidx.core.app.NotificationCompat;

import static com.example.placememo_project.MainActivity.mainContext;

public class Notification {
    Context rContext;
    private final static String TAG = "Notification : ";

    public Notification(String title, String memo, Context rContext, int notiNum, boolean isAlamOn){
        this.rContext = rContext;


        KeyguardManager km = (KeyguardManager) rContext.getSystemService(Context.KEYGUARD_SERVICE);
        if(km.inKeyguardRestrictedInputMode()){
            Intent intent = new Intent(rContext,Alam_activity.class);
            intent.putExtra("title",title);
            rContext.startActivity(intent);
            ((MainActivity)mainContext).overridePendingTransition(R.anim.fadein,R.anim.fadeout);

            if(isAlamOn) {
                WakeLock();
            }
        }else if(!km.inKeyguardRestrictedInputMode()) {
            PendingIntent pendingIntent = PendingIntent.getActivity(rContext, 0, new Intent(rContext, IntroActivity.class), 0);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(rContext, "default")
                    .setSmallIcon(R.drawable.nomemo)
                    .setContentTitle(title)
                    .setContentText(memo)
                    .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                    .setFullScreenIntent(pendingIntent, true);

            builder.setColor(Color.RED);
            // 사용자가 탭을 클릭하면 자동 제거
            builder.setAutoCancel(true);


            // 알림 표시
            NotificationManager notificationManager = (NotificationManager) rContext.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
            }
            if(isAlamOn) {
                notificationManager.notify(notiNum, builder.build());
                WakeLock();
            }
        }



    }

    void WakeLock(){
        PowerManager pm = (PowerManager) rContext.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP,TAG);
        wl.acquire(3000);
        wl.release();
    }

}
