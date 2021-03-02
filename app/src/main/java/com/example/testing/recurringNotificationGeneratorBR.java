package com.example.testing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class recurringNotificationGeneratorBR extends BroadcastReceiver {
    public static final String CHANNEL_ID = "regularNotification";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent intent1 = new Intent(context,recurringnotification.class);
        context.startForegroundService(intent1);
    }
}
