package com.example.paybuddy.Occasions.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.paybuddy.Models.ItemModel;
import com.example.paybuddy.Models.OccasionModel;

import java.util.List;

public class PreviewViewModel extends ViewModel {
    private final MutableLiveData<OccasionModel> items = new MutableLiveData<>();

    public void setItem(OccasionModel index) { items.setValue(index); }

    public LiveData<OccasionModel> getItem() {
        return items;
    }
}
