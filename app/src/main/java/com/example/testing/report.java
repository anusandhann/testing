package com.example.testing;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
import androidx.core.content.ContextCompat;

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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

//this is the main class..i read the dataset, create graph, and provoke notifications from this class.
@RequiresApi(api = Build.VERSION_CODES.O)
public class report extends AppCompatActivity {

    public CandleStickChart chart1, chart2, chart3, chart4, chart5, chart6, chart7;
    public TextView text1, text2, text3, text4, text5, text6, text7, durationTextview;
    RadioButton sleep, shower, breakfast, lunch, dinner, medication, tv;
    private RadioGroup sleepradio, showerradio, breakfastradio, medicationradio, lunchradio, tvradio, dinnerradio, thisRadiogp;
    Button submitreport;

    ArrayList<HashMap<String, String>> contactList;
    public CardView card1, card2, card3, card4, card5, card6, card7, thisCard;

    private HashMap<String, TextView> textHash = new HashMap<>();

    private HashMap<String, CardView> cardHash = new HashMap<>();

    private HashMap<String, RadioGroup> radioHash = new HashMap<>();

    private HashMap<String, CandleStickChart> activityChartMap = new HashMap<>();
    private String userStr = null;
    public static final String mypreference = "mypref";
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    breceiver ar = new breceiver();

    @RequiresApi(api = Build.VERSION_CODES.O)
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

        isMyServiceRunning();

        IntentFilter filter = new IntentFilter("intentAction");
        registerReceiver(ar, filter);

        Intent passedIntent = getIntent();
        userStr = passedIntent.getStringExtra("user");

        //why do i need to call userreport class in this class..doesnt work if i dont!!
        Intent userReportIntent = new Intent(this, userreport.class);
        userReportIntent.putExtra("userReportId", "");
       // ContextCompat.startForegroundService(this,userReportIntent );

        startService(new Intent(this, userreport.class));
        Log.d("", "service running check which might be the cause for error");

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(ar);
        super.onDestroy();

    }

    private void isMyServiceRunning() {
        Log.d("", "service runing check in activity");

        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (userreport.class.getName().equals(service.service.getClassName())) {
                Log.d("", "yes it is running");
                return;
            }
            else{
                Log.d("", "No the service isnt running");

                Intent userReportIntent = new Intent(this, userreport.class);
                ContextCompat.startForegroundService(this,userReportIntent );
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)

    public class breceiver extends BroadcastReceiver{
        @RequiresApi(api = Build.VERSION_CODES.O)
        @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
        final FirebaseUser thisuser = FirebaseAuth.getInstance().getCurrentUser();
        final String useremail = thisuser.getEmail();

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
//                    Log.d("endtime array", String.valueOf((endarray)));

                    ArrayList<String> durarray = intent.getStringArrayListExtra("durationarraylist");
//                    Log.d("duration array", String.valueOf((durarray)));

                    ArrayList<String> startarray = intent.getStringArrayListExtra("starttimearraylist");
//                    Log.d("starttime array", String.valueOf((startarray)));

                    ArrayList<String> datearray = intent.getStringArrayListExtra("datelist");
                    //Log.d("datearray", String.valueOf((datearray)));

                    ArrayList<String> endtimeArray = intent.getStringArrayListExtra("endtimeList");
//                    Log.d("endtimeList array", String.valueOf((endtimeArray)));

                    String activityType = intent.getStringExtra("actv");
//                    Log.d("getting activity from service", activityType);

                    submitbutton(username);

                    preferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
                    editor= preferences.edit();

                    String sleepusername = preferences.getString("sleepPrefuser","");
                    String sleepmonitor = preferences.getString("sleepPrefmonitor", "");
                    String sleepresponsedate = preferences.getString("sleepPrefdate","");
                    String sleepresponse = preferences.getString("sleepPrefresponse","");

                    String showerusername = preferences.getString("showerPrefuser","");
                    String showermonitor = preferences.getString("showerPrefmonitor", "");
                    String showerresponsedate = preferences.getString("showerPrefdate","");
                    String showerresponse = preferences.getString("showerPrefresponse","");

                    String medicationusername = preferences.getString("medicationPrefuser","");
                    String medicationmonitor = preferences.getString("medicationPrefmonitor", "");
                    String medicationresponsedate = preferences.getString("medicationPrefdate","");
                    String medicationresponse = preferences.getString("medicationPrefresponse","");

                    String breakfastusername = preferences.getString("breakfastPrefuser","");
                    String breakfastmonitor = preferences.getString("breakfastPrefmonitor", "");
                    String breakfastresponsedate = preferences.getString("breakfastPrefdate","");
                    String breakfastresponse = preferences.getString("breakfastPrefresponse","");

                    String lunchusername = preferences.getString("lunchPrefuser","");
                    String lunchmonitor = preferences.getString("lunchPrefmonitor", "");
                    String lunchresponsedate = preferences.getString("lunchPrefdate","");
                    String lunchresponse = preferences.getString("lunchPrefresponse","");

                    String tvusername = preferences.getString("tvPrefuser","");
                    String tvmonitor = preferences.getString("tvPrefmonitor", "");
                    String tvresponsedate = preferences.getString("tvPrefdate","");
                    String tvresponse = preferences.getString("tvPrefresponse","");

                    String dinnerusername = preferences.getString("dinnerPrefuser","");
                    String dinnermonitor = preferences.getString("dinnerPrefmonitor", "");
                    String dinnerresponsedate = preferences.getString("dinnerPrefdate","");
                    String dinnerresponse = preferences.getString("dinnerPrefresponse","");

                    String thisdate = String.valueOf(LocalDate.now());
//                    Log.d("harihari localdate  ", thisdate);
//                    Log.d("harihari monitor name  ", useremail);


                    if((thisdate.equals( sleepresponsedate )) && username.equals(sleepusername) && useremail.equals(sleepmonitor)) {
                        if ((preferences.contains("sleepPrefresponse"))){
                            Log.d("checking sleep shared preference   ", sleepresponse);
                            sleepradio.setVisibility(View.GONE);
                        }
                    }
                    else {
                        Log.d("checking if date not same shared preference   ", sleepusername);
                    }

                    if((thisdate.equals( showerresponsedate )) && username.equals(showerusername) && useremail.equals(showermonitor)) {
                        if ((preferences.contains("sleepPrefresponse"))){
                            Log.d("checking shower shared preference   ", showerresponse);
                            showerradio.setVisibility(View.GONE);
                        }
                    }
                    else {
                        Log.d("checking if date not same shower shared preference   ", sleepusername);
                    }

                    if((thisdate.equals( medicationresponsedate )) && username.equals(medicationusername) && useremail.equals(medicationmonitor)) {
                        if ((preferences.contains("medicationPrefresponse"))){
                            Log.d("checking medication shared preference   ", medicationresponse);
                            medicationradio.setVisibility(View.GONE);
                        }
                    }
                    else {
                        Log.d("checking if date not same med shared preference   ", medicationusername);
                    }

                    if((thisdate.equals( breakfastresponsedate )) && username.equals(breakfastusername) && useremail.equals(breakfastmonitor)) {
                        if ((preferences.contains("breakfastPrefresponse"))){
                            Log.d("checking breakfast shared preference   ", breakfastresponse);
                            breakfastradio.setVisibility(View.GONE);
                        }
                    }
                    else {
                        Log.d("checking if date not same breakfast shared preference   ", breakfastusername);
                    }

                    if((thisdate.equals( lunchresponsedate )) && username.equals(lunchusername) && useremail.equals(lunchmonitor)) {
                        if ((preferences.contains("lunchPrefresponse"))){
                            Log.d("checking lunch shared preference   ", lunchresponse);
                            lunchradio.setVisibility(View.GONE);
                        }
                    }
                    else {
                        Log.d("checking if date not same lunch shared preference   ", lunchusername);
                    }

                    if((thisdate.equals( tvresponsedate )) && username.equals(tvusername) && useremail.equals(tvmonitor)) {
                        if ((preferences.contains("tvPrefresponse"))){
                            Log.d("checking tv shared preference   ", tvresponse);
                            tvradio.setVisibility(View.GONE);
                        }
                    }
                    else {
                        Log.d("checking if date not same tv shared preference   ", tvusername);
                    }

                    if((thisdate.equals( dinnerresponsedate )) && username.equals(dinnerusername) && useremail.equals(dinnermonitor)) {
                        if ((preferences.contains("dinnerPrefresponse"))){
                            Log.d("checking dinner shared preference   ", dinnerresponse);
                            dinnerradio.setVisibility(View.GONE);
                        }
                    }
                    else {
                        Log.d("checking if date not same dinner shared preference   ", dinnerusername);
                    }

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Setting date format

                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // Setting time format

                    assert datearray != null;
                    LocalDate startDate = LocalDate.parse(datearray.get(0));

                    LocalDate today = LocalDate.now();
                    LocalTime currentTime = LocalTime.now();

                    int enddateindex = (today.getDayOfYear() % (datearray.size()-2)) + 2;
                    String activitydur = durarray.get(enddateindex);

//                    Log.d("activitydur", String.valueOf(activitydur));

                    LocalDate endDate = LocalDate.parse(datearray.get(enddateindex));
                   // Log.d("enddatelocal", String.valueOf(endDate));

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

                        String durationOfActivity = Long.parseLong(activitydur) / 60 % 24 + " Hours" + " & " + Long.parseLong(activitydur) % 60 + "  Minutes";
                        assert durationTextview != null;

                        assert endtimeArray != null;
                        String actvTime = endtimeArray.get(enddateindex);
                       // Log.d("compareactivitytime", (actvTime));

                        LocalDateTime lct = LocalDateTime.parse(actvTime, timeFormatter);
                        LocalTime actTime = lct.toLocalTime(); //get activity completion time in localtime format

                        if (currentTime.isAfter(actTime)) {
                            drawchart(modStartArray, modEndArray, activityType);
                            durationTextview.setText("Duration of Activity :  " + durationOfActivity);
                            }
                        else {
                            thisRadiogp.setVisibility(View.GONE);
                            drawchart(prevModStartArray , prevModEndArray, activityType);
                            durationTextview.setVisibility(View.GONE);
                        }
                    }
                    else {

                        long durationOfActivity = Long.parseLong(activitydur);
                        assert durationTextview != null;

                       // Log.d("durationOfActivity", String.valueOf(durationOfActivity));

                        assert endtimeArray != null;
                        String actvTime = endtimeArray.get(enddateindex);
                      //  Log.d("compareactivitytime2", (actvTime));

                        LocalDateTime lct = LocalDateTime.parse(actvTime,timeFormatter);
                        LocalTime actTime = lct.toLocalTime(); //get activity completion time in localtime format

                        if(currentTime.isAfter(actTime)){
                            thisCard.setVisibility(View.VISIBLE);
                            durationTextview.setText("Duration of Activity :  " + activitydur + "  minutes");
                            drawchart(modStartArray, modEndArray, activityType);
                            }
                            else
                            {
                                thisRadiogp.setVisibility(View.INVISIBLE);
                                drawchart(prevModStartArray , prevModEndArray, activityType);
                            }
                    }
                }
            }
        }

        public void drawchart(ArrayList<String> x, ArrayList<String> y, String activityType) {


            ArrayList<CandleEntry> candleEntryTry = new ArrayList<CandleEntry>();

            for (int i = 0; i < x.size(); i++) {
                candleEntryTry.add(new CandleEntry(i, 23, 0, Float.parseFloat(x.get(i)), Float.parseFloat(y.get(i))));
               // Log.d("graph thing", x.get(i));
            }

            CandleStickChart selectedChart = activityChartMap.get(activityType);
            assert selectedChart != null;
            selectedChart.setBackgroundColor(Color.WHITE);
            selectedChart.getDescription().setEnabled(false);
            selectedChart.setTouchEnabled(false);

//            selectedChart.setVisibleXRangeMaximum(2);
//            selectedChart.setVisibleXRangeMaximum(2);

            selectedChart.notifyDataSetChanged();

            selectedChart.getAxisRight().setDrawLabels(true);

            selectedChart.setDrawBorders(true);
            selectedChart.setHighlightPerDragEnabled(true);
            selectedChart.setBorderColor(getResources().getColor((R.color.borderofGraph)));

            XAxis xAxis = selectedChart.getXAxis();
            xAxis.setValueFormatter(new MyXAxisValueFormatter());

            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

            xAxis.setDrawGridLines(false);
            xAxis.setDrawLabels(true);
            xAxis.setGranularityEnabled(true);
            xAxis.setGranularity(1f);
            //xAxis.setLabelRotationAngle(45f);
            xAxis.setLabelCount(4);

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
            set1.setFormSize(7);
            set1.setDecreasingColor(getResources().getColor(R.color.colorinGraph));
            set1.setDecreasingPaintStyle(Paint.Style.FILL);
            set1.setIncreasingColor(getResources().getColor(R.color.colorinGraph));
            set1.setIncreasingPaintStyle(Paint.Style.FILL);
            set1.setNeutralColor(Color.LTGRAY);
            set1.setDrawValues(false);
            CandleData data = new CandleData(set1);
            selectedChart.setData(data);
           // selectedChart.setMinimumWidth(9);
            selectedChart.invalidate();
        }

//practice code to submit into Firebase
        private void submitbutton(String name) {

            final String username = name;

            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            final FirebaseUser thisuser = FirebaseAuth.getInstance().getCurrentUser();
            assert thisuser != null;
            final String email = thisuser.getEmail();
            final Object userdr = email + "  " + new Date();

            submitreport.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View view) {

                    HashMap<String, Object> sleepmap = new HashMap<>();
                    HashMap<String, Object> showermap = new HashMap<>();
                    HashMap<String, Object> medmap = new HashMap<>();
                    HashMap<String, Object> bfmap = new HashMap<>();
                    HashMap<String, Object> lunchmap = new HashMap<>();
                    HashMap<String, Object> dinnermap = new HashMap<>();
                    HashMap<String, Object> tvmap = new HashMap<>();

                    assert email != null;
                    Log.d("", email);
                    String id = ref.push().getKey();
                    assert id != null;

                    final int sleepstate = sleepradio.getCheckedRadioButtonId();
                    final int showerstate = showerradio.getCheckedRadioButtonId();
                    final int medstate = medicationradio.getCheckedRadioButtonId();
                    final int bfstate = breakfastradio.getCheckedRadioButtonId();
                    final int lunchstate = lunchradio.getCheckedRadioButtonId();
                    final int dinnerstate = dinnerradio.getCheckedRadioButtonId();
                    final int tvstate = tvradio.getCheckedRadioButtonId();


                    if (sleepradio!=null && sleepradio.isEnabled() && sleepstate!= -1){
                        sleep = (RadioButton) findViewById(sleepstate);
                        Log.d("value   ", String.valueOf(sleep.getText()));
                        sleepmap.put("Sleep state", userdr);
                        ref.child(username).child("Sleep").child(String.valueOf(sleep.getText())).child(id).updateChildren(sleepmap);

                        editor.putString("sleepPrefuser", username);
                        editor.putString("sleepPrefmonitor", email);
                        editor.putString("sleepPrefresponse", String.valueOf(sleep.getText()));
                        editor.putString("sleepPrefdate", String.valueOf(LocalDate.now()));

                        editor.apply(); //to get it back, need to do, preferences.getString("same key", )
                    }

                    if (showerradio!=null && showerradio.isEnabled() && showerstate!= -1){
                        shower = (RadioButton) findViewById(showerstate);
                        Log.d("value ", String.valueOf(shower.getText()));
                        showermap.put("Shower state", userdr);
                        ref.child(username).child("Shower").child(String.valueOf(shower.getText())).child(id).updateChildren(showermap);

                        editor.putString("showerPrefuser", username);
                        editor.putString("showerPrefmonitor", email);
                        editor.putString("showerPrefresponse", String.valueOf(shower.getText()));
                        editor.putString("showerPrefdate", String.valueOf(LocalDate.now()));

                        editor.apply();
                    }
                     if (breakfastradio!=null && breakfastradio.isEnabled() && bfstate!= -1){
                         breakfast = (RadioButton) findViewById(bfstate);
                         Log.d("value   ", String.valueOf(breakfast.getText()));
                         bfmap.put("Breakfast state", userdr);
                         ref.child(username).child("Breakfast").child(String.valueOf(breakfast.getText())).child(id).updateChildren(bfmap);

                         editor.putString("breakfastPrefuser", username);
                         editor.putString("breakfastPrefmonitor", email);
                         editor.putString("breakfastPrefresponse", String.valueOf(breakfast.getText()));
                         editor.putString("breakfastPrefdate", String.valueOf(LocalDate.now()));

                         editor.apply();

                     }
                     if (medicationradio!=null && medicationradio.isEnabled() && medstate!= -1){
                         medication = (RadioButton) findViewById(medstate);
                         Log.d("value   ", String.valueOf(medication.getText()));
                        medmap.put("Medication state", userdr);
                        ref.child(username).child("Medication").child(String.valueOf(medication.getText())).child(id).updateChildren(medmap);

                         editor.putString("medicationPrefuser", username);
                         editor.putString("medicationPrefmonitor", email);
                         editor.putString("medicationPrefresponse", String.valueOf(medication.getText()));
                         editor.putString("medicationPrefdate", String.valueOf(LocalDate.now()));

                         editor.apply();

                     }
                     if (lunchradio!=null && lunchradio.isEnabled() && lunchstate!= -1){
                         lunch = (RadioButton) findViewById(lunchstate);
                         Log.d("value   ", String.valueOf(lunch.getText()));
                        lunchmap.put("lunch state", userdr);
                        ref.child(username).child("Lunch").child(String.valueOf(lunch.getText())).child(id).updateChildren(lunchmap);

                         editor.putString("lunchPrefuser", username);
                         editor.putString("lunchPrefmonitor", email);
                         editor.putString("lunchPrefresponse", String.valueOf(lunch.getText()));
                         editor.putString("lunchPrefdate", String.valueOf(LocalDate.now()));

                         editor.apply();

                     }
                     if (tvradio!=null && tvradio.isEnabled() && tvstate!= -1){
                         tv = (RadioButton) findViewById(tvstate);
                         Log.d("value   ", String.valueOf(tv.getText()));
                        tvmap.put("TV state", userdr);
                        ref.child(username).child("TV").child(String.valueOf(tv.getText())).child(id).updateChildren(tvmap);

                         editor.putString("tvPrefuser", username);
                         editor.putString("tvPrefmonitor", email);
                         editor.putString("tvPrefresponse", String.valueOf(tv.getText()));
                         editor.putString("tvPrefdate", String.valueOf(LocalDate.now()));

                         editor.apply();

                     }
                     if (dinnerradio!=null && dinnerradio.isEnabled() && dinnerstate!= -1){
                         dinner = (RadioButton) findViewById(dinnerstate);
                         Log.d("value   ", String.valueOf(dinner.getText()));
                        dinnermap.put("dinner state", userdr);
                        ref.child(username).child("Dinner").child(String.valueOf(dinner.getText())).child(id).updateChildren(dinnermap);

                         editor.putString("dinnerPrefuser", username);
                         editor.putString("dinnerPrefmonitor", email);
                         editor.putString("dinnerPrefresponse", String.valueOf(dinner.getText()));
                         editor.putString("dinnerPrefdate", String.valueOf(LocalDate.now()));

                         editor.apply();

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
                return (int) value + ":00";
            }
        }

        private class MyXAxisValueFormatter implements IAxisValueFormatter {
            private ArrayList<String> mValues = new ArrayList<>();

            public MyXAxisValueFormatter() {

                for(int i =9; i< 25;i++){

                    mValues.add("11/"+ i);
               }
            }
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mValues.get((int) value);
            }
        }
    }
}