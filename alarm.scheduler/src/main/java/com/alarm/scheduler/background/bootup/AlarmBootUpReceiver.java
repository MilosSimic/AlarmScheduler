package com.alarm.scheduler.background.bootup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.alarm.scheduler.manager.SettingsManager;

/**
 * Created by milossimic on 12/19/17.
 */

public class AlarmBootUpReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SettingsManager settingsManager = SettingsManager.getInstance(context);

        if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            if (settingsManager.loadOnRestart()) {
                context.startService(new Intent(context, AlarmBootUpService.class));
            }
        }
    }
}
