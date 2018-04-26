package com.hunt.huntsound;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

//Created by Developer on 01.06.17.

public class ServiceClass extends Service {

    public final static int NOTIFY_ID = 5423730;

    @Override
    public void onCreate() {
        super.onCreate();

        Intent notificationIntent = new Intent(this, PrimaryActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Notification notification = new Notification.Builder(this)
                .setContentTitle("Hunting Stereo Sounds")
                .setContentText("Удачной охоты!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(233, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        stopForeground(true);
        stopSelf();

//        Intent myIntent = new Intent(getApplicationContext(), ServiceClass.class);
//        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, myIntent, 0);
//
//        AlarmManager alarmManager1 = (AlarmManager) getSystemService(ALARM_SERVICE);
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.add(Calendar.SECOND, 10);
//
//        alarmManager1.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        //Toast.makeText(getApplicationContext(), "Start PriceBOX Магазин", Toast.LENGTH_SHORT).show();

        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {
        //Toast.makeText(getApplicationContext(), "PriceBOX Магазин закрывается", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}