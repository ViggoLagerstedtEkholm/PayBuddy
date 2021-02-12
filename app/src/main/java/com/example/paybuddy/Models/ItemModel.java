package com.example.paybuddy.Models;

public class ItemModel {
    private int ID;
    private double price;
    private String description;
    private int quantity;

    public ItemModel(int ID, double price, String description, int quantity) {
        this.ID = ID;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

}
