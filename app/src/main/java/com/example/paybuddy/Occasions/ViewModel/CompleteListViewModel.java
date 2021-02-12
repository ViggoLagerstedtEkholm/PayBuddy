package com.example.paybuddy.Occasions.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.paybuddy.Models.ItemModel;

import java.util.List;

public class CompleteListViewModel extends ViewModel {
    private final MutableLiveData<List<ItemModel>> items = new MutableLiveData<>();

    public void setItem(List<ItemModel> index) { items.setValue(index); }

    public LiveData<List<ItemModel>> getItem() {
        return items;
    }
}
