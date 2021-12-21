package com.example.seaker.database.repositories;

import com.example.seaker.database.entities.Sighting;
import com.example.seaker.database.specifications.ISpecification;

public class SightingRepository implements IRepository<Sighting> {
    @Override
    public void add(Sighting item) {

    }

    @Override
    public Iterable<Sighting> getAll() {
        return null;
    }

    @Override
    public Sighting getById(long itemId) {
        return null;
    }

    @Override
    public Iterable<Sighting> find(ISpecification specification) {
        return null;
    }

    @Override
    public void update(Sighting oldItem, Sighting newItem) {

    }

    @Override
    public void remove(Sighting itemToRemove) {

    }

    @Override
    public void removeById(long itemId) {

    }
}
