package com.example.seaker.database.repositories;

import com.example.seaker.database.DTOs.UserDTO;
import com.example.seaker.database.specifications.ISpecification;

public class UserRepository implements IRepository<UserDTO>{
    @Override
    public void add(UserDTO item) {

    }

    @Override
    public Iterable<UserDTO> getAll() {
        return null;
    }

    @Override
    public UserDTO getById(long itemId) {
        return null;
    }

    @Override
    public Iterable<UserDTO> find(ISpecification specification) {
        return null;
    }

    @Override
    public void update(UserDTO oldItem, UserDTO newItem) {

    }

    @Override
    public void remove(UserDTO itemToRemove) {

    }

    @Override
    public void removeById(long itemId) {

    }
}
