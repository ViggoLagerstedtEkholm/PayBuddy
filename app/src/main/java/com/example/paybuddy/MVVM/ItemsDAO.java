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
    abstract void insert(List<ItemModel> occasionModel);

    @Update
    abstract void update(ItemModel occasionModel);

    @Delete
    abstract void delete(List<ItemModel> occasionModel);

    @Query("DELETE FROM Item_table")
    abstract void deleteAllItems();

    @Query("SELECT * FROM Item_table ORDER BY price DESC")
    abstract LiveData<List<ItemModel>> getAllItems();

    public void insertItemsAndOccasion(List<ItemModel> itemModels, long id){
        for(ItemModel itemModel : itemModels){
            itemModel.setOccasionID(id);
        }
        insert(itemModels);
    }
}
