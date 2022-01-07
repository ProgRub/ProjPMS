package com.example.seaker.database.repositories;

import com.example.seaker.database.DTOs.TypeOfAnimalDTO;
import com.example.seaker.database.specifications.ISpecification;

public class TypeOfAnimalRepository extends Repository<TypeOfAnimalDTO> {
    @Override
    public void add(TypeOfAnimalDTO item) {

    }

    @Override
    public Iterable<TypeOfAnimalDTO> getAll() {
        return null;
    }

    @Override
    public TypeOfAnimalDTO getById(long itemId) {
        return null;
    }

    @Override
    public Iterable<TypeOfAnimalDTO> find(ISpecification specification) {
        return null;
    }

    @Override
    public void update(TypeOfAnimalDTO oldItem, TypeOfAnimalDTO newItem) {

    }

    @Override
    public void remove(TypeOfAnimalDTO itemToRemove) {

    }

    @Override
    public void removeById(long itemId) {

    }
}
