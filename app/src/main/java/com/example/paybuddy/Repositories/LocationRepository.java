package com.example.paybuddy.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.paybuddy.Database.DatabaseHelper;
import com.example.paybuddy.DAO.LocationDAO;
import com.example.paybuddy.Models.LocationModel;

import java.util.List;

public class LocationRepository extends Repository<LocationModel> {
    private final LocationDAO locationDAO;

    public LocationRepository(Application application){
        DatabaseHelper database = DatabaseHelper.getInstance(application);

        locationDAO = database.locationDAO();
    }

    @Override
    void insert(LocationModel... entity) {
        new InsertLocationAsyncTask(locationDAO).doInBackground(entity);
    }

    @Override
    void insert(List<LocationModel> entities) {

    }

    @Override
    void update(LocationModel entity) {
        new UpdateLocationAsyncTask(locationDAO).doInBackground(entity);

    }

    @Override
    void delete(LocationModel entity) {
        new DeleteLocationAsyncTask(locationDAO).doInBackground(entity);
    }

    @Override
    void delete(List<LocationModel> entity) {

    }

    @Override
    void deleteAll(DELETE_TYPE delete_type) {

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
}
