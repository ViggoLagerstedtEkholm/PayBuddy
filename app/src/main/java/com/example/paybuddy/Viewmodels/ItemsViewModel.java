package com.example.paybuddy.Viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.paybuddy.Repositories.ItemsRepository;
import com.example.paybuddy.Models.ItemModel;
import com.example.paybuddy.Repositories.Repository;

import java.util.List;

public class ItemsViewModel extends AndroidViewModel {
    private final ItemsRepository itemsRepository;
    private final LiveData<List<ItemModel>> items;
    private final LiveData<Integer> totalCost;
    private final LiveData<List<ItemModel>> pendingItems;

    public ItemsViewModel(@NonNull Application application) {
        super(application);
        itemsRepository = new ItemsRepository(application);

        items = itemsRepository.getAll();
        totalCost = itemsRepository.getTotalCost();
        pendingItems = itemsRepository.getPendingItems();
    }

    public void insert(ItemModel itemModel) {
        itemsRepository.insert(itemModel);
    }

    public void update(ItemModel itemModel) {
        itemsRepository.update(itemModel);
    }

    public void delete(ItemModel itemModel) {
        itemsRepository.delete(itemModel);
    }

    public void delete(List<ItemModel> itemModels) {
        itemsRepository.delete(itemModels);
    }

    public void deletePendingItems() {
        itemsRepository.deletePending();
    }

    public LiveData<Integer> getTotalCost() {
        return totalCost;
    }

    public LiveData<List<ItemModel>> getPendingItems() {
        return pendingItems;
    }

    public LiveData<Integer> getOccasionTotalCost(double ID) {
        return itemsRepository.getOccasionTotalCost(ID);
    }

    public LiveData<List<String>> getPeopleOccasion(double ID) {
        return itemsRepository.getPeopleOccasion(ID);
    }

    public LiveData<List<ItemModel>> getOccasionItems(double ID) {
        return itemsRepository.getOccasionItems(ID);
    }

    public LiveData<Integer> getOccasionItemCount(double ID){
        return itemsRepository.getOccasionItemCount(ID);
    }


    public void deleteAllItems(Repository.DELETE_TYPE delete_type) {
        itemsRepository.deleteAll(delete_type);
    }

    public LiveData<List<ItemModel>> getItems() {
        return items;
    }
}
