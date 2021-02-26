package com.example.paybuddy.Repositories;

import androidx.lifecycle.LiveData;

import java.util.List;

public abstract class Repository<T> {

    public enum DELETE_TYPE{
        DELETE_ALL_EXPIRED,
        DELETE_ALL_HISTORY,
        DELETE_ALL_UNPAID,
        DELETE_ALL
    }

    abstract void insert(T... entity);
    abstract void insert(List<T> entities);

    abstract void update(T entity);

    abstract void delete(T entity);
    abstract void delete(List<T> entity);
    abstract void deleteAll(DELETE_TYPE delete_type);

    abstract LiveData<List<T>> getAll();
}