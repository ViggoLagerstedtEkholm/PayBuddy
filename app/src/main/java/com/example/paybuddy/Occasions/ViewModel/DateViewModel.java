package com.example.paybuddy.Occasions.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * This is the ViewModel that "sends" the date to our "Add items" fragment when we save the date.
 * @date 2021-03-09
 * @version 1.0
 * @author Viggo Lagerstedt Ekholm
 */
public class DateViewModel extends ViewModel {
    private final MutableLiveData<String> date = new MutableLiveData<>();

    //Set the LiveData
    public void setDate(String location){
        date.setValue(location);
    }

    //Get the LiveData
    public MutableLiveData<String> getDate() {
        return date;
    }
}
