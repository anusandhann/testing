package com.example.testing;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

public class mainactivity extends Activity {
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    AlarmManager alarmManager;

    private final String TAG = "Main";
    public static final int JOB_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.start);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent notificationIntent = new Intent(this, receiver.class);
        notificationIntent.putExtra("notifId", 1);

        alarmIntent = PendingIntent.getBroadcast(this, 1, notificationIntent, 0);

        Calendar calendar = Calendar.getInstance();

        alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),  55 * 60 * 1000, alarmIntent);


       FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            Intent i = new Intent(this, select.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            Log.d(TAG, "logged");
        } else {
            // User is signed out
            Log.d(TAG, "onAuthStateChanged:signed_out");
        }

   }
    public void signup(View view) {
        Intent intent2= new Intent(this, signingup.class);
        startActivity(intent2);
    }

    public void signin(View view) {
        Intent intent= new Intent(this, select.class);
        startActivity(intent);
    }
}

