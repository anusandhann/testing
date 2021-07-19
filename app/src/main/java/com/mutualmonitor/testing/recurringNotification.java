package com.mutualmonitor.testing;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


import androidx.core.app.NotificationManagerCompat;

public class recurringNotification extends BroadcastReceiver {

    public static final String CHANNEL_recurring_ID = "recurringNotification";


    public static final int notification_id = 1;
    private NotificationManagerCompat notifManager;
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(123456);
    }
}


