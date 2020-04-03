package com.example.testing;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class csvread extends AppCompatActivity
{
    private RecyclerView csvread;
public CardView card1;
    LineChart testbarchart;
    private String TAG = csvread.class.getSimpleName();
    private ListView lv;

    ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graphtest);

      //  csvread = (RecyclerView) findViewById(R.id.csvview);
        testbarchart = (LineChart)findViewById(R.id.testbarchart);
card1 = (CardView)findViewById(R.id.sleepcard);
        contactList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);
        new GetContacts().execute();

    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(csvread.this,"Json Data is downloading",Toast.LENGTH_LONG).show();

        }
        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "http://163.221.68.223:8080/api" ;
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr.substring(jsonStr.indexOf("{"), jsonStr.lastIndexOf("}") + 1));

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("result");

                    // looping through All Actv
                    for (int i = 0; i < contacts.length(); i++) {

                        JSONObject c = contacts.getJSONObject(i);

                        String activity = c.getString("actv");
                        String date = c.getString("date");
                        final String cookduration = c.getString("duration");
                        String sleepduration = c.getString("duration");
                        String eatduration = c.getString("duration");


                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value

                        contact.put("actv", activity);
                        contact.put("date", date);

                        if(activity.equals("Cook")){
                            contact.put("duration", cookduration);
                            int cookingtime = Integer.parseInt(cookduration) / (60 * 1000);
                            Log.d("khana pakako time", "" + cookingtime);

                            //report.drawchart();
                        }
                        if(activity.equals("Eat")){
                            contact.put("duration", eatduration);
                            int eatingtime = Integer.parseInt(cookduration)/(60*1000);
                            Log.d("khana khaeko time", "" + eatingtime);

                            Intent eatintent = new Intent(getApplicationContext(), csvread.class);
                            eatintent.putExtra("cookingintent", eatingtime);
                        }

                        Log.d("khana", eatduration);
                        Log.d("pakako", cookduration);

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

            Intent in = getIntent();
            String cookingtime = in.getStringExtra("cookingintent");
            Log.d("arko java classma aaeko", "" + cookingtime);

            ListAdapter adapter = new SimpleAdapter(csvread.this, contactList,
                    R.layout.list_item, new String[]{"actv", "date", "duration"},
                    new int[]{R.id.activity, R.id.date, R.id.duration});
//            lv.setAdapter(adapter);
                drawchart();

        }
    }
    private void drawchart() {

        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");


//        long diffSeconds = duration / 1000 % 60;
//        long diffMinutes = duration / (60 * 1000);
//        long diffHours = duration / (60 * 60 * 1000) % 24;
//        long diffDays = duration / (24 * 60 * 60 * 1000);
//
//        Log.d("now",  " Difference is : "
//
//                + diffDays + "  " + "days" + "  " + diffHours + " " + "hours" + "  "
//
//                + diffMinutes + " "+ "Minutes" + " " + diffSeconds + "  " + "Seconds");

//        ArrayList<Entry> entries = new ArrayList<>();
//        entries.add(new Entry(0, )); //5 is the value
//        entries.add(new Entry(15, R.id.duration));
//
//        ArrayList<Entry> entry = new ArrayList<>();
//        entry.add(new Entry(8, 0)); //8 is the value
//        entry.add(new Entry(8, 8));
//
//        LineData chartdata = new LineData();
//
//        LineDataSet lDataSet1 = new LineDataSet(entries, "Average");
//        lDataSet1.setColors(R.color.design_default_color_primary);
//        lDataSet1.setDrawValues(false);
//        lDataSet1.setLineWidth(4);
//        chartdata.addDataSet(lDataSet1);
//
//
//        LineDataSet lDataSet2 = new LineDataSet(entry, "Today");
//        lDataSet2.setColors(R.color.red);
//        lDataSet2.setLineWidth(4);
//        lDataSet2.setDrawValues(false);
//        chartdata.addDataSet(lDataSet2);
//
//        testbarchart.setData(chartdata);
//        testbarchart.invalidate();
    }


    }

