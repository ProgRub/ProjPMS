package com.example.seaker.database.repositories;

import com.example.seaker.database.specifications.ISpecification;

public abstract class IRepository<T> {
    protected static final String ip = "10.82.103.237"; //erro propositadamente, para n se esquecerem de alterar :P
    abstract void add(T item);
    abstract Iterable<T> getAll();
    abstract T getById(long itemId);
    abstract Iterable<T> find(ISpecification specification);
    abstract void update(T oldItem,T newItem);
    abstract void remove(T itemToRemove);
    abstract void removeById(long itemId);
}
