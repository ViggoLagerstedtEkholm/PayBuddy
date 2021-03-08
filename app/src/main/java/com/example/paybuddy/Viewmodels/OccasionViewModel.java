package com.example.paybuddy.Viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.paybuddy.Repositories.OccasionRepository;
import com.example.paybuddy.Models.OccasionModel;
import com.example.paybuddy.Models.OccasionWithItems;
import com.example.paybuddy.Repositories.Repository;

import java.util.List;

public class OccasionViewModel extends AndroidViewModel {
    private OccasionRepository occasionRepository;

    private LiveData<List<OccasionWithItems>> occasionsWithItems;
    private LiveData<List<OccasionWithItems>> paidOccasions;
    private LiveData<List<OccasionWithItems>> expiredOccasions;
    private LiveData<List<OccasionWithItems>> allOccasions;

    public OccasionViewModel(@NonNull Application application) {
        super(application);
        occasionRepository = new OccasionRepository(application);

        occasionsWithItems = occasionRepository.getActiveOccasions();
        paidOccasions = occasionRepository.getPaidOccasions();
        expiredOccasions = occasionRepository.getExpiredOccasions();
        allOccasions = occasionRepository.getAllOccasions();
    }

    public void insert(OccasionModel itemModel) {
        occasionRepository.insert(itemModel);
    }

    public void update(OccasionModel itemModel) {
        occasionRepository.update(itemModel);
    }

    public void delete(OccasionModel itemModel) {
        occasionRepository.delete(itemModel);
    }

    public void deleteAll() {
        occasionRepository.deleteAll(Repository.DELETE_TYPE.DELETE_ALL);
    }

    public void deleteAllUnpaid() {
        occasionRepository.deleteAll(Repository.DELETE_TYPE.DELETE_ALL_UNPAID);
    }

    public void deleteAllHistory() {
        occasionRepository.deleteAll(Repository.DELETE_TYPE.DELETE_ALL_HISTORY);
    }

    public void deleteAllExpired() {
        occasionRepository.deleteAll(Repository.DELETE_TYPE.DELETE_ALL_EXPIRED);
    }

    public LiveData<List<OccasionWithItems>> getActiveOccasions() {
        return occasionsWithItems;
    }

    public LiveData<List<OccasionWithItems>> getPaidOccasions() {
        return paidOccasions;
    }

    public LiveData<List<OccasionWithItems>> getExpiredOccasions() {
        return expiredOccasions;
    }

    public LiveData<List<OccasionWithItems>> getAllOccasions() {
        return allOccasions;
    }
}
