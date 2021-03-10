package com.example.paybuddy.Search.SearchViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * ViewModel for our occasion filtering.
 * @date 2021-03-09
 * @version 1.0
 * @author Viggo Lagerstedt Ekholm
 */
public class FilterSelectionViewModel extends ViewModel {
    private final MutableLiveData<String> searchWord = new MutableLiveData<String>();

    /**
     * Sets the query for our filtering.
     * @param item query
     */
    public void select(String item) {
        searchWord.setValue(item);
    }

    /**
     * Gets the query for our filtering.
     * @return LiveData<String> holds the query data.
     */
    public LiveData<String> getSelected() {
        return searchWord;
    }
}
