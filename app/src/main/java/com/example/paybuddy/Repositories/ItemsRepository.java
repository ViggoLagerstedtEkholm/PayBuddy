package com.example.paybuddy.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.paybuddy.Database.DatabaseHelper;
import com.example.paybuddy.DAO.ItemsDAO;
import com.example.paybuddy.Models.ItemModel;

import java.util.List;

public class ItemsRepository extends Repository<ItemModel>{
    private final ItemsDAO itemsDAO;
    private final LiveData<List<ItemModel>> items;
    private final LiveData<Integer> totalCost;

    public ItemsRepository(Application application){
        DatabaseHelper database = DatabaseHelper.getInstance(application);
        itemsDAO = database.itemsDao();

        items = itemsDAO.getAllItems();
        totalCost = itemsDAO.getTotalCost();
    }

    @Override
    public void insert(ItemModel... entity) {
        new InsertItemTaskAsync(itemsDAO).execute(entity);
    }

    @Override
    public void insert(List<ItemModel> entities) {
        new InsertItemsTaskAsync(itemsDAO).execute(entities);
    }

    @Override
    public void update(ItemModel entity) {
        new UpdateItemTaskAsync(itemsDAO).execute(entity);
    }

    public void delete(ItemModel itemModel){
        new DeleteItemTaskAsync(itemsDAO).execute(itemModel);
    }

    @Override
    public void delete(List<ItemModel> entites) {
        new DeleteItemsTaskAsync(itemsDAO).execute(entites);
    }

    public LiveData<Integer> getTotalCost(){
        return totalCost;
    }

    @Override
    public LiveData<List<ItemModel>> getAll() {
        return items;
    }

    @Override
    public void deleteAll(DELETE_TYPE delete_type) {
        new DeleteAllItemTaskAsync(itemsDAO, delete_type).execute();
    }

    private static class InsertItemsTaskAsync extends AsyncTask<List<ItemModel>, Void, Void> {
        private final ItemsDAO itemsDao;

        private InsertItemsTaskAsync(ItemsDAO itemsDao) {
            this.itemsDao = itemsDao;
        }

        @Override
        protected Void doInBackground(List<ItemModel>... itemModels) {
            itemsDao.insert(itemModels[0]);
            return null;
        }
    }

    private static class InsertItemTaskAsync extends AsyncTask<ItemModel, Void, Void> {
        private final ItemsDAO itemsDao;

        private InsertItemTaskAsync(ItemsDAO itemsDao) {
            this.itemsDao = itemsDao;
        }

        @Override
        protected Void doInBackground(ItemModel... itemModels) {
            itemsDao.insert(itemModels[0]);
            return null;
        }
    }

    private static class UpdateItemTaskAsync extends AsyncTask<ItemModel, Void, Void> {
        private final ItemsDAO itemsDao;

        private UpdateItemTaskAsync(ItemsDAO itemsDao) {
            this.itemsDao = itemsDao;
        }

        @Override
        protected Void doInBackground(ItemModel... itemModels) {
            itemsDao.update(itemModels[0]);
            return null;
        }
    }

    private static class DeleteItemTaskAsync extends AsyncTask<ItemModel, Void, Void> {
        private final ItemsDAO itemsDao;

        private DeleteItemTaskAsync(ItemsDAO itemsDao) {
            this.itemsDao = itemsDao;
        }

        @Override
        protected Void doInBackground(ItemModel... itemModels) {
            itemsDao.delete(itemModels[0]);
            return null;
        }
    }

    private static class DeleteItemsTaskAsync extends AsyncTask<List<ItemModel>, Void, Void> {
        private final ItemsDAO itemsDao;

        private DeleteItemsTaskAsync(ItemsDAO itemsDao) {
            this.itemsDao = itemsDao;
        }

        @Override
        protected Void doInBackground(List<ItemModel>... itemModels) {
            itemsDao.delete(itemModels[0]);
            return null;
        }
    }

    private static class DeleteAllItemTaskAsync extends AsyncTask<ItemModel, Void, Void> {
        private final ItemsDAO itemsDao;
        private DELETE_TYPE delete_type;

        private DeleteAllItemTaskAsync(ItemsDAO itemsDao, DELETE_TYPE delete_type) {
            this.itemsDao = itemsDao;
            this.delete_type = delete_type;
        }

        @Override
        protected Void doInBackground(ItemModel... itemModels) {
            switch(delete_type){
                case DELETE_ALL:
                    itemsDao.deleteAllItems();
                    break;
                case DELETE_ALL_HISTORY:
                    itemsDao.deleteAllItemsHistory();
                    break;
                case DELETE_ALL_EXPIRED:
                    itemsDao.deleteAllItemsExpired();
                    break;
                case DELETE_ALL_UNPAID:
                    itemsDao.deleteAllItemsUnPaid();
                    break;
            }
            return null;
        }
    }
}
