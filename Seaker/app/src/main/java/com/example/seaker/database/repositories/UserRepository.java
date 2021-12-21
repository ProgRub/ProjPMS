package com.example.seaker.database.repositories;

import com.example.seaker.database.entities.User;
import com.example.seaker.database.specifications.ISpecification;

public class UserRepository implements IRepository<User>{
    @Override
    public void add(User item) {

    }

    @Override
    public Iterable<User> getAll() {
        return null;
    }

    @Override
    public User getById(long itemId) {
        return null;
    }

    @Override
    public Iterable<User> find(ISpecification specification) {
        return null;
    }

    @Override
    public void update(User oldItem, User newItem) {

    }

    @Override
    public void remove(User itemToRemove) {

    }

    @Override
    public void removeById(long itemId) {

    }
}
