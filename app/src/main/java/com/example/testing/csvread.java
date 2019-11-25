package com.example.testing;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class csvread extends AppCompatActivity
{
    private RecyclerView csvread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.csvview);

        csvread = (RecyclerView) findViewById(R.id.csvview);

        readcsv();
    }

    private List<DataPoint> dailylife = new ArrayList<>();

    private void readcsv() {

        InputStream ins= getResources().openRawResource(R.raw.lessdata);
        BufferedReader bfread = new BufferedReader(new InputStreamReader
                (ins, Charset.forName("UTF-8")));

        String line = "";

        try {
                //step over headers

            bfread.readLine();

            while ( (line = bfread.readLine())!= null) {

              //  Log.d("my activity","Line" + line);

                //split columns

                String[] tokens = line.split(",");

                //Log.d("my activity","Line" + "     " + tokens[0]);

                String a = tokens[0];

                Log.d("my activity","Line" + "     " + a);


                    if (tokens[0] != tokens[1]) {
                        if (tokens[0] == tokens[0]) ;
                       // Log.d("my activity", "bhayo");

                    }

                //read data

                DataPoint sample = new DataPoint();

                try {
                    sample.setCook(Integer.parseInt(tokens[0]));
                }catch (NumberFormatException e){
                   // System.out.println("No no");
                }

                try {
                    sample.setSleep(Integer.parseInt(tokens[1]));
                }catch (NumberFormatException e){
                   // System.out.println("No no");
                }

                try {
                    sample.setTime(tokens[2]);
                }catch (NumberFormatException e){
                  //  System.out.println("No no");
                }


                dailylife.add(sample);

               // Log.d("try", "to read csv" + sample);

                csvread.setAdapter(sample);

            }

    }catch (IOException e){
            e.printStackTrace();
            //Log.wtf("error","cannot read csv" + line , e);
        }
    }

}
