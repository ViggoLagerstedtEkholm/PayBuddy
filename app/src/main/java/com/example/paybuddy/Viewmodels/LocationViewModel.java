package com.example.paybuddy.Viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.paybuddy.Models.ItemModel;
import com.example.paybuddy.Models.LocationModel;
import com.example.paybuddy.Models.OccasionModel;
import com.example.paybuddy.Repositories.ItemsRepository;
import com.example.paybuddy.Repositories.LocationRepository;
import com.example.paybuddy.Repositories.Repository;

import java.util.List;

public class LocationViewModel extends AndroidViewModel {
  private LocationRepository locationRepository;

    public LocationViewModel(@NonNull Application application) {
        super(application);
        locationRepository = new LocationRepository(application);
    }

    public void insert(LocationModel locationModel){
        locationRepository.insert(locationModel);
    }

    public void update(LocationModel locationModel){
        locationRepository.update(locationModel);
    }

    public void delete(LocationModel locationModel){
        locationRepository.delete(locationModel);
    }

    public void deleteAll(Repository.DELETE_TYPE delete_type){
        locationRepository.deleteAll(delete_type);
    }
}
