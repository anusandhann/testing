package com.example.testing;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

public class mainactivity extends AppCompatActivity {
    private PendingIntent alarmIntent, alarmIntent2;
    AlarmManager alarmManager, alarmManager2;
    private NotificationManagerCompat notManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.start);

        isMyServiceRunning();

        Intent notificationIntent = new Intent(this, servicecheck.class);
        notificationIntent.putExtra("serviceCheck", 32);
       // ContextCompat.startForegroundService(this,notificationIntent );

        Intent userReportIntent = new Intent(this, userreport.class);
        userReportIntent.putExtra("userReportId", 23);
      //  ContextCompat.startForegroundService(this,userReportIntent );

        Intent notificationReceiverIntent = new Intent(this,notificationGeneratorBR.class);
        notificationReceiverIntent.putExtra("notifID", 191);
        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmIntent = PendingIntent.getBroadcast(this, 1, notificationReceiverIntent, 0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(),50 * 1000, alarmIntent);

        Intent recurringNotificationIntent = new Intent(this,recurringNotificationGeneratorBR.class);
        recurringNotificationIntent.putExtra("regularnotifID", 196);
        alarmManager2 = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmIntent2 = PendingIntent.getBroadcast(this, 12, recurringNotificationIntent, 0);
        alarmManager2.setInexactRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(),20 * 1000, alarmIntent2);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String TAG = "Main";
        if (user != null) {
            // User is signed in
            Intent i = new Intent(this, select.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
//            Log.d(TAG, "logged in");

        } else {
            // User is signed out
//            Log.d(TAG, "onAuthStateChanged:signed_out");
        }

   }
    public void signup(View view) {
        Intent intent2= new Intent(this, signingup.class);
        startActivity(intent2);
    }

    public void signin(View view) {
        Intent intent= new Intent(this, signingin.class);
        startActivity(intent);

    }
    private void isMyServiceRunning() {
//        Log.d("", "checkingService run for Permanent notification");

        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (servicecheck.class.getName().equals(service.service.getClassName())) {
//                Log.d("", "yes permanent service running");
                return;
            }
            else{
//                Log.d("", "No the permanent service isnt running");

                Intent serviceCheckIntent = new Intent(this, servicecheck.class);
                ContextCompat.startForegroundService(this,serviceCheckIntent );
            }
        }
    }
}

