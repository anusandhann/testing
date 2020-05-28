package com.example.testing;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

//This is a practice class... i try to check different codes in this class.

public class csvread extends AppCompatActivity
{
    RecyclerView csvread;
    public CardView card1;
    LineChart testbarchart;
    private String TAG = csvread.class.getSimpleName();
    private ListView lv;

    ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graphtest);

        //csvread = (RecyclerView) findViewById(R.id.csvview);
        testbarchart = (LineChart)findViewById(R.id.testbarchart);
        card1 = (CardView)findViewById(R.id.sleepcard);
        contactList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);
        new GetContacts().execute();

        testbarchart.setBackgroundColor(Color.WHITE);
        testbarchart.getDescription().setEnabled(false);
        testbarchart.setTouchEnabled(false);

    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Toast.makeText(csvread.this,"Json Data is downloading",Toast.LENGTH_LONG).show();

        }
        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "https://api.androidhive.info/contacts/" ;
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr.substring(jsonStr.indexOf("{"), jsonStr.lastIndexOf("}") + 1));

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("contacts");

                    // looping through All Actvities
                    for (int i = 0; i < contacts.length(); i++) {

                        JSONObject c = contacts.getJSONObject(i);

                        String name = c.getString("name");
                        String email = c.getString("email");
//

                        //Phone node is JSON Object
                        JSONObject phone = c.getJSONObject("phone");
                        String mobile = phone.getString("mobile");
//

                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        if (name.equals("Will Smith")){
                            Log.d(TAG, "can read json from server");

                            drawchart(15,8);
                            managenotifications not = new managenotifications();
                            not.notice();
                        }

                        // adding each child node to HashMap key => value
//                        contact.put("id", id);
                        contact.put("name", name);
                        contact.put("email", email);
                        // contact.put("mobile", mobile);

                        // adding contact to contact list
                        contactList.add(contact);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
//            ListAdapter adapter = new SimpleAdapter(csvread.this, contactList,
//                    R.layout.list_item, new String[]{"name","email"},
//                    new int[]{R.id.name, R.id.email});
//            lv.setAdapter(adapter);
        }
    }
        public void drawchart( int x, int y){

            ArrayList<Entry> entries = new ArrayList<>();
            entries.add(new Entry(0, 9)); //x is the value
            entries.add(new Entry(x, 9));

            ArrayList<Entry> entry = new ArrayList<>();
            entry.add(new Entry(8, 0)); //y is the value
            entry.add(new Entry(8, y));

            LineData chartdata = new LineData();

            LineDataSet lDataSet1 = new LineDataSet(entries, "Average");
            lDataSet1.setColors(R.color.design_default_color_primary);
            lDataSet1.setDrawValues(false);
            lDataSet1.setLineWidth(4);
            chartdata.addDataSet(lDataSet1);


            LineDataSet lDataSet2 = new LineDataSet(entry, "Today");
            lDataSet2.setColor(R.color.graph);
            lDataSet2.setLineWidth(4);
            lDataSet2.setDrawValues(false);
            chartdata.addDataSet(lDataSet2);

            testbarchart.setData(chartdata);
            testbarchart.invalidate();
        }

    }

