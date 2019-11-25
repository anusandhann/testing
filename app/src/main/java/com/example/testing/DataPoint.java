package com.example.testing;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class DataPoint extends RecyclerView.Adapter {

    private int sleep, cook;
    private String time;


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getSleep() {
        return sleep;
    }

    public void setSleep(int sleep) {
        this.sleep = sleep;
    }

    public int getCook() {
        return cook;
    }

    public void setCook(int cook) {
        this.cook = cook;
    }

    @Override
    public String toString() {
        return "DataPoint{" +
                "time=" + time +
                ", sleep=" + sleep +
                ", cook=" + cook +
                '}';
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
