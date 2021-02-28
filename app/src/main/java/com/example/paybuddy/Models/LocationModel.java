package com.example.paybuddy.Models;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.TextView;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.paybuddy.R;

@Entity(tableName="Location_table")
public class LocationModel {
    @PrimaryKey(autoGenerate = true)
    private int ID;

    private double latitude;
    private double longitude;
    private double altititude;
    private double accuracy;
    private String adress;

    public long getOccasionID() {
        return occasionID;
    }

    public void setOccasionID(long occasionID) {
        this.occasionID = occasionID;
    }

    private long occasionID;

    public LocationModel(double latitude, double longitude, double altititude, double accuracy, String adress) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altititude = altititude;
        this.accuracy = accuracy;
        this.adress = adress;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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
