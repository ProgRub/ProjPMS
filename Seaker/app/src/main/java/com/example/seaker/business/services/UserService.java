package com.example.seaker.business.services;

public class UserService {
    private static UserService instance=null;

    public UserService() {
    }

    public static UserService getInstance(){
        if(instance==null) instance=new UserService();
        return instance;
    }
}
