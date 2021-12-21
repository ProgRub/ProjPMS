package com.example.seaker.database.repositories;

import com.example.seaker.database.entities.Zone;
import com.example.seaker.database.specifications.ISpecification;

public class ZoneRepository implements IRepository<Zone> {
    @Override
    public void add(Zone item) {

    }

    @Override
    public Iterable<Zone> getAll() {
        return null;
    }

    @Override
    public Zone getById(long itemId) {
        return null;
    }

    @Override
    public Iterable<Zone> find(ISpecification specification) {
        return null;
    }

    @Override
    public void update(Zone oldItem, Zone newItem) {

    }

    @Override
    public void remove(Zone itemToRemove) {

    }

    @Override
    public void removeById(long itemId) {

    }
}
