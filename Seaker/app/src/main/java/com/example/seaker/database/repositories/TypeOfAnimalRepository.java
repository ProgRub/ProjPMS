package com.example.seaker.database.repositories;

import com.example.seaker.database.entities.TypeOfAnimal;
import com.example.seaker.database.specifications.ISpecification;

public class TypeOfAnimalRepository implements IRepository<TypeOfAnimal> {
    @Override
    public void add(TypeOfAnimal item) {

    }

    @Override
    public Iterable<TypeOfAnimal> getAll() {
        return null;
    }

    @Override
    public TypeOfAnimal getById(long itemId) {
        return null;
    }

    @Override
    public Iterable<TypeOfAnimal> find(ISpecification specification) {
        return null;
    }

    @Override
    public void update(TypeOfAnimal oldItem, TypeOfAnimal newItem) {

    }

    @Override
    public void remove(TypeOfAnimal itemToRemove) {

    }

    @Override
    public void removeById(long itemId) {

    }
}
