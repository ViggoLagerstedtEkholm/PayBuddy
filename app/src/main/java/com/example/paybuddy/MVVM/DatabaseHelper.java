package com.example.paybuddy.MVVM;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.paybuddy.Models.ItemModel;
import com.example.paybuddy.Models.LocationModel;
import com.example.paybuddy.Models.OccasionModel;
import com.example.paybuddy.Models.OccasionWithItems;

@Database(entities = {OccasionModel.class, ItemModel.class, LocationModel.class}, version = 3)
public abstract class DatabaseHelper extends RoomDatabase {
   private static DatabaseHelper instance;
   public abstract OccasionDAO occasionDao();
   public abstract ItemsDAO itemsDao();
   public abstract LocationDAO locationDAO();
   public abstract OccasionWithItemsDAO occasionWithItemsDAO();

   public static synchronized DatabaseHelper getInstance(Context context){
      if(instance == null){
         instance = Room.databaseBuilder(context.getApplicationContext(), DatabaseHelper.class, "myRoomDBB")
                 .fallbackToDestructiveMigration()
                 .addCallback(roomCallBack)
                 .build();
      }
      return instance;
   }

   private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback(){
      @Override
      public void onCreate(@NonNull SupportSQLiteDatabase db) {
         super.onCreate(db);
         new PopulateDatabaseAsyncTask(instance).execute();
      }
   };

   private static class PopulateDatabaseAsyncTask extends AsyncTask<Void, Void, Void> {
      private OccasionDAO occasionDao;
      private ItemsDAO itemsDAO;
      private PopulateDatabaseAsyncTask(DatabaseHelper databaseHelper){
         occasionDao = databaseHelper.occasionDao();
         itemsDAO = databaseHelper.itemsDao();
      }
      @Override
      protected Void doInBackground(Void... voids) {
         occasionDao.insert(new OccasionModel("2021", "test1", false, false));
         occasionDao.insert(new OccasionModel("2021", "test2", false, false));
         occasionDao.insert(new OccasionModel("2021", "test3", false, false));
         return null;
      }
   }
}
