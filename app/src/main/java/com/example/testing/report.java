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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

//this is the main class..i read the dataset, create graph, and provoke notifications from this class.
public class report extends AppCompatActivity {

    public LineChart chart1, chart2;
    public TextView text1, text2;
    private RadioGroup sleepradio, showerradio, breakfastradio,medicationradio,lunchradio,tvradio,dinnerradio;
    Button submitreport;
    private String TAG = csvread.class.getSimpleName();
    ArrayList<HashMap<String, String>> contactList;
    private ListView lv;
    public CardView card1, card2, card3, card4, card5,card6,card7;
    private RadioButton radioButton;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.report);
        submitreport = (Button) findViewById(R.id.submitreport);

        chart1 = (LineChart) findViewById(R.id.barchart1);
        chart2 = (LineChart)findViewById(R.id.barchart2);

        card1 = (CardView)findViewById(R.id.sleepcard);
        card2 = (CardView)findViewById(R.id.showercard);
        card3= (CardView)findViewById(R.id.breakfastcard);
        card4= (CardView)findViewById(R.id.medicationcard);
        card5= (CardView)findViewById(R.id.lunchcard);
        card6= (CardView)findViewById(R.id.tvcard);
        card7= (CardView)findViewById(R.id.dinnercard);

        submitbutton();
       // drawchart(15,8);
        drawchart2();
        text1 = (TextView)findViewById(R.id.text1);
        text2 =(TextView)findViewById(R.id.text2);

        chart1.setBackgroundColor(Color.WHITE);
        chart1.getDescription().setEnabled(false);
        chart1.setTouchEnabled(false);

        chart2.setBackgroundColor(Color.WHITE);
        chart2.getDescription().setEnabled(false);
        chart2.setTouchEnabled(false);

        contactList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);
        new GetActivity().execute();

    }

    private class GetActivity extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Toast.makeText(report.this, "Json Data is downloading", Toast.LENGTH_LONG).show();
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

                    // looping through All Activities
                    for (int i = 0; i < contacts.length(); i++) {

                        JSONObject c = contacts.getJSONObject(i);

                        String activity = c.getString("actv");
                        String date = c.getString("start_time");

                        String cookduration = c.getString("duration");
                        String eatduration = c.getString("duration");

                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value

                        contact.put("actv", activity);
                        //contact.put("start_time", date);

                        if (activity.equals("Cook")) {
                            contact.put("duration", cookduration);
                            contact.put("start_time", date);

                            int cookingtime = Integer.parseInt(cookduration) / (60 * 1000);
                            if (cookingtime >= 60) {
                                long cookinghour = cookingtime / (60);
                                text1.setText("Sleep Duration: " + cookinghour + " " + "hours and " + cookingtime + " " + "minutes");
                            } else {
                                text1.setText("Sleep Duration: " + cookingtime + "  " + "minutes");
                            }
                            Log.d("duration of cooking", "" + cookingtime);

                            Log.d("start time for cooking", "" + date);


                            drawchart(15,8);
//                            managenotifications not = new managenotifications();
//                            not.notice();
                        }

                        if (activity.equals("Eat")) {
                            contact.put("duration", eatduration);
                            int eatingtime = Integer.parseInt(cookduration) / (60 * 1000);
                            Log.d("Eating time", "" + eatingtime);

                            if (eatingtime >= 60) {
                                long cookinghour = eatingtime / (60);
                                text2.setText("Shower Duration: " + cookinghour + " " + "hours and " + eatingtime + " " + "minutes");
                            } else {
                                text2.setText("Shower Duration: " + eatingtime + "  " + "minutes");
                            }

                            //drawchart2();

                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    managenotifications not = new managenotifications();
                                    not.notice();
                                     }
                            });
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
        }
    }
        public void drawchart(int x, int y) {

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

            chart1.setData(chartdata);
            chart1.invalidate();
        }

        public void drawchart2() {


            ArrayList<Entry> entries = new ArrayList<>();
            entries.add(new Entry(0, 9)); //15 is the value
            entries.add(new Entry(15, 9));

            ArrayList<Entry> entry = new ArrayList<>();
            entry.add(new Entry(7, 0)); //7 is the value
            entry.add(new Entry(7, 7));

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

            chart2.setData(chartdata);
            chart2.invalidate();
        }

        private void showerbutton() {

            showerradio = (RadioGroup) findViewById(R.id.showerbutton);

            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            final FirebaseUser thisuser = FirebaseAuth.getInstance().getCurrentUser();
            final String email = thisuser.getEmail();
            final Object userdr = email + "  " + new Date();

            submitreport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    HashMap<String, Object> map = new HashMap<>();
                    Log.d(TAG, email);
                    String id = ref.push().getKey();

                    radioButton = (RadioButton) findViewById(showerradio.getCheckedRadioButtonId());
                    String radiovalue =  radioButton.getText().toString();
                    Log.d("value    ", radiovalue);

//                    map.put("Shower state", userdr);
//                    ref.child("Reports").child(radiovalue).child(id).updateChildren(map);

                    Intent newintent = new Intent(report.this, mainactivity.class);
                    startActivity(newintent);
                }
            });
        }

//practice code to submit into Firebase

    private void submitbutton() {

        lunchradio = (RadioGroup) findViewById(R.id.lunchbutton);
        dinnerradio = (RadioGroup) findViewById(R.id.dinnerbutton);
        tvradio = (RadioGroup) findViewById(R.id.tvbutton);
        medicationradio = (RadioGroup) findViewById(R.id.medicationbutton);
        breakfastradio = (RadioGroup) findViewById(R.id.breakfastbutton);
        showerradio = (RadioGroup) findViewById(R.id.showerbutton);
        sleepradio = (RadioGroup) findViewById(R.id.sleepbutton);

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        final FirebaseUser thisuser = FirebaseAuth.getInstance().getCurrentUser();
        final String email = thisuser.getEmail();
        final Object userdr = email + "  " + new Date();


        final int selectedstate = lunchradio.getCheckedRadioButtonId();

        submitreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> maps = new HashMap<>();
                String id = ref.push().getKey();

                if((sleepradio.getCheckedRadioButtonId()!= -1) && showerradio.getCheckedRadioButtonId() != -1)
                {
                    Toast.makeText(getApplicationContext(),   "  " + selectedstate, Toast.LENGTH_LONG).show();

                }
            }
        });
    }
    }