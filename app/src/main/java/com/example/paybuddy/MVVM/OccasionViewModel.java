package com.example.paybuddy.MVVM;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.paybuddy.MVVM.Repositories.ItemsRepository;
import com.example.paybuddy.MVVM.Repositories.OccasionRepository;
import com.example.paybuddy.Models.ItemModel;
import com.example.paybuddy.Models.OccasionModel;
import com.example.paybuddy.Models.OccasionWithItems;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class OccasionViewModel extends AndroidViewModel {
    private OccasionRepository occasionRepository;
    private LiveData<List<OccasionWithItems>> occasionsWithItems;

    public OccasionViewModel(@NonNull Application application) {
        super(application);
        occasionRepository = new OccasionRepository(application);

        occasionsWithItems = occasionRepository.getOccasionsWitchItems();
    }

    public void insert(OccasionModel itemModel){
        occasionRepository.insert(itemModel);
    }

    public void update(OccasionModel itemModel){
        occasionRepository.update(itemModel);
    }

    public void delete(OccasionModel itemModel){
        occasionRepository.delete(itemModel);
    }

    public void deleteAllItems(){
        occasionRepository.deleteAll();
    }

    public LiveData<List<OccasionWithItems>> getOccasionsWithItems(){
        return occasionsWithItems;
    }
}
