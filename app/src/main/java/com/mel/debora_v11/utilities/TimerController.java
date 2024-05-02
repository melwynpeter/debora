package com.mel.debora_v11.utilities;


import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mel.debora_v11.utilities.database.TimerAdder;

import java.util.Calendar;

public class TimerController {
    AlarmManager alarmManager;

    Context context;
    String TAG = "deb11";

    public TimerController(){}
    public TimerController(Context context){
        this.context = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    private Calendar getCalender(String time){
        String timeArray[] = time.split(":");
        int hourOfDay = Integer.parseInt(timeArray[0]);
        int minute = Integer.parseInt(timeArray[1]);
        int seconds = Integer.parseInt(timeArray[2]);

        Calendar calNow = Calendar.getInstance();
        Calendar calSet = (Calendar) calNow.clone();
        calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calSet.set(Calendar.MINUTE, minute);
        calSet.set(Calendar.SECOND, seconds);
        calSet.set(Calendar.MILLISECOND, 0);

        if(calSet.compareTo(calNow) <= 0){
            calSet.add(Calendar.DATE, 1);
        }
        return calSet;
    }

    @SuppressLint("ScheduleExactAlarm")
    public boolean setTimer(String timerTime){
//        Calendar targetCal = getCalender(alarmTime);
//        int time = Integer.parseInt(alarmTime);
//        long triggerTime = System.currentTimeMillis() + (time * 1000);
//        long setTriggerTime = targetCal.getTimeInMillis();
        String timeArray[] = timerTime.split(":");
        int hourOfDay = Integer.parseInt(timeArray[0]);
        int minute = Integer.parseInt(timeArray[1]);
        int seconds = Integer.parseInt(timeArray[2]);

        int hSeconds = hourOfDay * 3600;
        int mSeconds = minute * 60;
        int totalSeconds = hSeconds + mSeconds + seconds;
        int millis = totalSeconds * 1000;

        long setTriggerTime = System.currentTimeMillis() + millis;
        Log.d(TAG, "setAlarm: " + setTriggerTime);

        Intent intent = new Intent(context, AlarmReceiver.class);

        PendingIntent pi = PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_IMMUTABLE);
        try {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, setTriggerTime, pi);
        }catch (Exception e){

        }

        // Add timer to database
        new Thread(new Runnable() {
            @Override
            public void run() {
                TimerAdder timerAdder = new TimerAdder(context);
                timerAdder.addTimer(timerTime);
            }
        }).start();
        return true;
    }
}
