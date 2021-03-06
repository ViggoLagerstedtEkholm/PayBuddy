package com.example.paybuddy.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.paybuddy.Models.LocationModel;

/**
 *  This interface handles queries for the location table.
 *  @date 2021-03-09
 *  @version 1.0
 *  @author Viggo Lagerstedt Ekholm
 */
@Dao
public abstract class LocationDAO {
    @Insert
    public abstract long insert(LocationModel occasionModel);

    @Update
    public abstract void update(LocationModel occasionModel);

    @Delete
    public abstract void delete(LocationModel occasionModel);

    @Query("DELETE FROM location_table")
    public abstract void deleteAllLocations();

    @Query("DELETE " +
            "FROM location_table " +
            "WHERE occasionID IN (" +
                                "SELECT ID " +
                                "FROM occasions_table " +
                                "WHERE IsExpired = " + 1 + ")")
    public abstract void deleteLocationExpired();

    @Query("DELETE " +
            "FROM location_table " +
            "WHERE occasionID IN (" +
                                "SELECT ID " +
                                "FROM occasions_table " +
                                "WHERE IsPaid = " + 1 + ")")
    public abstract void deleteLocationPaid();

    @Query("DELETE " +
            "FROM location_table " +
            "WHERE location_table.occasionID IN (" +
                                            "SELECT ID " +
                                            "FROM occasions_table " +
                                            "WHERE IsPaid = " + 0 + " AND IsExpired = " + 0 + ")")
    public abstract void deleteLocationUnPaid();

    public void insertLocation(LocationModel locationModel, long id){
        locationModel.setOccasionID(id);
        insert(locationModel);
    }
}
