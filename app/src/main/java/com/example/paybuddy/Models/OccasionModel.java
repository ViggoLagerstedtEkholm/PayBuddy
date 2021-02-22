package com.example.paybuddy.Models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName="Occasions_table")
public class OccasionModel {
    @PrimaryKey(autoGenerate = true)
    private int ID;
    private String date;
    private String description;
    @Ignore
    private List<ItemModel> items;
    @Ignore
    private LocationModel locationModel;

    private boolean isPaid;
    private boolean isExpired;

    public OccasionModel(String date, String description, boolean isPaid, boolean isExpired) {
        this.date = date;
        this.description = description;
        this.isExpired = isExpired;
        this.isPaid = isPaid;
    }

    public LocationModel getLocationModel(){
        return this.locationModel;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public List<ItemModel> getItems() {
        return items;
    }

    public void setItems(List<ItemModel> items) {
        this.items = items;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setPaid(boolean isPaid){
        this.isPaid = isPaid;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean isExpired){
        this.isExpired = isExpired;
    }

    public void setLocationModel(LocationModel locationModel){
        this.locationModel = locationModel;
    }
}
