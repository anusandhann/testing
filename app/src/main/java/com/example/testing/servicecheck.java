package com.example.testing;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import static android.content.ContentValues.TAG;


public class servicecheck extends Service {


    public static final String CHANNEL_1_ID = "trying";



    @Override //atstart
    public void onCreate() {
        super.onCreate();
    }


    @Override //everytimewecalltheservice
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("serviceCheck");
        createNotificationChannel();

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setContentTitle(".")
                .setContentText(input)
                .setSmallIcon(R.drawable.icon)
                .setAutoCancel(true)
                .build();

        startForeground(32, notification);

        //do heavy work on a background thread
        //stopSelf();
        return START_NOT_STICKY;

//        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_1_ID, "Contant Notification dot", NotificationManager.IMPORTANCE_LOW);
            channel.setDescription("This is permanent"); //to show to Users That the app is running
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
