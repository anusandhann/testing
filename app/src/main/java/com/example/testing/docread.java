package com.example.testing;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;


public class docread extends AppCompatActivity {

    private TextView fileContent;
    private Button gettextfromfolder;
    String information;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.textview);

        fileContent = (TextView) findViewById(R.id.contentoffile);

        gettextfromfolder = (Button) findViewById(R.id.gettext);

        gettextfromfolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputStream ins = getResources().openRawResource(R.raw.lessdata);

                try {
                    byte[] buffer = new byte[ins.available()];
                    while (ins.read(buffer)!=-1){
                        information = new String(buffer);
                    }

                }catch (IOException e){
                    e.printStackTrace();
                }
                fileContent.setText(information);

            }
        });

    }
    }