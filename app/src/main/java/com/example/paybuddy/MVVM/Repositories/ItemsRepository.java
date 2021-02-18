package com.example.paybuddy.MVVM.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.paybuddy.MVVM.DatabaseHelper;
import com.example.paybuddy.MVVM.ItemsDAO;
import com.example.paybuddy.Models.ItemModel;
import com.example.paybuddy.Models.OccasionWithItems;

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
    public void deleteAll() {
        new DeleteAllItemTaskAsync(itemsDAO).execute();
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

        private DeleteAllItemTaskAsync(ItemsDAO itemsDao) {
            this.itemsDao = itemsDao;
        }

        @Override
        protected Void doInBackground(ItemModel... itemModels) {
            itemsDao.deleteAllItems();
            return null;
        }
    }
}
