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

@Database(entities = {OccasionModel.class, ItemModel.class, LocationModel.class}, version = 4, exportSchema = false)
public abstract class DatabaseHelper extends RoomDatabase {
   private static DatabaseHelper instance;
   public abstract OccasionDAO occasionDao();
   public abstract ItemsDAO itemsDao();
   public abstract LocationDAO locationDAO();
   public abstract OccasionWithItemsDAO occasionWithItemsDAO();

   public static synchronized DatabaseHelper getInstance(Context context){
      if(instance == null){
         instance = Room.databaseBuilder(context.getApplicationContext(), DatabaseHelper.class, "PayBuddyDB")
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
         //Pending occasion test data.
         long id = occasionDao.insert(new OccasionModel("Mar 5, 2021", "Lunch in the city", false, false));
         ItemModel itemInOccasion1 = new ItemModel(20.5, "Coca Cola", 2, "Adam");
         ItemModel item2InOccasion1 = new ItemModel(18.5, "Sprite", 2, "Jakob");
         ItemModel item3InOccasion1 = new ItemModel(60, "Day's special", 1, "Adam");
         ItemModel item4InOccasion1 = new ItemModel(75, "Hamburger with fries", 1, "Jakob");

         LocationModel locationModelInOccasion1 = new LocationModel(59.309059,18.103723,0.0, 100.0, "Jan Inghes Torg 22, 120 71 Stockholm");

         itemInOccasion1.setOccasionID(id);
         item2InOccasion1.setOccasionID(id);
         item3InOccasion1.setOccasionID(id);
         item4InOccasion1.setOccasionID(id);

         locationModelInOccasion1.setOccasionID(id);

         locationDAO.insert(locationModelInOccasion1);

         itemsDAO.insert(itemInOccasion1);
         itemsDAO.insert(item2InOccasion1);
         itemsDAO.insert(item3InOccasion1);
         itemsDAO.insert(item4InOccasion1);

         //Pending occasion test data 2.
         long id_2 = occasionDao.insert(new OccasionModel("Mar 5, 2021", "Mc Donald's", false, false));
         ItemModel itemInOccasion1_2 = new ItemModel(12.5, "Cheese Burger", 2, "Adam");
         ItemModel item2InOccasion1_2 = new ItemModel(60, "Big Mac", 1, "Jakob");

         LocationModel locationModelInOccasion1_2 = new LocationModel(59.339006,18.090841,0.0, 100.0, "Karlaplan 13, 115 20 Stockholm");

         itemInOccasion1_2.setOccasionID(id_2);
         item2InOccasion1_2.setOccasionID(id_2);

         locationModelInOccasion1_2.setOccasionID(id_2);

         locationDAO.insert(locationModelInOccasion1_2);

         itemsDAO.insert(itemInOccasion1_2);
         itemsDAO.insert(item2InOccasion1_2);


         //Paid occasion test data.
         long id2 = occasionDao.insert(new OccasionModel("Mar 6, 2021", "Tasty hamburgers", true, false));
         ItemModel itemInOccasion2 = new ItemModel(55, "Cheese 'n' Bacon", 3, "David");
         ItemModel item2InOccasion2 = new ItemModel(67, "Triple Cheese", 3, "Jakob");
         ItemModel item3InOccasion2 = new ItemModel(70, "Pepper Jack Jr", 3, "Adam");

         LocationModel locationModelInOccasion2 = new LocationModel(59.314861,18.073395,0.0, 100.0, "Götgatan 47B, 116 21 Stockholm");

         itemInOccasion2.setOccasionID(id2);
         item2InOccasion2.setOccasionID(id2);
         item3InOccasion2.setOccasionID(id2);

         locationModelInOccasion2.setOccasionID(id2);

         locationDAO.insert(locationModelInOccasion2);

         itemsDAO.insert(itemInOccasion2);
         itemsDAO.insert(item2InOccasion2);
         itemsDAO.insert(item3InOccasion2);

         //Expired occasion test data.
         long id3 = occasionDao.insert(new OccasionModel("Mar 6, 2021", "Quality ribs", false, true));
         ItemModel itemInOccasion3 = new ItemModel(100, "10 Ribs", 1, "Robert");
         ItemModel item2InOccasion3 = new ItemModel(119, "Beyond Burger", 1, "Simon");
         ItemModel item3InOccasion3 = new ItemModel(99, "Classic Burger", 1, "Justin");

         LocationModel locationModelInOccasion3 = new LocationModel(59.332905,18.043541,0.0, 100.0, "Fleminggatan 27, 112 26 Stockholm");

         itemInOccasion3.setOccasionID(id3);
         item2InOccasion3.setOccasionID(id3);
         item3InOccasion3.setOccasionID(id3);

         locationModelInOccasion3.setOccasionID(id3);

         locationDAO.insert(locationModelInOccasion3);

         itemsDAO.insert(itemInOccasion3);
         itemsDAO.insert(item2InOccasion3);
         itemsDAO.insert(item3InOccasion3);

         //Expired occasion test data.
         long id3_2 = occasionDao.insert(new OccasionModel("Mar 6, 2021", "Snacks", false, true));
         ItemModel itemInOccasion3_2 = new ItemModel(37, "Coca Cola", 2, "Ludwig");
         ItemModel item2InOccasion3_2 = new ItemModel(25, "Snickers Bar", 2, "Abraham");
         ItemModel item3InOccasion3_2 = new ItemModel(30, "Twixx Bar", 3, "John");

         LocationModel locationModelInOccasion3_2 = new LocationModel(59.298938,18.080310,0.0, 100.0, "Gullmarsplan T-bana, Yttre, 121 40");

         itemInOccasion3_2.setOccasionID(id3_2);
         item2InOccasion3_2.setOccasionID(id3_2);
         item3InOccasion3_2.setOccasionID(id3_2);

         locationModelInOccasion3_2.setOccasionID(id3_2);

         locationDAO.insert(locationModelInOccasion3_2);

         itemsDAO.insert(itemInOccasion3_2);
         itemsDAO.insert(item2InOccasion3_2);
         itemsDAO.insert(item3InOccasion3_2);

         return null;
      }
   }
}
