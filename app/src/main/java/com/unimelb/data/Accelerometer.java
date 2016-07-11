package com.unimelb.data;


public class Accelerometer implements Record {
    private String date = null;
    private float value = 0.f;
    public Accelerometer(String date, float value) {
        this.date = date;
        this.value = value;
    }
    public String getDate() {
        return this.date;
    }
    public float getValue() {
        return this.value;
    }
}
