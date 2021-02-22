package com.example.paybuddy.Occasions.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DateViewModel extends ViewModel {
    private final MutableLiveData<String> date = new MutableLiveData<>();

    public void setDate(String location){
        date.setValue(location);
    }
    public MutableLiveData<String> getDate() {
        return date;
    }
}
