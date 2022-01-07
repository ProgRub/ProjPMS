package com.example.seaker.database.repositories;

import com.example.seaker.database.DTOs.SpeciesDTO;
import com.example.seaker.database.specifications.ISpecification;

public class SpeciesRepository implements IRepository<SpeciesDTO> {
    @Override
    public void add(SpeciesDTO item) {

    }

    @Override
    public Iterable<SpeciesDTO> getAll() {
        return null;
    }

    @Override
    public SpeciesDTO getById(long itemId) {
        return null;
    }

    @Override
    public Iterable<SpeciesDTO> find(ISpecification specification) {
        return null;
    }

    @Override
    public void update(SpeciesDTO oldItem, SpeciesDTO newItem) {

    }

    @Override
    public void remove(SpeciesDTO itemToRemove) {

    }

    @Override
    public void removeById(long itemId) {

    }
}
