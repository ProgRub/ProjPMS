package com.example.seaker.database.repositories;

import com.example.seaker.database.DTOs.SightingDTO;
import com.example.seaker.database.specifications.ISpecification;

public class SightingRepository implements IRepository<SightingDTO> {
    @Override
    public void add(SightingDTO item) {

    }

    @Override
    public Iterable<SightingDTO> getAll() {
        return null;
    }

    @Override
    public SightingDTO getById(long itemId) {
        return null;
    }

    @Override
    public Iterable<SightingDTO> find(ISpecification specification) {
        return null;
    }

    @Override
    public void update(SightingDTO oldItem, SightingDTO newItem) {

    }

    @Override
    public void remove(SightingDTO itemToRemove) {

    }

    @Override
    public void removeById(long itemId) {

    }
}
