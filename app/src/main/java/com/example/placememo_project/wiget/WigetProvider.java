package com.example.placememo_project.wiget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import android.widget.RemoteViews;

import com.example.placememo_project.R;

import com.example.placememo_project.activity.LoginActivity;
import com.example.placememo_project.activity.MainActivity;
import com.example.placememo_project.activity.WidgetInsertActivity;
import com.example.placememo_project.activity.WidgetNomalMemoInsetActivity;


public class WigetProvider extends AppWidgetProvider {

    /**
     *브로드캐스트를 수신할때, Override된 콜백 메소드가 호출되기 직전에 호출됨
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);


//        if(MyOnClick.equals(intent.getAction())){
//            Data_Icon data_icons = myRealm.where(Data_Icon.class).findFirst();
//            RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.widget);
//            updateViews.setInt(R.id.location_1,"setBackgroundResource",data_icons.getButtonclick());
//        }
    }

    /**
     * 위젯을 갱신할때 호출됨
     *
     * 주의 : Configure Activity를 정의했을때는 위젯 등록시 처음 한번은 호출이 되지 않습니다
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
        for (int i = 0; i < appWidgetIds.length; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }

    }

    /**
     * 위젯이 처음 생성될때 호출됨
     *
     * 동일한 위젯이 생성되도 최초 생성때만 호출됨
     */
    @Override
    public void onEnabled(Context context) {

        super.onEnabled(context);
    }

    /**
     * 위젯의 마지막 인스턴스가 제거될때 호출됨
     *
     * onEnabled()에서 정의한 리소스 정리할때
     */
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    /**
     * 위젯이 사용자에 의해 제거될때 호출됨
     */
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }


    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId){



        RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.widget);


        Intent intent = new Intent(context.getApplicationContext(), WidgetInsertActivity.class);
        PendingIntent pendingIntent  = PendingIntent.getActivity(context,0,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Intent intent1 = new Intent(context.getApplicationContext(), WidgetNomalMemoInsetActivity.class);
        PendingIntent pendingIntent1  = PendingIntent.getActivity(context,0,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        updateViews.setOnClickPendingIntent(R.id.widget_location,pendingIntent);
        updateViews.setOnClickPendingIntent(R.id.widget_nomal,pendingIntent1);


        appWidgetManager.updateAppWidget(appWidgetId,updateViews);

    }

//    public PendingIntent getPendingSelfIntent(Context context, String action) {
//        Intent intent = new Intent(context, getClass());
//        intent.setAction(action);
//        return PendingIntent.getBroadcast(context, 0, intent, 0);
//    }


}
