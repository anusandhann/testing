package com.example.testing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class notificationGeneratorBR extends BroadcastReceiver {
    public static final String CHANNEL_ID = "regularNotification";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
//        Log.d(TAG, "this receiver for usereport works");
//        userreport.enqueueWork(context, new Intent());
        Intent intent1 = new Intent(context,userreport.class);
        context.startForegroundService(intent1);
    }
}
