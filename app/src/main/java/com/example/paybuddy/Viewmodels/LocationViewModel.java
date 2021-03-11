package com.example.paybuddy.Viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.paybuddy.Models.LocationModel;
import com.example.paybuddy.Repositories.LocationRepository;
import com.example.paybuddy.Repositories.Repository;

public class LocationViewModel extends AndroidViewModel {
    private final LocationRepository locationRepository;

    public LocationViewModel(@NonNull Application application) {
        super(application);
        locationRepository = new LocationRepository(application);
    }

    public void insert(LocationModel locationModel) {
        locationRepository.insert(locationModel);
    }

    public void update(LocationModel locationModel) {
        locationRepository.update(locationModel);
    }

    public void delete(LocationModel locationModel) {
        locationRepository.delete(locationModel);
    }

    public void deleteAll(Repository.DELETE_TYPE delete_type) {
        locationRepository.deleteAll(delete_type);
    }
}
