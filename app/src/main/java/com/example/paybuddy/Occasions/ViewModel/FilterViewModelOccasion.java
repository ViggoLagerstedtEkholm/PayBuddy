package com.example.paybuddy.Occasions.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FilterViewModelOccasion extends ViewModel {
    private final MutableLiveData<String> selected = new MutableLiveData<String>();

    public void select(String item) {
        selected.setValue(item);
    }

    public LiveData<String> getSelected() {
        return selected;
    }
}
