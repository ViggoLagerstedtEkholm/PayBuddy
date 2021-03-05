package com.example.paybuddy.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.paybuddy.DAO.ItemsDAO;
import com.example.paybuddy.DAO.LocationDAO;
import com.example.paybuddy.DAO.OccasionDAO;
import com.example.paybuddy.DAO.OccasionWithItemsDAO;
import com.example.paybuddy.Models.ItemModel;
import com.example.paybuddy.Models.LocationModel;
import com.example.paybuddy.Models.OccasionModel;

@Database(entities = {OccasionModel.class, ItemModel.class, LocationModel.class}, version = 4)
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
      private LocationDAO locationDAO;
      private PopulateDatabaseAsyncTask(DatabaseHelper databaseHelper){
         occasionDao = databaseHelper.occasionDao();
         itemsDAO = databaseHelper.itemsDao();
         locationDAO = databaseHelper.locationDAO();
      }
      @Override
      protected Void doInBackground(Void... voids) {
         long id = occasionDao.insert(new OccasionModel("2021", "TestData1", false, true));
         ItemModel item = new ItemModel(20.5, "TestDataItem", 2, "Adam");
         LocationModel locationModel = new LocationModel(1.1,1.1,5.2, 100.0, "Test");
         item.setOccasionID(id);
         locationModel.setOccasionID(id);
         locationDAO.insert(locationModel);
         itemsDAO.insert(item);
         itemsDAO.insert(item);

         long id2 = occasionDao.insert(new OccasionModel("2021", "TestData2", false, false));
         ItemModel item2 = new ItemModel(250.5, "TestDataItem2", 3, "David");
         LocationModel locationModel2 = new LocationModel(1.6,2.1,7.2, 50.0, "Test2");
         item2.setOccasionID(id2);
         locationModel.setOccasionID(id2);
         locationDAO.insert(locationModel);
         itemsDAO.insert(item2);

         occasionDao.insert(new OccasionModel("2021", "TestData3", false, false));
         occasionDao.insert(new OccasionModel("2021", "TestData2", false, true));
         occasionDao.insert(new OccasionModel("2021", "TestData3", false, true));
         occasionDao.insert(new OccasionModel("2021", "TestData3", true, false));


         return null;
      }
   }
}
