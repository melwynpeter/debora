package com.mel.debora_v11.utilities;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;


import com.mel.debora_v11.activities.subactivities.AlarmActivity;

public class AlarmReceiver extends BroadcastReceiver {
    String TAG = "deb11";
    MediaPlayer mp;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: alarm detected!!!!!!!!!!!!!!!!!!!!!!!");
        mp = MediaPlayer.create(context, Settings.System.DEFAULT_ALARM_ALERT_URI);
        mp.setLooping(true);
        mp.start();

        Intent alarmIntent = new Intent(context, AlarmActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("message", "");
        alarmIntent.putExtras(bundle);
        alarmIntent.setFlags(Intent.FLAG_FROM_BACKGROUND);
        alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        alarmIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        context.startActivity(alarmIntent);
    }
}


