package com.example.testing;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.UUID;

import static android.content.ContentValues.TAG;
import static android.content.Context.*;
import static androidx.core.content.ContextCompat.getSystemService;

public class recurringNotificationGeneratorBR extends BroadcastReceiver {
    public static final String CHANNEL_RecurringNotification_ID = "recurringNotification";


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {

        int NOTIFICATION_ID = 1234;
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_RecurringNotification_ID, "Recurring Notification", manager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("This is for recurring notification");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.rgb(17, 102, 152));
            notificationChannel.enableVibration(true);
            manager.createNotificationChannel(notificationChannel);
        }

        String currentTime = String.valueOf(Calendar.getInstance().getTime());

        Intent recurringIntent = new Intent(context, select.class);

        recurringIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        recurringIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        recurringIntent.putExtra("recurring_notificationGeneratedTime", currentTime);

        //Pass  PendingIntent to addAction method of Intent Builder
        NotificationCompat.Builder notif = new NotificationCompat.Builder(context, CHANNEL_RecurringNotification_ID);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, UUID.randomUUID().hashCode(), recurringIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Log.e(TAG, "checkingIntent    : " + recurringIntent.getStringExtra("recurring_notificationTime"));

      //  Log.e(TAG, "checkingNotOpenTime     : " + currentTime);

        notif.setSmallIcon(R.drawable.ic_stat_name);
//        notif.setContentText(R.string.recurringnotificationHeader);
        notif.setContentText("Please check recent activity of the elderly.");
        notif.setWhen(System.currentTimeMillis());
        notif.setColor(Color.BLUE);
        notif.setDefaults(Notification.DEFAULT_SOUND);
        notif.setContentIntent(resultPendingIntent);
        notif.setAutoCancel(true);

        int timeOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 6) {

            Log.d("No notification", String.valueOf(timeOfDay));
        }

        else {

            manager.notify(123456, notif.build());
            Log.e("Recurring Notification", String.valueOf(Calendar.getInstance().getTime()));

        }

    }

}
