package com.example.paybuddy.Maps;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.paybuddy.Models.LocationModel;
import com.example.paybuddy.Models.OccasionModel;

import java.util.List;

public class CoordinatesViewModel extends ViewModel {
    private final MutableLiveData<LocationModel> location = new MutableLiveData<>();
    private final MutableLiveData<List<OccasionModel>> locations = new MutableLiveData<>();

    public void setLocation(LocationModel locationObj){
        location.setValue(locationObj);
    }
    public LiveData<LocationModel> getLocation() {
        return location;
    }

    public void setLocations(List<OccasionModel> locationObj){
        locations.setValue(locationObj);
    }
    public LiveData<List<OccasionModel>> getLocations() {
        return locations;
    }
}
