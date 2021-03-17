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
import android.widget.Toast;

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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

//this is the main class..i read the dataset, create graph, and provoke notifications from this class.
@RequiresApi(api = Build.VERSION_CODES.O)
public class report  extends AppCompatActivity {

    public CandleStickChart chart1, chart2, chart3, chart4, chart5, chart6, chart7;
    public TextView text1, text2, text3, text4, text5, text6, text7, durationTextview, activityReport;
    public TextView sleepEndtime, bfEndtime, medicineEndtime, lunchEndtime, tvEndtime, dinnerEndtime, showerEndtime, endTimeTextview;
    public TextView sleepState, showerState, bfState, medicationState, lunchState, tvState, dinnerState, stateTextview;
    RadioButton sleep, shower, breakfast, lunch, dinner, medication, tv, overall;
    private RadioGroup sleepradio, showerradio, breakfastradio, medicationradio, lunchradio, tvradio, dinnerradio, thisRadiogp,overallradio;
    Button submitreport;
    String TAG = "ttyl";

    ArrayList<HashMap<String, String>> contactList;

    public CardView sleepCard, showerCard, bfCard, medicineCard, lunchCard, tvCard, dinnerCard, thisCard;

    private final HashMap<String, TextView> durationtextHash = new HashMap<>();

    private HashMap<String, TextView> endTimetextHash = new HashMap<>();

    private HashMap<String, TextView> activitystateHash = new HashMap<>();

    private HashMap<String, CardView> cardHash = new HashMap<>();

    private HashMap<String, RadioGroup> radioHash = new HashMap<>();

    private HashMap<String, CandleStickChart> activityChartMap = new HashMap<>();

    private String userStr = null;
    public static final String mypreference = "mypref";

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    breceiver ar = new breceiver();

    String timeOfOpeningofReport = String.valueOf(Calendar.getInstance().getTime());


    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.newreportlayout);

        submitreport = (Button) findViewById(R.id.submitreport);
        activityReport = (TextView) findViewById(R.id.report);

        chart1 = (CandleStickChart) findViewById(R.id.sleepLinechart);
        chart2 = (CandleStickChart) findViewById(R.id.showerLinechart);
        chart3 = (CandleStickChart) findViewById(R.id.breakfastLinechart);
        chart4 = (CandleStickChart) findViewById(R.id.medicationLinechart);
        chart5 = (CandleStickChart) findViewById(R.id.lunchLinechart);
        chart6 = (CandleStickChart) findViewById(R.id.tvLinechart);
        chart7 = (CandleStickChart) findViewById(R.id.dinnerLinechart);


        YAxis sleepleft = chart1.getAxisLeft();
        sleepleft.setAxisMaximum((float) 35.0);
        sleepleft.setAxisMinimum(16);

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

        sleepEndtime = (TextView) findViewById(R.id.sleeptime);
        bfEndtime = (TextView) findViewById(R.id.breakfasttime);
        medicineEndtime = (TextView) findViewById(R.id.medicationtime);
        lunchEndtime = (TextView) findViewById(R.id.lunchtime);
        tvEndtime = (TextView) findViewById(R.id.tvtime);
        dinnerEndtime = (TextView) findViewById(R.id.dinnertime);
        showerEndtime = (TextView) findViewById(R.id.showertime);

        sleepState = (TextView) findViewById(R.id.sleepstatus);
        bfState = (TextView) findViewById(R.id.breakfaststatus);
        medicationState = (TextView) findViewById(R.id.medicationstatus);
        lunchState = (TextView) findViewById(R.id.lunchstatus);
        tvState = (TextView) findViewById(R.id.tvstatus);
        dinnerState = (TextView) findViewById(R.id.dinnerstatus);
        showerState = (TextView) findViewById(R.id.showerstatus);

        activitystateHash.put("Sleep", sleepState);
        activitystateHash.put("Bath", showerState);
        activitystateHash.put("Breakfast", bfState);
        activitystateHash.put("medicine ", medicationState); // TODO Remove the space at end next time
        activitystateHash.put("Lunch", lunchState);
        activitystateHash.put("TV", tvState);
        activitystateHash.put("Dinner", dinnerState);

        endTimetextHash.put("Sleep", sleepEndtime);
        endTimetextHash.put("Bath", showerEndtime);
        endTimetextHash.put("Breakfast", bfEndtime);
        endTimetextHash.put("medicine ", medicineEndtime); // TODO Remove the space at end next time
        endTimetextHash.put("Lunch", lunchEndtime);
        endTimetextHash.put("TV", tvEndtime);
        endTimetextHash.put("Dinner", dinnerEndtime);

        durationtextHash.put("Sleep", text1);
        durationtextHash.put("Bath", text2);
        durationtextHash.put("Breakfast", text3);
        durationtextHash.put("medicine ", text4); // TODO Remove the space at end next time
        durationtextHash.put("Lunch", text5);
        durationtextHash.put("TV", text6);
        durationtextHash.put("Dinner", text7);


        sleepCard = (CardView) findViewById(R.id.sleepcard);
        showerCard = (CardView) findViewById(R.id.showercard);
        bfCard = (CardView) findViewById(R.id.breakfastcard);
        medicineCard = (CardView) findViewById(R.id.medicationcard);
        lunchCard = (CardView) findViewById(R.id.lunchcard);
        tvCard = (CardView) findViewById(R.id.tvcard);
        dinnerCard = (CardView) findViewById(R.id.dinnercard);

        cardHash.put("Sleep", sleepCard);
        cardHash.put("Bath", showerCard);
        cardHash.put("Breakfast", bfCard);
        cardHash.put("medicine ", medicineCard);
        cardHash.put("Lunch", lunchCard);
        cardHash.put("TV", tvCard);
        cardHash.put("Dinner", dinnerCard);

        lunchradio = (RadioGroup) findViewById(R.id.lunchbutton);
        dinnerradio = (RadioGroup) findViewById(R.id.dinnerbutton);
        tvradio = (RadioGroup) findViewById(R.id.tvbutton);
        medicationradio = (RadioGroup) findViewById(R.id.medicationbutton);
        breakfastradio = (RadioGroup) findViewById(R.id.breakfastbutton);
        showerradio = (RadioGroup) findViewById(R.id.showerbutton);
        sleepradio = (RadioGroup) findViewById(R.id.sleepbutton);
        overallradio = (RadioGroup) findViewById(R.id.overallButton);

        radioHash.put("Sleep", sleepradio);
        radioHash.put("Bath", showerradio);
        radioHash.put("Breakfast", breakfastradio);
        radioHash.put("medicine ", medicationradio);
        radioHash.put("Lunch", lunchradio);
        radioHash.put("TV", tvradio);
        radioHash.put("Dinner", dinnerradio);
        radioHash.put("Overall", overallradio);


        contactList = new ArrayList<>();

        isMyServiceRunning();

        IntentFilter filter = new IntentFilter("intentAction");
        registerReceiver(ar, filter);

        Intent passedIntent = getIntent();
        userStr = passedIntent.getStringExtra("user");

        //why do i need to call userreport class in this class..doesnt work if i dont!!
        Intent userReportIntent = new Intent(this, userreport.class);
        userReportIntent.putExtra("user", userStr);
        ContextCompat.startForegroundService(this,userReportIntent );

        Intent recurringNotifIntent = new Intent(this, recurringnotification.class);
        ContextCompat.startForegroundService(this,recurringNotifIntent );

//        startService(new Intent(this, userreport.class));
//        Log.d("", "service running check which might be the cause for error");

    }

    @Override
    protected void onResume() {
        super.onResume();

      //  Log.e(TAG, "onResume: " + "hari om" );

        Intent recurringNotIntent = getIntent();

        String abN = recurringNotIntent.getStringExtra("activityBased_notificationGeneratedTime");

        Log.e(TAG, "activity based notification time: " + abN );

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(ar);
        super.onDestroy();

    }

    private void isMyServiceRunning() {
//        Log.d("", "service runing check in activity");

        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (userreport.class.getName().equals(service.service.getClassName())) {
//                Log.d("", "checking from report class: yes it is running");
                return;
            }
            else{
//                Log.d("", "checking from report class: No the service isnt running");

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

                    ArrayList<String> endtimeArray2 = intent.getStringArrayListExtra("endtimeList2");

//                   Log.d("endtimeList array22", String.valueOf((endtimeArray2)));

                    String activityType = intent.getStringExtra("actv");
//                    Log.d("getting activity from service", activityType);

                    submitbutton(username);

                    String targetName = null;

                    if (username.equals("1")){
                        targetName = getResources().getString(R.string.target1);
                    }
                    if (username.equals("2")){
                        targetName = getResources().getString(R.string.target2);

                    }
                    if (username.equals("3")){
                        targetName = getResources().getString(R.string.target3);
                        ;
                    }

                    activityReport.setText("Activity Report - " + " " + targetName );


//                    activityReport.setText("Activity Report of - " + "  " + "Target" + " " + username );

                    preferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
                    editor= preferences.edit();
                    //editor.apply();


                    String sleepusername = preferences.getString("sleepPrefuser_" + username + LocalDate.now(),"");
                    String sleepmonitor = preferences.getString("sleepPrefmonitor_" + username + LocalDate.now(), "");
                    String sleepresponsedate = preferences.getString("sleepPrefdate_" + username + LocalDate.now(),"");
                    String sleepresponse = preferences.getString("sleepPrefresponse_" + username + LocalDate.now(),"");


                    String showerusername = preferences.getString("showerPrefuser_" + username + LocalDate.now(),"");
                    String showermonitor = preferences.getString("showerPrefmonitor_" + username + LocalDate.now(), "");
                    String showerresponsedate = preferences.getString("showerPrefdate_" + username + LocalDate.now(),"");
                    String showerresponse = preferences.getString("showerPrefresponse_" + username + LocalDate.now(),"");

                    String medicationusername = preferences.getString("medicationPrefuser_" + username + LocalDate.now(),"");
                    String medicationmonitor = preferences.getString("medicationPrefmonitor_" + username + LocalDate.now(), "");
                    String medicationresponsedate = preferences.getString("medicationPrefdate_" + username + LocalDate.now(),"");
                    String medicationresponse = preferences.getString("medicationPrefresponse_" + username + LocalDate.now(),"");

                    String breakfastusername = preferences.getString("breakfastPrefuser_" + username + LocalDate.now(),"");
                    String breakfastmonitor = preferences.getString("breakfastPrefmonitor_" + username + LocalDate.now(), "");
                    String breakfastresponsedate = preferences.getString("breakfastPrefdate_" + username + LocalDate.now(),"");
                    String breakfastresponse = preferences.getString("breakfastPrefresponse_" + username + LocalDate.now(),"");

                    String lunchusername = preferences.getString("lunchPrefuser_" + username + LocalDate.now(),"");
                    String lunchmonitor = preferences.getString("lunchPrefmonitor_" + username + LocalDate.now(), "");
                    String lunchresponsedate = preferences.getString("lunchPrefdate_" + username + LocalDate.now(),"");
                    String lunchresponse = preferences.getString("lunchPrefresponse_" + username + LocalDate.now(),"");

                    String tvusername = preferences.getString("tvPrefuser_" + username + LocalDate.now(),"");
                    String tvmonitor = preferences.getString("tvPrefmonitor_" + username + LocalDate.now(), "");
                    String tvresponsedate = preferences.getString("tvPrefdate_" + username + LocalDate.now(),"");
                    String tvresponse = preferences.getString("tvPrefresponse_" + username + LocalDate.now(),"");

                    String dinnerusername = preferences.getString("dinnerPrefuser_" + username + LocalDate.now(),"");
                    String dinnermonitor = preferences.getString("dinnerPrefmonitor_" + username + LocalDate.now(), "");
                    String dinnerresponsedate = preferences.getString("dinnerPrefdate_" + username + LocalDate.now(),"");
                    String dinnerresponse = preferences.getString("dinnerPrefresponse_" + username + LocalDate.now(),"");

                    String thisdate = String.valueOf(LocalDate.now());


                    assert useremail != null;
                    if((thisdate.equals( sleepresponsedate )) && username.equals(sleepusername) && useremail.equals(sleepmonitor)) {
                        if ((preferences.contains("sleepPrefresponse_" + username + LocalDate.now()))){
//                            Log.d("checking sleep shared preference   ", sleepresponse);
                            sleepradio.setVisibility(View.GONE);
                            sleepCard.setCardBackgroundColor(getColor(R.color.trafficGreen));
                        }
                    }
                    else {
//                        Log.d("checking if date not same shared preference   ", sleepusername);
                    }

                    if((thisdate.equals( showerresponsedate )) && username.equals(showerusername) && useremail.equals(showermonitor)) {
                        if ((preferences.contains("showerPrefresponse_" + username + LocalDate.now()))){
//                            Log.d("checking shower shared preference   ", showerresponse);
                            showerradio.setVisibility(View.GONE);
                            showerCard.setCardBackgroundColor(getColor(R.color.reportComplete));

                        }
                    }
                    else {
//                        Log.d("checking if date not same shower shared preference   ", sleepusername);
                    }

                    if((thisdate.equals( medicationresponsedate )) && username.equals(medicationusername) && useremail.equals(medicationmonitor)) {
                        if ((preferences.contains("medicationPrefresponse_" + username + LocalDate.now()))){
//                            Log.d("checking medication shared preference   ", medicationresponse);
                            medicationradio.setVisibility(View.GONE);
                            medicineCard.setCardBackgroundColor(getColor(R.color.reportComplete));

                        }
                    }
                    else {
//                        Log.d("checking if date not same med shared preference   ", medicationusername);
                    }

                    if((thisdate.equals( breakfastresponsedate )) && username.equals(breakfastusername) && useremail.equals(breakfastmonitor)) {
                        if ((preferences.contains("breakfastPrefresponse_" + username + LocalDate.now()))){
//                            Log.d("checking breakfast shared preference   ", breakfastresponse);
                            breakfastradio.setVisibility(View.GONE);
                            bfCard.setCardBackgroundColor(getColor(R.color.reportComplete));

                        }
                    }
                    else {
//                        Log.d("checking if date not same breakfast shared preference   ", breakfastusername);
                    }

                    if((thisdate.equals( lunchresponsedate )) && username.equals(lunchusername) && useremail.equals(lunchmonitor)) {
                        if ((preferences.contains("lunchPrefresponse_" + username + LocalDate.now()))){
//                            Log.d("checking lunch shared preference   ", lunchresponse);
                            lunchradio.setVisibility(View.GONE);
                            lunchCard.setCardBackgroundColor(getColor(R.color.reportComplete));

                        }
                    }
                    else {
//                        Log.d("checking if date not same lunch shared preference   ", lunchusername);
                    }

                    assert useremail != null;
                    if((thisdate.equals( tvresponsedate )) && username.equals(tvusername) && useremail.equals(tvmonitor)) {
                        if ((preferences.contains("tvPrefresponse_" + username + LocalDate.now()))){
//                            Log.d("checking tv shared preference   ", tvresponse);
                            tvradio.setVisibility(View.GONE);
                            tvCard.setCardBackgroundColor(getColor(R.color.reportComplete));

                        }
                    }
                    else {
//                        Log.d("checking if date not same tv shared preference   ", tvusername);
                    }

                    if((thisdate.equals( dinnerresponsedate )) && username.equals(dinnerusername) && useremail.equals(dinnermonitor)) {
                        if ((preferences.contains("dinnerPrefresponse_" + username + LocalDate.now()))){
//                            Log.d("checking dinner shared preference   ", dinnerresponse);
                            dinnerradio.setVisibility(View.GONE);
                            dinnerCard.setCardBackgroundColor(getColor(R.color.reportComplete));

                        }
                    }
                    else {
//                        Log.d("checking if date not same dinner shared preference   ", dinnerusername);
                    }

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Setting date format

                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // Setting time format

                    assert datearray != null;
                    LocalDate startDate = LocalDate.parse(datearray.get(0));

                    LocalDate today = LocalDate.now();
                    LocalTime currentTime = LocalTime.now();

                    //this index helps to start the graph at a aprticular date: pref after 3 days of data. Is in 2 places, userreport 232
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
//                        Log.d("PrevEndDate", String.valueOf(PrevEndDate));
                        String datestr = datearray.get(i);
                        LocalDate d = LocalDate.parse(datestr, formatter);
                        if((d.isAfter(startDate) || d.isEqual(startDate)) && (d.isBefore(PrevEndDate)|| d.isEqual(PrevEndDate))){
                            prevModEndArray.add(endarray.get(i));
                            prevModStartArray.add(startarray.get(i));
                        }
                    }

                    durationTextview = durationtextHash.get(activityType);
                    endTimeTextview = endTimetextHash.get(activityType);
                    thisRadiogp = radioHash.get(activityType);
                    thisCard = cardHash.get(activityType);
                    stateTextview = activitystateHash.get(activityType);

                    if (Float.parseFloat(activitydur) >= 60) {

                        String durationOfActivity = Long.parseLong(activitydur) / 60 % 24 + " Hrs" + " & " + Long.parseLong(activitydur) % 60 + " Min";
                        assert durationTextview != null;

                        assert endtimeArray != null;
                        String actvTime = endtimeArray.get(enddateindex);
                       // Log.d("compareactivitytime", (actvTime));


                        LocalDateTime lct = LocalDateTime.parse(actvTime, timeFormatter);
                        LocalTime actTime = lct.toLocalTime(); //get activity completion time in localtime format


                        String endtimeText = endtimeArray2.get(enddateindex);

                        if (currentTime.isAfter(actTime)) {

                            thisCard.setCardBackgroundColor(getColor(R.color.trafficYellow));
                            drawchart(modStartArray, modEndArray, activityType);
                            durationTextview.setText(durationOfActivity);
                            endTimeTextview.setText(endtimeText);
                            stateTextview.setText("Complete");


                        }
                        else {

                            thisCard.setCardBackgroundColor(getColor(R.color.trafficRed));
                            thisRadiogp.setVisibility(View.GONE);
                            drawchart(prevModStartArray , prevModEndArray, activityType);
                            durationTextview.setVisibility(View.GONE);
                            endTimeTextview.setVisibility(View.GONE);
                            stateTextview.setText("Incomplete");


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

                        String endtimeText = endtimeArray2.get(enddateindex);

                        if(currentTime.isAfter(actTime)){

                            thisCard.setCardBackgroundColor(getColor(R.color.trafficYellow));
                            durationTextview.setText(activitydur + " Minutes");
                            endTimeTextview.setText(endtimeText);
                            stateTextview.setText("Complete");
                            drawchart(modStartArray, modEndArray, activityType);


                        }
                            else
                            {
                                thisCard.setCardBackgroundColor(getColor(R.color.trafficRed));
                                thisRadiogp.setVisibility(View.GONE);
                                stateTextview.setText("Incomplete");
                                durationTextview.setVisibility(View.GONE);
                                endTimeTextview.setVisibility(View.GONE);
                                drawchart(prevModStartArray, prevModEndArray, activityType);

                            }
                    }
                }
            }
        }
        public void drawchart(ArrayList<String> x, ArrayList<String> y, String activityType) {

            ArrayList<CandleEntry> candleEntryTry = new ArrayList<CandleEntry>();

            for (int i = 0; i < x.size(); i++) {
                int shift = activityType.equals("Sleep") && !x.get(i).equals(y.get(i)) ? 24 : 0;
                candleEntryTry.add(new CandleEntry(i, 35, 0, Float.parseFloat(x.get(i)), Float.parseFloat(y.get(i))+ shift));
//                Log.d("graph thing", x.get(i));
            }

            CandleStickChart selectedChart = activityChartMap.get(activityType);
            assert selectedChart != null;
            selectedChart.setBackgroundColor(Color.WHITE);
            selectedChart.getDescription().setEnabled(false);
            selectedChart.setTouchEnabled(true);
//            selectedChart.setDoubleTapToZoomEnabled(false);
//            selectedChart.setPinchZoom(false);
//
//            selectedChart.setVisibleXRangeMaximum(7);
//            selectedChart.setVisibleXRangeMaximum(7);

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

            selectedChart.invalidate();

        }

//practice code to submit into Firebase
        private void submitbutton(String name) {

            final String username = name;

            Bundle bundle = getIntent().getExtras();
            String checkingTime = null;

            Intent actBasedNotIntent = getIntent();

            if (bundle.getString("recurringNotification_generatedANDclick")!=null)
            {
                checkingTime = bundle.getString("recurringNotification_generatedANDclick");

            }

            else if (actBasedNotIntent.hasExtra("activityBased_notificationGeneratedTime"))
            {
                checkingTime = actBasedNotIntent.getStringExtra("activityBased_notificationGeneratedTime");
            }

            else
                {
                checkingTime = bundle.getString("FreeWillOpeningMutualMonitor");
            }


           // Log.d(TAG, "submitbutton: " + actvEndTime);

            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            final FirebaseUser thisuser = FirebaseAuth.getInstance().getCurrentUser();
            assert thisuser != null;
            final String email = thisuser.getEmail();
            final Object userdr = email + "  " + new Date();


            String finalCheckingTime = checkingTime;
          //  Log.e(TAG, "submitbutton: aaaaaaaa" + " :" + finalCheckingTime);

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
                    HashMap<String, Object> overallmap = new HashMap<>();


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
                    final int overallstate = overallradio.getCheckedRadioButtonId();


                    if ( sleepstate == -1
                            && showerstate == -1
                            &&  bfstate == -1
                            && medstate == -1
                            && lunchstate == -1
                            && tvstate == -1
                            && dinnerstate == -1
                            && overallstate == -1)
                    {
                        Toast.makeText(getApplicationContext(), "Seems like you have missed selecting!! ", Toast.LENGTH_SHORT).show();
                    }
                    else {

                        if (sleepradio != null && sleepradio.isEnabled() && sleepstate != -1) {
                            sleep = (RadioButton) findViewById(sleepstate);
//                            Log.d("value   ", String.valueOf(sleep.getText()));
                            sleepmap.put("Sleep state", userdr);
                            ref.child(username).child("Sleep").child(String.valueOf(sleep.getText())).child(id).updateChildren(sleepmap);

                            editor.putString("sleepPrefuser_" + username + LocalDate.now(), username);
                            editor.putString("sleepPrefmonitor_" + username + LocalDate.now(), email);
                            editor.putString("sleepPrefresponse_" + username + LocalDate.now(), String.valueOf(sleep.getText()));
                            editor.putString("sleepPrefdate_" + username + LocalDate.now(), String.valueOf(LocalDate.now()));

                            editor.apply(); //to get it back, need to do, preferences.getString("same key", )

                            Log.e(TAG, "onClick: testing report submission time" + " ^_^ " + finalCheckingTime + " --> " + timeOfOpeningofReport + " -> LT  " + Calendar.getInstance().getTime() );

                        }

                        if (showerradio != null && showerradio.isEnabled() && showerstate != -1) {
                            shower = (RadioButton) findViewById(showerstate);
//                            Log.d("value ", String.valueOf(shower.getText()));
                            showermap.put("Shower state", userdr);
                            ref.child("testing for FB").child(username).child("Shower").child(String.valueOf(shower.getText())).child(id).updateChildren(showermap);

                            editor.putString("showerPrefuser_" + username+ LocalDate.now(), username);
                            editor.putString("showerPrefmonitor_" + username+ LocalDate.now(), email);
                            editor.putString("showerPrefresponse_" + username+ LocalDate.now(), String.valueOf(shower.getText()));
                            editor.putString("showerPrefdate_" + username+ LocalDate.now(), String.valueOf(LocalDate.now()));

                            editor.apply();
                            Log.e(TAG, "onClick: testing report submission time" + " ^_^ " + finalCheckingTime + " --> " + timeOfOpeningofReport + " -> LT  " + Calendar.getInstance().getTime() );

                        }
                        if (breakfastradio != null && breakfastradio.isEnabled() && bfstate != -1) {
                            breakfast = (RadioButton) findViewById(bfstate);
//                            Log.d("value   ", String.valueOf(breakfast.getText()));
                            bfmap.put("Breakfast state", userdr);
                            ref.child(username).child("Breakfast").child(String.valueOf(breakfast.getText())).child(id).updateChildren(bfmap);

                            editor.putString("breakfastPrefuser_" + username+ LocalDate.now(), username);
                            editor.putString("breakfastPrefmonitor_" + username+ LocalDate.now(), email);
                            editor.putString("breakfastPrefresponse_" + username+ LocalDate.now(), String.valueOf(breakfast.getText()));
                            editor.putString("breakfastPrefdate_" + username+ LocalDate.now(), String.valueOf(LocalDate.now()));

                            editor.apply();
                            Log.e(TAG, "onClick: testing report submission time" + " ^_^ " + finalCheckingTime + " --> " + timeOfOpeningofReport + " -> LT  " + Calendar.getInstance().getTime() );


                        }
                        if (medicationradio != null && medicationradio.isEnabled() && medstate != -1) {
                            medication = (RadioButton) findViewById(medstate);
//                            Log.d("value   ", String.valueOf(medication.getText()));
                            medmap.put("Medication state", userdr);
                            ref.child(username).child("Medication").child(String.valueOf(medication.getText())).child(id).updateChildren(medmap);

                            editor.putString("medicationPrefuser_" + username+ LocalDate.now(), username);
                            editor.putString("medicationPrefmonitor_" + username+ LocalDate.now(), email);
                            editor.putString("medicationPrefresponse_" + username+ LocalDate.now(), String.valueOf(medication.getText()));
                            editor.putString("medicationPrefdate_" + username+ LocalDate.now(), String.valueOf(LocalDate.now()));

                            editor.apply();
                            Log.e(TAG, "onClick: testing report submission time" + " ^_^ " + finalCheckingTime + " --> " + timeOfOpeningofReport+ " -> LT  " + Calendar.getInstance().getTime() );


                        }
                        if (lunchradio != null && lunchradio.isEnabled() && lunchstate != -1) {
                            lunch = (RadioButton) findViewById(lunchstate);
//                            Log.d("value   ", String.valueOf(lunch.getText()));
                            lunchmap.put("lunch state", userdr);
                            ref.child(username).child("Lunch").child(String.valueOf(lunch.getText())).child(id).updateChildren(lunchmap);

                            editor.putString("lunchPrefuser_" + username+ LocalDate.now(), username);
                            editor.putString("lunchPrefmonitor_" + username+ LocalDate.now(), email);
                            editor.putString("lunchPrefresponse_" + username+ LocalDate.now(), String.valueOf(lunch.getText()));
                            editor.putString("lunchPrefdate_" + username+ LocalDate.now(), String.valueOf(LocalDate.now()));

                            editor.apply();
                            Log.e(TAG, "onClick: testing report submission time" + " ^_^ " + finalCheckingTime + " --> " + timeOfOpeningofReport+ " -> LT  " + Calendar.getInstance().getTime() );


                        }
                        if (tvradio != null && tvradio.isEnabled() && tvstate != -1) {
                            tv = (RadioButton) findViewById(tvstate);
//                            Log.d("value   ", String.valueOf(tv.getText()));
                            tvmap.put("TV state", userdr);
                            ref.child(username).child("TV").child(String.valueOf(tv.getText())).child(id).updateChildren(tvmap);

                            editor.putString("tvPrefuser_" + username+ LocalDate.now(), username);
                            editor.putString("tvPrefmonitor_" + username+ LocalDate.now(), email);
                            editor.putString("tvPrefresponse_" + username+ LocalDate.now(), String.valueOf(tv.getText()));
                            editor.putString("tvPrefdate_" + username+ LocalDate.now(), String.valueOf(LocalDate.now()));

                            editor.apply();
                            Log.e(TAG, "onClick: testing report submission time" + " ^_^ " + finalCheckingTime + " --> " + timeOfOpeningofReport+ " -> LT  " + Calendar.getInstance().getTime() );

                        }
                        if (dinnerradio != null && dinnerradio.isEnabled() && dinnerstate != -1) {
                            dinner = (RadioButton) findViewById(dinnerstate);
//                            Log.d("value   ", String.valueOf(dinner.getText()));
                            dinnermap.put("dinner state", userdr);
                            ref.child(username).child("Dinner").child(String.valueOf(dinner.getText())).child(id).updateChildren(dinnermap);

                            editor.putString("dinnerPrefuser_" + username+ LocalDate.now(), username);
                            editor.putString("dinnerPrefmonitor_" + username+ LocalDate.now(), email);
                            editor.putString("dinnerPrefresponse_" + username+ LocalDate.now(), String.valueOf(dinner.getText()));
                            editor.putString("dinnerPrefdate_" + username+ LocalDate.now(), String.valueOf(LocalDate.now()));

                            editor.apply();
                            Log.e(TAG, "onClick: testing report submission time" + " ^_^ " + finalCheckingTime + " --> " + timeOfOpeningofReport + " -> LT  " + Calendar.getInstance().getTime() );

                        }
                        if (overallradio != null && overallradio.isEnabled() && overallstate != -1) {
                            overall = (RadioButton) findViewById(overallstate);
//                            Log.d("value   ", String.valueOf(sleep.getText()));
                            overallmap.put("Overall state", userdr);
                            ref.child(username).child("Overall").child(String.valueOf(overall.getText())).child(id).updateChildren(overallmap);

                            editor.putString("overallPrefuser_" + username + LocalDate.now(), username);
                            editor.putString("overallPrefmonitor_" + username + LocalDate.now(), email);
                            editor.putString("overallPrefresponse_" + username + LocalDate.now(), String.valueOf(overall.getText()));
                            editor.putString("overallPrefdate_" + username + LocalDate.now(), String.valueOf(LocalDate.now()));


                            editor.apply(); //to get it back, need to do, preferences.getString("same key", )

                            Log.e(TAG, "onClick: testing report submission time" + " ^_^ " + finalCheckingTime + " --> " + timeOfOpeningofReport+ " -> LT  " + Calendar.getInstance().getTime() );

                        }

                        Intent newintent = new Intent(report.this, thankYou.class);
                        startActivity(newintent);
                    }
                }
            });
        }

        private class MyYAxisValueFormatter implements IAxisValueFormatter {
            public MyYAxisValueFormatter() {

            }

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                //return (int) value + ":00";
                if(value>23) { return ((int) value-24) + " "; }
                return (int) value + " ";

            }
        }

        private class MyXAxisValueFormatter implements IAxisValueFormatter {
            private ArrayList<String> mValues = new ArrayList<>();

            public MyXAxisValueFormatter() {
                mValues.add("11/" + "29");
                mValues.add("11/" + "30");
                mValues.add("12/" + "1");
                mValues.add("12/" + "2");
                mValues.add("12/" + "3");
                mValues.add("12/" + "4");
                mValues.add("12/" + "5");
                mValues.add("12/" + "6");
                mValues.add("12/" + "7");
                mValues.add("12/" + "8");
                mValues.add("12/" + "9");

//                for(int i = 1; i< 12;i++){
//
//                    mValues.add("12/"+ i);
//               }
            }
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mValues.get((int) value);
            }
        }
    }
    @Override
    public void onBackPressed() {
//        Log.d("CDA", "onBackPressed Called");
        Intent setIntent = new Intent(this, select.class);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }
}