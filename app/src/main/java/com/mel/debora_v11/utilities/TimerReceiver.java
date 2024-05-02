package com.mel.debora_v11.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.util.Log;

public class TimerReceiver extends BroadcastReceiver {
    String TAG = "deb11";
    MediaPlayer mp;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: alarm detected!!!!!!!!!!!!!!!!!!!!!!!");
        mp = MediaPlayer.create(context, Settings.System.DEFAULT_ALARM_ALERT_URI);
        mp.setLooping(true);
        mp.start();
    }
}
