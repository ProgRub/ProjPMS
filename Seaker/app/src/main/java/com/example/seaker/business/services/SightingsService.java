package com.example.seaker.business.services;

import com.example.seaker.database.DTOs.BoatDTO;
import com.example.seaker.database.DTOs.SightingDTO;
import com.example.seaker.database.DTOs.ZoneDTO;
import com.example.seaker.database.repositories.BoatRepository;
import com.example.seaker.database.repositories.SightingRepository;
import com.example.seaker.database.repositories.UserRepository;
import com.example.seaker.database.repositories.ZoneRepository;

import java.util.ArrayList;

public class SightingsService {
    private static SightingsService instance = null;
    private BoatRepository boatRepository;
    private ZoneRepository zoneRepository;
    private SightingRepository sightingRepository;
    private long currentBoatId;
    private String startingZone,endingZone;

    public SightingsService() {
        boatRepository=new BoatRepository();
        zoneRepository=new ZoneRepository();
        sightingRepository=new SightingRepository();
    }


    public static SightingsService getInstance() {
        if (instance == null) instance = new SightingsService();
        return instance;
    }

    public void addNewSighting(SightingDTO sighting){
        sightingRepository.add(sighting,startingZone,endingZone);
    }

    public String getEndingZone() {
        return endingZone;
    }

    public void setEndingZone(String endingZone) {
                this.endingZone = endingZone;

    }

    public String getStartingZone() {
        return startingZone;
    }

    public void setStartingZone(String startingZone) {
                this.startingZone = startingZone;
    }

    public long getCurrentBoatId() {
        return currentBoatId;
    }

        public void setCurrentBoat(long id) {
                this.currentBoatId = id;
    }

    public Iterable<BoatDTO> getAllBoats(){return boatRepository.getAll();}
    public Iterable<ZoneDTO> getZonesFrom(){
        ArrayList<ZoneDTO> zonesFrom=new ArrayList<>();
        for (ZoneDTO zone:zoneRepository.getAll()) {
            if(zone.getFromOrTo().equals("From")) zonesFrom.add(zone);
        }
        return zonesFrom;
    }
    public Iterable<ZoneDTO> getZonesTo(){
        ArrayList<ZoneDTO> zonesTo=new ArrayList<>();
        for (ZoneDTO zone:zoneRepository.getAll()) {
            if(zone.getFromOrTo().equals("To")) zonesTo.add(zone);
        }
        return zonesTo;
    }

    public Iterable<SightingDTO> getAllSightings(){return sightingRepository.getAll();}

    public void editSighting(SightingDTO sightingToEdit) {
        sightingRepository.editSighting(sightingToEdit);
    }

    public void deleteSighting(SightingDTO sightingToDelete){
        sightingRepository.removeById(sightingToDelete.getId());
    }
}
