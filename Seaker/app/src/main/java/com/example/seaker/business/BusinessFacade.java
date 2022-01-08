package com.example.seaker.business;

import android.util.Log;

import com.example.seaker.database.DTOs.*;
import com.example.seaker.business.services.ReportService;
import com.example.seaker.business.services.SightingsService;
import com.example.seaker.business.services.UserService;

public class BusinessFacade {
    private static BusinessFacade instance = null;

    public void BusinessFacade() {
    }

    public static BusinessFacade getInstance() {
        if (instance == null) instance = new BusinessFacade();
        return instance;
    }

    public void addSighting(SightingDTO sighting) {
        SightingsService.getInstance().addNewSighting(sighting);
    }

    public void addUser(UserDTO user) {
        UserService.getInstance().addUser(user);
    }

    public ErrorType userIsValid(UserDTO user) {
        return UserService.getInstance().validateFields(user);
    }

    public Iterable<UserDTO> getAllTeamMembers() {
        return UserService.getInstance().getTeamMembers();
    }

    public Iterable<UserDTO> getAllCompanyManagers() {
        return UserService.getInstance().getCompanyManagers();
    }

    public Iterable<UserDTO> getAllUsers() {
        return UserService.getInstance().getUsers();
    }

    public void deleteUser(UserDTO userDTO) {
        UserService.getInstance().deleteUser(userDTO.getId());

    }

    public ErrorType loginIsValid(UserDTO loginCredentials) {
        return UserService.getInstance().verifyLogin(loginCredentials);
    }

    public void selectRole(String role){
        UserService.getInstance().setSelectedRole(role);
    }

    public String getSelectedRole(){
        return UserService.getInstance().getSelectedRole();
    }

    public UserDTO getUserByID(long id){return UserService.getInstance().getUserByID(id);}

    public void setLoggedInUser(UserDTO loginCredentials) {UserService.getInstance().setLoggedInUser(loginCredentials); }

}

