package com.daiyiming.lifecountdown;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.util.Calendar;

/**
 * Created by daiyiming on 2016/11/28.
 */

public class MainWidgetProvider extends AppWidgetProvider {

    public final static String ACTION_HAND_UPDATE = "hand_update";

    @Override
    public void onReceive(Context context, Intent intent) {
        display(context);
    }

    private void display(Context context) {
        Config config = new Config(context);
        RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.appwidget_provider_main);
        // 父控件添加点击事件
        remoteView.setOnClickPendingIntent(R.id.ll_layout, PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), 0));
        if (config.isEnable()) {
            remoteView.setTextViewText(R.id.tv_anim, context.getString(R.string.age_countdown, config.getAnimAge()));
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(config.getBirthday());
            calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + config.getAnimAge());
            remoteView.setTextViewText(R.id.tv_num, String.valueOf((int) Math.ceil((calendar.getTimeInMillis() - System.currentTimeMillis()) / 86400000)));
            remoteView.setTextViewText(R.id.tv_day, context.getString(R.string.day));
            remoteView.setTextViewText(R.id.tv_motto, config.getMotto());
        } else {
            remoteView.setTextViewText(R.id.tv_anim, null);
            remoteView.setTextViewText(R.id.tv_num, null);
            remoteView.setTextViewText(R.id.tv_day, null);
            remoteView.setTextViewText(R.id.tv_motto, context.getString(R.string.notification_settings));
        }
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, MainWidgetProvider.class));
        appWidgetManager.updateAppWidget(appIds, remoteView);
    }


}
