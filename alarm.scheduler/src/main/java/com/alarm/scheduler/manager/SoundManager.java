package com.alarm.scheduler.manager;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by milossimic on 12/21/17.
 */

public class SoundManager {

    private static SoundManager sInstance;
    private MediaPlayer mp;

    public static synchronized SoundManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SoundManager();
        }
        return sInstance;
    }

    private SoundManager(){
        mp = new MediaPlayer();
    }

    public void setSource(String dataSourcePath, boolean loop){
        try {
            mp.setDataSource(dataSourcePath);
            mp.setLooping(loop);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(){
        mp.start();
    }

    public void stop(){
        mp.stop();
    }
}
