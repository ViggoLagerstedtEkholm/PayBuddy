package com.example.paybuddy.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.paybuddy.Models.OccasionModel;

/**
 *  This interface handles queries for the occasion table.
 *  @date 2021-03-09
 *  @version 1.0
 *  @author Viggo Lagerstedt Ekholm
 */
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

    @Query("DELETE FROM occasions_table WHERE IsExpired = " + 1)
    void deleteAllExpired();

    @Query("DELETE FROM occasions_table WHERE isPaid = " + 1)
    void deleteAllHistory();

    @Query("DELETE FROM occasions_table WHERE IsPaid = " + 0 + " AND isExpired = " + 0)
    void deleteAllUnPaid();
}
