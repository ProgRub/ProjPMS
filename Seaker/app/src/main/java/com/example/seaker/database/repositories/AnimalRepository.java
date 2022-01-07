package com.example.seaker.database.repositories;

import com.example.seaker.database.DTOs.AnimalDTO;
import com.example.seaker.database.specifications.ISpecification;

public class AnimalRepository implements IRepository<AnimalDTO> {
    @Override
    public void add(AnimalDTO item) {

    }

    @Override
    public Iterable<AnimalDTO> getAll() {
        return null;
    }

    @Override
    public AnimalDTO getById(long itemId) {
        return null;
    }

    @Override
    public Iterable<AnimalDTO> find(ISpecification specification) {
        return null;
    }

    @Override
    public void update(AnimalDTO oldItem, AnimalDTO newItem) {

    }

    @Override
    public void remove(AnimalDTO itemToRemove) {

    }

    @Override
    public void removeById(long itemId) {

    }
}
