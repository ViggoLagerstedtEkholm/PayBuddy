package com.example.paybuddy.Models;

import android.widget.TextView;

import com.example.paybuddy.R;

//TODO
public class LocationModel {
    private double latitude;
    private double longitude;
    private double altititude;
    private double accuracy;
    private String adress;

    public LocationModel(double latitude, double longitude, double altititude, double accuracy, String adress) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altititude = altititude;
        this.accuracy = accuracy;
        this.adress = adress;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAltititude() {
        return altititude;
    }

    public void setAltititude(double altititude) {
        this.altititude = altititude;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }
}
