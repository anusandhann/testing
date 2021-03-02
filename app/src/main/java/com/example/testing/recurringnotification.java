package com.example.testing;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.Calendar;

import static android.content.ContentValues.TAG;


public class recurringnotification extends Service {

    public static final int notification_id = 2;
    public static final String CHANNEL_RecurringNotification_ID = "trying";

    @Override //atstart
    public void onCreate() {
        super.onCreate();
    }

    @Override //everytimewecalltheservice
    public int onStartCommand(Intent intent, int flags, int startId) {

        String input = intent.getStringExtra("regularnotifID");

        Log.d(TAG, "checkingchecking: " + input );

        createNotificationChannel();


        Intent recurringnotificationIntent = new Intent(this, select.class);

        recurringnotificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingRecurringIntent = PendingIntent.getActivity(this, 0, recurringnotificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification recurringNotification = new NotificationCompat.Builder(this, CHANNEL_RecurringNotification_ID)
                .setContentTitle("MutualMonitor")
                .setContentText("Please have a look at the elderly o!!")
                .setSmallIcon(R.drawable.notificationlogo)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingRecurringIntent)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        //startForeground(42, recurringNotification);


        int timeOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 6) {

            Log.d("huhu", String.valueOf(timeOfDay));

        } else {

            notificationManager.notify(notification_id, recurringNotification);

        }
//        notificationManager.notify(notification_id, recurringNotification);

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
            NotificationChannel channel = new NotificationChannel(CHANNEL_RecurringNotification_ID, "Frequent Notification", NotificationManager.IMPORTANCE_LOW);
            channel.setDescription("This is recurring"); //to show to Users That the app is running
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
