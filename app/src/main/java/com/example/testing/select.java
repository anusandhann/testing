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
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class select extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener fbauth;
    private FirebaseAuth firebaseauth;
    boolean doubleBackToExitPressedOnce = false;

    ListView simpleList;
    String[] targetList = {"","Target 1", "Target 2", "Target 3"};

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

    @Override
    public void onStart(){
        super.onStart();
        firebaseauth.addAuthStateListener(fbauth);

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
                        Intent intent = new Intent(select.this, report.class);
                        intent.putExtra("user", String.valueOf(position));

                        if(position ==0) {

                        }
                        else{
                        startActivity(intent);}
                    }
                });
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
