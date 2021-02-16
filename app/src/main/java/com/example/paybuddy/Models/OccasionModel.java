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

    private boolean isPaid;
    private boolean isExpired;

    public OccasionModel(String date, String description, boolean isPaid, boolean isExpired) {
        this.date = date;
        this.description = description;
        this.isExpired = isExpired;
        this.isPaid = isPaid;
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

    public boolean isPaid() {
        return isPaid;
    }

    public boolean isExpired() {
        return isExpired;
    }

}
