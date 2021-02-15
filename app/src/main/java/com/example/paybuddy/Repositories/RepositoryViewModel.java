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
    private OccasionRepository occasionRepository;
    private MutableLiveData<List<OccasionModel>> occasions;
    public LiveData<List<OccasionModel>> getOccasions(FILTER_TYPE filter){
        fetchOccasions(filter);
        return occasions;
    }

    private void fetchOccasions(FILTER_TYPE filter){
        occasions.setValue(occasionRepository.getOccasions(filter));
    }

    public void init(Context context){
        if(occasions != null){
            return;
        }
        occasions = new MutableLiveData<>();
        occasionRepository = OccasionRepository.getInstance(context);
    }

    public void updateOccasionIsPaid(int ID){
        occasionRepository.updateOccasionIsPaid(ID);

        List<OccasionModel> currentItems = new ArrayList<>();
        occasions.setValue(currentItems);
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
        occasionRepository.addOccasion(occasionModel);

        List<OccasionModel> currentItems = occasions.getValue();
        currentItems.add(occasionModel);
        occasions.setValue(currentItems);
    }

    public void deleteOccasion(OccasionModel occasionModel){
        occasionRepository.deleteOccasion(occasionModel);

        List<OccasionModel> currentItems = occasions.getValue();
        currentItems.remove(occasionModel);
        occasions.setValue(currentItems);
    }
}
