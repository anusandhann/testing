package com.example.testing;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;

public class receiver extends BroadcastReceiver {
    private final String TAG = "Receiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "receiver for notification");

        managenotifications.enqueueWork(context, new Intent());

    }
}

