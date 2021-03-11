package com.example.paybuddy.Repositories;

/**
 *  Generic CRUD repository superclass.
 *  @date 2021-03-09
 *  @version 1.0
 *  @author Viggo Lagerstedt Ekholm
 */
public abstract class Repository<T> {
    //Deletion types.
    public enum DELETE_TYPE{
        DELETE_ALL_EXPIRED,
        DELETE_ALL_HISTORY,
        DELETE_ALL_UNPAID,
        DELETE_ALL
    }
    public abstract void insert(T entity);
    public abstract void update(T entity);
    public abstract void delete(T entity);
    public abstract void deleteAll(DELETE_TYPE delete_type);
}


