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

public class signingup extends Activity {
    private FirebaseAuth firebaseAuth;
    private EditText emailinput, passwordinput;
    private Button buttonsignup;
    private FirebaseDatabase fbdata;
    private DatabaseReference fbref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.signingup);

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
                    Toast.makeText(getApplicationContext(), "Enter email please", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password please", Toast.LENGTH_LONG).show();
                    return;
                }

                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(signingup.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete( Task<AuthResult> task) {
                        Toast.makeText(signingup.this, "User Created -> " + "" + task.isSuccessful(), Toast.LENGTH_LONG).show();

                        if (!task.isSuccessful()) {
                            Toast.makeText(signingup.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {

                            adduser();
                            Intent intent = new Intent(signingup.this, mainactivity.class);
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
        Intent intent = new Intent(signingup.this, loggingin.class);
        startActivity(intent);
    }

}
