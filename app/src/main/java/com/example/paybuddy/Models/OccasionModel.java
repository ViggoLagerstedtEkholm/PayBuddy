package com.example.paybuddy.Models;

import java.util.List;

public class OccasionModel {
    private int ID;
    private String date;
    private String description;
    private List<ItemModel> items;
    private boolean isPaid;

    public OccasionModel(int ID, String date, String description, List<ItemModel> items) {
        this.ID = ID;
        this.date = date;
        this.description = description;
        this.items = items;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public void setPaid(boolean paid) {
        isPaid = paid;
    }
}
