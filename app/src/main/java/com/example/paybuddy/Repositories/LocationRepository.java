package com.example.paybuddy.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import com.example.paybuddy.Database.DatabaseHelper;
import com.example.paybuddy.DAO.LocationDAO;
import com.example.paybuddy.Models.LocationModel;

/**
 *  This is the location repository, this class is responsible for modifying/fetching items in our database.
 *  We use the generic repository for CRUD operations.
 *  @date 2021-03-09
 *  @version 1.0
 *  @author Viggo Lagerstedt Ekholm
 */
public class LocationRepository extends Repository<LocationModel> {
    private final LocationDAO locationDAO;

    //The constructor takes the application as an argument because we want to get our singleton database instance.
    public LocationRepository(Application application){
        DatabaseHelper database = DatabaseHelper.getInstance(application);

        locationDAO = database.locationDAO();
    }

    /**
     * Insert a locationModel.
     * @param locationModel LocationModel
     */
    @Override
    public void insert(LocationModel locationModel) {
        new InsertLocationAsyncTask(locationDAO).execute(locationModel);
    }

    /**
     * Update a locationModel.
     * @param locationModel LocationModel
     */
    @Override
    public void update(LocationModel locationModel) {
        new UpdateLocationAsyncTask(locationDAO).execute(locationModel);
    }

    /**
     * Delete a locationModel.
     * @param locationModel LocationModel
     */
    @Override
    public void delete(LocationModel locationModel) {
        new DeleteLocationAsyncTask(locationDAO).execute(locationModel);
    }

    /**
     * Delete all location models of specified type.
     * @param delete_type specified deletion type.
     */
    @Override
    public void deleteAll(DELETE_TYPE delete_type) {
       new DeleteAllLocationAsyncTask(locationDAO, delete_type).execute();
    }

    /**
     * Class handles the insert into the database.
     */
    private static class InsertLocationAsyncTask extends AsyncTask<LocationModel, Void, Void> {
        private final LocationDAO locationDAO;

        private InsertLocationAsyncTask(LocationDAO locationDAO){
            this.locationDAO = locationDAO;
        }

        /**
         * Task is done in the background.
         * @param locationModels LocationModel
         * @return
         */
        @Override
        protected Void doInBackground(LocationModel... locationModels) {
            if (locationModels[0] != null){
                locationDAO.insert(locationModels[0]);
            }
            return null;
        }
    }

    /**
     * Class handles the updates the database.
     */
    private static class UpdateLocationAsyncTask extends AsyncTask<LocationModel, Void, Void>{
        private final LocationDAO locationDAO;

        private UpdateLocationAsyncTask(LocationDAO locationDAO){
            this.locationDAO = locationDAO;
        }

        /**
         * Task is done in the background.
         * @param locationModels LocationModel
         * @return
         */
        @Override
        protected Void doInBackground(LocationModel... locationModels) {
            if(locationModels[0] != null){
                locationDAO.update(locationModels[0]);
            }
            return null;
        }
    }

    /**
     * Class handles the delete into the database.
     */
    private static class DeleteLocationAsyncTask extends AsyncTask<LocationModel, Void, Void>{
        private final LocationDAO locationDAO;

        private DeleteLocationAsyncTask(LocationDAO locationDAO){
            this.locationDAO = locationDAO;
        }

        /**
         * Task is done in the background.
         * @param locationModels LocationModel
         * @return
         */
        @Override
        protected Void doInBackground(LocationModel... locationModels) {
            if(locationModels[0] != null){
                locationDAO.delete(locationModels[0]);
            }
            return null;
        }
    }

    /**
     * Class handles the deletion of all entries in the database.
     */
    private static class DeleteAllLocationAsyncTask extends AsyncTask<Void, Void, Void>{
        private final LocationDAO locationDAO;
        private DELETE_TYPE delete_type;

        private DeleteAllLocationAsyncTask(LocationDAO locationDAO, DELETE_TYPE delete_type){
            this.locationDAO = locationDAO;
            this.delete_type = delete_type;
        }

        /**
         * Task is done in the background.
         * @param itemModels Void
         * @return
         */
        @Override
        protected Void doInBackground(Void... itemModels) {
            switch(delete_type){
                case DELETE_ALL:
                    locationDAO.deleteAllLocations();
                    break;
                case DELETE_ALL_HISTORY:
                    locationDAO.deleteLocationPaid();
                    break;
                case DELETE_ALL_EXPIRED:
                    locationDAO.deleteLocationExpired();
                    break;
                case DELETE_ALL_UNPAID:
                    locationDAO.deleteLocationUnPaid();
                    break;

            }
            return null;
        }
    }
}
