package com.example.paybuddy.MVVM;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.paybuddy.Models.ItemModel;
import com.example.paybuddy.Models.OccasionModel;
import com.example.paybuddy.Models.OccasionWithItems;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    private final OccasionDAO occasionDao;
    private final ItemsDAO itemsDAO;
    private final OccasionWithItemsDAO occasionWithItemsDAO;
    private LiveData<List<OccasionModel>> listLiveData;
    private LiveData<List<OccasionWithItems>> occasionsWithItems;

    public Repository(Application application){
        DatabaseHelper database = DatabaseHelper.getInstance(application);
        occasionDao = database.occasionDao();
        itemsDAO = database.itemsDao();
        occasionWithItemsDAO = database.occasionWithItemsDAO();
        //Log.d("Occasion length", String.valueOf(occasionDao.getAllOccasions().getValue().size()));
        //Log.d("Items length", String.valueOf(itemsDAO.getAllItems().size()));

        listLiveData = occasionDao.getAllOccasions();
        occasionsWithItems = occasionWithItemsDAO.loadOccasionsWithItems();
        //itemsLiveData = itemsDAO.getAllItems();
    }

    public void insert(OccasionModel occasionModel){
        new InsertOccasionAsyncTask(occasionDao, itemsDAO).execute(occasionModel);
    }

    public void updateOccasion(OccasionModel occasionModel){
        new UpdateOccasionAsyncTask(occasionDao).execute(occasionModel);
    }

    public void delete(OccasionModel occasionModel){
        new DeleteOccasionAsyncTask(occasionDao, itemsDAO).execute(occasionModel);
    }

    public void updateItem(ItemModel itemModel){
        new UpdateItemAsyncTask(itemsDAO).execute(itemModel);
    }

    public void deleteAllOccasions(){
        new DeleteAllOccasionAsyncTask(occasionDao, itemsDAO).execute();
    }

    public LiveData<List<OccasionWithItems>> getOccasionsWitchItems(){
        return occasionsWithItems;
    }


    public LiveData<List<OccasionModel>> getListLiveData(){
        //listLiveData = mergedModel(occasionDao.getAllOccasions().getValue(), itemsDAO.getAllItems().getValue());


        return listLiveData;
    }

    private LiveData<List<OccasionModel>> mergedModel(List<OccasionModel> occasionModels, List<ItemModel> itemModels){
        List<OccasionModel> occasionModelsWithItems = new ArrayList<>();
        List<ItemModel> itemsInOccasion = new ArrayList<>();
        for (OccasionModel occasionModel : occasionModels){
            for(ItemModel itemModel : itemModels){
                if(itemModel.getOccasionID() == occasionModel.getID()){
                    itemsInOccasion.add(itemModel);
                }
            }
            occasionModel.setItems(itemsInOccasion);
            occasionModelsWithItems.add(occasionModel);
            itemsInOccasion.clear();
        }
        LiveData<List<OccasionModel>> liveData = (LiveData<List<OccasionModel>>) occasionModelsWithItems;
        return liveData;
    }

    private static class InsertOccasionAsyncTask extends AsyncTask<OccasionModel, Void, Void>{
        private final OccasionDAO occasionDao;
        private ItemsDAO itemsDAO;

        private InsertOccasionAsyncTask(OccasionDAO occasionDao, ItemsDAO itemsDAO){
            this.occasionDao = occasionDao;
            this.itemsDAO = itemsDAO;
        }
        @Override
        protected Void doInBackground(OccasionModel... occasionModels) {
            long id = occasionDao.insert(occasionModels[0]); //Insert occasion
            itemsDAO.insertItemsAndOccasion(occasionModels[0].getItems(), id); //Insert items for occasion.
            return null;
        }
    }

    private static class UpdateItemAsyncTask extends AsyncTask<ItemModel, Void, Void>{
        private final ItemsDAO itemsDAO;

        private UpdateItemAsyncTask(ItemsDAO itemsDAO) {
            this.itemsDAO = itemsDAO;
        }

        @Override
        protected Void doInBackground(ItemModel... itemModels) {
            itemsDAO.update(itemModels[0]);
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
            itemsDAO.delete(occasionModels[0].getItems());
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
            itemsDAO.deleteAllItems();
            return null;
        }
    }
}
