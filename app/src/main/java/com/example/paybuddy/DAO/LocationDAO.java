package com.example.paybuddy.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.paybuddy.Models.LocationModel;

import java.util.List;

@Dao
public abstract class LocationDAO {
    @Insert
    public abstract long insert(LocationModel occasionModel);

    @Update
    public abstract void update(LocationModel occasionModel);

    @Delete
    public abstract void delete(LocationModel occasionModel);

    @Query("DELETE FROM location_table")
    public abstract void deleteAllOccasions();

    @Query("SELECT * FROM location_table ORDER BY ID DESC")
    public abstract LiveData<List<LocationModel>> getAllOccasions();

    public void insertLocation(LocationModel locationModel, long id){
        locationModel.setOccasionID(id);
        insert(locationModel);
    }
}
