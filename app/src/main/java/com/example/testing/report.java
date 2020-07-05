package com.example.testing;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

//this is the main class..i read the dataset, create graph, and provoke notifications from this class.
public class report extends AppCompatActivity {

    public LineChart chart1, chart2;
    public TextView text1, text2;
    RadioButton sleep, shower, bf, lunch, dinner, medication, tv;
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
        //new GetActivity().execute();

        isMyServiceRunning(userreport.class);

        breceiver ar = new breceiver();
        IntentFilter filter = new IntentFilter("cookact");
        registerReceiver(ar,filter);

        startService(new Intent(this, userreport.class));
        Log.d("", "service runing check 1");

    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        Log.d("", "service runing check");

        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
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
            String url = "http://163.221.68.248:8080/api";
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

                           DateFormat format = new SimpleDateFormat("YY-mm-dd HH:mm:ss");
                           Date newdate = format.parse(date);
                          // long dd = Long.parseLong(new SimpleDateFormat("HH:mm").format(newdate));
                          // Double d = Double.parseDouble(dd);


                           Log.d("time time time time", " " + newdate + " and then"  + " hawa hwa hwa" );

                           card1.setVisibility(View.INVISIBLE);
                           //sleepradio.setVisibility(View.INVISIBLE);



                            //drawchart(5.5f, 8.2f);

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

                                     }
                            });
                        }
                        // adding contact to contact list
                        contactList.add(contact);
                    }
                } catch (final JSONException | ParseException e) {
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


    public class breceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {

            if (Objects.equals(intent.getAction(), "cookact")){

                String cookingduration = intent.getStringExtra("cookduration");
                String completiontime =intent.getStringExtra("cookingtime");
                String cookingdate = intent.getStringExtra("cookdate");

                String durr = intent.getStringExtra("dd");
                Log.d("durrrrrrrrr", durr);

                String username = intent.getStringExtra("user");
                Log.d("userrrrrr", username);
                submitbutton(username);

                Bundle extra = intent.getBundleExtra("extra");
                ArrayList<report> transArrayListFromBroadCast = (ArrayList<report>) extra.getSerializable("transArray");
                Log.d("rrrrrrrrr", String.valueOf(transArrayListFromBroadCast));

                Log.d("cooking completed time", completiontime);
                Log.d("cooking date", cookingdate);

                if (Float.valueOf(cookingduration) >= 60) {

                    String cookinghour = Long.valueOf(cookingduration)/60%24 + " Hours" +':' + Long.valueOf(cookingduration)%60 + "  Minutes";

                    Log.d("duration", cookinghour);

                } else {
                    long cookinghour = Long.valueOf(cookingduration);

                    drawchart(cookinghour,cookinghour);

                    Log.d("duration", String.valueOf(cookinghour));
                }
                if (Float.valueOf(cookingduration) == (0)){
                    card1.setVisibility(View.INVISIBLE);
                }
            }
        }
        }

    public void drawchart(float x, float y) {



        ArrayList<Entry> entries = new ArrayList<>();

        entries.add(new Entry(x, y));//x is the value


        LineData chartdata = new LineData();

            LineDataSet lDataSet1 = new LineDataSet(entries, "Average");
            lDataSet1.setColors(R.color.design_default_color_primary);
            lDataSet1.setDrawValues(false);
            lDataSet1.setLineWidth(4);
            chartdata.addDataSet(lDataSet1);


            chart1.setData(chartdata);
            chart1.invalidate();
        }

//practice code to submit into Firebase

    private void submitbutton(String name) {

        final String x= name;
        Log.d("aayo hai ayo", x);


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

        submitreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String, Object> sleepmap = new HashMap<>();
                HashMap<String, Object> showermap = new HashMap<>();
                HashMap<String, Object> medmap = new HashMap<>();
                HashMap<String, Object> bfmap = new HashMap<>();
                HashMap<String, Object> lunchmap = new HashMap<>();
                HashMap<String, Object> dinnermap = new HashMap<>();
                HashMap<String, Object> tvmap = new HashMap<>();

                Log.d(TAG, email);
                String id = ref.push().getKey();
                assert id!= null;

                final int sleepstate = sleepradio.getCheckedRadioButtonId();
                final int showerstate = showerradio.getCheckedRadioButtonId();
                final int medstate = medicationradio.getCheckedRadioButtonId();
                final int bfstate = breakfastradio.getCheckedRadioButtonId();
                final int lunchstate = lunchradio.getCheckedRadioButtonId();
                final int dinnerstate = dinnerradio.getCheckedRadioButtonId();
                final int tvstate = tvradio.getCheckedRadioButtonId();


                sleep = (RadioButton) findViewById(sleepstate);
                shower = (RadioButton) findViewById(showerstate);
                medication = (RadioButton) findViewById(medstate);
                bf = (RadioButton) findViewById(bfstate);
                lunch = (RadioButton) findViewById(lunchstate);
                dinner = (RadioButton) findViewById(dinnerstate);
                tv = (RadioButton) findViewById(tvstate);


                if (sleep.getText().equals("Normal")) {
                    Log.d("value    ", String.valueOf(sleep.getText()));
                    sleepmap.put("Sleep state", userdr);
                    ref.child(x).child("Sleep").child(String.valueOf(sleep.getText())).child(id).updateChildren(sleepmap);
                }
                else if(sleep.getText().equals("Suspicious")) {
                    Log.d("value    ", String.valueOf(sleep.getText()));
                    sleepmap.put("Sleep state", userdr);
                    ref.child(x).child("Sleep").child(String.valueOf(sleep.getText())).child(id).updateChildren(sleepmap);
                }
//                else if(!sleepradio.isSelected()) {
//                    Toast.makeText(getApplicationContext(), "Please check selection1" , Toast.LENGTH_LONG).show();
//                }

                if (shower.getText().equals("Normal")){
                    Log.d("value    ", String.valueOf(shower.getText()));
                    showermap.put("Shower state", userdr);
                    ref.child(x).child("Shower").child(String.valueOf(shower.getText())).child(id).updateChildren(showermap);
                }
                else if(shower.getText().equals("Suspicious")){
                    Log.d("value    ", String.valueOf(shower.getText()));
                    showermap.put("Shower state", userdr);
                    ref.child(x).child("Shower").child(String.valueOf(shower.getText())).child(id).updateChildren(showermap);
                }
//                else if(!showerradio.isSelected()) {
//                    Toast.makeText(getApplicationContext(), "Please check selection2" , Toast.LENGTH_LONG).show();
//                }

                if (bf.getText().equals("Normal")){
                    Log.d("value    ", String.valueOf(bf.getText()));
                    bfmap.put("Breakfast state", userdr);
                    ref.child(x).child("Breakfast").child(String.valueOf(bf.getText())).child(id).updateChildren(bfmap);
                }
                else if (bf.getText().equals("Suspicious")){
                    Log.d("value    ", String.valueOf(bf.getText()));
                    bfmap.put("Breakfast state", userdr);
                    ref.child(x).child("Breakfast").child(String.valueOf(bf.getText())).child(id).updateChildren(bfmap);
                }
//                else if(!breakfastradio.isSelected()) {
//                    Toast.makeText(getApplicationContext(), "Please check selection4" , Toast.LENGTH_LONG).show();
//                }

                if (medication.getText().equals("Normal")){
                    Log.d("value    ", String.valueOf(medication.getText()));
                    medmap.put("Medication state", userdr);
                    ref.child(x).child("Medication").child(String.valueOf(medication.getText())).child(id).updateChildren(medmap);

                }
                else if (medication.getText().equals("Suspicious")){
                    Log.d("value    ", String.valueOf(medication.getText()));
                    medmap.put("Medication state", userdr);
                    ref.child(x).child("Medication").child(String.valueOf(medication.getText())).child(id).updateChildren(medmap);
                }
//                else if(!medicationradio.isSelected()) {
//                    Toast.makeText(getApplicationContext(), "Please check selection3" , Toast.LENGTH_LONG).show();
//                }

                if (lunch.getText().equals("Normal")){
                    Log.d("value    ", String.valueOf(lunch.getText()));
                    lunchmap.put("lunch state", userdr);
                    ref.child(x).child("Lunch").child(String.valueOf(lunch.getText())).child(id).updateChildren(lunchmap);
                }
                else if (lunch.getText().equals("Suspicious")){
                    Log.d("value    ", String.valueOf(bf.getText()));
                    lunchmap.put("lunch state", userdr);
                    ref.child(x).child("Lunch").child(String.valueOf(bf.getText())).child(id).updateChildren(lunchmap);
                }
//                else  if(!lunchradio.isSelected()) {
//                    Toast.makeText(getApplicationContext(), "Please check selection5" , Toast.LENGTH_LONG).show();
//                }

                if (tv.getText().equals("Normal")){
                    Log.d("value    ", String.valueOf(tv.getText()));
                    tvmap.put("TV state", userdr);
                    ref.child(x).child("TV").child(String.valueOf(tv.getText())).child(id).updateChildren(tvmap);
                }
                else if (tv.getText().equals("Suspicious")){
                    Log.d("value    ", String.valueOf(tv.getText()));
                    tvmap.put("TV state", userdr);
                    ref.child(x).child("TV ").child(String.valueOf(tv.getText())).child(id).updateChildren(tvmap);
                }
//                else if (!tvradio.isSelected()) {
//                    Toast.makeText(getApplicationContext(), "Please check selection7" , Toast.LENGTH_LONG).show();
//                }
                if (dinner.getText().equals("Normal")){
                    Log.d("value    ", String.valueOf(dinner.getText()));
                    dinnermap.put("dinner state", userdr);
                    ref.child(x).child("Dinner").child(String.valueOf(dinner.getText())).child(id).updateChildren(dinnermap);
                }
                else if (dinner.getText().equals("Suspicious")){
                    Log.d("value    ", String.valueOf(dinner.getText()));
                    dinnermap.put("dinner state", userdr);
                    ref.child(x).child("Dinner").child(String.valueOf(dinner.getText())).child(id).updateChildren(dinnermap);
                }
//                else if(!dinnerradio.isSelected()) {
//                    Toast.makeText(getApplicationContext(), "Please check selection6" , Toast.LENGTH_LONG).show();
//                }

                Intent newintent = new Intent(report.this, select.class);
                startActivity(newintent);
            }
        });
    }

}