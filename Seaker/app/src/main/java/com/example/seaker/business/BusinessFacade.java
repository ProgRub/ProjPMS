package com.example.seaker.business;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.seaker.database.DTOs.*;
import com.example.seaker.business.services.ReportService;
import com.example.seaker.business.services.SightingsService;
import com.example.seaker.business.services.UserService;
import com.example.seaker.database.repositories.Repository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

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

    public void selectRole(String role) {
        UserService.getInstance().setSelectedRole(role);
    }

    public String getSelectedRole() {
        return UserService.getInstance().getSelectedRole();
    }

    public UserDTO getUserByID(long id) {
        return UserService.getInstance().getUserByID(id);
    }

    public void setCurrentBoat(long boatID) {
        SightingsService.getInstance().setCurrentBoat(boatID);
    }

    public Iterable<BoatDTO> getAllBoats() {
        return SightingsService.getInstance().getAllBoats();
    }

    public Iterable<ZoneDTO> getAllZonesFrom() {
        return SightingsService.getInstance().getZonesFrom();
    }

    public Iterable<ZoneDTO> getAllZonesTo() {
        return SightingsService.getInstance().getZonesTo();
    }

    public void setZoneFrom(String zoneFrom) {
        SightingsService.getInstance().setStartingZone(zoneFrom);
    }

    public void setZoneTo(String zoneTo) {
        SightingsService.getInstance().setEndingZone(zoneTo);
    }

    public BoatDTO getCurrentBoat() {
        return SightingsService.getInstance().getCurrentBoat();
    }

    public ZoneDTO getStartingZone() {
        return SightingsService.getInstance().getStartingZone();
    }

    public ZoneDTO getEndingZone() {
        return SightingsService.getInstance().getEndingZone();
    }

    public UserDTO getLoggedInUser() {
        return UserService.getInstance().getLoggedInUser();
    }

    public Iterable<SightingDTO> getAllSightings(){return SightingsService.getInstance().getAllSightings();}

    //    public void saveArrayListToSD(Context mContext, String filename, ArrayList<ArrayList<String>> list){
//        UserService.getInstance().saveArrayListToSD(mContext,filename,list);
//    }
    public boolean isInternetWorking() { //VERIFICA CONEXÃO COM A BD
        return Repository.isInternetWorking();
    }

    public void setSelectedRole(String selectedRole) {
        UserService.getInstance().setSelectedRole(selectedRole);
    }
}

