package com.example.testing;

import android.annotation.SuppressLint;
import android.app.Service;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

                            } else {
                                long cookinghour = cookingtime;
                            }
                            Log.d("duration of cooking", "" + cookingtime);

                            Log.d("start time for cooking", "" + date);

                            managenotifications not = new managenotifications(context);
                            not.notice();
                        }

                        if (activity.equals("Eat")) {
                            contact.put("duration", eatduration);
                            int eatingtime = Integer.parseInt(cookduration) / (60 * 1000);
                            Log.d("Eating time", "" + eatingtime);

                            if (eatingtime >= 60) {
                                long cookinghour = eatingtime / (60);
                                report rn = new report();
                                rn.drawchart2();

                            } else {
//                                report rn = new report();
//                                rn.drawchart2();
                            }
                        }
                        else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                }
                            });
                        }
                        // adding contact to contact list
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

        private void runOnUiThread(Runnable runnable) {
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
}



