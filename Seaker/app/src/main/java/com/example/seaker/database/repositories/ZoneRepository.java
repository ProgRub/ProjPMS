package com.example.seaker.database.repositories;

import com.example.seaker.database.DTOs.ZoneDTO;
import com.example.seaker.database.specifications.ISpecification;

public class ZoneRepository extends Repository<ZoneDTO> {
    @Override
    public void add(ZoneDTO item) {

    }

    @Override
    public Iterable<ZoneDTO> getAll() {
        return null;
    }

    @Override
    public ZoneDTO getById(long itemId) {
        return null;
    }

    @Override
    public Iterable<ZoneDTO> find(ISpecification specification) {
        return null;
    }

    @Override
    public void update(ZoneDTO oldItem, ZoneDTO newItem) {

    }

    @Override
    public void remove(ZoneDTO itemToRemove) {

    }

    @Override
    public void removeById(long itemId) {

    }
}
