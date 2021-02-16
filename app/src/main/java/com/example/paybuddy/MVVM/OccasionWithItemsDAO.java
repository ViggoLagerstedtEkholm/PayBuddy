package com.example.paybuddy.MVVM;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.paybuddy.Models.OccasionWithItems;

import java.util.List;

@Dao
public interface OccasionWithItemsDAO {
    @Transaction
    @Query("SELECT * FROM OCCASIONS_TABLE")
    LiveData<List<OccasionWithItems>> loadOccasionsWithItems();
}
