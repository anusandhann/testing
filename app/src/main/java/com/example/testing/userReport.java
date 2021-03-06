package com.example.testing;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class userReport extends JobIntentService {
    public static final String CHANNEL_2_ID = "channel2";
    public static final String CHANNEL_delete_ID = "deletethischannel";
    public static final String CHANNEL_recurring_ID = "recurringNotification";

    AlarmManager alarmManager;
    private PendingIntent alarmIntent;
    public static final int notification_id = 1;
    private NotificationManagerCompat notifManager;
    Context context;

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, userReport.class,notification_id,intent);
//        Log.d(TAG, "enqueueWork: userReport");
    }

    @Override
    protected void onHandleWork(@NonNull Intent Tintent) {
//        Log.d(TAG, "onHandleWork: for enque");
        }


    @Override
    public void onCreate() {
        super.onCreate();
        context = getBaseContext();
        notifManager = NotificationManagerCompat.from(this);
//        Log.d(TAG, "onCreate: userReport");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("", "start of the usereport service");

        getActivityData task = new getActivityData();
        task.execute();

        String input = intent.getStringExtra("userReport");

        createNotificationChannel();

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_delete_ID)
                .setContentTitle("Thank You for Checking the Elderly!!")
                .setContentText(input)
                .setSmallIcon(R.drawable.icon)
                .build();
        startForeground(100, notification);

        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() { super.onDestroy();
    }


    private class getActivityData extends AsyncTask<Void, Void, Void> {
        
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "http://ec2-18-177-160-75.ap-northeast-1.compute.amazonaws.com/api";
//            String url = "http://163.221.68.248/api";

            String jsonStr = sh.makeServiceCall(url);
            ArrayList<HashMap<String, String>> activityMapList = new ArrayList<>();

//            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr.substring(jsonStr.indexOf("{"), jsonStr.lastIndexOf("}") + 1));

                    // Getting JSON Array node
                    JSONArray jsonActivities = jsonObj.getJSONArray("result");

                    // looping through All JSON Activities, putting them into the array list
                    for (int i = 0; i < jsonActivities.length(); i++) {

                        JSONObject c = jsonActivities.getJSONObject(i);

                        String activity = c.getString("actv");
                        String username = c.getString("user");
                        String endtime = c.getString("end_time");
                        String starttime = c.getString("start_time");
                        String activityDuration = c.getString("duration");
                        String activityDate = c.getString("date");

                        // tmp hash map for single activityMap
                        HashMap<String, String> activityMap = new HashMap<>();
                        // adding each child node to HashMap key => value
                        activityMap.put("actv", activity);
                        activityMap.put("user", username);
                        activityMap.put("end_time", endtime);
                        activityMap.put("start_time", starttime);
                        activityMap.put("duration", activityDuration);
                        activityMap.put("date", activityDate);

                        activityMapList.add(activityMap);
                    }

                    // Find all users
                    ArrayList<String> usernameList = new ArrayList<>();
                    for ( HashMap<String, String> activityMap : activityMapList ) {
//                        if activityMap.get("user") is not in username list: put it in
                        if (!usernameList.contains(activityMap.get("user"))) {
                            usernameList.add(activityMap.get("user"));
                        }
                    }
                    // Find all activity types
                    ArrayList<String> activityTypeList = new ArrayList<>();
                    for ( HashMap<String, String> activityMap : activityMapList ) {
                        if (!activityTypeList.contains(activityMap.get("actv"))) {
                            activityTypeList.add(activityMap.get("actv"));
                        }
                    }

                    // Loop through all users
                    for ( String userFilter : usernameList ) {

                        // Loop through all activity types
                        for (String activityTypeFilter : activityTypeList) {
                            ArrayList<String> startList = new ArrayList<>();
                            ArrayList<String> endList = new ArrayList<>();
                            ArrayList<String> dateList = new ArrayList<>();
                            ArrayList<String> durationList = new ArrayList<>();
                            ArrayList<String> endtimeList = new ArrayList<>();
                            ArrayList<String> userList = new ArrayList<>();
                            ArrayList<String> endtimeList2 = new ArrayList<>();
                            ArrayList<String> startTimeList = new ArrayList<>();



                            // Find all actvities in the activity map list that match our filters
                            for (HashMap<String, String> activityMap : activityMapList) {

                                // Check if the user does not match the filter
                                if (!Objects.equals(activityMap.get("user"), userFilter)) {
                                    continue;
                                }

                                // Check if the activity type does not match the filter
                                if (!Objects.equals(activityMap.get("actv"), activityTypeFilter)) {
                                    continue;
                                }

                                // Fill up the array lists using values from each activity map
                                startList.add(activityMap.get("start_time"));
                                endList.add(activityMap.get("end_time"));
                                endtimeList.add(activityMap.get("end_time"));
                                durationList.add(activityMap.get("duration"));
                                dateList.add(activityMap.get("date"));
                                userList.add(activityMap.get("user"));
                                endtimeList2.add(activityMap.get("end_time"));
                                startTimeList.add(activityMap.get("start_time"));
                            }

                            // Correct the format of the start times in the start time list
                            for (int i = 0; i < startList.size(); i++) {
                                // Convert the timestamp format to numeric values that can be graphed
                                @SuppressLint("SimpleDateFormat") Date newtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startList.get(i));
                                Calendar cal = Calendar.getInstance();
                                assert newtime != null;
                                cal.setTime(newtime);
                                String startTimeChartVal = String.valueOf(cal.get(Calendar.HOUR_OF_DAY) + ((float) (cal.get(Calendar.MINUTE)) / 60.0));

                                // Replace the original list item
                                startList.set(i, startTimeChartVal);
                            }

                            // Correct the format of the end times in the end time list
                            for (int i = 0; i < endList.size(); i++) {
                                // Convert the timestamp format to numeric values that can be graphed

                                @SuppressLint("SimpleDateFormat") Date newtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endList.get(i));
                                Calendar cal = Calendar.getInstance();
                                assert newtime != null;
                                cal.setTime(newtime);
                                String endTimeVal = String.valueOf(cal.get(Calendar.HOUR_OF_DAY) + ((float) (cal.get(Calendar.MINUTE)) / 60.0));

                                // Replace the original list item

                                endList.set(i, endTimeVal);
                            }

                            //to get the end time of activities for each day for all users
                            LocalDate today = LocalDate.now();
                            int enddateindex = (today.getDayOfYear() % (dateList.size() - 2)+ 2);

                            String endOfEachForToday = endtimeList.get(enddateindex);  //this is the end time of each activity for one particular day, the day defined by the index

                            LocalDate endDate = LocalDate.parse(dateList.get(enddateindex));

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date thisDate = sdf.parse(endOfEachForToday);
                            sdf.applyPattern("HH:mm");
                            String endActivityTime = sdf.format(thisDate);
                            Log.d("endtime for activity ", ((endActivityTime) + "  " + activityTypeFilter + "  user " + userFilter));

                           //  showsNotification(endActivityTime);

                            //to get the current time in same format as end time, to compare and send notification if both are same

                            for (int i = 0; i < endtimeList.size(); i++) {

                                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                                @SuppressLint("SimpleDateFormat") Date newtimes = sdf2.parse(endtimeList2.get(i));
                                sdf2.applyPattern("HH:mm");
                                String endActivityTimes = sdf2.format(newtimes);

                                endtimeList2.set(i, endActivityTimes);
                            }

                            String currentTime = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
                            Calendar cal2 = Calendar.getInstance();
                            Date d = new SimpleDateFormat("HH:mm").parse(currentTime);
                            assert d != null;
                            cal2.setTime(d);
                           // Log.d("timefortodaycurrent", (currentTime));

                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//                            Log.d(TAG, "doInBackground:" + currentTime );


                            for (int i = 0; i < dateList.size(); i++) {

                                String datestr = dateList.get(i);
                                LocalDate localOne = LocalDate.parse(datestr, formatter);

                                if(localOne.isEqual(endDate)){
                                  //  Log.d("datefornotification", String.valueOf((localOne)));

                                    for (int y = 0; y <= 60 * 24; y++){
                                        cal2.add(Calendar.MINUTE, 1);
                                        @SuppressLint("SimpleDateFormat") String increasedTime = new SimpleDateFormat("HH:mm").format(cal2.getTime());
                                        // Log.d("increased time", increasedTime);

                                        if ((increasedTime.equals(endActivityTime)) && (increasedTime.equals(currentTime))){
//                                            Log.d("sametimefornot", increasedTime);
                                            //send notification from here at that moment when increased time value equals end time of activity
                                            showsNotification(increasedTime, userFilter, activityTypeFilter);
                                        }
                                    }
                            }}

                            // Correct the format of the duration in the duration list
                            for ( int i = 0; i < durationList.size(); i++ ) {
                                // Convert the duration format to something
                                String durationVal = String.valueOf(Integer.parseInt(durationList.get(i))/ (60 * 1000));
                                // Replace the original list item
                                durationList.set(i, durationVal);
                            }

                            for (int i = 0; i < userList.size(); i++) {
                                String thisUser = String.valueOf(userList.get(i));
                                userList.set(i,thisUser);
                            }

                            // Build the intent
                            String lastactivityduration = durationList.get(durationList.size()-1);
                            Intent in = new Intent("intentAction");
                            in.putExtra("user", userFilter);
                            in.putExtra("actv", activityTypeFilter);
                            in.putExtra("activityDuration", lastactivityduration);
                            in.putStringArrayListExtra("starttimearraylist", startList);
                            in.putStringArrayListExtra("endtimearraylist", endList);
                            in.putStringArrayListExtra("durationarraylist", durationList);
                            in.putStringArrayListExtra("datelist", dateList);
                            in.putStringArrayListExtra("endtimeList", endtimeList);
                            in.putStringArrayListExtra("thisUser", userList);
                            in.putStringArrayListExtra("endtimeList2", endtimeList2);
                            in.putStringArrayListExtra("starttimelist", startTimeList);


                            // Broadcast the intent
                            sendBroadcast(in);

                        }
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        stopForeground(true);
                    } else {
                        stopSelf();
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

        private void runOnUiThread(Runnable runnable) {
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    private void showsNotification(String nTime, String user, String whichActivity) {


        createNotificationChannel2();

        String abNotificationTime = String.valueOf(Calendar.getInstance().getTime());

        Intent activityBasedNotificationIntent = new Intent(this, report.class);

        activityBasedNotificationIntent.putExtra("user", user);

        activityBasedNotificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        activityBasedNotificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        activityBasedNotificationIntent.putExtra("activityBased_notificationGeneratedTime", abNotificationTime);

//        PendingIntent pendingIntent = PendingIntent.getActivity(this,
//                0, notificationIntent, 0);

        Log.e(TAG, "showsNotification: currentTimeforActivitybasedNotification  -> " + abNotificationTime );

        PendingIntent pendingIntent = PendingIntent.getActivity(this, UUID.randomUUID().hashCode(), activityBasedNotificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        String targetName = null;

        if (user.equals("1")){
            targetName = getResources().getString(R.string.target1);
        }
        if (user.equals("2")){
            targetName = getResources().getString(R.string.target2);
        }
        if (user.equals("3")){
            targetName = getResources().getString(R.string.target3);
        }


        Notification notification = new NotificationCompat.Builder(this, CHANNEL_2_ID)
                .setContentText(getString(R.string.pleaseCheck) + targetName + getString(R.string.whoFinished)+ whichActivity + getString(R.string.activity))
                .setSmallIcon(R.drawable.ic_stat_name)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent)
                .setColor(getColor(R.color.appTheme))
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .build();

        //notifManager.notify(12, notification);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        //checking current time, and comparing it to received time from method call, if same send

        String exactTime = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
//        Log.d( "timeValueHere", exactTime);
//        Log.d(TAG, "received time" + "     " + nTime);

        if (exactTime.equals(nTime)) {
            notificationManager.notify(notification_id, notification);
//            Log.d(TAG, "notification is sent");
        }
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_delete_ID, getString(R.string.temporary), NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("This is temporary"); //to show to Users That the app is running
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private void createNotificationChannel2() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_2_ID, getString(R.string.activitybasedNotification), NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("This is main"); //to show to Users That the app is running
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }
}



