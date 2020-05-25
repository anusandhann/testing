package com.example.testing;

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

import java.util.Calendar;

public class managenotifications extends JobIntentService {
    private final String TAG = "MyService";
    public static final int JOB_ID = 1;

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, managenotifications.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
            //notice();
    }

    private NotificationManager notifManager;

    public void createNotification(String aMessage, String newtitle, Context context, Class newclass, String id) {
        final int NOTIFY_ID = 0; // ID of notification
        String title = newtitle; // Default Channel
        NotificationManager notifManager = null;

        Intent intent;
        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;
        notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(context, id);
            intent = new Intent(context, newclass);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            builder.setContentTitle(title)                            // required
                    .setSmallIcon(R.drawable.icon)   // required
                    .setContentText(aMessage) // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(aMessage)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        } else {
            builder = new NotificationCompat.Builder(context, id);
            intent = new Intent(context, newclass);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            builder.setContentTitle(title)                            // required
                    .setSmallIcon(R.drawable.icon)   // required
                    .setContentText(aMessage) // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(aMessage)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setPriority(Notification.PRIORITY_HIGH);
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            Notification notification = builder.build();
            notifManager.notify(NOTIFY_ID, notification);

        } else
            {
            // User is signed out
            Log.d("", "onAuthStateChanged:signed_out");
        }
//        Notification notification = builder.build();
//        notifManager.notify(NOTIFY_ID, notification);
    }

    final public void notice() {
        int mHour;
        Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);

        Log.d("", "notification is sent");

        if (mHour > 6 && mHour < 9 ) {
            createNotification("Check when the elderly woke up ", "MutualMonitor", this, report.class, "7");
        } else if (mHour > 8 && mHour < 11) {
            createNotification("Did the elderly take medicine today? ", "MutualMonitor", this, report.class, "9");
        } else if (mHour > 11 && mHour < 14) {
            createNotification("Lunch time!! Did the elderly had lunch? ", "MutualMonitor", this, report.class, "14");
        } else if (mHour > 14 && mHour < 17) {
            createNotification("Did you check activities of the elderly today? ", "MutualMonitor", this, report.class, "17");
        } else if (mHour > 19 && mHour < 22) {
            createNotification("Time to check if the elderly had dinner ", "MutualMonitor", this, report.class, "20");
        }
        else {
            Log.d("", "no notification");
        }
    }


}