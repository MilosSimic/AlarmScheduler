package com.alarm.scheduler.manager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.alarm.scheduler.background.alarm.AlarmReceiver;
import com.alarm.scheduler.db.DatabaseHelper;
import com.alarm.scheduler.db.model.Alarm;
import com.alarm.scheduler.tools.AlarmTools;

import java.sql.SQLException;
import java.util.Calendar;

import static com.alarm.scheduler.db.contract.AlarmContract.ALARM_ID;
import static com.alarm.scheduler.db.contract.AlarmContract.ALARM_TIME;
import static com.alarm.scheduler.db.contract.AlarmContract.ALARM_TYPE;
import static com.alarm.scheduler.db.model.Alarm.WEEKDAY_ALARM;
import static com.alarm.scheduler.db.model.Alarm.WEEKLY_ALARM;
import static com.alarm.scheduler.tools.AlarmTools.nextAlarmText;

/**
 * Created by milossimic on 12/19/17.
 */

public class ScheduleManager {
    private static ScheduleManager sInstance;

    public static synchronized ScheduleManager getInstance() {
        if (sInstance == null) {
            sInstance = new ScheduleManager();
        }
        return sInstance;
    }

    private ScheduleManager() {
    }

    private int next(int minutes){
        return 1000 * 60 * minutes;
    }

    public void scheduleAlarm(Context context, Alarm alarm, PendingIntent pIntent){
        Calendar setAlarm = Calendar.getInstance();
        setAlarm.setTimeInMillis(alarm.getWhen());

        long interval;
        if (alarm.getType() == WEEKLY_ALARM){
            //interval = compare(setAlarm, AlarmManager.INTERVAL_DAY*7, alarm.getType());
            interval = compare(setAlarm, alarm.getType());
        }else{
            //interval = compare(setAlarm, AlarmManager.INTERVAL_DAY, alarm.getType());
            interval = compare(setAlarm, alarm.getType());
        }

        schedule(context, interval, pIntent);
        Toast.makeText(context, nextAlarmText(interval, alarm), Toast.LENGTH_LONG).show();
        Log.i("REZ", nextAlarmText(interval, alarm));
    }

    private void schedule(Context context, long when, PendingIntent pIntent){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, when, pIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, when, pIntent);
            }
        }
    }

    private long decideWeekdays(int day_of_week){
        switch (day_of_week){
            case Calendar.FRIDAY:
                return AlarmManager.INTERVAL_DAY * 3;
            case Calendar.SATURDAY:
                return AlarmManager.INTERVAL_DAY * 2;
            default:
                return AlarmManager.INTERVAL_DAY;
        }
    }

    private long compare(Calendar startAlarm, int alarmType) {
        long start = startAlarm.getTimeInMillis();
        Calendar nowTime = Calendar.getInstance();

        //to prevent possible problem with seconds
        nowTime.set(Calendar.SECOND, 0);
        nowTime.set(Calendar.MILLISECOND, 0);

        // if the settled alarm is in the past by time (lower than now),
        // than add a (day | 7 days) to settled alarm so it will try to
        // ring (next day | after 7 days) at the specified time
        if(startAlarm.compareTo(nowTime) <= 0){
            switch (alarmType){
                case WEEKLY_ALARM:
                    //weekly alarms are different by 7 days
                    return start + AlarmManager.INTERVAL_DAY * 7;
                case WEEKDAY_ALARM:
                    //weekday alarms are different by a day, except friday that different is in 3 days
                    //and saturday that is different by 2 days
                    return start + decideWeekdays(nowTime.get(Calendar.DAY_OF_WEEK));
                default:
                    //everyday alarms are different by a day
                    return start + AlarmManager.INTERVAL_DAY;
            }

            //return start+constant;
        }

        // else if settled alarm is in the future
        // than calculate till that one in future
        return start;
    }

    public Intent createIntent(Context context, Alarm alarm){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(alarm.getWhen());

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(ALARM_ID, alarm.getId());
        intent.putExtra(ALARM_TYPE, alarm.getType());
        intent.putExtra(ALARM_TIME, AlarmTools.generateShow(c.getTime()));

        return intent;
    }

    public void cancelAlarm(Context context, PendingIntent pIntent){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null) {
            alarmManager.cancel(pIntent);
        }
    }

    public void snooze(Context context, Alarm alarm, int snoozeMinutes){
        try {
            DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);

            Calendar now = Calendar.getInstance();
            now.setTimeInMillis(System.currentTimeMillis() + next(snoozeMinutes));
            now.set(Calendar.SECOND, 0);
            now.set(Calendar.MILLISECOND, 0);

            Intent intent = createIntent(context, alarm);
            PendingIntent pIntent = PendingIntent.getBroadcast(context, alarm.getId(), intent, 0);

            //snooze will hold real value set by user
            //ad when user hold button, than we will
            //replace when with snooze and remove snooze
            alarm.setSnooze(alarm.getWhen());
            alarm.setWhen(now.getTimeInMillis());
            databaseHelper.alarmDao().update(alarm);

            scheduleAlarm(context, alarm, pIntent);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void reschedule(Context context, Alarm alarm){
        try {
            DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);

            Intent intent = createIntent(context, alarm);
            PendingIntent pIntent = PendingIntent.getBroadcast(context, alarm.getId(), intent, 0);

            //since date can change update current alarm date component
            //and leave time component intact, time stays the same
            databaseHelper.alarmDao().update(updateAlarm(alarm));

            scheduleAlarm(context, alarm,  pIntent);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Alarm updateAlarm(Alarm alarm){
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH);
        int day_of_week = now.get(Calendar.DAY_OF_WEEK);

        Calendar alarmDay = Calendar.getInstance();
        alarmDay.setTimeInMillis(alarm.getWhen());
        alarmDay.set(Calendar.YEAR, year);
        alarmDay.set(Calendar.MONTH, month);
        alarmDay.set(Calendar.DAY_OF_WEEK, day_of_week);

        alarm.setWhen(alarmDay.getTimeInMillis());

        //reset snooze if user snoozed
        //this alarm. Snooze is value set by user
        //when he defined alarm, so just replace values
        if (alarm.getSnooze() != 0L){
            alarm.setWhen(alarm.getSnooze());
            alarm.setSnooze(0L);
        }

        return alarm;
    }
}
