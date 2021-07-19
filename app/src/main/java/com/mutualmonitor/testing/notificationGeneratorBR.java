package com.mutualmonitor.testing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class notificationGeneratorBR extends BroadcastReceiver {
    public static final String CHANNEL_ID = "regularNotification";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "this receiver for usereport works");
//        userReport.enqueueWork(context, new Intent());
        Intent intent1 = new Intent(context, userReport.class);
        context.startForegroundService(intent1);
    }
}
