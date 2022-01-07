package com.example.seaker.database.repositories;

import com.example.seaker.database.DTOs.BoatDTO;
import com.example.seaker.database.specifications.ISpecification;

public class BoatRepository implements IRepository<BoatDTO> {
    @Override
    public void add(BoatDTO item) {

    }

    @Override
    public Iterable<BoatDTO> getAll() {
        return null;
    }

    @Override
    public BoatDTO getById(long itemId) {
        return null;
    }

    @Override
    public Iterable<BoatDTO> find(ISpecification specification) {
        return null;
    }

    @Override
    public void update(BoatDTO oldItem, BoatDTO newItem) {

    }

    @Override
    public void remove(BoatDTO itemToRemove) {

    }

    @Override
    public void removeById(long itemId) {

    }
}
