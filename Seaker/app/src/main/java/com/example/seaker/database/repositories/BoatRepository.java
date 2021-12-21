package com.example.seaker.database.repositories;

import com.example.seaker.database.entities.Boat;
import com.example.seaker.database.specifications.ISpecification;

public class BoatRepository implements IRepository<Boat> {
    @Override
    public void add(Boat item) {

    }

    @Override
    public Iterable<Boat> getAll() {
        return null;
    }

    @Override
    public Boat getById(long itemId) {
        return null;
    }

    @Override
    public Iterable<Boat> find(ISpecification specification) {
        return null;
    }

    @Override
    public void update(Boat oldItem, Boat newItem) {

    }

    @Override
    public void remove(Boat itemToRemove) {

    }

    @Override
    public void removeById(long itemId) {

    }
}
