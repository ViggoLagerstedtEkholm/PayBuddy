package com.example.paybuddy.Models;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

/**
 * This class contains all the data our OccasionWithItems should have.
 * We also use getters and setters to access this data.
 *  @date 2021-03-09
 *  @version 1.0
 *  @author Viggo Lagerstedt Ekholm
 */


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
