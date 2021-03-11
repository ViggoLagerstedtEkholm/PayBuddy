package com.example.paybuddy.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * This class contains all the data our location should have.
 * We also use getters and setters to access this data.
 *  @date 2021-03-09
 *  @version 1.0
 *  @author Viggo Lagerstedt Ekholm
 */

@Entity(tableName="Location_table")
public class LocationModel {
    @PrimaryKey(autoGenerate = true)
    private int ID;

    private final double latitude;
    private final double longitude;
    private final double altitude;
    private final double accuracy;
    private final String address;

    public long getOccasionID() {
        return occasionID;
    }

    public void setOccasionID(long occasionID) {
        this.occasionID = occasionID;
    }

    private long occasionID;

    public LocationModel(double latitude, double longitude, double altitude, double accuracy, String address) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.accuracy = accuracy;
        this.address = address;
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

    public double getLongitude() {
        return longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public String getAddress() {
        return address;
    }
}
