package com.example.paybuddy.MVVM.Repositories;

import androidx.lifecycle.LiveData;

import com.example.paybuddy.Models.OccasionWithItems;

import java.util.List;

public abstract class Repository<T> {
    abstract void insert(T... entity);
    abstract void insert(List<T> entities);

    abstract void update(T entity);

    abstract void delete(T entity);
    abstract void delete(List<T> entity);
    abstract void deleteAll();

    abstract LiveData<List<T>> getAll();
}
