package com.example.testing;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;

class MyYAxisValueFormatter implements IAxisValueFormatter {

    private String[] mValues;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.hh");

    public MyYAxisValueFormatter() {
        //this.mValues = values;

    }


    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mValues[(int) value];
    }
}