package com.mel.debora_v11.utilities;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class TimerService extends Service {

    TimerReceiver timerReceiver;
    String TAG = "deb11";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        timerReceiver = new TimerReceiver();
        // Registers the receiver so that your service will listen for
        // broadcasts
        this.registerReceiver(timerReceiver, intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Do not forget to unregister the receiver!!!
        Log.d(TAG, "onDestroy: service killed");
        this.unregisterReceiver(timerReceiver);
    }
}
