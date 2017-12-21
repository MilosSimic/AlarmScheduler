package com.alarm.scheduler.tools;

/**
 * Created by milossimic on 12/19/17.
 */

import android.content.Context;
import android.os.PowerManager;

import static com.alarm.scheduler.db.contract.AlarmContract.APP_TAG;

/**
 * Created by milossimic on 12/19/17.
 */

public abstract class WakeLocker {
    private static PowerManager.WakeLock wakeLock;

    public static void acquire(Context ctx, long timeout) {
        if (wakeLock != null) wakeLock.release();

        PowerManager pm = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);
        if (pm != null) {
            wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                    PowerManager.ACQUIRE_CAUSES_WAKEUP |
                    PowerManager.ON_AFTER_RELEASE, APP_TAG);

            wakeLock.acquire(timeout);
        }
    }

    public static void acquire(Context ctx) {
        if (wakeLock != null) wakeLock.release();

        PowerManager pm = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);
        if (pm != null) {
            wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                    PowerManager.ACQUIRE_CAUSES_WAKEUP |
                    PowerManager.ON_AFTER_RELEASE, APP_TAG);

            wakeLock.acquire();
        }
    }

    public static void release() {
        if (wakeLock != null && wakeLock.isHeld()) wakeLock.release(); wakeLock = null;
    }
}