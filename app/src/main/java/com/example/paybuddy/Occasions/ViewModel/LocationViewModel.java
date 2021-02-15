package com.example.paybuddy.Occasions.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.paybuddy.Models.LocationModel;

public class LocationViewModel extends ViewModel {
    private final MutableLiveData<LocationModel> itemToAddList = new MutableLiveData<>();

    public void setLocation(LocationModel location){
        itemToAddList.setValue(location);
    }
    public MutableLiveData<LocationModel> getLocation() {
        return itemToAddList;
    }
}
