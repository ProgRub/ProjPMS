package com.example.seaker.database.repositories;

import com.example.seaker.database.entities.Animal;
import com.example.seaker.database.specifications.ISpecification;

public class AnimalRepository implements IRepository<Animal> {
    @Override
    public void add(Animal item) {

    }

    @Override
    public Iterable<Animal> getAll() {
        return null;
    }

    @Override
    public Animal getById(long itemId) {
        return null;
    }

    @Override
    public Iterable<Animal> find(ISpecification specification) {
        return null;
    }

    @Override
    public void update(Animal oldItem, Animal newItem) {

    }

    @Override
    public void remove(Animal itemToRemove) {

    }

    @Override
    public void removeById(long itemId) {

    }
}
