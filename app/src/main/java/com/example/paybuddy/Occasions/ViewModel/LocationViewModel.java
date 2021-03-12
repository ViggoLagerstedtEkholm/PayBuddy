package com.example.paybuddy.Occasions.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.paybuddy.Models.LocationModel;

/**
 * This is the ViewModel that "sends" the location to our "Add items" fragment when we save a location.
 * @date 2021-03-09
 * @version 1.0
 * @author Viggo Lagerstedt Ekholm
 */
public class LocationViewModel extends ViewModel {
    private final MutableLiveData<LocationModel> locationData = new MutableLiveData<>();

    //Set the LiveData
    public void setLocation(LocationModel location){
        locationData.setValue(location);
    }

    //Get the LiveData
    public MutableLiveData<LocationModel> getLocation() {
        return locationData;
    }
}