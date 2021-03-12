package com.example.paybuddy.Maps;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.paybuddy.Models.LocationModel;
import com.example.paybuddy.Models.OccasionModel;

import java.util.List;

/**
 * This is the ViewModel that "sends" the location/locations to our "Google Maps" fragment when we want to show all the locations.
 * @date 2021-03-09
 * @version 1.0
 * @author Viggo Lagerstedt Ekholm
 */
public class CoordinatesViewModel extends ViewModel {
    private final MutableLiveData<LocationModel> location = new MutableLiveData<>();
    private final MutableLiveData<List<OccasionModel>> locations = new MutableLiveData<>();

    //Set and get location.
    public void setLocation(LocationModel locationObj){
        location.setValue(locationObj);
    }
    public LiveData<LocationModel> getLocation() {
        return location;
    }

    //Set and get locations.
    public void setLocations(List<OccasionModel> locationObj){
        locations.setValue(locationObj);
    }
    public LiveData<List<OccasionModel>> getLocations() {
        return locations;
    }
}
