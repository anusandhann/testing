package com.example.testing;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class userreport extends Service {

    Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getBaseContext();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("", "start of the usereport service");
        final getactivitydata task = new getactivitydata();
        task.execute();
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private class getactivitydata extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @SuppressLint("SetTextI18n")
        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "http://163.221.68.248:8080/api";
            String jsonStr = sh.makeServiceCall(url);
            ArrayList<HashMap<String, String>> activityMapList = new ArrayList<>();

            Log.e(TAG, "Response from url: " + jsonStr);
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
                    Log.d("usernameList array", String.valueOf((usernameList)));

                    // Find all activity types
                    ArrayList<String> activityTypeList = new ArrayList<>();
                    for ( HashMap<String, String> activityMap : activityMapList ) {
                        if (!activityTypeList.contains(activityMap.get("actv"))) {
                            activityTypeList.add(activityMap.get("actv"));
                        }
                    }
                    Log.d("activityTypeList array", String.valueOf((activityTypeList)));

                    // Loop through all users
                    for ( String userFilter : usernameList ) {

                        // Loop through all activity types
                        for (String activityTypeFilter : activityTypeList) {
                            ArrayList<String>  startList = new ArrayList<>();
                            ArrayList<String>  endList = new ArrayList<>();
                            ArrayList<String>  dateList = new ArrayList<>();
                            ArrayList<String>  durationList = new ArrayList<>();

                            // Find all actvities in the activity map list that match our filters
                            for (HashMap<String, String> activityMap : activityMapList) {
                                Log.d("User/Activity", activityMap.get("user") + " " + activityMap.get("actv"));
                                // Check if the user does not match the filter
                                if (!activityMap.get("user").equals(userFilter)) {
                                    continue;
                                }

                                // Check if the activity type does not match the filter
                                if (!activityMap.get("actv").equals(activityTypeFilter)) {
                                    continue;
                                }

                                // Fill up the array lists using values from each activity map
                                startList.add( activityMap.get("start_time")  );
                                endList.add( activityMap.get("end_time")  );
                                durationList.add( activityMap.get("duration")  );
                                dateList.add(activityMap.get("date"));

                            }
                            Log.d("startList array", String.valueOf((startList)));
                            Log.d("endList array", String.valueOf((endList)));
                            Log.d("durationList array", String.valueOf((durationList)));

                            // Correct the format of the start times in the start time list
                            for ( int i = 0; i < startList.size(); i++ ) {
                                // Convert the timestamp format to numeric values that can be graphed
                                @SuppressLint("SimpleDateFormat") Date newtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startList.get(i));
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(newtime);
                                String startTimeChartVal = String.valueOf(cal.get(Calendar.HOUR_OF_DAY) + ((float)(cal.get(Calendar.MINUTE)) / 60.0));


                                Log.d("testing newtime array", (startTimeChartVal));
                                // Replace the original list item
                                startList.set(i, startTimeChartVal);
                            }

                            // Correct the format of the end times in the end time list
                            for ( int i = 0; i < endList.size(); i++ ) {
                                // Convert the timestamp format to numeric values that can be graphed

                                @SuppressLint("SimpleDateFormat") Date newtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endList.get(i));
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(newtime);
                                String endTimeVal = String.valueOf(cal.get(Calendar.HOUR_OF_DAY) + ((float)(cal.get(Calendar.MINUTE)) / 60.0));

                                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                                String endtime = sdf.format(newtime);
                                String endTimeMinutes = String.valueOf(endtime);

                                @SuppressLint("SimpleDateFormat") String currentTime = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());

                               // Log.d("testing endtime array", (endTimeMinutes));
                                //Log.d("testing currenttime array", (currentTime));


                                if (endTimeMinutes.equals(currentTime)) {
                                    managenotifications sendNotification = new managenotifications(context);
                                    sendNotification.notice(currentTime);
                                    Log.d("testing for notification", currentTime);
                                }

                                // Replace the original list item
                                endList.set(i, endTimeVal);
                            }

                            // Correct the format of the duration in the duration list
                            for ( int i = 0; i < durationList.size(); i++ ) {
                                // Convert the duration format to something
                                String durationVal = String.valueOf(Integer.parseInt(durationList.get(i))/ (60 * 1000));
                                // Replace the original list item
                                durationList.set(i, durationVal);
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


                            // Broadcast the intent
                            sendBroadcast(in);

                        }
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
}



