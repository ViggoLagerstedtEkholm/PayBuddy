package com.example.paybuddy.MVVM;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.paybuddy.Models.ItemModel;
import com.example.paybuddy.Models.OccasionModel;
import com.example.paybuddy.Models.OccasionWithItems;

import java.util.List;

public class RepositoryViewModel extends AndroidViewModel {
    private Repository repository;
    private LiveData<List<OccasionModel>> occasions;
    private LiveData<List<OccasionWithItems>> occasionsWithItems;

    public RepositoryViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        occasions = repository.getListLiveData();
        occasionsWithItems = repository.getOccasionsWitchItems();
    }

    public void insert(OccasionModel occasionModel){
        repository.insert(occasionModel);
    }

    public void updateOccasion(OccasionModel occasionModel){
        repository.updateOccasion(occasionModel);
    }

    public void updateItem(ItemModel itemModel){
        repository.updateItem(itemModel);
    }

    public void delete(OccasionModel occasionModel){
        repository.delete(occasionModel);
    }

    public void deleteAllOccasions(){
        repository.deleteAllOccasions();
    }

    public LiveData<List<OccasionModel>> getAllOccasions(){
        return occasions;
    }

    public LiveData<List<OccasionWithItems>> getOccasionsWithItems(){
        return occasionsWithItems;
    }

}
