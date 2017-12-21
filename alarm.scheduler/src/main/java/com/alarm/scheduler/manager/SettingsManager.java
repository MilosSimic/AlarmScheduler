package com.alarm.scheduler.manager;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by milossimic on 12/19/17.
 */

public class SettingsManager {

    private static final String APP_SETTINGS = "ALARM_SCHEDULER_APP_SETTINGS";
    public static final String WAKE_UP_SCREEN_PATH = "WAKE_UP_SCREEN_PATH";
    public static final String SHOW_ALARM_NOTIFICATION = "SHOW_ALARM_NOTIFICATION";
    public static final String LOAD_ALARM_ON_RESTART = "LOAD_ALARM_ON_PHONE_RESTART";
    public static final String WAKE_SCREEN_ON_ALARM = "WAKE_SCREEN_ON_ALARM";

    private static SettingsManager sInstance;
    private static SharedPreferences sharedPreferences;

    public static synchronized SettingsManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SettingsManager();
            sharedPreferences = context.getSharedPreferences(APP_SETTINGS, Activity.MODE_PRIVATE);
        }
        return sInstance;
    }

    public String getWakeUpScreen() {
        return sharedPreferences.getString(WAKE_UP_SCREEN_PATH , null);
    }

    public void setWakeUpScreen(String newValue) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(WAKE_UP_SCREEN_PATH , newValue);
        editor.apply();
    }

    public Boolean showNotification() {
        return sharedPreferences.getBoolean(SHOW_ALARM_NOTIFICATION , false);
    }

    public void setShowNotifications(Boolean newValue) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SHOW_ALARM_NOTIFICATION , newValue);
        editor.apply();
    }

    public Boolean loadOnRestart() {
        return sharedPreferences.getBoolean(LOAD_ALARM_ON_RESTART , false);
    }

    public void setLoadOnRestart(Boolean newValue) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(LOAD_ALARM_ON_RESTART , newValue);
        editor.apply();
    }

    public Boolean wakeScreen() {
        return sharedPreferences.getBoolean(WAKE_SCREEN_ON_ALARM , false);
    }

    public void setWakeScreen(Boolean newValue) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(WAKE_SCREEN_ON_ALARM , newValue);
        editor.apply();
    }

    private SettingsManager() {
    }
}
