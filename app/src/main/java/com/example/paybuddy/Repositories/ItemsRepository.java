package com.example.paybuddy.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.paybuddy.Database.DatabaseHelper;
import com.example.paybuddy.DAO.ItemsDAO;
import com.example.paybuddy.Models.ItemModel;

import java.util.List;

/**
 *  This is the item repository, this class is responsible for modifying/fetching items in our database.
 *  We use the generic repository for CRUD operations.
 *  @date 2021-03-09
 *  @version 1.0
 *  @author Viggo Lagerstedt Ekholm
 */
public class ItemsRepository extends Repository<ItemModel>{
    private final ItemsDAO itemsDAO;
    private final LiveData<List<ItemModel>> items;
    private final LiveData<Integer> totalCost;
    private final LiveData<List<ItemModel>> pendingItems;

    //The constructor takes the application as an argument because we want to get our singleton database instance.
    public ItemsRepository(Application application){
        DatabaseHelper database = DatabaseHelper.getInstance(application);
        itemsDAO = database.itemsDao();

        items = itemsDAO.getAllItems();
        totalCost = itemsDAO.getTotalCost();
        pendingItems = itemsDAO.getPendingItems();
    }

    /**
     * Insert an itemModel.
     * @param itemModel ItemModel
     */
    @Override
    public void insert(ItemModel itemModel) {
        new InsertItemTaskAsync(itemsDAO).execute(itemModel);
    }

    /**
     * Insert a collection of itemModel.
     * @param items List<ItemModel>
     */
    @SuppressWarnings("unchecked")
    public void insert(List<ItemModel>... items) {
        new InsertItemsTaskAsync(itemsDAO).execute(items);
    }

    /**
     * Update an itemModel.
     * @param itemModel ItemModel
     */
    @Override
    public void update(ItemModel itemModel) {
        new UpdateItemTaskAsync(itemsDAO).execute(itemModel);
    }

    /**
     * Delete an itemModel.
     * @param itemModel ItemModel
     */
    @Override
    public void delete(ItemModel itemModel){
        new DeleteItemTaskAsync(itemsDAO).execute(itemModel);
    }

    /**
     * Deletes a list of items.
     * @param items List<ItemModel>
     */
    @SuppressWarnings("unchecked")
    public void delete(List<ItemModel> items) {
        new DeleteItemsTaskAsync(itemsDAO).execute(items);
    }

    /**
     * Delete all items of specified type.
     * @param delete_type specified deletion type.
     */
    @Override
    public void deleteAll(DELETE_TYPE delete_type) {
        new DeleteAllItemTaskAsync(itemsDAO, delete_type).execute();
    }

    /**
     * Get total item cost.
     * @return LiveData<Integer>
     */
    public LiveData<Integer> getTotalCost(){
        return totalCost;
    }

    /**
     * Get total of items in occasion..
     * @return LiveData<Integer>
     */
    public LiveData<Integer> getOccasionItemCount(double ID){return itemsDAO.getOccasionItemCount(ID);}

    /**
     * Gets pending items.
     * @return LiveData<List<ItemModel>>
     */
    public LiveData<List<ItemModel>> getPendingItems(){return pendingItems;}

    /**
     * Gets all people from an occasion.
     * @return LiveData<List<String>>
     */
    public LiveData<List<String>> getPeopleOccasion(double ID){
        return itemsDAO.getPeopleOccasion(ID);
    }

    /**
     * Get occasion total item cost.
     * @return LiveData<Integer>
     */
    public LiveData<Integer> getOccasionTotalCost(double ID){
        return itemsDAO.getOccasionTotalCost(ID);
    }

    /**
     * Get all items in occasion.
     * @return LiveData<List<ItemModel>>
     */
    public LiveData<List<ItemModel>> getOccasionItems(double ID){
        return itemsDAO.getOccasionItems(ID);
    }

    /**
     * Get all items.
     * @return LiveData<List<ItemModel>>
     */
    public LiveData<List<ItemModel>> getAll() {
        return items;
    }

    /**
     * Deletes all pending items.
     * @return void
     */
    public void deletePending() {
        new DeletePendingItemsTaskAsync(itemsDAO).execute();
    }

    /**
     * Class handles the deletion of a collection of items into the database.
     */
    private static class InsertItemsTaskAsync extends AsyncTask<List<ItemModel>, Void, Void> {
        private final ItemsDAO itemsDao;

        private InsertItemsTaskAsync(ItemsDAO itemsDao) {
            this.itemsDao = itemsDao;
        }

        /**
         * Task is done in the background.
         * @param itemModels List<ItemModel>
         * @return
         */
        @Override
        protected Void doInBackground(List<ItemModel>... itemModels) {
            itemsDao.insert(itemModels[0]);
            return null;
        }
    }

    /**
     * Class handles the insertion into the database.
     */
    private static class InsertItemTaskAsync extends AsyncTask<ItemModel, Void, Void> {
        private final ItemsDAO itemsDao;

        private InsertItemTaskAsync(ItemsDAO itemsDao) {
            this.itemsDao = itemsDao;
        }

        /**
         * Task is done in the background.
         * @param itemModels ItemModel
         * @return
         */
        @Override
        protected Void doInBackground(ItemModel... itemModels) {
            itemsDao.insert(itemModels[0]);
            return null;
        }
    }

    /**
     * Class handles the updating into the database.
     */
    private static class UpdateItemTaskAsync extends AsyncTask<ItemModel, Void, Void> {
        private final ItemsDAO itemsDao;

        private UpdateItemTaskAsync(ItemsDAO itemsDao) {
            this.itemsDao = itemsDao;
        }

        /**
         * Task is done in the background.
         * @param itemModels ItemModel
         * @return
         */
        @Override
        protected Void doInBackground(ItemModel... itemModels) {
            itemsDao.update(itemModels[0]);
            return null;
        }
    }

    /**
     * Class handles the delete into the database.
     */
    private static class DeleteItemTaskAsync extends AsyncTask<ItemModel, Void, Void> {
        private final ItemsDAO itemsDao;

        private DeleteItemTaskAsync(ItemsDAO itemsDao) {
            this.itemsDao = itemsDao;
        }

        /**
         * Task is done in the background.
         * @param itemModels ItemModel
         * @return
         */
        @Override
        protected Void doInBackground(ItemModel... itemModels) {
            itemsDao.delete(itemModels[0]);
            return null;
        }
    }

    /**
     * Class handles the delete of a collection into the database.
     */
    private static class DeleteItemsTaskAsync extends AsyncTask<List<ItemModel>, Void, Void> {
        private final ItemsDAO itemsDao;

        private DeleteItemsTaskAsync(ItemsDAO itemsDao) {
            this.itemsDao = itemsDao;
        }

        /**
         * Task is done in the background.
         * @param itemModels List<ItemModel>
         * @return
         */
        @Override
        protected Void doInBackground(List<ItemModel>... itemModels) {
            itemsDao.delete(itemModels[0]);
            return null;
        }
    }

    /**
     * Class handles the delete into the database.
     */
    private static class DeletePendingItemsTaskAsync extends AsyncTask<Void, Void, Void> {
        private final ItemsDAO itemsDao;

        private DeletePendingItemsTaskAsync(ItemsDAO itemsDao) {
            this.itemsDao = itemsDao;
        }

        /**
         * Task is done in the background.
         * @param voids Void
         * @return
         */
        @Override
        protected Void doInBackground(Void... voids) {
            itemsDao.deletePending();
            return null;
        }
    }

    /**
     * Class handles the delete into the database.
     */
    private static class DeleteAllItemTaskAsync extends AsyncTask<ItemModel, Void, Void> {
        private final ItemsDAO itemsDao;
        private DELETE_TYPE delete_type;

        private DeleteAllItemTaskAsync(ItemsDAO itemsDao, DELETE_TYPE delete_type) {
            this.itemsDao = itemsDao;
            this.delete_type = delete_type;
        }

        /**
         * Task is done in the background.
         * @param itemModels ItemModel
         * @return
         */
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
