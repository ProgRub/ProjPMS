package com.example.seaker.business.services;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageButton;

import com.example.seaker.R;
import com.example.seaker.business.DTOs.SightingDTO;

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
