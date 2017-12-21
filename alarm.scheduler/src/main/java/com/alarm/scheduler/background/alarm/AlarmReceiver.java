package com.alarm.scheduler.background.alarm;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.alarm.scheduler.BuildConfig;
import com.alarm.scheduler.db.model.Alarm;
import com.alarm.scheduler.manager.SettingsManager;
import com.alarm.scheduler.tools.WakeLocker;

import java.util.Date;
import java.util.Locale;

import static com.alarm.scheduler.db.contract.AlarmContract.ALARM_ID;
import static com.alarm.scheduler.db.contract.AlarmContract.ALARM_TIME;
import static com.alarm.scheduler.db.contract.AlarmContract.ALARM_TYPE;

/**
 * Created by milossimic on 12/19/17.
 */

public class AlarmReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        SettingsManager settingsManager = SettingsManager.getInstance(context);

        if (settingsManager.wakeScreen()){
            WakeLocker.acquire(context);
            notify(context, intent, settingsManager);
            WakeLocker.release();
        }else{
            notify(context, intent, settingsManager);
        }
    }

    private void notify(Context context, Intent intent, SettingsManager settingsManager){
        int alarm_id = intent.getIntExtra(ALARM_ID, -1);
        int alarm_type = intent.getIntExtra(ALARM_TYPE, -1);
        String alarm_time = intent.getStringExtra(ALARM_TIME);

        if (settingsManager.showNotification()) {
            prepareNotification(context, alarm_id, alarm_type, alarm_time);
        }

        startMyActivity(context, alarm_id, settingsManager.getWakeUpScreen());
    }

    private void startMyActivity(Context context, int alarmId, String wakUpScreen){
        Intent i = new Intent();
        i.setClassName(BuildConfig.APPLICATION_ID, wakUpScreen);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra(ALARM_ID, alarmId);
        context.startActivity(i);
    }

    private void prepareNotification(Context context, int alarmId, int alarmType, String alarmTime){
        NotificationManager mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

        String atypeSTR = "";

        switch (alarmType){
            case Alarm.EVERY_DAY_ALARM:
                atypeSTR = "EVERY_DAY_ALARMS";
                break;
            case Alarm.WEEKDAY_ALARM:
                atypeSTR = "WEEKDAY_ALARMS";
                break;
            default:
                atypeSTR = "WEEKLY_ALARMS";

        }

        String data = String.format(Locale.US,"time:%s type:%s alarm:%d", alarmTime, atypeSTR, alarmId);

        mBuilder.setSmallIcon(android.R.drawable.ic_btn_speak_now);
        mBuilder.setContentTitle("Simple alarm");
        mBuilder.setContentText(data);

        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        if (mNotificationManager != null) {
            // notificationID allows you to update the notification later on.
            mNotificationManager.notify(m, mBuilder.build());
        }
    }
}
