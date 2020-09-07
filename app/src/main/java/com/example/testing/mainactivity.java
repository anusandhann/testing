package com.example.testing;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
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

public class mainactivity extends AppCompatActivity {
    private PendingIntent alarmIntent;
    AlarmManager alarmManager;
    private NotificationManagerCompat notManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.start);

        Intent notificationIntent = new Intent(this, servicecheck.class);
        notificationIntent.putExtra("serviceCheck", 32);
        ContextCompat.startForegroundService(this,notificationIntent );

        Intent userReportIntent = new Intent(this, userreport.class);
        userReportIntent.putExtra("userReportId", 23);
       // ContextCompat.startForegroundService(this,userReportIntent );

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String TAG = "Main";
        if (user != null) {
            // User is signed in
            Intent i = new Intent(this, select.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            Log.d(TAG, "logged in");

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
        Intent intent= new Intent(this, signingin.class);
        startActivity(intent);

    }
}

