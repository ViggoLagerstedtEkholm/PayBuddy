package com.example.paybuddy.Search.SearchViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FilterContactViewModel extends ViewModel {
    private final MutableLiveData<String> searchWord = new MutableLiveData<String>();

    public void select(String item) {
        searchWord.setValue(item);
    }

    public LiveData<String> getSelected() {
        return searchWord;
    }
}
