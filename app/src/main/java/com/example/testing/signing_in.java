package com.example.testing;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signing_in extends AppCompatActivity {
    private EditText emailinput, passwordinput;
    private FirebaseAuth firebaseauth;

    private Button buttonlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            Intent i = new Intent(this, select.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);

        } else {
            // User is signed out
//            Log.d("", "onAuthStateChanged:signed_out");
        }

        emailinput = (EditText) findViewById(R.id.email);
        passwordinput = (EditText) findViewById(R.id.password);
        buttonlogin = (Button) findViewById(R.id.buttonsignin);

        firebaseauth = FirebaseAuth.getInstance();

        buttonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailinput.getText().toString();
                String password = passwordinput.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.enterEmailCorrectly), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.enterPasswordCorrectly), Toast.LENGTH_SHORT).show();
                    return;
                }
                firebaseauth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(signing_in.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (!task.isSuccessful()) {
                                    // there was an error
                                    {
                                        Toast.makeText(signing_in.this, getResources().getString(R.string.enterDetailsCorrectly), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Intent intent = new Intent(signing_in.this, select.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });
    }
    public void signup(View view) {
        Intent intent= new Intent(this, signing_up.class);
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