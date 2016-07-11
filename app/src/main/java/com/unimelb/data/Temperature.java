package com.unimelb.data;


public class Temperature implements Record {
    private String date = null;
    private float value = 0.f;
    public Temperature(String date, float value) {
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
