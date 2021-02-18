package com.example.paybuddy.MVVM.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.paybuddy.MVVM.DatabaseHelper;
import com.example.paybuddy.MVVM.ItemsDAO;
import com.example.paybuddy.MVVM.OccasionDAO;
import com.example.paybuddy.MVVM.OccasionWithItemsDAO;
import com.example.paybuddy.Models.OccasionModel;
import com.example.paybuddy.Models.OccasionWithItems;

import java.util.List;

public class OccasionRepository extends Repository<OccasionModel>{
    private final OccasionDAO occasionDao;
    private final ItemsDAO itemsDAO;
    private final OccasionWithItemsDAO occasionWithItemsDAO;

    private LiveData<List<OccasionWithItems>> activeOccasions;
    private LiveData<List<OccasionWithItems>> paidOccasions;
    private LiveData<List<OccasionWithItems>> expiredOccasions;
    private LiveData<List<OccasionWithItems>> allOccasions;

    public OccasionRepository(Application application){
        DatabaseHelper database = DatabaseHelper.getInstance(application);

        occasionDao = database.occasionDao();
        itemsDAO = database.itemsDao();
        occasionWithItemsDAO = database.occasionWithItemsDAO();

        activeOccasions = occasionWithItemsDAO.getActiveOccasions();
        paidOccasions = occasionWithItemsDAO.getPaidOccasions();
        expiredOccasions = occasionWithItemsDAO.getExpiredOccasions();
        allOccasions = occasionWithItemsDAO.getAllOccasions();
    }

    @Override
    public void insert(OccasionModel... entity) {
        new InsertOccasionAsyncTask(occasionDao, itemsDAO).execute(entity);
    }

    @Override
    public void insert(List<OccasionModel> entities) {
        //TODO
    }

    @Override
    public void update(OccasionModel entity) {
        new UpdateOccasionAsyncTask(occasionDao).execute(entity);
    }

    @Override
    public void delete(OccasionModel entity) {
        new DeleteOccasionAsyncTask(occasionDao, itemsDAO).execute(entity);
    }

    @Override
    void delete(List<OccasionModel> entity) {
        //TODO
    }

    @Override
    public void deleteAll() {
        new DeleteAllOccasionAsyncTask(occasionDao, itemsDAO).execute();
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

    @Override
    LiveData<List<OccasionModel>> getAll() {
        return null;
    }

    private static class InsertOccasionAsyncTask extends AsyncTask<OccasionModel, Void, Void> {
        private final OccasionDAO occasionDao;
        private final ItemsDAO itemsDao;

        private InsertOccasionAsyncTask(OccasionDAO occasionDao, ItemsDAO itemsDAO){
            this.occasionDao = occasionDao;
            this.itemsDao = itemsDAO;
        }
        @Override
        protected Void doInBackground(OccasionModel... occasionModels) {
            long id  = occasionDao.insert(occasionModels[0]); //Insert occasion
            itemsDao.insertItemsAndOccasion(occasionModels[0].getItems(), id);
            return null;
        }
    }

    private static class UpdateOccasionAsyncTask extends AsyncTask<OccasionModel, Void, Void>{
        private final OccasionDAO occasionDao;

        private UpdateOccasionAsyncTask(OccasionDAO occasionDao){
            this.occasionDao = occasionDao;
        }
        @Override
        protected Void doInBackground(OccasionModel... occasionModels) {
            occasionDao.update(occasionModels[0]);
            return null;
        }
    }

    private static class DeleteOccasionAsyncTask extends AsyncTask<OccasionModel, Void, Void>{
        private final OccasionDAO occasionDao;
        private final ItemsDAO itemsDAO;

        private DeleteOccasionAsyncTask(OccasionDAO occasionDao, ItemsDAO itemsDAO){
            this.occasionDao = occasionDao;
            this.itemsDAO = itemsDAO;
        }
        @Override
        protected Void doInBackground(OccasionModel... occasionModels) {
            occasionDao.delete(occasionModels[0]);
            return null;
        }
    }

    private static class DeleteAllOccasionAsyncTask extends AsyncTask<Void, Void, Void>{
        private final OccasionDAO occasionDao;
        private final ItemsDAO itemsDAO;

        private DeleteAllOccasionAsyncTask(OccasionDAO occasionDao, ItemsDAO itemsDAO){
            this.occasionDao = occasionDao;
            this.itemsDAO = itemsDAO;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            occasionDao.deleteAllOccasions();
            return null;
        }
    }
}
