package com.example.paybuddy.database;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.paybuddy.Models.ItemModel;
import com.example.paybuddy.Models.OccasionModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String OCCASION_TABLE = "OCCASION_TABLE";
    public static final String COLUMN_OCCASION_ID = "ID";
    public static final String COLUMN_OCCASION_DESCRIPTION = "DESCRIPTION";
    public static final String COLUMN_DATE = "DATE";

    public static final String ITEM_TABLE = "ITEM_TABLE";
    public static final String COLUMN_ITEM_ID = "ID";
    public static final String COLUMN_ITEM_DESCRIPTION = "DESCRIPTION";
    public static final String COLUMN_PRICE = "PRICE";
    public static final String COLUMN_ITEM_IN_OCCASION = "OCCASION_ID";
    public static final String COLUMN_QUANTITY = "QUANTITY";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "payBuddie.db", null, 1);
        getReadableDatabase();
        Log.d("Created db class", "...");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + OCCASION_TABLE + " (" + COLUMN_OCCASION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_OCCASION_DESCRIPTION + " TEXT, " + COLUMN_DATE + " TEXT)";
        String createValuesTable = "CREATE TABLE " + ITEM_TABLE + " (" + COLUMN_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_ITEM_DESCRIPTION + " TEXT, " + COLUMN_PRICE + " INTEGER, "
                + COLUMN_QUANTITY + " INTEGER ," + COLUMN_ITEM_IN_OCCASION + " INTEGER,  FOREIGN KEY( " + COLUMN_ITEM_IN_OCCASION + " ) REFERENCES OCCASION_TABLE( " + COLUMN_OCCASION_ID + " ))";
        Log.d("QUERY1: ", createTableQuery);
        Log.d("QUERY2: ", createValuesTable);
        db.execSQL(createTableQuery);
        db.execSQL(createValuesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Update if new db found.
    }

    public List<OccasionModel> filterOccasion(String searchWord){
        List<OccasionModel> occasionModels = new ArrayList<>();

        try{
            String queryString = "SELECT * FROM " + OCCASION_TABLE + " WHERE " +  COLUMN_OCCASION_DESCRIPTION + " LIKE '%" + searchWord + "%'";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursorOccasion = db.rawQuery(queryString, null);

            if(cursorOccasion.moveToFirst()){
                do{
                    int OccasionID = cursorOccasion.getInt(0);
                    String description = cursorOccasion.getString(1);
                    String date = cursorOccasion.getString(2);
                    ArrayList<ItemModel> items = getItemsForOccasion(db, OccasionID);

                    OccasionModel primeModel = new OccasionModel(OccasionID, date,description, items);
                    occasionModels.add(primeModel);
                }while(cursorOccasion.moveToNext());
            }else{
                //don't add anything to the list.
            }
            Log.d("ITEM_COUNT", String.valueOf(occasionModels.size()));
            cursorOccasion.close();
            db.close();
        }catch(Exception e){
            Log.d("NO RECORDS FOUND", "NO RECORDS FOUND IN HISTORY!");
        }

        return occasionModels;
    }

    public void delete(OccasionModel occasionModel){
        String queryString = "DELETE FROM " + OCCASION_TABLE + " WHERE ID = " + occasionModel.getID();
        String queryStringItems = "DELETE FROM " + ITEM_TABLE + " WHERE OCCASION_ID = " + occasionModel.getID();

        SQLiteDatabase db = this.getReadableDatabase();

        db.execSQL(queryStringItems);
        db.execSQL(queryString);
    }

    public double getSumItems(){
        String queryString = "SELECT SUM(PRICE) FROM " + ITEM_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorOccasion = db.rawQuery(queryString, null);
        double sumOfPrices = 0.0;

        try{
            if(cursorOccasion.moveToFirst()){
                sumOfPrices = cursorOccasion.getDouble(0);
            }
            cursorOccasion.close();
            db.close();
        }catch(Exception e){
            Log.d("NO_RECORDS_FOUND", "NO RECORDS FOUND!");
        }
        return sumOfPrices;
    }

    public int getAmountOfOccasion(){
        String queryString = "SELECT COUNT(ID) FROM " + OCCASION_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorOccasion = db.rawQuery(queryString, null);
        int countOfRows = 0;

        try{
            if(cursorOccasion.moveToFirst()){
                countOfRows = cursorOccasion.getInt(0);
            }
            cursorOccasion.close();
            db.close();
        }catch(Exception e){
            Log.d("NO_RECORDS_FOUND", "NO RECORDS FOUND!");
        }
        return countOfRows;
    }

    //public int getSumOfExpiredOccasions(){

    //}

    public boolean insertOccasion(OccasionModel occasionModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_OCCASION_DESCRIPTION, occasionModel.getDescription());
        cv.put(COLUMN_DATE, occasionModel.getDate().toString());

        long insert = db.insert(OCCASION_TABLE, null, cv);
        boolean itemsInserted = insertItemsForOccasion(occasionModel, insert);
        if(insert == -1 || !itemsInserted){
            return false;
        }else{
            return true;
        }
    }

    private boolean insertItemsForOccasion(OccasionModel occasionModel, long idOfInsertedOccasion){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        long[] longs = new long[occasionModel.getItems().size()];
        int index = 0;
        for(ItemModel aItemModel : occasionModel.getItems()){
            cv.put(COLUMN_ITEM_DESCRIPTION, aItemModel.getDescription());
            cv.put(COLUMN_PRICE, aItemModel.getPrice());
            cv.put(COLUMN_ITEM_IN_OCCASION, idOfInsertedOccasion);
            cv.put(COLUMN_QUANTITY, aItemModel.getQuantity());

            long insert = db.insert(ITEM_TABLE, null, cv);
            longs[index++] = insert;
        }

        for(int i = 0; i < longs.length; i++){
            if(longs[i] == -1){
                return false;
            }
        }

        return true;
    }

    public List<OccasionModel> getAllOccasions(){
        List<OccasionModel> occasionModels = new ArrayList<>();

        try{
            String queryString = "SELECT * FROM " + OCCASION_TABLE;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursorOccasion = db.rawQuery(queryString, null);

            if(cursorOccasion.moveToFirst()){
                do{
                    int OccasionID = cursorOccasion.getInt(0);
                    String description = cursorOccasion.getString(1);
                    String date = cursorOccasion.getString(2);
                    ArrayList<ItemModel> items = getItemsForOccasion(db, OccasionID);

                    OccasionModel primeModel = new OccasionModel(OccasionID, date,description, items);
                    occasionModels.add(primeModel);
                }while(cursorOccasion.moveToNext());
            }else{
                //don't add anything to the list.
            }
            Log.d("ITEM_COUNT", String.valueOf(occasionModels.size()));
            cursorOccasion.close();
            db.close();
        }catch(Exception e){
            Log.d("NO RECORDS FOUND", "NO RECORDS FOUND IN HISTORY!");
        }

        return occasionModels;
    }

    private ArrayList<ItemModel> getItemsForOccasion(SQLiteDatabase db, long OccasionID){
        ArrayList<ItemModel> items = new ArrayList<>();
        String itemQuery = "SELECT * FROM " + ITEM_TABLE + " WHERE " + COLUMN_ITEM_IN_OCCASION + " = " + OccasionID;
        Cursor cursorItem = db.rawQuery(itemQuery, null);

        if(cursorItem.moveToFirst()){
            do{
                int itemID = cursorItem.getInt(0);
                String description = cursorItem.getString(1);
                int price = cursorItem.getInt(2);
                int quantity = cursorItem.getInt(3);

                ItemModel itemModel = new ItemModel(itemID, price, description, quantity);
                items.add(itemModel);
            }while(cursorItem.moveToNext());
        }else{
            //don't add anything to the list.
        }
        cursorItem.close();
        return items;
    }
}
