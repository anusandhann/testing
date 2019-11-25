package com.example.testing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class mainactivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.start);
    }

    public void signin(View view )
    {
        Intent intent= new Intent(this, select.class);
        startActivity(intent);
    }
    public void signup(View view )
    {
        Intent intent1= new Intent(this, csvread.class);
        startActivity(intent1);
    }
}