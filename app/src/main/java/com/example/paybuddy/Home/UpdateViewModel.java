package com.example.paybuddy.Home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.paybuddy.MainActivity;
import com.example.paybuddy.Models.ItemModel;

import java.util.List;

public class UpdateViewModel extends ViewModel {
    private final MutableLiveData<Double> totalPrice = new MutableLiveData<>();
    private final MutableLiveData<Integer> totalOccasion = new MutableLiveData<>();
    private final MutableLiveData<Integer> totalExpired = new MutableLiveData<>();

    public LiveData<Double> getTotalPrice(){
        return totalPrice;
    }

    public LiveData<Integer> getTotalOccasion(){
        return totalOccasion;
    }
    public void updateTotalOccasion(Integer value){
        totalOccasion.setValue(value);
    }

    public void updateTotalPrice(Double value){
        totalPrice.setValue(value);
    }
}
