package com.example.paybuddy.Occasions.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.paybuddy.Models.LocationModel;

public class LocationViewModel extends ViewModel {
    private final MutableLiveData<LocationModel> locationData = new MutableLiveData<>();

    public void setLocation(LocationModel location){
        locationData.setValue(location);
    }
    public MutableLiveData<LocationModel> getLocation() {
        return locationData;
    }
}
