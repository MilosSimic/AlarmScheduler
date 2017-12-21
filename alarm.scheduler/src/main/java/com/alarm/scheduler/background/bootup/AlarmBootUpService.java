package com.alarm.scheduler.background.bootup;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.alarm.scheduler.manager.ScheduleManager;
import com.alarm.scheduler.db.DatabaseHelper;
import com.alarm.scheduler.db.contract.AlarmContract;
import com.alarm.scheduler.db.model.Alarm;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by milossimic on 12/19/17.
 */

public class AlarmBootUpService extends IntentService {

    public AlarmBootUpService() {super("AlarmBootUpService");}

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getApplicationContext());
        ScheduleManager scheduleManager = ScheduleManager.getInstance();

        try {
            List<Alarm> alarms = databaseHelper.alarmDao().queryBuilder()
                    .where().eq(AlarmContract.ALARM_ACTIVE, true)
                    .query();

            for (Alarm alarm : alarms) {
                scheduleManager.scheduleAlarm(getApplicationContext(), alarm);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        stopSelf();
    }
}
