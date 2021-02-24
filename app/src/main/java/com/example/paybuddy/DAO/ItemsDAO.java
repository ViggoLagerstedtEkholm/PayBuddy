package com.example.paybuddy.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.paybuddy.Models.ItemModel;
import com.example.paybuddy.Models.OccasionModel;

import java.util.List;

@Dao
public abstract class ItemsDAO {
    @Insert
    public abstract void insert(List<ItemModel> itemModels);

    @Insert
    public abstract void insert(ItemModel itemModel);

    @Update
    public abstract void update(ItemModel itemModel);

    @Delete
    public abstract void delete(ItemModel itemModel);

    @Delete
    public abstract void delete(List<ItemModel> itemModel);

    @Query("DELETE FROM Item_table")
    public abstract void deleteAllItems();

    @Query("DELETE " +
            "FROM Item_table " +
            "WHERE Item_table.occasionID IN (" +
                                        "SELECT ID " +
                                        "FROM occasions_table " +
                                        "WHERE IsExpired = " + 1 + " AND IsPaid = " + 0 + ")")
    public abstract void deleteAllItemsExpired();

    @Query("DELETE " +
            "FROM Item_table " +
            "WHERE Item_table.occasionID IN (" +
                                        "SELECT ID " +
                                        "FROM occasions_table " +
                                        "WHERE IsPaid = " + 0 + " AND IsExpired = " + 0 + ")")
    public abstract void deleteAllItemsUnPaid();

    @Query("DELETE " +
            "FROM Item_table " +
            "WHERE Item_table.occasionID IN (" +
                                        "SELECT ID " +
                                        "FROM occasions_table " +
                                        "WHERE IsPaid = " + 1 + " AND IsExpired = " + 0 + ")")
    public abstract void deleteAllItemsHistory();

    @Query("SELECT * FROM Item_table ORDER BY price DESC")
    public abstract LiveData<List<ItemModel>> getAllItems();

    @Query("SELECT SUM(PRICE * QUANTITY)" +
            " FROM Item_table " +
            "JOIN Occasions_table " +
            "ON Occasions_table.ID = Item_table.occasionID " +
            "WHERE IsPaid = "+ '0')
    public abstract LiveData<Integer> getTotalCost();

    public void insertItemsAndOccasion(List<ItemModel> itemModels, long id){
        for(ItemModel itemModel : itemModels){
            itemModel.setOccasionID(id);
        }
        insert(itemModels);
    }
}
