package com.example.testing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signing_up extends Activity {
    private FirebaseAuth firebaseAuth;
    private EditText emailinput, passwordinput;
    private Button buttonsignup;
    private FirebaseDatabase fbdata;
    private DatabaseReference fbref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance();
        buttonsignup = (Button) findViewById(R.id.buttonsignup);
        emailinput = (EditText) findViewById(R.id.email);
        passwordinput = (EditText) findViewById(R.id.password);


        fbdata= FirebaseDatabase.getInstance();
        fbref=fbdata.getReference("Users");

        buttonsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailinput.getText().toString();
                final String password = passwordinput.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.enterEmailCorrectly), Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.enterPasswordCorrectly), Toast.LENGTH_LONG).show();
                    return;
                }

                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(signing_up.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete( Task<AuthResult> task) {
//                        Toast.makeText(signing_up.this, "User Created -> " + "" + task.isSuccessful(), Toast.LENGTH_LONG).show();

                        if (!task.isSuccessful()) {
                            Toast.makeText(signing_up.this, getResources().getString(R.string.pleaseTryAgain), Toast.LENGTH_SHORT).show();
                        } else {

                            adduser();
                            Intent intent = new Intent(signing_up.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });

    }

    public void adduser(){

        String useremail= emailinput.getText().toString();

        String id= fbref.push().getKey();

        user users= new user(id, useremail);

        fbref.child(id).setValue(users);

    }
    public void loginoption(View view) {
        Intent intent = new Intent(signing_up.this, signing_in.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
//        Log.d("CDA", "onBackPressed Called");
        Intent setIntent = new Intent(this, MainActivity.class);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }
}
