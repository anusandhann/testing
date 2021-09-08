package com.mutualmonitor.testing;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

//this is the main class..i read the dataset, create graph, and provoke notifications from this class.
@RequiresApi(api = Build.VERSION_CODES.O)
public class report  extends AppCompatActivity {

    public CandleStickChart sleepChart, showerChart, breakfastChart, medicineChart, lunchChart, tv1Chart, dinnerChart, tv2Chart, tv3Chart;
    public TextView sleepDuration, showerDuration, breakfastDuration, medicationDuration, lunchDuration, tv1Duration, tv2Duration,tv3Duration, dinnerDuration, durationTextview, activityReport;
    public TextView sleepEndtime, bfEndtime, medicineEndtime, lunchEndtime, tv1Endtime, tv2Endtime,tv3Endtime, dinnerEndtime, showerEndtime, endTimeTextview;
    public TextView sleepState, showerState, bfState, medicationState, lunchState, tv1State, tv2State, tv3State, dinnerState, stateTextview, overallLastResponse,overallLastResponseTime ;
    public RadioButton sleepRisk, showerRisk, breakfastRisk, lunchRisk, dinnerRisk, medicationRisk, tv1Risk, tv2Risk, tv3Risk, overallRisk;
    public RadioButton sleepConfidence, showerConfidence, breakfastConfidence, lunchConfidence, dinnerConfidence, medicationConfidence, tv1Confidence, tv2Confidence,tv3Confidence, overallConfidence;

    public TextView sleepRiskLevelText,breakfastRiskLevelText,medicationRiskLevelText,lunchRiskLevelText,tv1RiskLevelText,tv2RiskLevelText,tv3RiskLevelText, dinnerRiskLevelText,showerRiskLevelText;
    public TextView sleepConfidenceText,breakfastConfidenceText,medicationConfidenceText,lunchConfidenceText,tv1ConfidenceText,tv2ConfidenceText,tv3ConfidenceText, dinnerConfidenceText,showerConfidenceText;
    private RadioGroup sleepRiskGroup, breakfastRiskGroup,medicationRiskGroup,lunchRiskGroup,tv1RiskGroup,tv2RiskGroup,tv3RiskGroup, dinnerRiskGroup,showerRiskGroup;
    private RadioGroup sleepConfidenceGroup, breakfastConfidenceGroup,medicationConfidenceGroup,lunchConfidenceGroup,tv1ConfidenceGroup,tv2ConfidenceGroup,tv3ConfidenceGroup,dinnerConfidenceGroup,showerConfidenceGroup;

    private RadioGroup overallRiskGroup, overallConfidenceGroup;
    public TextView overallRiskLevelText, overallConfidenceText;
    public RadioGroup riskLevelRadioGroup, reportConfidenceRadioGroup;
    public TextView risklevelText, confidenceReportText;
    public View medicineLunchLine, dinnerTv3Line, lunchTV2Line;

    public TextView sleepEndtimeHeader, bfEndtimeHeader, medicineEndtimeHeader, lunchEndtimeHeader, tv1EndtimeHeader, tv2EndtimeHeader,tv3EndtimeHeader, dinnerEndtimeHeader, showerEndtimeHeader, endTimeHeaderTextview;


    Button submitreport;
    String TAG = "ttyl";

    ArrayList<HashMap<String, String>> contactList;

    public CardView sleepCard, showerCard, bfCard, medicineCard, lunchCard, tv1Card, tv2Card,tv3Card, dinnerCard, thisCard, overallCard;

    private final HashMap<String, TextView> durationtextHash = new HashMap<>();

    private final HashMap<String, TextView> endTimetextHash = new HashMap<>();

    private final HashMap<String, TextView> endTimeHeaderTextviewHash = new HashMap<>();

    private final  HashMap<String, TextView> activitystateHash = new HashMap<>();

    private final  HashMap<String, TextView> riskLevelTextHash = new HashMap<>();

    private final  HashMap<String, TextView> reportConfidenceTextHash = new HashMap<>();

    private final  HashMap<String, CardView> cardHash = new HashMap<>();

    private final  HashMap<String, RadioGroup> riskLevelGroupHash = new HashMap<>();

    private final  HashMap<String, RadioGroup> reportConfidenceGroupHash = new HashMap<>();


    private final  HashMap<String, CandleStickChart> activityChartMap = new HashMap<>();

    private String userStr = null;
    public static final String mypreference = "mypref";

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    breceiver ar = new breceiver();

    String timeOfOpeningofReport = "ReportOpeningTime ->  " + String.valueOf(Calendar.getInstance().getTime());


    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.newreportlayout);

        submitreport = (Button) findViewById(R.id.submitreport);
        activityReport = (TextView) findViewById(R.id.report);

        sleepChart = (CandleStickChart) findViewById(R.id.sleepLinechart);
        showerChart = (CandleStickChart) findViewById(R.id.showerLinechart);
        breakfastChart = (CandleStickChart) findViewById(R.id.breakfastLinechart);
        medicineChart = (CandleStickChart) findViewById(R.id.medicationLinechart);
        lunchChart = (CandleStickChart) findViewById(R.id.lunchLinechart);
        tv1Chart = (CandleStickChart) findViewById(R.id.tv1Linechart);
        dinnerChart = (CandleStickChart) findViewById(R.id.dinnerLinechart);
        tv2Chart = (CandleStickChart) findViewById(R.id.tv2Linechart);
        tv3Chart = (CandleStickChart) findViewById(R.id.tv3Linechart);


        YAxis sleepleft = sleepChart.getAxisLeft();
        sleepleft.setAxisMaximum((float) 35.0);
        sleepleft.setAxisMinimum(16);

        YAxis showerleft = showerChart.getAxisLeft();
        showerleft.setAxisMaximum((float) 24.0);
        showerleft.setAxisMinimum(9);

        YAxis bfleft = breakfastChart.getAxisLeft();
        bfleft.setAxisMaximum((float) 11.0);
        bfleft.setAxisMinimum(5);

        YAxis medleft = medicineChart.getAxisLeft();
        medleft.setAxisMaximum((float) 12.0);
        medleft.setAxisMinimum(6);

        YAxis lunchleft = lunchChart.getAxisLeft();
        lunchleft.setAxisMaximum((float) 16.0);
        lunchleft.setAxisMinimum(10);

        YAxis tvleft = tv1Chart.getAxisLeft();
        tvleft.setAxisMaximum((float) 12.0);
        tvleft.setAxisMinimum(6);

        YAxis tvleft2 = tv2Chart.getAxisLeft();
        tvleft2.setAxisMaximum(17);
        tvleft2.setAxisMinimum(11);

        YAxis tvleft3 = tv3Chart.getAxisLeft();
        tvleft3.setAxisMaximum((float) 24.0);
        tvleft3.setAxisMinimum(17);

//        YAxis tvleft3 = chart9.getAxisLeft();
//        tvleft3.setAxisMaximum((float) 12.0);
//        tvleft3.setAxisMinimum(6);

        YAxis dinnerleft = dinnerChart.getAxisLeft();
        dinnerleft.setAxisMaximum((float) 23.0);
        dinnerleft.setAxisMinimum(15);

        activityChartMap.put("Sleep", sleepChart);
        activityChartMap.put("Bath", showerChart);
        activityChartMap.put("Breakfast", breakfastChart);
        activityChartMap.put("medicine ", medicineChart); // TODO Remove the space at end next time
        activityChartMap.put("Lunch", lunchChart);
        activityChartMap.put("TV", tv1Chart);
        activityChartMap.put("TV2", tv2Chart);
        activityChartMap.put("TV3", tv3Chart);
        activityChartMap.put("Dinner", dinnerChart);


        sleepDuration = (TextView) findViewById(R.id.sleepDuration);
        showerDuration = (TextView) findViewById(R.id.showerDuration);
        breakfastDuration = (TextView) findViewById(R.id.breakfastDuration);
        medicationDuration = (TextView) findViewById(R.id.medicationDuration);
        lunchDuration = (TextView) findViewById(R.id.lunchDuration);
        tv1Duration = (TextView) findViewById(R.id.tv1Duration);
        dinnerDuration = (TextView) findViewById(R.id.dinnerDuration);
        tv2Duration = (TextView) findViewById(R.id.tv2Duration);
        tv3Duration = (TextView) findViewById(R.id.tv3Duration);

        sleepEndtime = (TextView) findViewById(R.id.sleeptime);
        bfEndtime = (TextView) findViewById(R.id.breakfasttime);
        medicineEndtime = (TextView) findViewById(R.id.medicationtime);
        lunchEndtime = (TextView) findViewById(R.id.lunchtime);
        tv1Endtime = (TextView) findViewById(R.id.tv1time);
        tv2Endtime = (TextView) findViewById(R.id.tv2time);
        tv3Endtime = (TextView) findViewById(R.id.tv3time);
        dinnerEndtime = (TextView) findViewById(R.id.dinnertime);
        showerEndtime = (TextView) findViewById(R.id.showertime);

        sleepEndtimeHeader = (TextView) findViewById(R.id.sleependtime);
        bfEndtimeHeader = (TextView) findViewById(R.id.breakfastendtime);
        medicineEndtimeHeader = (TextView) findViewById(R.id.medicationendtime);
        lunchEndtimeHeader = (TextView) findViewById(R.id.lunchendtime);
        tv1EndtimeHeader = (TextView) findViewById(R.id.tv1endtime);
        tv2EndtimeHeader = (TextView) findViewById(R.id.tv2endtime);
        tv3EndtimeHeader = (TextView) findViewById(R.id.tv3endtime);
        dinnerEndtimeHeader = (TextView) findViewById(R.id.dinnerendtime);
        showerEndtimeHeader = (TextView) findViewById(R.id.showerendtime);

        sleepState = (TextView) findViewById(R.id.sleepstatus);
        bfState = (TextView) findViewById(R.id.breakfaststatus);
        medicationState = (TextView) findViewById(R.id.medicationstatus);
        lunchState = (TextView) findViewById(R.id.lunchstatus);
        tv1State = (TextView) findViewById(R.id.tv1status);
        tv2State = (TextView) findViewById(R.id.tv2status);
        tv3State = (TextView) findViewById(R.id.tv3status);
        dinnerState = (TextView) findViewById(R.id.dinnerstatus);
        showerState = (TextView) findViewById(R.id.showerstatus);

        overallLastResponse = (TextView) findViewById(R.id.overallLastResponse);
        overallLastResponseTime = (TextView) findViewById(R.id.overallLastResponseTime);

        activitystateHash.put("Sleep", sleepState);
        activitystateHash.put("Bath", showerState);
        activitystateHash.put("Breakfast", bfState);
        activitystateHash.put("medicine ", medicationState); // TODO Remove the space at end next time
        activitystateHash.put("Lunch", lunchState);
        activitystateHash.put("TV", tv1State);
        activitystateHash.put("TV2", tv2State);
        activitystateHash.put("TV3", tv3State);
        activitystateHash.put("Dinner", dinnerState);

        endTimeHeaderTextviewHash.put("Sleep", sleepEndtimeHeader);
        endTimeHeaderTextviewHash.put("Bath", showerEndtimeHeader);
        endTimeHeaderTextviewHash.put("Breakfast", bfEndtimeHeader);
        endTimeHeaderTextviewHash.put("medicine ", medicineEndtimeHeader); // TODO Remove the space at end next time
        endTimeHeaderTextviewHash.put("Lunch", lunchEndtimeHeader);
        endTimeHeaderTextviewHash.put("TV", tv1EndtimeHeader);
        endTimeHeaderTextviewHash.put("TV2", tv2EndtimeHeader);
        endTimeHeaderTextviewHash.put("TV3", tv3EndtimeHeader);
        endTimeHeaderTextviewHash.put("Dinner", dinnerEndtimeHeader);

        endTimetextHash.put("Sleep", sleepEndtime);
        endTimetextHash.put("Bath", showerEndtime);
        endTimetextHash.put("Breakfast", bfEndtime);
        endTimetextHash.put("medicine ", medicineEndtime); // TODO Remove the space at end next time
        endTimetextHash.put("Lunch", lunchEndtime);
        endTimetextHash.put("TV", tv1Endtime);
        endTimetextHash.put("TV2", tv2Endtime);
        endTimetextHash.put("TV3", tv3Endtime);
        endTimetextHash.put("Dinner", dinnerEndtime);

        durationtextHash.put("Sleep", sleepDuration);
        durationtextHash.put("Bath", showerDuration);
        durationtextHash.put("Breakfast", breakfastDuration);
        durationtextHash.put("medicine ", medicationDuration); // TODO Remove the space at end next time
        durationtextHash.put("Lunch", lunchDuration);
        durationtextHash.put("TV", tv1Duration);
        durationtextHash.put("Dinner", dinnerDuration);
        durationtextHash.put("TV2", tv2Duration);
        durationtextHash.put("TV3", tv3Duration);

        sleepCard = (CardView) findViewById(R.id.sleepcard);
        showerCard = (CardView) findViewById(R.id.showercard);
        bfCard = (CardView) findViewById(R.id.breakfastcard);
        medicineCard = (CardView) findViewById(R.id.medicationcard);
        lunchCard = (CardView) findViewById(R.id.lunchcard);
        tv1Card = (CardView) findViewById(R.id.tv1card);
        tv2Card = (CardView) findViewById(R.id.tv2card);
        tv3Card = (CardView) findViewById(R.id.tv3card);
        dinnerCard = (CardView) findViewById(R.id.dinnercard);
        overallCard = (CardView) findViewById(R.id.overallCard);

        cardHash.put("Sleep", sleepCard);
        cardHash.put("Bath", showerCard);
        cardHash.put("Breakfast", bfCard);
        cardHash.put("medicine ", medicineCard);
        cardHash.put("Lunch", lunchCard);
        cardHash.put("TV", tv1Card);
        cardHash.put("Dinner", dinnerCard);
        cardHash.put("TV2", tv2Card);
        cardHash.put("TV3", tv3Card);

        sleepRiskGroup = (RadioGroup) findViewById(R.id.sleepRiskbuttonGroup);
        breakfastRiskGroup = (RadioGroup) findViewById(R.id.breakfastRiskbuttonGroup);
        medicationRiskGroup  = (RadioGroup) findViewById(R.id.medicationRiskbuttonGroup);
        lunchRiskGroup  = (RadioGroup) findViewById(R.id.lunchRiskbuttonGroup);
        tv1RiskGroup  = (RadioGroup) findViewById(R.id.tv1RiskbuttonGroup);
        tv2RiskGroup  = (RadioGroup) findViewById(R.id.tv2RiskbuttonGroup);
        tv3RiskGroup  = (RadioGroup) findViewById(R.id.tv3RiskbuttonGroup);
        dinnerRiskGroup  = (RadioGroup) findViewById(R.id.dinnerRiskbuttonGroup);
        showerRiskGroup  = (RadioGroup) findViewById(R.id.showerRiskbuttonGroup);
        overallRiskGroup = (RadioGroup) findViewById(R.id.overallRiskbuttonGroup);


        sleepRiskLevelText = (TextView) findViewById(R.id.sleepRisklevelText);
        breakfastRiskLevelText = (TextView) findViewById(R.id.breakfastRisklevelText);
        medicationRiskLevelText = (TextView) findViewById(R.id. medicationRisklevelText);
        lunchRiskLevelText = (TextView) findViewById(R.id.lunchRisklevelText);
        tv1RiskLevelText = (TextView) findViewById(R.id.tv1RisklevelText);
        tv2RiskLevelText = (TextView) findViewById(R.id.tv2RisklevelText);
        tv3RiskLevelText = (TextView) findViewById(R.id.tv3RisklevelText);
        dinnerRiskLevelText = (TextView) findViewById(R.id.dinnerRisklevelText);
        showerRiskLevelText = (TextView) findViewById(R.id.showerRisklevelText);
        sleepRiskLevelText = (TextView) findViewById(R.id.sleepRisklevelText);
        overallRiskLevelText = (TextView) findViewById(R.id.overallRisklevelText);


        sleepConfidenceGroup = (RadioGroup) findViewById(R.id.sleepConfidenceGroup);
        breakfastConfidenceGroup = (RadioGroup) findViewById(R.id. breakfastConfidenceGroup);
        medicationConfidenceGroup = (RadioGroup) findViewById(R.id. medicationConfidenceGroup);
        lunchConfidenceGroup = (RadioGroup) findViewById(R.id.lunchConfidenceGroup);
        tv1ConfidenceGroup = (RadioGroup) findViewById(R.id.tv1ConfidenceGroup);
        tv2ConfidenceGroup = (RadioGroup) findViewById(R.id.tv2ConfidenceGroup);
        tv3ConfidenceGroup = (RadioGroup) findViewById(R.id.tv3ConfidenceGroup);
        dinnerConfidenceGroup = (RadioGroup) findViewById(R.id.dinnerConfidenceGroup);
        showerConfidenceGroup = (RadioGroup) findViewById(R.id.showerConfidenceGroup);
        overallConfidenceGroup = (RadioGroup) findViewById(R.id.overallConfidenceGroup);


        sleepConfidenceText = (TextView) findViewById(R.id.sleepReportConfidenceText);
        breakfastConfidenceText = (TextView) findViewById(R.id. breakfastReportConfidenceText);
        medicationConfidenceText = (TextView) findViewById(R.id.medicationReportConfidenceText);
        lunchConfidenceText = (TextView) findViewById(R.id.lunchReportConfidenceText);
        tv1ConfidenceText = (TextView) findViewById(R.id.tv1ReportConfidenceText);
        tv2ConfidenceText = (TextView) findViewById(R.id.tv2ReportConfidenceText);
        tv3ConfidenceText = (TextView) findViewById(R.id.tv3ReportConfidenceText);
        dinnerConfidenceText = (TextView) findViewById(R.id.dinnerReportConfidenceText);
        showerConfidenceText = (TextView) findViewById(R.id.showerReportConfidenceText);
        overallConfidenceText = (TextView) findViewById(R.id.overallReportConfidenceText);

        riskLevelGroupHash.put("Sleep", sleepRiskGroup);
        riskLevelGroupHash.put("Bath", showerRiskGroup);
        riskLevelGroupHash.put("Breakfast", breakfastRiskGroup);
        riskLevelGroupHash.put("medicine ", medicationRiskGroup);
        riskLevelGroupHash.put("Lunch", lunchRiskGroup);
        riskLevelGroupHash.put("TV", tv1RiskGroup);
        riskLevelGroupHash.put("TV2", tv2RiskGroup);
        riskLevelGroupHash.put("TV3", tv3RiskGroup);
        riskLevelGroupHash.put("Dinner", dinnerRiskGroup);
        riskLevelGroupHash.put("Overall", overallRiskGroup);


        reportConfidenceGroupHash.put("Sleep", sleepConfidenceGroup);
        reportConfidenceGroupHash.put("Bath", showerConfidenceGroup);
        reportConfidenceGroupHash.put("Breakfast", breakfastConfidenceGroup);
        reportConfidenceGroupHash.put("medicine ", medicationConfidenceGroup);
        reportConfidenceGroupHash.put("Lunch", lunchConfidenceGroup);
        reportConfidenceGroupHash.put("TV", tv1ConfidenceGroup);
        reportConfidenceGroupHash.put("TV2", tv2ConfidenceGroup);
        reportConfidenceGroupHash.put("TV3", tv3ConfidenceGroup);
        reportConfidenceGroupHash.put("Dinner", dinnerConfidenceGroup);
        reportConfidenceGroupHash.put("Overall", overallConfidenceGroup);


        riskLevelTextHash.put("Sleep", sleepRiskLevelText);
        riskLevelTextHash.put("Bath", showerRiskLevelText);
        riskLevelTextHash.put("Breakfast", breakfastRiskLevelText);
        riskLevelTextHash.put("medicine ", medicationRiskLevelText);
        riskLevelTextHash.put("Lunch", lunchRiskLevelText);
        riskLevelTextHash.put("TV", tv1RiskLevelText);
        riskLevelTextHash.put("TV2", tv2RiskLevelText);
        riskLevelTextHash.put("TV3", tv3RiskLevelText);
        riskLevelTextHash.put("Dinner", dinnerRiskLevelText);
        riskLevelTextHash.put("Overall", overallRiskLevelText);

        reportConfidenceTextHash.put("Sleep", sleepConfidenceText);
        reportConfidenceTextHash.put("Bath", showerConfidenceText);
        reportConfidenceTextHash.put("Breakfast", breakfastConfidenceText);
        reportConfidenceTextHash.put("medicine ", medicationConfidenceText);
        reportConfidenceTextHash.put("Lunch", lunchConfidenceText);
        reportConfidenceTextHash.put("TV", tv1ConfidenceText);
        reportConfidenceTextHash.put("TV2", tv2ConfidenceText);
        reportConfidenceTextHash.put("TV3", tv3ConfidenceText);
        reportConfidenceTextHash.put("Dinner", dinnerConfidenceText);
        reportConfidenceTextHash.put("Overall", overallConfidenceText);

        medicineLunchLine = (View)findViewById(R.id.medicineLunchLine);
        dinnerTv3Line = (View)findViewById(R.id.dinnerTV3Line);
        lunchTV2Line= (View)findViewById(R.id.lunchTV2Line);

        contactList = new ArrayList<>();

        isMyServiceRunning();

        IntentFilter filter = new IntentFilter("intentAction");
        registerReceiver(ar, filter);

        Intent passedIntent = getIntent();
        userStr = passedIntent.getStringExtra("user");

        //why do i need to call userReport class in this class..doesnt work if i dont!!
        Intent userReportIntent = new Intent(this, userReport.class);
        userReportIntent.putExtra("user", userStr);
        ContextCompat.startForegroundService(this,userReportIntent );

        Intent recurringNotifIntent = new Intent(this, recurringNotification.class);
        ContextCompat.startForegroundService(this,recurringNotifIntent );

//        startService(new Intent(this, userReport.class));
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
            if (userReport.class.getName().equals(service.service.getClassName())) {
//                Log.d("", "checking from report class: yes it is running");
                return;
            }
            else{
//                Log.d("", "checking from report class: No the service isnt running");

                Intent userReportIntent = new Intent(this, userReport.class);
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

                    ArrayList<String> startTimearray = intent.getStringArrayListExtra("starttimelist");
//                    Log.d("starttime array", String.valueOf((startTimearray)));

                    ArrayList<String> startTimearray2 = intent.getStringArrayListExtra("starttimelist2");

                    ArrayList<String> datearray = intent.getStringArrayListExtra("datelist");
//                    Log.e("datearray", String.valueOf((datearray)));

                    ArrayList<String> endtimeArray = intent.getStringArrayListExtra("endtimeList");

                    ArrayList<String> endtimeArray2 = intent.getStringArrayListExtra("endtimeList2");

//                   Log.d("endtimeList array22", String.valueOf((endtimeArray2)));

                    String activityType = intent.getStringExtra("actv");
//                    Log.d("getting activity from service", activityType);



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

                    submitbutton(username);
                    
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

                    String tvusername2 = preferences.getString("tvPrefuser2_" + username + LocalDate.now(),"");
                    String tvmonitor2 = preferences.getString("tvPrefmonitor2_" + username + LocalDate.now(), "");
                    String tvresponsedate2 = preferences.getString("tvPrefdate2_" + username + LocalDate.now(),"");
                    String tvresponse2 = preferences.getString("tvPrefresponse2_" + username + LocalDate.now(),"");

                    String tvusername3 = preferences.getString("tvPrefuser3_" + username + LocalDate.now(),"");
                    String tvmonitor3 = preferences.getString("tvPrefmonitor3_" + username + LocalDate.now(), "");
                    String tvresponsedate3 = preferences.getString("tvPrefdate3_" + username + LocalDate.now(),"");
                    String tvresponse3 = preferences.getString("tvPrefresponse3_" + username + LocalDate.now(),"");

                    String dinnerusername = preferences.getString("dinnerPrefuser_" + username + LocalDate.now(),"");
                    String dinnermonitor = preferences.getString("dinnerPrefmonitor_" + username + LocalDate.now(), "");
                    String dinnerresponsedate = preferences.getString("dinnerPrefdate_" + username + LocalDate.now(),"");
                    String dinnerresponse = preferences.getString("dinnerPrefresponse_" + username + LocalDate.now(),"");

                    String overallusername = preferences.getString("overallPrefuser_" + username + LocalDate.now(),"");
                    String overallmonitor = preferences.getString("overallPrefmonitor_" + username + LocalDate.now(), "");
                    String overallresponsedate = preferences.getString("overallPrefdate_" + username + LocalDate.now(),"");
                    String overallresponse = preferences.getString("overallPrefresponse_" + username + LocalDate.now(),"");
                    String overallresponseTime = preferences.getString("overallPrefresponsetime_" + username + LocalDate.now(),"");
                    String overallConfidence = preferences.getString("overallPrefresponseConfidence_" + username + LocalDate.now(),"");


                    String thisdate = String.valueOf(LocalDate.now());


                    assert useremail != null;
                    if((thisdate.equals( sleepresponsedate )) && username.equals(sleepusername) && useremail.equals(sleepmonitor)) {
                        if ((preferences.contains("sleepPrefresponse_" + username + LocalDate.now()))){
//                            Log.d("checking sleep shared preference   ", sleepresponse);
//                            sleepradio.setVisibility(View.GONE);
                            sleepConfidenceGroup.setVisibility(View.GONE);
                            sleepConfidenceText.setVisibility(View.GONE);
                            sleepRiskGroup.setVisibility(View.GONE);
                            sleepRiskLevelText.setVisibility(View.GONE);
                            sleepEndtimeHeader.setText(R.string.endtime);

                            sleepCard.setCardBackgroundColor(getColor(R.color.trafficGreen));
                        }
                    }
                    else {
//                        Log.d("checking if date not same shared preference   ", sleepusername);
                    }

                    if((thisdate.equals( showerresponsedate )) && username.equals(showerusername) && useremail.equals(showermonitor)) {
                        if ((preferences.contains("showerPrefresponse_" + username + LocalDate.now()))){
//                            Log.d("checking shower shared preference   ", showerresponse);
//                            showerradio.setVisibility(View.GONE);

                            showerConfidenceGroup.setVisibility(View.GONE);
                            showerConfidenceText.setVisibility(View.GONE);
                            showerRiskGroup.setVisibility(View.GONE);
                            showerRiskLevelText.setVisibility(View.GONE);
                            showerEndtimeHeader.setText(R.string.endtime);

                            showerCard.setCardBackgroundColor(getColor(R.color.trafficGreen));

                        }
                    }
                    else {
//                        Log.d("checking if date not same shower shared preference   ", sleepusername);
                    }

                    if((thisdate.equals( medicationresponsedate )) && username.equals(medicationusername) && useremail.equals(medicationmonitor)) {
                        if ((preferences.contains("medicationPrefresponse_" + username + LocalDate.now()))){
//                            Log.d("checking medication shared preference   ", medicationresponse);
//                            medicationradio.setVisibility(View.GONE);

                            medicationConfidenceGroup.setVisibility(View.GONE);
                            medicationConfidenceText.setVisibility(View.GONE);
                            medicationRiskGroup.setVisibility(View.GONE);
                            medicationRiskLevelText.setVisibility(View.GONE);
                            medicineEndtimeHeader.setText(R.string.endtime);

                            medicineCard.setCardBackgroundColor(getColor(R.color.trafficGreen));

                        }
                    }
                    else {
//                        Log.d("checking if date not same med shared preference   ", medicationusername);
                    }

                    if((thisdate.equals( breakfastresponsedate )) && username.equals(breakfastusername) && useremail.equals(breakfastmonitor)) {
                        if ((preferences.contains("breakfastPrefresponse_" + username + LocalDate.now()))){
//                            Log.d("checking breakfast shared preference   ", breakfastresponse);
//                            breakfastradio.setVisibility(View.GONE);

                            breakfastConfidenceGroup.setVisibility(View.GONE);
                            breakfastConfidenceText.setVisibility(View.GONE);
                            breakfastRiskGroup.setVisibility(View.GONE);
                            breakfastRiskLevelText.setVisibility(View.GONE);
                            bfEndtimeHeader.setText(R.string.endtime);

                            bfCard.setCardBackgroundColor(getColor(R.color.trafficGreen));

                        }
                    }
                    else {
//                        Log.d("checking if date not same breakfast shared preference   ", breakfastusername);
                    }

                    if((thisdate.equals( lunchresponsedate )) && username.equals(lunchusername) && useremail.equals(lunchmonitor)) {
                        if ((preferences.contains("lunchPrefresponse_" + username + LocalDate.now()))){
//                            Log.d("checking lunch shared preference   ", lunchresponse);
//                            lunchradio.setVisibility(View.GONE);

                            lunchConfidenceGroup.setVisibility(View.GONE);
                            lunchConfidenceText.setVisibility(View.GONE);
                            lunchRiskGroup.setVisibility(View.GONE);
                            lunchRiskLevelText.setVisibility(View.GONE);
                            lunchEndtimeHeader.setText(R.string.endtime);

                            lunchCard.setCardBackgroundColor(getColor(R.color.trafficGreen));

                        }
                    }
                    else {
//                        Log.d("checking if date not same lunch shared preference   ", lunchusername);
                    }

                    assert useremail != null;

                    if((thisdate.equals( tvresponsedate )) && username.equals(tvusername) && useremail.equals(tvmonitor)) {
                        if ((preferences.contains("tvPrefresponse_" + username + LocalDate.now()))){
//                            Log.d("checking tv shared preference   ", tvresponse);
                            tv1ConfidenceGroup.setVisibility(View.GONE);
                            tv1ConfidenceText.setVisibility(View.GONE);
                            tv1RiskGroup.setVisibility(View.GONE);
                            tv1RiskLevelText.setVisibility(View.GONE);
                            tv1EndtimeHeader.setText(R.string.endtime);

                            tv1Card.setCardBackgroundColor(getColor(R.color.trafficGreen));
                        }
                    }
                    else {
//                        Log.d("checking if date not same tv shared preference   ", tvusername);
                    }

                    if((thisdate.equals( tvresponsedate2 )) && username.equals(tvusername2) && useremail.equals(tvmonitor2)) {
                        if ((preferences.contains("tvPrefresponse2_" + username + LocalDate.now()))){
//                            Log.d("checking tv shared preference   ", tvresponse);
                            tv2ConfidenceGroup.setVisibility(View.GONE);
                            tv2ConfidenceText.setVisibility(View.GONE);
                            tv2RiskGroup.setVisibility(View.GONE);
                            tv2RiskLevelText.setVisibility(View.GONE);
                            tv2EndtimeHeader.setText(R.string.endtime);

                            tv2Card.setVisibility(View.GONE);
                        }
                    }
                    else {
//                        Log.d("checking if date not same tv shared preference   ", tvusername);
                    }

                    if(thisdate.equals( tvresponsedate3) && username.equals(tvusername3) && useremail.equals(tvmonitor3)) {
                        if ((preferences.contains("tvPrefresponse3_" + username + LocalDate.now()))){
//                            Log.d("checking tv shared preference   ", tvresponse);

                            tv3ConfidenceGroup.setVisibility(View.GONE);
                            tv3ConfidenceText.setVisibility(View.GONE);
                            tv3RiskGroup.setVisibility(View.GONE);
                            tv3RiskLevelText.setVisibility(View.GONE);
                            tv3EndtimeHeader.setText(R.string.endtime);

                            tv3Card.setVisibility(View.GONE);

                        }
                    }
                    else {
//                        Log.d("checking if date not same tv shared preference   ", tvusername);
                    }
                    if((thisdate.equals( dinnerresponsedate )) && username.equals(dinnerusername) && useremail.equals(dinnermonitor)) {
                        if ((preferences.contains("dinnerPrefresponse_" + username + LocalDate.now()))){
//                            Log.d("checking dinner shared preference   ", dinnerresponse);
//                            dinnerradio.setVisibility(View.GONE);

                            dinnerConfidenceGroup.setVisibility(View.GONE);
                            dinnerConfidenceText.setVisibility(View.GONE);
                            dinnerRiskGroup.setVisibility(View.GONE);
                            dinnerRiskLevelText.setVisibility(View.GONE);
                            dinnerEndtimeHeader.setText(R.string.endtime);

                            dinnerCard.setCardBackgroundColor(getColor(R.color.trafficGreen));

                        }
                    }
                    else {
//                        Log.d("checking if date not same dinner shared preference   ", dinnerusername);
                    }
                    if((thisdate.equals( overallresponsedate )) && username.equals(overallusername) && useremail.equals(overallmonitor)) {
                        if ((preferences.contains("overallPrefresponse_" + username + LocalDate.now()))){
//                            Log.d("checking dinner shared preference   ", dinnerresponse);
                            overallLastResponse.setText(overallresponse + getString(R.string.risk) + " & " + overallConfidence + getString(R.string.confidence));

                            overallLastResponseTime.setText(overallresponseTime);
                        }
                    }
                    else {
                        overallLastResponse.setText(R.string.overallResponsenotReportedYet);


                        overallLastResponseTime.setText(R.string.overallResponsenotReportedYet);
//                        Log.d("checking if date not same dinner shared preference   ", dinnerusername);
                    }


                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Setting date format //for graph

                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // Setting time format //for notification <time check to send notification>

                    assert datearray != null;
                    LocalDate startDate = LocalDate.parse(datearray.get(0));

                    LocalDate today = LocalDate.now();
                    LocalTime currentTime = LocalTime.now();

                    //this index helps to start the graph at a aprticular date: pref after 3 days of data. Is in 2 places, userReport 234
                    int enddateindex = (today.getDayOfYear() % (datearray.size()-2) +2);
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
                    endTimeHeaderTextview = endTimeHeaderTextviewHash.get(activityType);
                    thisCard = cardHash.get(activityType);
                    stateTextview = activitystateHash.get(activityType);

                    riskLevelRadioGroup = riskLevelGroupHash.get(activityType);
                    reportConfidenceRadioGroup = reportConfidenceGroupHash.get(activityType);
                    risklevelText = riskLevelTextHash.get(activityType);
                    confidenceReportText = reportConfidenceTextHash.get(activityType);

                    if (Float.parseFloat(activitydur) >= 60) {

                        String durationOfActivity = Long.parseLong(activitydur) / 60 % 24 + getString(R.string.hrs) + " & " + Long.parseLong(activitydur) % 60 + getString(R.string.mins);
                        assert durationTextview != null;

                        assert endarray != null;
                        assert endtimeArray2 != null;
                        String actvTime = endtimeArray.get(enddateindex);
                        String startTime = startTimearray.get(enddateindex);
//                        Log.d("compareactivitytime", (startTime));

                        LocalDateTime lct = LocalDateTime.parse(actvTime, timeFormatter);
                        LocalTime actTime = lct.toLocalTime(); //get activity completion time in localtime format

                        LocalDateTime startlct = LocalDateTime.parse(startTime, timeFormatter);
                        LocalTime startactTime = startlct.toLocalTime();

                        String endtimeText = endtimeArray2.get(enddateindex);
                        String starttimeText = startTimearray2.get(enddateindex);

                        if (currentTime.isAfter(startactTime) && currentTime.isBefore(actTime)) {
                            thisCard.setCardBackgroundColor(getColor(R.color.trafficRed));

                            prevModEndArray.add("0");
                            prevModStartArray.add("0");

                            drawchart(prevModStartArray, prevModEndArray, activityType);
                            durationTextview.setVisibility(View.GONE);
                            endTimeTextview.setText(starttimeText);
                            endTimeHeaderTextview.setText(R.string.starttime);
                            stateTextview.setText(getString(R.string.activityOngoing));

                            riskLevelRadioGroup.setVisibility(View.GONE);
                            risklevelText.setVisibility(View.GONE);
                            reportConfidenceRadioGroup.setVisibility(View.GONE);
                            confidenceReportText.setVisibility(View.GONE);

//                            Log.e(TAG, "onReceive: mocktheweek- > " + "->> " + prevModEndArray + "-->> " + prevModStartArray);


                        } else if (currentTime.isAfter(actTime)) {

                            thisCard.setCardBackgroundColor(getColor(R.color.trafficYellow));
                            drawchart(modStartArray, modEndArray, activityType);
                            durationTextview.setText(durationOfActivity);
                            endTimeHeaderTextview.setText(R.string.endtime);
                            endTimeTextview.setText(endtimeText);
                            stateTextview.setText(getString(R.string.activityComplete));

                        } else {

                            if (activityType.equals("TV2") || activityType.equals("TV3")) {
                                thisCard.setVisibility(View.GONE);
                                dinnerTv3Line.setVisibility(View.GONE);
                                lunchTV2Line.setVisibility(View.GONE);
                            }
                            else {
                                thisCard.setCardBackgroundColor(getColor(R.color.trafficRed));
                                stateTextview.setText(getString(R.string.activityIncomplete));
                                endTimeHeaderTextview.setText(R.string.endtime);
                                durationTextview.setVisibility(View.GONE);
                                endTimeTextview.setVisibility(View.GONE);
//                                Log.e(TAG, "onReceive: mocktheweek- > " + "->> " + prevModEndArray + "-->> " + prevModStartArray);
                                prevModEndArray.add("0");
                                prevModStartArray.add("0");
                                drawchart(prevModStartArray, prevModEndArray, activityType);
                            }

                            riskLevelRadioGroup.setVisibility(View.GONE);
                            risklevelText.setVisibility(View.GONE);
                            reportConfidenceRadioGroup.setVisibility(View.GONE);
                            confidenceReportText.setVisibility(View.GONE);
                            }

                        }

                    else{
                            // Log.d("durationOfActivity", String.valueOf(durationOfActivity));

                            assert endtimeArray2 != null;
                            String actvTime = endtimeArray.get(enddateindex);
//                        Log.d("compareactivitytime2", (actvTime));
                            String startTime = startTimearray.get(enddateindex);

                            LocalDateTime lct = LocalDateTime.parse(actvTime, timeFormatter);
                            LocalTime actTime = lct.toLocalTime(); //get activity completion time in localtime format

//                        Log.e(TAG, "onReceive: parsing error checking ->  " +lct);

                            String endtimeText = endtimeArray2.get(enddateindex);
                            String starttimeText = startTimearray2.get(enddateindex);

                            LocalDateTime startlct = LocalDateTime.parse(startTime, timeFormatter);
                            LocalTime startactTime = startlct.toLocalTime();


                            if (currentTime.isAfter(startactTime) && currentTime.isBefore(actTime)) {

                                thisCard.setCardBackgroundColor(getColor(R.color.trafficRed));
                                durationTextview.setVisibility(View.GONE);
                                endTimeHeaderTextview.setText(R.string.starttime);
                                endTimeTextview.setText(starttimeText);
                                stateTextview.setText(getString(R.string.activityOngoing));
                                prevModEndArray.add("0");
                                prevModStartArray.add("0");
                                drawchart(prevModStartArray, prevModEndArray, activityType);
//                                Log.e(TAG, "onReceive: mocktheweek- > " + "->> " + prevModEndArray + "-->> " + prevModStartArray);

                                riskLevelRadioGroup.setVisibility(View.GONE);
                                risklevelText.setVisibility(View.GONE);
                                reportConfidenceRadioGroup.setVisibility(View.GONE);
                                confidenceReportText.setVisibility(View.GONE);

//                                Log.e(TAG, "onReceive: mocktheweek- > " + "->> " + startactTime + "-->> " + actTime);

                            }
                            else if (currentTime.isAfter(actTime)) {

                                if((activityType.equals("medicine "))&& (activitydur.equals("0"))){
                                    medicineCard.setCardBackgroundColor(getColor(R.color.trafficYellow));
                                    drawchart(modStartArray, modEndArray, activityType);
                                    durationTextview.setVisibility(View.GONE);
                                    endTimeHeaderTextview.setText(R.string.endtime);
                                    endTimeTextview.setText(getString(R.string.notTaken));
                                    stateTextview.setText(getString(R.string.missed));
                                }else{
                                thisCard.setCardBackgroundColor(getColor(R.color.trafficYellow));
                                durationTextview.setText(activitydur + getString(R.string.minutes));
                                endTimeHeaderTextview.setText(R.string.endtime);
                                endTimeTextview.setText(endtimeText);
                                stateTextview.setText(getString(R.string.activityComplete));
                                drawchart(modStartArray, modEndArray, activityType);}

                            } else {

                                if(activityType.equals("TV2") ||activityType.equals("TV3")) {
                                    thisCard.setVisibility(View.GONE);
                                    dinnerTv3Line.setVisibility(View.GONE);
                                    lunchTV2Line.setVisibility(View.GONE);
                                }
                                else{
                                    thisCard.setCardBackgroundColor(getColor(R.color.trafficRed));
                                    stateTextview.setText(getString(R.string.activityIncomplete));
                                    endTimeHeaderTextview.setText(R.string.endtime);
                                    durationTextview.setVisibility(View.GONE);
                                    endTimeTextview.setVisibility(View.GONE);
//                                    Log.e(TAG, "onReceive: mocktheweek- > " + "->> " + prevModEndArray + "-->> " + prevModStartArray);
                                    prevModEndArray.add("0");
                                    prevModStartArray.add("0");
                                    drawchart(prevModStartArray, prevModEndArray, activityType);
                                }

                                riskLevelRadioGroup.setVisibility(View.GONE);
                                risklevelText.setVisibility(View.GONE);
                                reportConfidenceRadioGroup.setVisibility(View.GONE);
                                confidenceReportText.setVisibility(View.GONE);
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

            selectedChart.setTouchEnabled(false);
//            selectedChart.setDoubleTapToZoomEnabled(false);
//            selectedChart.setPinchZoom(false);
//            selectedChart.setVisibleXRangeMaximum(7);
//            selectedChart.setVisibleXRangeMaximum(7);

            selectedChart.notifyDataSetChanged();
            selectedChart.getAxisRight().setDrawLabels(true);
            selectedChart.setDrawBorders(true);
            selectedChart.setHighlightPerDragEnabled(true);
            selectedChart.setBorderColor(getResources().getColor((R.color.borderofGraph)));

            XAxis xAxis = selectedChart.getXAxis();
            xAxis.setValueFormatter(new MyXAxisValueFormatter(x.size()));

          Log.d("graph thing", String.valueOf(x.size()));


            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

            xAxis.setDrawGridLines(false);
            xAxis.setDrawLabels(true);
            xAxis.setGranularityEnabled(true);
            xAxis.setGranularity(1f);
            //xAxis.setLabelRotationAngle(45f);
            xAxis.setLabelCount(4);

            YAxis leftAxis = selectedChart.getAxisLeft();
            YAxis rightAxis = selectedChart.getAxisRight();
            leftAxis.setValueFormatter(new MyYAxisValueFormatter(x.size()));

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
                checkingTime = "RecurringNotificationTime ->  " + bundle.getString("recurringNotification_generatedANDclick");

            }

            else if (actBasedNotIntent.hasExtra("activityBased_notificationGeneratedTime"))
            {
                checkingTime = "ActivityBasedNotificationTime ->  " + actBasedNotIntent.getStringExtra("activityBased_notificationGeneratedTime");
            }

            else
                {
                checkingTime = "FreeWillApplicationOpeningTime ->  " + bundle.getString("FreeWillOpeningMutualMonitor");
            }


           // Log.d(TAG, "submitbutton: " + actvEndTime);

            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            final FirebaseUser thisuser = FirebaseAuth.getInstance().getCurrentUser();
            assert thisuser != null;
            final String email = thisuser.getEmail();
            final Object userdr = email; //change date format here

            String finalCheckingTime = checkingTime;
          //  Log.e(TAG, "submitbutton: aaaaaaaa" + " :" + finalCheckingTime);

            submitreport.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View view) {

                    HashMap<String, Object> sleepMap = new HashMap<>();
                    HashMap<String, Object> showerMap = new HashMap<>();
                    HashMap<String, Object> medicationMap = new HashMap<>();
                    HashMap<String, Object> breakfastMap = new HashMap<>();
                    HashMap<String, Object> lunchMap = new HashMap<>();
                    HashMap<String, Object> dinnerMap = new HashMap<>();
                    HashMap<String, Object> tv1Map = new HashMap<>();
                    HashMap<String, Object> tv2Map = new HashMap<>();
                    HashMap<String, Object> tv3Map = new HashMap<>();
                    HashMap<String, Object> overallMap = new HashMap<>();

                    assert email != null;
//                    Log.d("EdVanWood", email);
                    String id = ref.push().getKey();
                    assert id != null;

                    final int sleepRiskResponse = sleepRiskGroup.getCheckedRadioButtonId();
                    final int showerRiskResponse = showerRiskGroup.getCheckedRadioButtonId();
                    final int medicationRiskResponse = medicationRiskGroup.getCheckedRadioButtonId();
                    final int breakfastRiskResponse = breakfastRiskGroup.getCheckedRadioButtonId();
                    final int lunchRiskResponse = lunchRiskGroup.getCheckedRadioButtonId();
                    final int dinnerRiskResponse = dinnerRiskGroup.getCheckedRadioButtonId();
                    final int tv1RiskResponse = tv1RiskGroup.getCheckedRadioButtonId();
                    final int tv2RiskResponse = tv2RiskGroup.getCheckedRadioButtonId();
                    final int tv3RiskResponse = tv3RiskGroup.getCheckedRadioButtonId();

                    final int sleepReportConfidence = sleepConfidenceGroup.getCheckedRadioButtonId();
                    final int showerReportConfidence = showerConfidenceGroup.getCheckedRadioButtonId();
                    final int medicationReportConfidence = medicationConfidenceGroup.getCheckedRadioButtonId();
                    final int breakfastReportConfidence = breakfastConfidenceGroup.getCheckedRadioButtonId();
                    final int lunchReportConfidence = lunchConfidenceGroup.getCheckedRadioButtonId();
                    final int dinnerReportConfidence = dinnerConfidenceGroup.getCheckedRadioButtonId();
                    final int tv1ReportConfidence = tv1ConfidenceGroup.getCheckedRadioButtonId();
                    final int tv2ReportConfidence = tv2ConfidenceGroup.getCheckedRadioButtonId();
                    final int tv3ReportConfidence = tv3ConfidenceGroup.getCheckedRadioButtonId();

                    final int overallRiskResponse = overallRiskGroup.getCheckedRadioButtonId();
                    final int overallReportConfidence = overallConfidenceGroup.getCheckedRadioButtonId();


                    String timeValues = " Time values  ^_^ " + finalCheckingTime + " -> " + timeOfOpeningofReport + "  ReportingTime ->  " + Calendar.getInstance().getTime();

                    if ( sleepRiskResponse == -1
                            && showerRiskResponse == -1
                            && breakfastRiskResponse == -1
                            && medicationRiskResponse == -1
                            && lunchRiskResponse == -1
                            && tv1RiskResponse == -1
                            && dinnerRiskResponse == -1
                            && overallRiskResponse == -1
                            && tv2RiskResponse == -1
                            && tv3RiskResponse == -1

                            && sleepReportConfidence == -1
                            && showerReportConfidence == -1
                            && breakfastReportConfidence == -1
                            && medicationReportConfidence == -1
                            && lunchReportConfidence == -1
                            && tv1ReportConfidence == -1
                            && dinnerReportConfidence== -1
                            && overallReportConfidence == -1
                            && tv2ReportConfidence == -1
                            && tv3ReportConfidence == -1 )
                    {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.missedWhileReporting), Toast.LENGTH_SHORT).show();
                    }
                    else {

                        if (sleepRiskGroup != null && sleepRiskGroup.isEnabled() && sleepRiskResponse != -1 && sleepConfidenceGroup != null && sleepConfidenceGroup.isEnabled() && sleepReportConfidence != -1) {
                            sleepRisk = (RadioButton) findViewById(sleepRiskResponse);
                            sleepConfidence = (RadioButton)findViewById(sleepReportConfidence);

                            sleepMap.put("Sleep state", userdr);

                            String finalTimes = finalCheckingTime + " -> " + timeOfOpeningofReport + "  ReportingTime ->  " + Calendar.getInstance().getTime();

                            ref.child(username).child("Sleep").child( "Risk is - >  " + String.valueOf(sleepRisk.getText())).child("Confidence is ->  "+ String.valueOf(sleepConfidence.getText())).child(id).child(finalTimes).updateChildren(sleepMap);

                            editor.putString("sleepPrefuser_" + username + LocalDate.now(), username);
                            editor.putString("sleepPrefmonitor_" + username + LocalDate.now(), email);
                            editor.putString("sleepPrefresponse_" + username + LocalDate.now(), String.valueOf(sleepRisk.getText()));
                            editor.putString("sleepPrefdate_" + username + LocalDate.now(), String.valueOf(LocalDate.now()));

                            editor.apply(); //to get it back, need to do, preferences.getString("same key", )

//                            Log.e(TAG, "onClick: testing report submission time" + " ^_^ " + finalCheckingTime + " -> " + timeOfOpeningofReport + "  ReportingTime ->  " + Calendar.getInstance().getTime() );
//                            Log.e(TAG, "onClick: testing if local time is varied" +  "  " + timeValues );
                        }

                        if (showerRiskGroup != null && showerRiskGroup.isEnabled() && showerRiskResponse != -1 && showerConfidenceGroup != null && showerConfidenceGroup.isEnabled() && showerReportConfidence != -1) {
                            showerRisk = (RadioButton) findViewById(showerRiskResponse);
                            showerConfidence = (RadioButton)findViewById(showerReportConfidence);

//                            Log.d("value ", String.valueOf(shower.getText()));
                            showerMap.put("Shower state", userdr);

                            String finalTimes = finalCheckingTime + " -> " + timeOfOpeningofReport + "  ReportingTime ->  " + Calendar.getInstance().getTime();

                            ref.child(username).child("Shower").child("Risk is - >  " +String.valueOf(showerRisk.getText())).child("Confidence is ->  "+ String.valueOf(showerConfidence.getText())).child(id).child(finalTimes).updateChildren(showerMap);

                            editor.putString("showerPrefuser_" + username+ LocalDate.now(), username);
                            editor.putString("showerPrefmonitor_" + username+ LocalDate.now(), email);
                            editor.putString("showerPrefresponse_" + username+ LocalDate.now(), String.valueOf(showerRisk.getText()));
                            editor.putString("showerPrefdate_" + username+ LocalDate.now(), String.valueOf(LocalDate.now()));

                            editor.apply();
//                            Log.e(TAG, "onClick: testing report submission time" + " ^_^ " + finalCheckingTime + " -> " + timeOfOpeningofReport + "  ReportingTime ->  " + Calendar.getInstance().getTime() );

                        }
                        if (breakfastRiskGroup != null && breakfastRiskGroup.isEnabled() && breakfastRiskResponse != -1 && breakfastConfidenceGroup != null && breakfastConfidenceGroup.isEnabled() && breakfastReportConfidence != -1){
                            breakfastRisk = (RadioButton) findViewById(breakfastRiskResponse);
                            breakfastConfidence= (RadioButton) findViewById(breakfastReportConfidence);

//                            Log.d("value   ", String.valueOf(breakfast.getText()));
                            breakfastMap.put("Breakfast state", userdr);
                            String finalTimes = finalCheckingTime + " -> " + timeOfOpeningofReport + "  ReportingTime ->  " + Calendar.getInstance().getTime();

                            ref.child(username).child("Breakfast").child("Risk is - >  " +String.valueOf(breakfastRisk.getText())).child("Confidence is ->  "+ String.valueOf(breakfastConfidence.getText())).child(id).child(finalTimes).updateChildren(breakfastMap);

                            editor.putString("breakfastPrefuser_" + username+ LocalDate.now(), username);
                            editor.putString("breakfastPrefmonitor_" + username+ LocalDate.now(), email);
                            editor.putString("breakfastPrefresponse_" + username+ LocalDate.now(), String.valueOf(breakfastRisk.getText()));
                            editor.putString("breakfastPrefdate_" + username+ LocalDate.now(), String.valueOf(LocalDate.now()));

                            editor.apply();
//                            Log.e(TAG, "onClick: testing report submission time" + " ^_^ " + finalCheckingTime + " -> " + timeOfOpeningofReport + "  ReportingTime ->  " + Calendar.getInstance().getTime() );


                        }
                        if (medicationRiskGroup != null && medicationRiskGroup.isEnabled() && medicationRiskResponse != -1 && medicationConfidenceGroup != null && medicationConfidenceGroup.isEnabled() && medicationReportConfidence != -1) {
                            medicationRisk = (RadioButton) findViewById(medicationRiskResponse);
                            medicationConfidence= (RadioButton) findViewById(medicationReportConfidence);
//                            Log.d("value   ", String.valueOf(medication.getText()));
                            medicationMap.put("Medication state", userdr);
                            String finalTimes = finalCheckingTime + " -> " + timeOfOpeningofReport + "  ReportingTime ->  " + Calendar.getInstance().getTime();

                            ref.child(username).child("Medication").child("Risk is - >  " +String.valueOf(medicationRisk.getText())).child("Confidence is ->  "+ String.valueOf(medicationConfidence.getText())).child(id).child(finalTimes).updateChildren(medicationMap);

                            editor.putString("medicationPrefuser_" + username+ LocalDate.now(), username);
                            editor.putString("medicationPrefmonitor_" + username+ LocalDate.now(), email);
                            editor.putString("medicationPrefresponse_" + username+ LocalDate.now(), String.valueOf(medicationRisk.getText()));
                            editor.putString("medicationPrefdate_" + username+ LocalDate.now(), String.valueOf(LocalDate.now()));

                            editor.apply();
//                            Log.e(TAG, "onClick: testing report submission time" + " ^_^ " + finalCheckingTime + " -> " + timeOfOpeningofReport+ "  ReportingTime ->  " + Calendar.getInstance().getTime() );


                        }
                        if (lunchRiskGroup != null && lunchRiskGroup.isEnabled() && lunchRiskResponse != -1 && lunchConfidenceGroup != null && lunchConfidenceGroup.isEnabled() && lunchReportConfidence != -1) {
                            lunchRisk = (RadioButton) findViewById(lunchRiskResponse);
                            lunchConfidence= (RadioButton) findViewById(lunchReportConfidence);

                            lunchMap.put("lunch state", userdr);
                            String finalTimes = finalCheckingTime + " -> " + timeOfOpeningofReport + "  ReportingTime ->  " + Calendar.getInstance().getTime();

                            ref.child(username).child("Lunch").child("Risk is - >  " +String.valueOf(lunchRisk.getText())).child("Confidence is ->  "+ String.valueOf(lunchConfidence.getText())).child(id).child(finalTimes).updateChildren(lunchMap);

                            editor.putString("lunchPrefuser_" + username+ LocalDate.now(), username);
                            editor.putString("lunchPrefmonitor_" + username+ LocalDate.now(), email);
                            editor.putString("lunchPrefresponse_" + username+ LocalDate.now(), String.valueOf(lunchRisk.getText()));
                            editor.putString("lunchPrefdate_" + username+ LocalDate.now(), String.valueOf(LocalDate.now()));

                            editor.apply();
//                            Log.e(TAG, "onClick: testing report submission time" + " ^_^ " + finalCheckingTime + " -> " + timeOfOpeningofReport+ "  ReportingTime ->  " + Calendar.getInstance().getTime() );


                        }
                        if (tv1RiskGroup != null && tv1RiskGroup.isEnabled() && tv1RiskResponse != -1 && tv1ConfidenceGroup != null && tv1ConfidenceGroup.isEnabled() && tv1ReportConfidence != -1) {
                            tv1Risk = (RadioButton) findViewById(tv1RiskResponse);
                            tv1Confidence= (RadioButton) findViewById(tv1ReportConfidence);

//                            Log.d("value   ", String.valueOf(tv.getText()));
                            tv1Map.put("TV state", userdr);
                            String finalTimes = finalCheckingTime + " -> " + timeOfOpeningofReport + "  ReportingTime ->  " + Calendar.getInstance().getTime();

                            ref.child(username).child("TV").child("Risk is - >  " +String.valueOf(tv1Risk.getText())).child("Confidence is ->  "+ String.valueOf(tv1Confidence.getText())).child(id).child(finalTimes).updateChildren(tv1Map);

                            editor.putString("tvPrefuser_" + username+ LocalDate.now(), username);
                            editor.putString("tvPrefmonitor_" + username+ LocalDate.now(), email);
                            editor.putString("tvPrefresponse_" + username+ LocalDate.now(), String.valueOf(tv1Risk.getText()));
                            editor.putString("tvPrefdate_" + username+ LocalDate.now(), String.valueOf(LocalDate.now()));

                            editor.apply();
//                            Log.e(TAG, "onClick: testing report submission time" + " ^_^ " + finalCheckingTime + " -> " + timeOfOpeningofReport+ "  ReportingTime ->  " + Calendar.getInstance().getTime() );

                        }
                        if (tv2RiskGroup != null && tv2RiskGroup.isEnabled() && tv2RiskResponse != -1 && tv2ConfidenceGroup != null && tv2ConfidenceGroup.isEnabled() && tv2ReportConfidence != -1) {
                            tv2Risk = (RadioButton) findViewById(tv2RiskResponse);
                            tv2Confidence= (RadioButton) findViewById(tv2ReportConfidence);

//                            Log.d("value   ", String.valueOf(tv.getText()));
                            tv2Map.put("TV state2", userdr);
                            String finalTimes = finalCheckingTime + " -> " + timeOfOpeningofReport + "  ReportingTime ->  " + Calendar.getInstance().getTime();

                            ref.child(username).child("TV2").child("Risk is - >  " +String.valueOf(tv2Risk.getText())).child("Confidence is ->  "+ String.valueOf(tv2Confidence.getText())).child(id).child(finalTimes).updateChildren(tv2Map);

                            editor.putString("tvPrefuser2_" + username+ LocalDate.now(), username);
                            editor.putString("tvPrefmonitor2_" + username+ LocalDate.now(), email);
                            editor.putString("tvPrefresponse2_" + username+ LocalDate.now(), String.valueOf(tv2Risk.getText()));
                            editor.putString("tvPrefdate2_" + username+ LocalDate.now(), String.valueOf(LocalDate.now()));

                            editor.apply();
//                            Log.e(TAG, "onClick: testing report submission time" + " ^_^ " + finalCheckingTime + " -> " + timeOfOpeningofReport+ "  ReportingTime ->  " + Calendar.getInstance().getTime() );

                        }
                        if (tv3RiskGroup != null && tv3RiskGroup.isEnabled() && tv3RiskResponse != -1 && tv3ConfidenceGroup != null && tv3ConfidenceGroup.isEnabled() && tv3ReportConfidence != -1) {
                            tv3Risk = (RadioButton) findViewById(tv3RiskResponse);
                            tv3Confidence= (RadioButton) findViewById(tv3ReportConfidence);

//                            Log.d("value   ", String.valueOf(tv.getText()));
                            tv3Map.put("TV state3", userdr);
                            String finalTimes = finalCheckingTime + " -> " + timeOfOpeningofReport + "  ReportingTime ->  " + Calendar.getInstance().getTime();

                            ref.child(username).child("TV3").child("Risk is - >  " +String.valueOf(tv3Risk.getText())).child("Confidence is ->  "+ String.valueOf(tv3Confidence.getText())).child(id).child(finalTimes).updateChildren(tv3Map);

                            editor.putString("tvPrefuser3_" + username+ LocalDate.now(), username);
                            editor.putString("tvPrefmonitor3_" + username+ LocalDate.now(), email);
                            editor.putString("tvPrefresponse3_" + username+ LocalDate.now(), String.valueOf(tv3Risk.getText()));
                            editor.putString("tvPrefdate3_" + username+ LocalDate.now(), String.valueOf(LocalDate.now()));

                            editor.apply();
//                            Log.e(TAG, "onClick: testing report submission time" + " ^_^ " + finalCheckingTime + " -> " + timeOfOpeningofReport+ "  ReportingTime ->  " + Calendar.getInstance().getTime() );

                        }
                        if (dinnerRiskGroup != null && dinnerRiskGroup.isEnabled() && dinnerRiskResponse != -1 && dinnerConfidenceGroup != null && dinnerConfidenceGroup.isEnabled() && dinnerReportConfidence != -1) {
                            dinnerRisk = (RadioButton) findViewById(dinnerRiskResponse);
                            dinnerConfidence= (RadioButton) findViewById(dinnerReportConfidence);

//                            Log.d("value   ", String.valueOf(dinner.getText()));
                            dinnerMap.put("dinner state", userdr);
                            String finalTimes = finalCheckingTime + " -> " + timeOfOpeningofReport + "  ReportingTime ->  " + Calendar.getInstance().getTime();

                            ref.child(username).child("Dinner").child("Risk is - >  " +String.valueOf(dinnerRisk.getText())).child("Confidence is ->  "+ String.valueOf(dinnerConfidence.getText())).child(id).child(finalTimes).updateChildren(dinnerMap);

                            editor.putString("dinnerPrefuser_" + username+ LocalDate.now(), username);
                            editor.putString("dinnerPrefmonitor_" + username+ LocalDate.now(), email);
                            editor.putString("dinnerPrefresponse_" + username+ LocalDate.now(), String.valueOf(dinnerRisk.getText()));
                            editor.putString("dinnerPrefdate_" + username+ LocalDate.now(), String.valueOf(LocalDate.now()));

                            editor.apply();
//                            Log.e(TAG, "onClick: testing report submission time" + " ^_^ " + finalCheckingTime + " -> " + timeOfOpeningofReport + "  ReportingTime ->  " + Calendar.getInstance().getTime() );

                        }
                        if (overallRiskGroup != null && overallRiskGroup.isEnabled() && overallRiskResponse != -1 && overallConfidenceGroup != null && overallConfidenceGroup.isEnabled() && overallReportConfidence != -1) {
                            overallRisk = (RadioButton) findViewById(overallRiskResponse);
                            overallConfidence= (RadioButton) findViewById(overallReportConfidence);

//                          Log.d("value   ", String.valueOf(sleep.getText()));
                            overallMap.put("Overall state", userdr);
                            String finalTimes = finalCheckingTime + " -> " + timeOfOpeningofReport + "  ReportingTime ->  " + Calendar.getInstance().getTime();

                            ref.child(username).child("Overall").child("Risk is - >  " +String.valueOf(overallRisk.getText())).child("Confidence is ->  "+ String.valueOf(overallConfidence.getText())).child(id).child(finalTimes).updateChildren(overallMap);

                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
                            LocalDateTime now = LocalDateTime.now();
                            String lastResponseTime = dtf.format(now);

                            editor.putString("overallPrefuser_" + username + LocalDate.now(), username);
                            editor.putString("overallPrefmonitor_" + username + LocalDate.now(), email);
                            editor.putString("overallPrefresponse_" + username + LocalDate.now(), String.valueOf(overallRisk.getText()));
                            editor.putString("overallPrefresponseConfidence_" + username + LocalDate.now(), String.valueOf(overallConfidence.getText()));
                            editor.putString("overallPrefdate_" + username + LocalDate.now(), String.valueOf(LocalDate.now()));
                            editor.putString("overallPrefresponsetime_" + username + LocalDate.now(), lastResponseTime);


                            editor.apply(); //to get it back, need to do, preferences.getString("same key", )

//                            Log.e(TAG, "onClick: testing report submission time" + " ^_^ " + finalCheckingTime + " -> " + timeOfOpeningofReport+ "  ReportingTime ->  " + Calendar.getInstance().getTime() );

                        }

                        Intent newintent = new Intent(report.this, thankYou.class);
                        startActivity(newintent);
                    }
                }
            });
        }

        private class MyYAxisValueFormatter implements IAxisValueFormatter {
            public MyYAxisValueFormatter(int size) {

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

            public MyXAxisValueFormatter(int dateSize) {

                LocalDate today = LocalDate.now();

                LocalDate graphDateStart = today.minusDays(dateSize);

                for(int i = 1; i< 12;i++){

                    Log.e(TAG, "today->  " + String.valueOf(today));
                    Log.e(TAG, String.valueOf(dateSize));
                    Log.e(TAG, "start "+ String.valueOf(graphDateStart));

                    ZoneId defaultZoneId = ZoneId.systemDefault();
                    Date graphDate = Date.from(graphDateStart.atStartOfDay(defaultZoneId).toInstant());

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    sdf.applyPattern("MM/dd");
                    String endActivityTime = sdf.format(graphDate);

                    Calendar c = Calendar.getInstance();
                    try{
                        c.setTime(sdf.parse(endActivityTime));
                    }catch(ParseException e){
                        e.printStackTrace();
                    }
                    //Incrementing the date by 1 day
                    c.add(Calendar.DAY_OF_MONTH, i);
                    String thisNewDate = sdf.format(c.getTime());

                    mValues.add(thisNewDate);
                }

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