package com.example.testing;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;

public class select extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener fbauth;
    private FirebaseAuth firebaseauth;


    ListView simpleList;
    String[] targetList = {"","Taro", "Francis", "Hyuckjin"};


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.select);

        firebaseauth= FirebaseAuth.getInstance();
        fbauth =new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser user= firebaseAuth.getCurrentUser();
                if (user == null){
                    startActivity(new Intent(select.this, mainactivity.class));
                    finish();
                }
                else
                {  choosetarget();
                }
            }
        };
    };



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onStart(){
        super.onStart();
        firebaseauth.addAuthStateListener(fbauth);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();

        Log.e("TAG", "--------------->>");

        Intent recurringNotIntent = getIntent();
        String recNotificationGeneratedTime = recurringNotIntent.getStringExtra("recurring_notificationGeneratedTime");

        if (recurringNotIntent.hasExtra("recurring_notificationGeneratedTime")){

        Intent recurringIntent = new Intent(this, report.class);

        recurringIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        recurringIntent.putExtra("recurring_notificationClickTime", recNotificationGeneratedTime + " &  " + Calendar.getInstance().getTime() );

        final String recNotClicktimeX = recurringIntent.getStringExtra("recurring_notificationClickTime");

        Log.e("TAG", "recurring notification click time" + " --> " + recNotClicktimeX);

        Log.e("TAG", " <<---------------");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (fbauth != null) {
            firebaseauth.removeAuthStateListener(fbauth);
        }
    }
    public void choosetarget () {

        simpleList = (ListView) findViewById(R.id.list);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.listings, R.id.textView, targetList);
        simpleList.setAdapter(arrayAdapter);

        simpleList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                        Intent recurringNotIntent = getIntent();
                        String recNotificationGeneratedtime = recurringNotIntent.getStringExtra("recurring_notificationGeneratedTime");
                        Log.e("TAG", "this is to test : " + " notification generated time -->" + recNotificationGeneratedtime + "  clicked time -->" + Calendar.getInstance().getTime());

                        //here for rN opening
                        if (recurringNotIntent.hasExtra("recurring_notificationGeneratedTime")) {

                            String recNotificationClickTime = recNotificationGeneratedtime;

                            Bundle nClicktime = new Bundle();
                            nClicktime.putString("recurringNotification_generatedANDclick", recNotificationClickTime);

                            Intent intent = new Intent(select.this, report.class);
                            intent.putExtras(nClicktime);

                            intent.putExtra("user", String.valueOf(position));

                            if (position == 0) {

                            } else {
                                startActivity(intent);
                            }
                        }
                        else
                        {
                            String freeWillOpeningTime = String.valueOf(Calendar.getInstance().getTime());

                            Bundle freeWillOpen = new Bundle();
                            freeWillOpen.putString("FreeWillOpeningMutualMonitor", freeWillOpeningTime);

                            Log.e("TAG", "onItemClick: freewillopening " +  freeWillOpeningTime );

                            Intent intent = new Intent(select.this, report.class);
                            intent.putExtras(freeWillOpen);

                            intent.putExtra("user", String.valueOf(position));

                            if (position == 0) {

                            } else {
                                startActivity(intent);
                            }
                        }

                        //here for abN opening

                        //here for free will opening
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void signout(View view) {
        Intent intent = new Intent(this, mainactivity.class);
        firebaseauth.signOut();
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
       finishAffinity();
    }


}
