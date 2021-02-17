package com.example.paybuddy.MVVM;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.paybuddy.MVVM.Repositories.ItemsRepository;
import com.example.paybuddy.MVVM.Repositories.OccasionRepository;
import com.example.paybuddy.Models.ItemModel;
import com.example.paybuddy.Models.OccasionWithItems;

import java.util.List;

public class ItemsViewModel extends AndroidViewModel {
    private ItemsRepository itemsRepository;
    private LiveData<List<ItemModel>> items;

    public ItemsViewModel(@NonNull Application application) {
        super(application);
        itemsRepository = new ItemsRepository(application);

        items = itemsRepository.getAll();
    }

    public void insert(ItemModel itemModel){
        itemsRepository.insert(itemModel);
    }

    public void update(ItemModel itemModel){
        itemsRepository.update(itemModel);
    }

    public void delete(ItemModel itemModel){
        itemsRepository.delete(itemModel);
    }

    public void delete(List<ItemModel> itemModels){
        itemsRepository.delete(itemModels);
    }

    public void deleteAllItems(){
        itemsRepository.deleteAll();
    }

    public LiveData<List<ItemModel>> getItems(){
        return items;
    }
}
