package com.example.paybuddy.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.paybuddy.Database.DatabaseHelper;
import com.example.paybuddy.DAO.LocationDAO;
import com.example.paybuddy.Models.ItemModel;
import com.example.paybuddy.Models.LocationModel;

import java.util.List;

public class LocationRepository extends Repository<LocationModel> {
    private final LocationDAO locationDAO;

    public LocationRepository(Application application){
        DatabaseHelper database = DatabaseHelper.getInstance(application);

        locationDAO = database.locationDAO();
    }

    @Override
    public void insert(LocationModel... entity) {
        new InsertLocationAsyncTask(locationDAO).doInBackground(entity);
    }

    @Override
    void insert(List<LocationModel> entities) {

    }

    @Override
    public void update(LocationModel entity) {
        new UpdateLocationAsyncTask(locationDAO).doInBackground(entity);

    }

    @Override
    public void delete(LocationModel entity) {
        new DeleteLocationAsyncTask(locationDAO).doInBackground(entity);
    }

    @Override
    public void delete(List<LocationModel> entity) {

    }

    @Override
    public void deleteAll(DELETE_TYPE delete_type) {
       new DeleteAllLocationAsyncTask(locationDAO, delete_type).execute();
    }

    @Override
    LiveData<List<LocationModel>> getAll() {
        return null;
    }

    private static class InsertLocationAsyncTask extends AsyncTask<LocationModel, Void, Void> {
        private final LocationDAO locationDAO;

        private InsertLocationAsyncTask(LocationDAO locationDAO){
            this.locationDAO = locationDAO;
        }

        @Override
        protected Void doInBackground(LocationModel... locationModels) {
            locationDAO.insert(locationModels[0]);
            return null;
        }
    }

    private static class UpdateLocationAsyncTask extends AsyncTask<LocationModel, Void, Void>{
        private final LocationDAO locationDAO;

        private UpdateLocationAsyncTask(LocationDAO locationDAO){
            this.locationDAO = locationDAO;
        }
        @Override
        protected Void doInBackground(LocationModel... locationModels) {
            locationDAO.update(locationModels[0]);
            return null;
        }
    }

    private static class DeleteLocationAsyncTask extends AsyncTask<LocationModel, Void, Void>{
        private final LocationDAO locationDAO;

        private DeleteLocationAsyncTask(LocationDAO locationDAO){
            this.locationDAO = locationDAO;
        }
        @Override
        protected Void doInBackground(LocationModel... locationModels) {
            locationDAO.delete(locationModels[0]);
            return null;
        }
    }

    private static class DeleteAllLocationAsyncTask extends AsyncTask<Void, Void, Void>{
        private final LocationDAO locationDAO;
        private DELETE_TYPE delete_type;

        private DeleteAllLocationAsyncTask(LocationDAO locationDAO, DELETE_TYPE delete_type){
            this.locationDAO = locationDAO;
            this.delete_type = delete_type;
        }

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
