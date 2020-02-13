package com.example.testing;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


public class report extends AppCompatActivity {

    LineChart chart1;
    private RadioGroup statebutton;
    Button submitreport;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.report);

        chart1 = (LineChart) findViewById(R.id.barchart1);
        chart1.setBackgroundColor(Color.WHITE);
        chart1.getDescription().setEnabled(false);
        chart1.setTouchEnabled(false);
        drawchart();

        buttonreader();
    }

    private void drawchart(){

    ArrayList<Entry> entries = new ArrayList<>();
    entries.add(new Entry(0, 5)); //5 is the value
    entries.add(new Entry(15, 5));

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

    chart1.setData(chartdata);
    chart1.invalidate();
}

    private void buttonreader(){

        statebutton = (RadioGroup)findViewById(R.id.radiostate);
        submitreport = (Button)findViewById(R.id.submitreport);

        final int selectedstate = statebutton.getCheckedRadioButtonId();

        submitreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(report.this,submitreport.getText(),Toast.LENGTH_LONG).show();
            }
        });

}

}