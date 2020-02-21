package com.example.testing;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class report extends AppCompatActivity {

    public LineChart chart1;
    public TextView text1;
    private RadioGroup statebutton;
    Button submitreport;
    private String TAG = csvread.class.getSimpleName();
    ArrayList<HashMap<String, String>> contactList;
    private ListView lv;
    public CardView card1;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.report);
        card1 = (CardView)findViewById(R.id.card1);

        chart1 = (LineChart) findViewById(R.id.barchart1);
        text1 = (TextView)findViewById(R.id.text1);
        chart1.setBackgroundColor(Color.WHITE);
        chart1.getDescription().setEnabled(false);
        chart1.setTouchEnabled(false);
        contactList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);
        new GetContacts().execute();

    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(report.this, "Json Data is downloading", Toast.LENGTH_LONG).show();

        }

        @SuppressLint("SetTextI18n")
        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "http://163.221.68.223:8080/api";
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
                        String date = c.getString("start_time");
                        final String cookduration = c.getString("duration");
                        String sleepduration = c.getString("duration");
                        String eatduration = c.getString("duration");


                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value

                        contact.put("actv", activity);
                        contact.put("start_time", date);

                        if (activity.equals("Cook")) {
                            contact.put("duration", cookduration);

                            int cookingtime = Integer.parseInt(cookduration) / (60 * 1000);
                            if(cookingtime >= 60){
                                long cookinghour = cookingtime / (60);
                                text1.setText("Sleep Duration: " + cookinghour + " " + "hours and " + cookingtime + " " + "minutes");
                            }
                            else{
                                text1.setText("Sleep Duration: " + cookingtime + "  " + "minutes");
                            }
                            Log.d("khana pakako time", "" + cookingtime);

                            Log.d("khana pakako suruwat", "" + date);

                            drawchart();
                            buttonreader();
                        }

                        if (activity.equals("Eat")) {
                            contact.put("duration", eatduration);
                            int eatingtime = Integer.parseInt(cookduration) / (60 * 1000);
                            Log.d("khana khaeko time", "" + eatingtime);

                            Intent eatintent = new Intent(getApplicationContext(), csvread.class);
                            eatintent.putExtra("cookingintent", eatingtime);
                        }

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

            ListAdapter adapter = new SimpleAdapter(report.this, contactList,
                    R.layout.list_item, new String[]{"actv", "start_time", "duration"},
                    new int[]{R.id.activity, R.id.date, R.id.duration});
//            lv.setAdapter(adapter);
            drawchart();

        }

        public void drawchart() {


            ArrayList<Entry> entries = new ArrayList<>();
            entries.add(new Entry(0, 5)); //5 is the value
            entries.add(new Entry(15, 5));

            ArrayList<Entry> entry = new ArrayList<>();
            entry.add(new Entry(8, 0)); //8 is the value
            entry.add(new Entry(8, 8));

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

            chart1.setData(chartdata);
            chart1.invalidate();
        }

        private void buttonreader() {

            statebutton = (RadioGroup) findViewById(R.id.radiostate);
            submitreport = (Button) findViewById(R.id.submitreport);

            final int selectedstate = statebutton.getCheckedRadioButtonId();

            submitreport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Toast.makeText(report.this, submitreport.getText(), Toast.LENGTH_LONG).show();
                    Intent newintent = new Intent(report.this, mainactivity.class);
                    startActivity(newintent);
                }
            });

        }

    }
}