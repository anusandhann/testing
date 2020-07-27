package com.example.testing;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

//this is the main class..i read the dataset, create graph, and provoke notifications from this class.
public class report extends AppCompatActivity {

    public CandleStickChart chart1, chart2, chart3, chart4, chart5, chart6, chart7;
    public TextView text1, text2, text3, text4, text5, text6, text7, durationTextview;
    RadioButton sleep, shower, bf, lunch, dinner, medication, tv;
    private RadioGroup sleepradio, showerradio, breakfastradio, medicationradio, lunchradio, tvradio, dinnerradio, thisRadiogp;
    Button submitreport;
    private String TAG = csvread.class.getSimpleName();

    ArrayList<HashMap<String, String>> contactList;
    public CardView card1, card2, card3, card4, card5, card6, card7, thisCard;

    private HashMap<String, TextView> textHash = new HashMap<>();

    private HashMap<String, CardView> cardHash = new HashMap<>();

    private HashMap<String, RadioGroup> radioHash = new HashMap<>();

    private HashMap<String, CandleStickChart> activityChartMap = new HashMap<>();
    private String userStr = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.report);
        submitreport = (Button) findViewById(R.id.submitreport);

        chart1 = (CandleStickChart) findViewById(R.id.sleepLinechart);
        chart2 = (CandleStickChart) findViewById(R.id.ShowerLinechart);
        chart3 = (CandleStickChart) findViewById(R.id.breakfastLinechart);
        chart4 = (CandleStickChart) findViewById(R.id.medicineLinechart);
        chart5 = (CandleStickChart) findViewById(R.id.lunchLinechart);
        chart6 = (CandleStickChart) findViewById(R.id.tvLinechart);
        chart7 = (CandleStickChart) findViewById(R.id.dinnerLinechart);

        YAxis sleepleft = chart1.getAxisLeft();
        sleepleft.setAxisMaximum((float) 26.0);
        sleepleft.setAxisMinimum(4);

        YAxis showerleft = chart2.getAxisLeft();
        showerleft.setAxisMaximum((float) 24.0);
        showerleft.setAxisMinimum(9);

        YAxis bfleft = chart3.getAxisLeft();
        bfleft.setAxisMaximum((float) 11.0);
        bfleft.setAxisMinimum(5);

        YAxis medleft = chart4.getAxisLeft();
        medleft.setAxisMaximum((float) 10.0);
        medleft.setAxisMinimum(6);

        YAxis lunchleft = chart5.getAxisLeft();
        lunchleft.setAxisMaximum((float) 16.0);
        lunchleft.setAxisMinimum(10);

        YAxis tvleft = chart6.getAxisLeft();
        tvleft.setAxisMaximum((float) 20.0);
        tvleft.setAxisMinimum(12);

        YAxis dinnerleft = chart7.getAxisLeft();
        dinnerleft.setAxisMaximum((float) 23.0);
        dinnerleft.setAxisMinimum(16);

        activityChartMap.put("Sleep", chart1);
        activityChartMap.put("Bath", chart2);
        activityChartMap.put("Breakfast", chart3);
        activityChartMap.put("medicine ", chart4); // TODO Remove the space at end next time
        activityChartMap.put("Lunch", chart5);
        activityChartMap.put("TV", chart6);
        activityChartMap.put("Dinner", chart7);

        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);
        text3 = (TextView) findViewById(R.id.text3);
        text4 = (TextView) findViewById(R.id.text4);
        text5 = (TextView) findViewById(R.id.text5);
        text6 = (TextView) findViewById(R.id.text6);
        text7 = (TextView) findViewById(R.id.text7);


        textHash.put("Sleep", text1);
        textHash.put("Bath", text2);
        textHash.put("Breakfast", text3);
        textHash.put("medicine ", text4); // TODO Remove the space at end next time
        textHash.put("Lunch", text5);
        textHash.put("TV", text6);
        textHash.put("Dinner", text7);


        card1 = (CardView) findViewById(R.id.sleepcard);
        card2 = (CardView) findViewById(R.id.showercard);
        card3 = (CardView) findViewById(R.id.breakfastcard);
        card4 = (CardView) findViewById(R.id.medicationcard);
        card5 = (CardView) findViewById(R.id.lunchcard);
        card6 = (CardView) findViewById(R.id.tvcard);
        card7 = (CardView) findViewById(R.id.dinnercard);

        cardHash.put("Sleep", card1);
        cardHash.put("Bath", card2);
        cardHash.put("Breakfast", card3);
        cardHash.put("medicine ", card4);
        cardHash.put("Lunch", card5);
        cardHash.put("TV", card6);
        cardHash.put("Dinner", card7);

        lunchradio = (RadioGroup) findViewById(R.id.lunchbutton);
        dinnerradio = (RadioGroup) findViewById(R.id.dinnerbutton);
        tvradio = (RadioGroup) findViewById(R.id.tvbutton);
        medicationradio = (RadioGroup) findViewById(R.id.medicationbutton);
        breakfastradio = (RadioGroup) findViewById(R.id.breakfastbutton);
        showerradio = (RadioGroup) findViewById(R.id.showerbutton);
        sleepradio = (RadioGroup) findViewById(R.id.sleepbutton);

        radioHash.put("Sleep", sleepradio);
        radioHash.put("Bath", showerradio);
        radioHash.put("Breakfast", breakfastradio);
        radioHash.put("medicine ", medicationradio);
        radioHash.put("Lunch", lunchradio);
        radioHash.put("TV", tvradio);
        radioHash.put("Dinner", dinnerradio);

        contactList = new ArrayList<>();

        isMyServiceRunning(userreport.class);

        breceiver ar = new breceiver();
        IntentFilter filter = new IntentFilter("intentAction");
        registerReceiver(ar, filter);
        // unregisterReceiver(ar);

        Intent passedIntent = getIntent();
        userStr = passedIntent.getStringExtra("user");

       startService(new Intent(this, userreport.class));
        Log.d("", "service running check which might be the cause for error");
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        Log.d("", "service runing check in acitivy");

        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public class breceiver extends BroadcastReceiver {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Objects.equals(intent.getAction(), "intentAction")) {
                // Get username from intent
                String username = intent.getStringExtra("user");
                Log.d("getting username from service", username);

                // Check if this is the username we want
                if (userStr.equals(username)) {
                    String activityDuration = intent.getStringExtra("activityDuration"); //text

                    ArrayList<String> endarray = intent.getStringArrayListExtra("endtimearraylist");
                    Log.d("endtime array", String.valueOf((endarray)));

                    ArrayList<String> durarray = intent.getStringArrayListExtra("durationarraylist");
                    Log.d("duration array", String.valueOf((durarray)));

                    ArrayList<String> startarray = intent.getStringArrayListExtra("starttimearraylist");
                    Log.d("starttime array", String.valueOf((startarray)));

                    ArrayList<String> datearray = intent.getStringArrayListExtra("datelist");
                    Log.d("datearray", String.valueOf((datearray)));

                    ArrayList<String> endtimeArray = intent.getStringArrayListExtra("endtimeList");
                    Log.d("endtimeList array", String.valueOf((endtimeArray)));

                    String activityType = intent.getStringExtra("actv");
                    Log.d("getting activity from service", activityType);

                    submitbutton(username);

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Setting date format

                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // Setting time format

                    LocalDate startDate = LocalDate.parse(datearray.get(0));

                    LocalDate today = LocalDate.now();
                    LocalTime currentTime = LocalTime.now();

                    Log.d("current localtime", String.valueOf(currentTime));

                    Log.d("startdatelocal", String.valueOf(startDate));
                    Log.d("dayoftheyear", String.valueOf(today.getDayOfYear()));

                    int enddateindex = (today.getDayOfYear() % (datearray.size()-2)) + 2;
                    String activitydur = durarray.get(enddateindex);
                    Log.d("activitydur", String.valueOf(activitydur));

                    LocalDate endDate = LocalDate.parse(datearray.get(enddateindex));
                    Log.d("enddatelocal", String.valueOf(endDate));

                    Log.d("startttt", String.valueOf(endDate.getDayOfWeek().ordinal()));

                    ArrayList<String> modStartArray = new ArrayList<>();
                    ArrayList<String> modEndArray = new ArrayList<>();
                    ArrayList<String> prevModStartArray = new ArrayList<>();
                    ArrayList<String> prevModEndArray = new ArrayList<>();

                    //for graph where activity has been completed already for today
                    for(int i = 0; i < datearray.size(); i++){
                        String datestr = datearray.get(i);
                        LocalDate d = LocalDate.parse(datestr, formatter);
                        if((d.isAfter(startDate) || d.isEqual(startDate)) && (d.isBefore(endDate)|| d.isEqual(endDate))){
                            modEndArray.add(endarray.get(i));
                            modStartArray.add(startarray.get(i));
                        }
                    }
                    //for graph where activity has not been completed for today (one less than Today's data)
                    for(int i = 0; i < datearray.size(); i++){
                        LocalDate PrevEndDate = LocalDate.parse(datearray.get(enddateindex-1));
                        Log.d("PrevEndDate", String.valueOf(PrevEndDate));
                        String datestr = datearray.get(i);
                        LocalDate d = LocalDate.parse(datestr, formatter);
                        if((d.isAfter(startDate) || d.isEqual(startDate)) && (d.isBefore(PrevEndDate)|| d.isEqual(PrevEndDate))){
                            prevModEndArray.add(endarray.get(i));
                            prevModStartArray.add(startarray.get(i));
                        }
                    }

                    durationTextview = textHash.get(activityType);
                    thisRadiogp = radioHash.get(activityType);
                    thisCard = cardHash.get(activityType);

                    if (Float.parseFloat(activitydur) >= 60) {

                        String durationOfActivity = Long.parseLong(activitydur) / 60 % 24 + " Hours" + ":  " + Long.parseLong(activitydur) % 60 + "  Minutes";
                        assert durationTextview != null;
                        Log.d("durationOfActivity", (durationOfActivity));

                        String actvTime = endtimeArray.get(enddateindex);
                        Log.d("compareactivitytime", (actvTime));

                        LocalDateTime lct = LocalDateTime.parse(actvTime, timeFormatter);
                        LocalTime actTime = lct.toLocalTime(); //get activity completion time in localtime format

                        if (currentTime.isAfter(actTime)) {
                            thisCard.setVisibility(View.VISIBLE);
                            drawchart(modStartArray, modEndArray, datearray, activityType);
                            durationTextview.setText("Duration of Activity :  " + durationOfActivity);
                            }
                        else {
                           // thisRadiogp.setVisibility(View.GONE);
                            drawchart(prevModStartArray , prevModEndArray, datearray, activityType);
                            durationTextview.setVisibility(View.INVISIBLE);
                        }
                    }
                    else {

                        long durationOfActivity = Long.parseLong(activitydur);
                        assert durationTextview != null;

                        Log.d("durationOfActivity", String.valueOf(durationOfActivity));

                        String actvTime = endtimeArray.get(enddateindex);
                        Log.d("compareactivitytime2", (actvTime));

                        LocalDateTime lct = LocalDateTime.parse(actvTime,timeFormatter);
                        LocalTime actTime = lct.toLocalTime(); //get activity completion time in localtime format

                        if(currentTime.isAfter(actTime)){
                            thisCard.setVisibility(View.VISIBLE);
                            durationTextview.setText("Duration of Activity :  " + activitydur + "  minutes");
                            drawchart(modStartArray, modEndArray, datearray, activityType);
                            }
                            else
                            { //thisRadiogp.setVisibility(View.INVISIBLE);
                                drawchart(prevModStartArray , prevModEndArray, datearray, activityType);
                            }
                    }
                }
            }
        }


        public void drawchart(ArrayList<String> x, ArrayList<String> y, ArrayList<String> z, String activityType) {

            ArrayList<CandleEntry> candleEntryTry = new ArrayList<CandleEntry>();

            for (int i = 0; i < x.size(); i++) {
                candleEntryTry.add(new CandleEntry(i, 23, 0, Float.parseFloat(x.get(i)), Float.parseFloat(y.get(i))));
                Log.d("graph thing", x.get(i));
            }


            CandleStickChart selectedChart = activityChartMap.get(activityType);
            assert selectedChart != null;
            selectedChart.setBackgroundColor(Color.WHITE);
            selectedChart.getDescription().setEnabled(false);
            selectedChart.setTouchEnabled(false);

            selectedChart.notifyDataSetChanged();

            selectedChart.getAxisRight().setDrawLabels(true);

            selectedChart.setDrawBorders(true);
            selectedChart.setHighlightPerDragEnabled(true);
            selectedChart.setBorderColor(getResources().getColor((R.color.borderofGraph)));

            XAxis xAxis = selectedChart.getXAxis();
            xAxis.setValueFormatter(new MyXAxisValueFormatter(z));

            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

            xAxis.setDrawGridLines(false);
            xAxis.setDrawLabels(true);
            xAxis.setGranularityEnabled(true);
            xAxis.setGranularity(1f);
            xAxis.setAvoidFirstLastClipping(true);

            YAxis leftAxis = selectedChart.getAxisLeft();
            YAxis rightAxis = selectedChart.getAxisRight();
            leftAxis.setValueFormatter(new MyYAxisValueFormatter());


            rightAxis.setTextColor(Color.WHITE);
            leftAxis.setDrawGridLines(false);
            rightAxis.setDrawGridLines(false);

            selectedChart.requestDisallowInterceptTouchEvent(true);

            Legend l = selectedChart.getLegend();
            l.setEnabled(false);

            CandleDataSet set1 = new CandleDataSet(candleEntryTry, "DataSet");

            set1.setColor(Color.rgb(80, 80, 80));
            set1.setShadowColor(getResources().getColor(R.color.textforApp));  //need to change to WHITE later
            set1.setShadowWidth(0.8f);
            set1.setDecreasingColor(getResources().getColor(R.color.colorinGraph));
            set1.setDecreasingPaintStyle(Paint.Style.FILL);
            set1.setIncreasingColor(getResources().getColor(R.color.colorinGraph));
            set1.setIncreasingPaintStyle(Paint.Style.FILL);
            set1.setNeutralColor(Color.LTGRAY);
            set1.setDrawValues(false);
            CandleData data = new CandleData(set1);
            selectedChart.setData(data);

            selectedChart.invalidate();
        }

//practice code to submit into Firebase

        private void submitbutton(String name) {

            final String x = name;


            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            final FirebaseUser thisuser = FirebaseAuth.getInstance().getCurrentUser();
            assert thisuser != null;
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
                    assert id != null;

                    final int sleepstate = sleepradio.getCheckedRadioButtonId();
                    final int showerstate = showerradio.getCheckedRadioButtonId();
                    final int medstate = medicationradio.getCheckedRadioButtonId();
                    final int bfstate = breakfastradio.getCheckedRadioButtonId();
                    final int lunchstate = lunchradio.getCheckedRadioButtonId();
                    final int dinnerstate = dinnerradio.getCheckedRadioButtonId();
                    final int tvstate = tvradio.getCheckedRadioButtonId();


                    shower = (RadioButton) findViewById(showerstate);
                    medication = (RadioButton) findViewById(medstate);
                    bf = (RadioButton) findViewById(bfstate);
                    lunch = (RadioButton) findViewById(lunchstate);
                    dinner = (RadioButton) findViewById(dinnerstate);
                    tv = (RadioButton) findViewById(tvstate);

                    if (sleepradio!=null && sleepradio.isEnabled() && sleepstate!= -1){
                        sleep = (RadioButton) findViewById(sleepstate);
                        Log.d("value   ", String.valueOf(sleep.getText()));
                        sleepmap.put("Sleep state", userdr);
                        ref.child(x).child("Sleep").child(String.valueOf(sleep.getText())).child(id).updateChildren(sleepmap); }

                     if (showerradio!=null && showerradio.isEnabled() && showerstate!= -1){
                        Log.d("value ", String.valueOf(shower.getText()));
                        showermap.put("Shower state", userdr);
                        ref.child(x).child("Shower").child(String.valueOf(shower.getText())).child(id).updateChildren(showermap);
                    }
                     if (breakfastradio!=null && breakfastradio.isEnabled() && bfstate!= -1){
                        Log.d("value   ", String.valueOf(bf.getText()));
                        bfmap.put("Breakfast state", userdr);
                        ref.child(x).child("Breakfast").child(String.valueOf(bf.getText())).child(id).updateChildren(bfmap);
                    }
                     if (medicationradio!=null && medicationradio.isEnabled() && medstate!= -1){
                        Log.d("value   ", String.valueOf(medication.getText()));
                        medmap.put("Medication state", userdr);
                        ref.child(x).child("Medication").child(String.valueOf(medication.getText())).child(id).updateChildren(medmap);
                    }
                     if (lunchradio!=null && lunchradio.isEnabled() && lunchstate!= -1){
                        Log.d("value   ", String.valueOf(lunch.getText()));
                        lunchmap.put("lunch state", userdr);
                        ref.child(x).child("Lunch").child(String.valueOf(lunch.getText())).child(id).updateChildren(lunchmap);
                    }
                     if (tvradio!=null && tvradio.isEnabled() && tvstate!= -1){
                        Log.d("value   ", String.valueOf(tv.getText()));
                        tvmap.put("TV state", userdr);
                        ref.child(x).child("TV").child(String.valueOf(tv.getText())).child(id).updateChildren(tvmap);
                    }
                     if (dinnerradio!=null && dinnerradio.isEnabled() && dinnerstate!= -1){
                        Log.d("value   ", String.valueOf(dinner.getText()));
                        dinnermap.put("dinner state", userdr);
                        ref.child(x).child("Dinner").child(String.valueOf(dinner.getText())).child(id).updateChildren(dinnermap);
                    }

                    Intent newintent = new Intent(report.this, select.class);
                    startActivity(newintent);
                }
            });
        }

        private class MyYAxisValueFormatter implements IAxisValueFormatter {
            public MyYAxisValueFormatter() {
            }

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                //return mFormat.format(value) + "am";
                return (int) value + ":00";
            }
        }

        private class MyXAxisValueFormatter implements IAxisValueFormatter {
            private ArrayList<String> mValues;


            public MyXAxisValueFormatter(ArrayList<String> values) {
                this.mValues = values;

                for(int i =0; i< mValues.size();i++){
                    String newval = mValues.get(i).replace("2019-", "");
                    mValues.set(i, newval);
               }
            }

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mValues.get((int) value);
            }
        }
    }
}