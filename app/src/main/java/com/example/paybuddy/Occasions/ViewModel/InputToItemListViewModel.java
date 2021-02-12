package com.example.paybuddy.Occasions.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.paybuddy.Models.ItemModel;

public class InputToItemListViewModel extends ViewModel {
    private final MutableLiveData<ItemModel> itemToAddList = new MutableLiveData<>();

    public void setItem(ItemModel index) { itemToAddList.setValue(index); }

    public LiveData<ItemModel> getItem() {
        return itemToAddList;
    }
}
