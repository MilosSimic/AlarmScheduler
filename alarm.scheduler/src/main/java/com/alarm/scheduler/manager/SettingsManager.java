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

public class SettingsManager {
    private static SettingsManager sInstance;

    public static synchronized SettingsManager getInstance() {
        if (sInstance == null) {
            sInstance = new SettingsManager();
        }
        return sInstance;
    }

    private SettingsManager() {
    }
}
