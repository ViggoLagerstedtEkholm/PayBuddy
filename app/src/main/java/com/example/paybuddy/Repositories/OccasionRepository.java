package com.example.paybuddy.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.paybuddy.Database.DatabaseHelper;
import com.example.paybuddy.DAO.ItemsDAO;
import com.example.paybuddy.DAO.LocationDAO;
import com.example.paybuddy.DAO.OccasionDAO;
import com.example.paybuddy.DAO.OccasionWithItemsDAO;
import com.example.paybuddy.Models.OccasionModel;
import com.example.paybuddy.Models.OccasionWithItems;

import java.util.List;

/**
 *  This is the occasion repository, this class is responsible for modifying/fetching items in our database.
 *  We use the generic repository for CRUD operations.
 *  @date 2021-03-09
 *  @version 1.0
 *  @author Viggo Lagerstedt Ekholm
 */
public class OccasionRepository extends Repository<OccasionModel>{
    private final OccasionDAO occasionDao;
    private final ItemsDAO itemsDAO;
    private final LocationDAO locationDAO;

    private final LiveData<List<OccasionWithItems>> activeOccasions;
    private final LiveData<List<OccasionWithItems>> paidOccasions;
    private final LiveData<List<OccasionWithItems>> expiredOccasions;
    private final LiveData<List<OccasionWithItems>> allOccasions;

    //The constructor takes the application as an argument because we want to get our singleton database instance.
    public OccasionRepository(Application application){
        DatabaseHelper database = DatabaseHelper.getInstance(application);

        occasionDao = database.occasionDao();
        itemsDAO = database.itemsDao();
        locationDAO = database.locationDAO();

        OccasionWithItemsDAO occasionWithItemsDAO = database.occasionWithItemsDAO();

        activeOccasions = occasionWithItemsDAO.getActiveOccasions();
        paidOccasions = occasionWithItemsDAO.getPaidOccasions();
        expiredOccasions = occasionWithItemsDAO.getExpiredOccasions();
        allOccasions = occasionWithItemsDAO.getAllOccasions();
    }

    /**
     * Insert a occasionModel.
     * @param occasionModel OccasionModel
     */
    @Override
    public void insert(OccasionModel occasionModel) {
        new InsertOccasionAsyncTask(occasionDao, itemsDAO, locationDAO).execute(occasionModel);
    }

    /**
     * Update a occasionModel.
     * @param occasionModel OccasionModel
     */
    @Override
    public void update(OccasionModel occasionModel) {
        new UpdateOccasionAsyncTask(occasionDao).execute(occasionModel);
    }

    /**
     * Delete a occasionModel.
     * @param occasionModel OccasionModel
     */
    @Override
    public void delete(OccasionModel occasionModel) {
        new DeleteOccasionAsyncTask(occasionDao).execute(occasionModel);
    }

    /**
     * Delete all occasions of specified type.
     * @param delete_type specified deletion type.
     */
    @Override
    public void deleteAll(DELETE_TYPE delete_type) {
        new DeleteAllOccasionAsyncTask(occasionDao, delete_type).execute();
    }

    public LiveData<List<OccasionWithItems>> getActiveOccasions(){
        return activeOccasions;
    }
    public LiveData<List<OccasionWithItems>> getPaidOccasions(){
        return paidOccasions;
    }
    public LiveData<List<OccasionWithItems>> getExpiredOccasions(){
        return expiredOccasions;
    }
    public LiveData<List<OccasionWithItems>> getAllOccasions(){return allOccasions;}

    /**
     * Class handles the insert into the database.
     */
    private static class InsertOccasionAsyncTask extends AsyncTask<OccasionModel, Void, Void> {
        private final OccasionDAO occasionDao;
        private final ItemsDAO itemsDao;
        private final LocationDAO locationDAO;

        private InsertOccasionAsyncTask(OccasionDAO occasionDao, ItemsDAO itemsDAO, LocationDAO locationDAO){
            this.occasionDao = occasionDao;
            this.itemsDao = itemsDAO;
            this.locationDAO = locationDAO;
        }

        /**
         * Task is done in the background.
         * @param occasionModels OccasionModel
         * @return
         */
        @Override
        protected Void doInBackground(OccasionModel... occasionModels) {
            long id  = occasionDao.insert(occasionModels[0]); //Insert occasion
            itemsDao.updateItemsAndOccasion(occasionModels[0].getItems(), id);
            locationDAO.insertLocation(occasionModels[0].getLocationModel(), id);
            return null;
        }
    }

    /**
     * Class handles the update into the database.
     */
    private static class UpdateOccasionAsyncTask extends AsyncTask<OccasionModel, Void, Void>{
        private final OccasionDAO occasionDao;

        private UpdateOccasionAsyncTask(OccasionDAO occasionDao){
            this.occasionDao = occasionDao;
        }

        /**
         * Task is done in the background.
         * @param occasionModels OccasionModel
         * @return Void
         */
        @Override
        protected Void doInBackground(OccasionModel... occasionModels) {
            occasionDao.update(occasionModels[0]);
            return null;
        }
    }

    /**
     * Class handles the delete into the database.
     */
    private static class DeleteOccasionAsyncTask extends AsyncTask<OccasionModel, Void, Void>{
        private final OccasionDAO occasionDao;

        private DeleteOccasionAsyncTask(OccasionDAO occasionDao){
            this.occasionDao = occasionDao;
        }

        /**
         * Task is done in the background.
         * @param occasionModels
         * @return Void
         */
        @Override
        protected Void doInBackground(OccasionModel... occasionModels) {
            occasionDao.delete(occasionModels[0]);
            return null;
        }
    }

    /**
     * Class handles the deletion of all entries in the database.
     */
    private static class DeleteAllOccasionAsyncTask extends AsyncTask<Void, Void, Void>{
        private final OccasionDAO occasionDao;
        private final DELETE_TYPE delete_type;

        private DeleteAllOccasionAsyncTask(OccasionDAO occasionDao, DELETE_TYPE delete_type){
            this.occasionDao = occasionDao;
            this.delete_type = delete_type;
        }

        /**
         * Task is done in the background.
         * @param voids Void
         * @return Void
         */
        @Override
        protected Void doInBackground(Void... voids) {
            switch(delete_type){
                case DELETE_ALL:
                    occasionDao.deleteAllOccasions();
                break;
                case DELETE_ALL_EXPIRED:
                    occasionDao.deleteAllExpired();
                    break;
                case DELETE_ALL_HISTORY:
                    occasionDao.deleteAllHistory();
                    break;
                case DELETE_ALL_UNPAID:
                    occasionDao.deleteAllUnPaid();
                    break;
            }
            return null;
        }
    }
}
