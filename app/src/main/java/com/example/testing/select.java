package com.example.testing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class select extends AppCompatActivity {
    ListView simpleList;
    String countryList[] = {"", "Target 1", "Target 2", "Target 3", "Target 4", "Target 5", "Target 6"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.select);

        simpleList = (ListView) findViewById(R.id.list);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.listings, R.id.textView, countryList);
        simpleList.setAdapter(arrayAdapter);


        simpleList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(select.this, report.class);
                        startActivity(intent);
                    }


                });

    }

}
