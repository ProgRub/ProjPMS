package com.example.seaker.business;

import com.example.seaker.business.DTOs.SightingDTO;
import com.example.seaker.business.services.ReportService;
import com.example.seaker.business.services.SightingsService;
import com.example.seaker.business.services.UserService;

public class BusinessFacade {
    private static BusinessFacade instance=null;

    private SightingsService sightingsService;
    private ReportService reportService;
    private UserService userService;

    public void BusinessFacade(){
        sightingsService=SightingsService.getInstance();
        reportService=ReportService.getInstance();
        userService=UserService.getInstance();
    }

    public static BusinessFacade getInstance(){
        if(instance==null) instance=new BusinessFacade();
        return instance;
    }

    public void addSighting(SightingDTO sighting){
        sightingsService.addNewSighting(sighting);
    }
}
