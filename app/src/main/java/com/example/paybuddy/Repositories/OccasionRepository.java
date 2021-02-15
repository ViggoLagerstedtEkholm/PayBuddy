package com.example.paybuddy.Repositories;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.paybuddy.Models.OccasionModel;
import com.example.paybuddy.database.DatabaseHelper;
import com.example.paybuddy.database.FILTER_TYPE;

import java.util.ArrayList;
import java.util.List;

public class OccasionRepository {
    private static OccasionRepository instance;
    private static DatabaseHelper databaseHelper;

    public static OccasionRepository getInstance(Context context){
        if(instance == null){
            instance = new OccasionRepository();
        }
        databaseHelper = DatabaseHelper.getInstance(context);
        return instance;
    }

    public List<OccasionModel> getOccasions(FILTER_TYPE filter){
        return databaseHelper.filterOccasion(filter);
    }

    public void updateOccasionIsPaid(int ID){
        databaseHelper.updateOccasionIsPaid(ID);
    }

    public void updateOccasionExpiredDate(int ID){
        databaseHelper.updateOccasionExpiredDate(ID);
    }

    public int getSumOccasions(){
        return databaseHelper.getAmountOfOccasion();
    }

    public double getSumCost(){
        return databaseHelper.getSumItems();
    }

    public void addOccasion(OccasionModel occasionModel){
        databaseHelper.insertOccasion(occasionModel);
    }

    public void deleteOccasion(OccasionModel occasionModel){
        databaseHelper.delete(occasionModel);
    }
}
