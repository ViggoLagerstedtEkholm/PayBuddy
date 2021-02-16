package com.example.paybuddy.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="Item_table")
public class ItemModel {
    @PrimaryKey(autoGenerate =true )
    private int ID;

    private double price;
    private String description;
    private int quantity;
    private long occasionID;

    public ItemModel(double price, String description, int quantity) {
        this.price = price;
        this.description = description;
        this.quantity = quantity;
    }

    public long getOccasionID(){
        return this.occasionID;
    }

    public void setOccasionID(long ID){
        this.occasionID = ID;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

}
