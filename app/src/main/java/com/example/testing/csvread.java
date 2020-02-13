package com.example.testing;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class csvread extends AppCompatActivity
{
    private RecyclerView csvread;
    LineChart testbarchart;
    private String TAG = csvread.class.getSimpleName();
    private ListView lv;

    ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      //  csvread = (RecyclerView) findViewById(R.id.csvview);
        testbarchart = (LineChart)findViewById(R.id.testbarchart);

       // readcsv();
       // readjson();

        contactList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);
        new GetContacts().execute();

    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(csvread.this,"Json Data is downloading",Toast.LENGTH_LONG).show();

        }
        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "https://api.androidhive.info/contacts/";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr.substring(jsonStr.indexOf("{"), jsonStr.lastIndexOf("}") + 1));

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("contacts");

                    // looping through All Actv
                    for (int i = 0; i < contacts.length(); i++) {

                        JSONObject c = contacts.getJSONObject(i);

//                        String id = c.getString("id");
                        String name = c.getString("name");
                        String email = c.getString("email");
//                      String address = c.getString("address");
//                        String gender = c.getString("gender");

                         //Phone node is JSON Object
                        JSONObject phone = c.getJSONObject("phone");
                        String mobile = phone.getString("mobile");
//                        String home = phone.getString("home");
//                        String office = phone.getString("office");

                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
//                        contact.put("id", id);
                        contact.put("name", name);
                        contact.put("email", email);
                       // contact.put("mobile", mobile);

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
            ListAdapter adapter = new SimpleAdapter(csvread.this, contactList,
                    R.layout.list_item, new String[]{"name","email"},
                    new int[]{R.id.name, R.id.email});
            lv.setAdapter(adapter);
            //drawchart();
        }
    }
    private void drawchart() {

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, R.id.mobile)); //5 is the value
        entries.add(new Entry(15, R.id.mobile));

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
        lDataSet2.setColors(R.color.red);
        lDataSet2.setLineWidth(4);
        lDataSet2.setDrawValues(false);
        chartdata.addDataSet(lDataSet2);

        testbarchart.setData(chartdata);
        testbarchart.invalidate();
    }

    private List<DataPoint> dailylife = new ArrayList<>();

    private void readcsv() {

        InputStream ins= getResources().openRawResource(R.raw.lessdata);
        BufferedReader bfread = new BufferedReader(new InputStreamReader
                (ins, Charset.forName("UTF-8")));

        String line = "";

        try {
                //step over headers

            bfread.readLine();

            int prevtime = 0, prevcook = 0, cookstarttime = 0, cookendtime = 0;

            while ( (line = bfread.readLine())!= null) {

                //split columns

                String[] tokens = line.split(",");
                //read data

                DataPoint sample = new DataPoint();

                try {

                    sample.setCook(Integer.parseInt(tokens[1]));

                    prevcook = sample.getCook();

                    if ((cookstarttime == 0) && (prevcook != sample.getCook())){
                        cookstarttime = Integer.parseInt(tokens[0]);
                    }
                    else if (prevcook != sample.getCook()){
                        cookendtime = Integer.parseInt(tokens[0]);

                    }

                    String time1 = "01/14/2012 23:29:58";
                    String time2 = "01/15/2012 08:31:48";

                    @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    Date date1 = format.parse(time1);
                    Date date2 = format.parse(time2);

                    long diff = date2.getTime() - date1.getTime();

                    long diffSeconds = diff / 1000 % 60;
                    long diffMinutes = diff / (60 * 1000) % 60;
                    long diffHours = diff / (60 * 60 * 1000) % 24;
                    long diffDays = diff / (24 * 60 * 60 * 1000);

                    Log.d("now", cookstarttime + " " + cookendtime + " Difference is : "

                            + diffDays + "  " + "days" + "  " + diffHours + " " + "hours" + "  "

                            + diffMinutes + " "+ "Minutes" + " " + diffSeconds + "  " + "Seconds");


                    Date timeforgraph = format.parse(time1);

                    format.applyPattern("HH:mm");

                    String graphtiming = format.format(timeforgraph);

                    Log.d("graphtime", "  " + graphtiming);


                } catch (NumberFormatException e) {
                     System.out.println("No no");
                     e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                try {
                    sample.setSleep(Integer.parseInt(tokens[2]));
                } catch (NumberFormatException e) {
                    // System.out.println("No no");
                }

                try {
                    sample.setTime(tokens[0]);
                } catch (NumberFormatException e) {
                    //  System.out.println("No no");
                }


                Log.d("that", tokens[0]);

                Log.d("this", cookstarttime + "      " + cookendtime + "  " + prevcook);

                dailylife.add(sample);
                // csvread.setAdapter(sample);

            }

    }catch (IOException e){
            e.printStackTrace();
        }


    }

    private void readjson() {


    }


    }
