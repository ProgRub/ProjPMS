package com.example.seaker.database.repositories;

import com.example.seaker.database.entities.Species;
import com.example.seaker.database.specifications.ISpecification;

public class SpeciesRepository implements IRepository<Species> {
    @Override
    public void add(Species item) {

    }

    @Override
    public Iterable<Species> getAll() {
        return null;
    }

    @Override
    public Species getById(long itemId) {
        return null;
    }

    @Override
    public Iterable<Species> find(ISpecification specification) {
        return null;
    }

    @Override
    public void update(Species oldItem, Species newItem) {

    }

    @Override
    public void remove(Species itemToRemove) {

    }

    @Override
    public void removeById(long itemId) {

    }
}
