package com.example.seaker.business.services;

import com.example.seaker.database.DTOs.SightingDTO;

public class SightingsService {
    private static SightingsService instance = null;

    public SightingsService() {

    }


    public static SightingsService getInstance() {
        if (instance == null) instance = new SightingsService();
        return instance;
    }

    public void addNewSighting(SightingDTO sighting){

    }
}
