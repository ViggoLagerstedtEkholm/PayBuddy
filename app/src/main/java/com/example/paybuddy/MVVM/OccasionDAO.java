package com.example.paybuddy.MVVM;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.paybuddy.Models.ItemModel;
import com.example.paybuddy.Models.OccasionModel;
import com.example.paybuddy.Models.OccasionWithItems;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface OccasionDAO {

    @Insert
    long insert(OccasionModel occasionModel);

    @Update
    void update(OccasionModel occasionModel);

    @Delete
    void delete(OccasionModel occasionModel);

    @Query("DELETE FROM occasions_table")
    void deleteAllOccasions();

    @Query("SELECT * FROM occasions_table ORDER BY date DESC")
    LiveData<List<OccasionModel>> getAllOccasions();
}
