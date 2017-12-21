package com.alarm.scheduler.manager;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.alarm.scheduler.db.DatabaseHelper;
import com.alarm.scheduler.db.model.Alarm;

import java.sql.SQLException;
import java.util.Calendar;

import static com.alarm.scheduler.db.model.Alarm.EVERY_DAY_ALARM;
import static com.alarm.scheduler.db.model.Alarm.WEEKDAY_ALARM;
import static com.alarm.scheduler.db.model.Alarm.WEEKLY_ALARM;

/**
 * Created by milossimic on 12/19/17.
 */

public class AlarmManager {
    private static AlarmManager sInstance;

    public static synchronized AlarmManager getInstance() {
        if (sInstance == null) {
            sInstance = new AlarmManager();
        }
        return sInstance;
    }

    private AlarmManager() {
    }

    private void setupAlarm(Context context, Alarm alarm){
        ScheduleManager scheduleManager = ScheduleManager.getInstance();

        Intent intent = scheduleManager.createIntent(context, alarm);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, alarm.getId(), intent, 0);

        scheduleManager.scheduleAlarm(context, alarm,  pIntent);
    }

    private boolean createAlarm(Context context, int hour, int minute, int day, String soundURI){
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        try {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, hour);
            c.set(Calendar.MINUTE, minute);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);

            Alarm alarm = new Alarm();
            alarm.setActive(true);
            alarm.setSound(soundURI);

            switch (day){
                case EVERY_DAY_ALARM:
                    alarm.setType(EVERY_DAY_ALARM);
                    break;
                case WEEKDAY_ALARM:
                    alarm.setType(WEEKDAY_ALARM);
                    break;
                default:
                    c.set(Calendar.DAY_OF_WEEK, day);
                    alarm.setType(WEEKLY_ALARM);
            }

            alarm.setWhen(c.getTimeInMillis());

            databaseHelper.alarmDao().create(alarm);
            setupAlarm(context, alarm);
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private boolean createAlarm(Context context, Calendar c, int alarmType, String soundURI){
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        try {
            Alarm alarm = new Alarm();
            alarm.setActive(true);
            alarm.setSound(soundURI);
            alarm.setWhen(c.getTimeInMillis());

            switch (alarmType){
                case EVERY_DAY_ALARM:
                    alarm.setType(EVERY_DAY_ALARM);
                    break;
                case WEEKDAY_ALARM:
                    alarm.setType(WEEKDAY_ALARM);
                    break;
                default:
                    alarm.setType(WEEKLY_ALARM);
            }

            databaseHelper.alarmDao().create(alarm);
            setupAlarm(context, alarm);
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
