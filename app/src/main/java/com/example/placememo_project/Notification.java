package com.example.placememo_project;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.PowerManager;

import androidx.core.app.NotificationCompat;

public class Notification {
    public Notification(String title, String memo, Context rContext, int notiNum){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(rContext, "default");

        PendingIntent intent = PendingIntent.getActivity(rContext,0,new Intent(rContext,IntroActivity.class),0);


        builder.setSmallIcon(R.drawable.nomemo);
        builder.setContentTitle(title);
        builder.setContentText(memo);
        builder.setContentIntent(intent);

        builder.setColor(Color.RED);
        // 사용자가 탭을 클릭하면 자동 제거
        builder.setAutoCancel(true);

        // 알림 표시
        NotificationManager notificationManager = (NotificationManager) rContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
        }

        // id값은
        // 정의해야하는 각 알림의 고유한 int값
        notificationManager.notify(notiNum, builder.build());

    }

}
