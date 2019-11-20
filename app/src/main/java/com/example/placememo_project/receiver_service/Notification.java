package com.example.placememo_project.receiver_service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.placememo_project.R;
import com.example.placememo_project.activity.IntroActivity;

public class Notification {
    Context rContext;
    private final static String TAG = "Notification : ";

    public Notification(String title, String memo, Context rContext, int notiNum, boolean isAlamOn){
        this.rContext = rContext;


            PendingIntent pendingIntent = PendingIntent.getActivity(rContext, 0, new Intent(rContext, IntroActivity.class), 0);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(rContext, "default")
                    .setSmallIcon(R.drawable.nomemo)
                    .setContentTitle(title)
                    .setContentText(memo)
                    .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                    .setFullScreenIntent(pendingIntent, true);

            builder.setColor(Color.RED);
            builder.setAutoCancel(true);


            // 알림 표시
            NotificationManager notificationManager = (NotificationManager) rContext.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
            }
            if(isAlamOn) {
                notificationManager.notify(notiNum, builder.build());
            }
//        }



    }


}
