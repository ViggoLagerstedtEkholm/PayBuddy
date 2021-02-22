package com.example.paybuddy.Models;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Relation;

import java.util.List;

public class OccasionWithItems {
    @Embedded
    public OccasionModel occasionModel;
    @Relation(
            parentColumn = "ID",
            entityColumn = "occasionID"
    )
    public List<ItemModel> itemModelList;

    @Relation(
            parentColumn = "ID",
            entityColumn = "occasionID"
    )
    public LocationModel locationModel;
}
