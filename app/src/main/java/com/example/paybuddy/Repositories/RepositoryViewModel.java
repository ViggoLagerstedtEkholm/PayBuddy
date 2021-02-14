package com.example.paybuddy.Repositories;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.paybuddy.Models.OccasionModel;
import com.example.paybuddy.database.DatabaseHelper;
import com.example.paybuddy.database.FILTER_TYPE;

import org.w3c.dom.CDATASection;

import java.util.ArrayList;
import java.util.List;

public class RepositoryViewModel extends ViewModel {
    private MutableLiveData<List<OccasionModel>> items;
    private OccasionRepository occasionRepository;

    public void init(Context context){
        if(items != null){
            return;
        }
        items = new MutableLiveData<>();
        occasionRepository = OccasionRepository.getInstance(context);
        items.setValue(occasionRepository.getOccasion("", FILTER_TYPE.SEARCH_NOTPAID));
    }

    public LiveData<List<OccasionModel>> getOccasionList(String searchWord, FILTER_TYPE filter){
        List<OccasionModel> models = occasionRepository.getOccasion(searchWord, filter);
        items.setValue(models);
        return items;
    }

    public void setFilterOccasionList(String searchWord, FILTER_TYPE filter){
        items.setValue(occasionRepository.getOccasion(searchWord, filter));
        Log.d("Filtering", "...");
    }

    public void updateOccasionIsPaid(int ID){
        occasionRepository.updateOccasionIsPaid(ID);

        List<OccasionModel> currentItems = new ArrayList<>();
        items.setValue(currentItems);
    }

    public void updateOccasionExpiredDate(int ID){
        occasionRepository.updateOccasionExpiredDate(ID);
    }

    public int getSumOccasions(){
        return occasionRepository.getSumOccasions();
    }

    public double getSumCost(){
        return occasionRepository.getSumCost();
    }

    public void insertOccasion(OccasionModel occasionModel){
        List<OccasionModel> currentItems = items.getValue();
        currentItems.add(occasionModel);
        items.setValue(currentItems);
        occasionRepository.addOccasion(occasionModel);
    }

    public void deleteOccasion(OccasionModel occasionModel){
        List<OccasionModel> currentItems = items.getValue();
        currentItems.remove(occasionModel);
        items.setValue(currentItems);
        occasionRepository.deleteOccasion(occasionModel);
    }
}
