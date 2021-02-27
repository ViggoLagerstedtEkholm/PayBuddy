package com.example.paybuddy.Models;

public class HistoryModel {
    private String number;
    private String date;
    private String time;
    private String type;

    public HistoryModel(String number, String date, String time, String type) {
        this.number = number;
        this.date = date;
        this.type = type;
        this.time = time;
    }

    public String getTime(){
        return time;
    }

    public String getNumber() {
        return number;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }
}
