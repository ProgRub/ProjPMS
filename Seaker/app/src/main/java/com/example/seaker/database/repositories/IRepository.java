package com.example.seaker.database.repositories;

import com.example.seaker.database.specifications.ISpecification;

public interface IRepository<T> {
    void add(T item);
    Iterable<T> getAll();
    T getById(long itemId);
    Iterable<T> find(ISpecification specification);
    void update(T oldItem,T newItem);
    void remove(T itemToRemove);
    void removeById(long itemId);
}
