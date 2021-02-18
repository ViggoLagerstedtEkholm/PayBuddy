package com.example.paybuddy.MVVM;

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

    @Query("SELECT * FROM Item_table ORDER BY price DESC")
    public abstract LiveData<List<ItemModel>> getAllItems();

    @Query("SELECT SUM(PRICE * QUANTITY) FROM Item_table")
    public abstract LiveData<Integer> getTotalCost();

    public void insertItemsAndOccasion(List<ItemModel> itemModels, long id){
        for(ItemModel itemModel : itemModels){
            itemModel.setOccasionID(id);
        }
        insert(itemModels);
    }
}
