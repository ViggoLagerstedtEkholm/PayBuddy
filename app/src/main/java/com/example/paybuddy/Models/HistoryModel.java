package com.example.paybuddy.Models;

/**
 * This class contains all the data our history should have.
 * We also use getters and setters to access this data.
 *  @date 2021-03-09
 *  @version 1.0
 *  @author Viggo Lagerstedt Ekholm
 */

public class HistoryModel {
    private final String number;
    private final String date;
    private final String time;
    private final String type;

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
