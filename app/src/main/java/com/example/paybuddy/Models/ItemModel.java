package com.example.paybuddy.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * This class contains all the data our item should have.
 * We also use getters and setters to access this data.
 *  @date 2021-03-09
 *  @version 1.0
 *  @author Viggo Lagerstedt Ekholm
 */

@Entity(tableName="Item_table")
public class ItemModel {
    @PrimaryKey(autoGenerate =true )
    private int ID;

    private double price;
    private final String description;
    private final int quantity;
    private long occasionID;
    private final String assignedPerson;

    public ItemModel(double price, String description, int quantity, String assignedPerson) {
        this.price = price;
        this.description = description;
        this.quantity = quantity;
        this.assignedPerson = assignedPerson;
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

    public String getAssignedPerson() {
        return assignedPerson;
    }
}
