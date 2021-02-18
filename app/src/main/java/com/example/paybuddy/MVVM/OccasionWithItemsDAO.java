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
    @Query("SELECT * FROM OCCASIONS_TABLE WHERE IsPaid = 0 AND IsExpired = 0")
    LiveData<List<OccasionWithItems>> getActiveOccasions();

    @Transaction
    @Query("SELECT * FROM OCCASIONS_TABLE WHERE IsPaid = 1")
    LiveData<List<OccasionWithItems>> getPaidOccasions();

    @Transaction
    @Query("SELECT * FROM OCCASIONS_TABLE WHERE IsExpired = 1")
    LiveData<List<OccasionWithItems>> getExpiredOccasions();

    @Transaction
    @Query("SELECT * FROM OCCASIONS_TABLE WHERE IsExpired = 1")
    LiveData<List<OccasionWithItems>> getAllOccasions();
}
