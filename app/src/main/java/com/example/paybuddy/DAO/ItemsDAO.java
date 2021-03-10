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

    @Update
    public abstract void update(List<ItemModel> itemModel);

    @Delete
    public abstract void delete(ItemModel itemModel);

    @Query("DELETE FROM Item_table WHERE occasionID = '-1'")
    public abstract void deletePending();

    @Delete
    public abstract void delete(List<ItemModel> itemModel);

    @Query("DELETE FROM Item_table")
    public abstract void deleteAllItems();

    @Query("DELETE " +
            "FROM Item_table " +
            "WHERE Item_table.occasionID IN (" +
                                        "SELECT ID " +
                                        "FROM occasions_table " +
                                        "WHERE IsExpired = " + 1 + ")")
    public abstract void deleteAllItemsExpired();

    @Query("DELETE " +
            "FROM Item_table " +
            "WHERE Item_table.occasionID IN (" +
                                        "SELECT ID " +
                                        "FROM occasions_table " +
                                        "WHERE IsPaid = " + 1 + ")")
    public abstract void deleteAllItemsHistory();

    @Query("DELETE " +
            "FROM Item_table " +
            "WHERE Item_table.occasionID IN (" +
                                        "SELECT ID " +
                                        "FROM occasions_table " +
                                        "WHERE IsPaid = " + 0 + " AND IsExpired = " + 0 + ")")
    public abstract void deleteAllItemsUnPaid();

    @Query("SELECT * FROM Item_table ORDER BY price DESC")
    public abstract LiveData<List<ItemModel>> getAllItems();

    @Query("SELECT SUM(PRICE * QUANTITY)" +
            " FROM Item_table " +
            "JOIN Occasions_table " +
            "ON Occasions_table.ID = Item_table.occasionID " +
            "WHERE IsPaid = "+ '0' + " AND occasionID != " + -1)
    public abstract LiveData<Integer> getTotalCost();

    @Query("SELECT SUM(PRICE * QUANTITY) FROM Item_table WHERE occasionID = :id")
    public abstract LiveData<Integer> getOccasionTotalCost(double id);

    @Query("SELECT * FROM ITEM_TABLE WHERE occasionID = '-1'")
    public abstract LiveData<List<ItemModel>> getPendingItems();

    @Query("SELECT assignedPerson FROM Item_table WHERE occasionID = :ID")
    public abstract LiveData<List<String>> getPeopleOccasion(double ID);

    @Query("SELECT * FROM item_table WHERE occasionID = :ID")
    public abstract LiveData<List<ItemModel>> getOccasionItems(double ID);

    public void updateItemsAndOccasion(List<ItemModel> itemModels, long id){
        for(ItemModel itemModel : itemModels){
            itemModel.setOccasionID(id);
        }
        update(itemModels);
    }
}
