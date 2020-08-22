package com.example.testing;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class managenotifications extends JobIntentService {
    private final String TAG = "MyService";
    public static final int JOB_ID = 1;

    String timefornotice = String.valueOf(new Date());

    Context context;


    public managenotifications(Context context) {
        this.context = context;
    }

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, managenotifications.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        //this provokes the notification
        //notice(timefornotice);
    }

    public managenotifications(){

    }

    public void createNotification(String aMessage, String newtitle, Context context, Class newclass, String id) {
        final int NOTIFY_ID = 0; // ID of notification
        String groupsNotification = "com.example.testing";

        String CHANNEL_ID = "com.example.testing";
        Intent intent;
        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;
        Log.d("why crash", Context.NOTIFICATION_SERVICE);

        //NotificationManager notificationManager = getSystemService(NotificationManager.class);
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        // notifManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        //notifManager = getSystemService(NotificationManager.class);
        builder = new NotificationCompat.Builder(context, id);
        builder.setChannelId("id");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, newtitle, importance);
            notificationManager.createNotificationChannel(channel);

            builder = new NotificationCompat.Builder(context, id);
            intent = new Intent(context, newclass);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            Notification notification = builder.setOngoing(true)
                    .setContentTitle(newtitle)                            // required
                    .setSmallIcon(R.drawable.icon)   // required
                    .setContentText(aMessage) // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(aMessage)
                    .setGroup(groupsNotification)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setPriority(Notification.PRIORITY_HIGH)
                    .build();
            startForeground(1,notification);
        }


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            int importance = NotificationManager.IMPORTANCE_HIGH;
//            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
//            if (mChannel == null) {
//                mChannel = new NotificationChannel(id, newtitle, importance);
//                mChannel.enableVibration(true);
//                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//                notifManager.createNotificationChannel(mChannel);
//            }
//            builder = new NotificationCompat.Builder(context, id);
//            builder.setChannelId("id");
//            intent = new Intent(context, newclass);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//            builder.setContentTitle(newtitle)                            // required
//                    .setSmallIcon(R.drawable.icon)   // required
//                    .setContentText(aMessage) // required
//                    .setGroup(groupsNotification)
//                    .setDefaults(Notification.DEFAULT_ALL)
//                    .setAutoCancel(true)
//                    .setContentIntent(pendingIntent)
//                    .setTicker(aMessage)
//                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//        } else {
//            builder = new NotificationCompat.Builder(context, id);
//            intent = new Intent(context, newclass);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//            builder.setContentTitle(newtitle)                            // required
//                    .setSmallIcon(R.drawable.icon)   // required
//                    .setContentText(aMessage) // required
//                    .setDefaults(Notification.DEFAULT_ALL)
//                    .setAutoCancel(true)
//                    .setContentIntent(pendingIntent)
//                    .setTicker(aMessage)
//                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
//                    .setPriority(Notification.PRIORITY_HIGH);
//        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Notification notification = builder.build();
            notificationManager.notify(NOTIFY_ID, notification);

        } else
            {
            // User is signed out
            Log.d("", "onAuthStateChanged:signed_out");
        }
    }

    final public void notice(String timefornot) {

        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

        Log.d("current time", timeStamp);

        Log.d("", "notification is sent");

        if (timefornot!= null) {
            createNotification("Please Check the recent activity of the elderly!! ", "MutualMonitor", context, report.class, "7");
        }
//        else if (mHour==9) {
//            createNotification("Did the elderly take medicine today? ", "MutualMonitor", context, report.class, "9");
//        } else if (mHour == 12) {
//            createNotification("Lunch time!! Did the elderly had lunch? ", "MutualMonitor", context, report.class, "14");
//        } else if (mHour == 14) {
//            createNotification("Did you check activities of the elderly today? ", "MutualMonitor", context, report.class, "17");
//        } else if (mHour == 17) {
//            createNotification("Time to check if the elderly had dinner ", "MutualMonitor", context, report.class, "20");
//        }
        else {
            Log.d("", "no notification");
        }
    }
}