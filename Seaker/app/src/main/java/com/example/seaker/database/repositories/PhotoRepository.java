package com.example.seaker.database.repositories;

import com.example.seaker.database.entities.Photo;
import com.example.seaker.database.specifications.ISpecification;

public class PhotoRepository implements IRepository<Photo> {
    @Override
    public void add(Photo item) {

    }

    @Override
    public Iterable<Photo> getAll() {
        return null;
    }

    @Override
    public Photo getById(long itemId) {
        return null;
    }

    @Override
    public Iterable<Photo> find(ISpecification specification) {
        return null;
    }

    @Override
    public void update(Photo oldItem, Photo newItem) {

    }

    @Override
    public void remove(Photo itemToRemove) {

    }

    @Override
    public void removeById(long itemId) {

    }
}
