package com.example.seaker.database.repositories;

import com.example.seaker.database.DTOs.PhotoDTO;
import com.example.seaker.database.specifications.ISpecification;

public class PhotoRepository extends Repository<PhotoDTO> {
    @Override
    public void add(PhotoDTO item) {

    }

    @Override
    public Iterable<PhotoDTO> getAll() {
        return null;
    }

    @Override
    public PhotoDTO getById(long itemId) {
        return null;
    }

    @Override
    public Iterable<PhotoDTO> find(ISpecification specification) {
        return null;
    }

    @Override
    public void update(PhotoDTO oldItem, PhotoDTO newItem) {

    }

    @Override
    public void remove(PhotoDTO itemToRemove) {

    }

    @Override
    public void removeById(long itemId) {

    }
}
