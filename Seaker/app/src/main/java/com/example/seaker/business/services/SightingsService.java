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
    private BoatDTO currentBoat;
    private ZoneDTO startingZone,endingZone;

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

    public ZoneDTO getEndingZone() {
        return endingZone;
    }

    public void setEndingZone(String endingZone) {
        for (ZoneDTO zone:getZonesTo() ) {
            if(zone.getName().equals(endingZone)) {
                this.endingZone = zone;
                return;
            }
        }
    }

    public ZoneDTO getStartingZone() {
        return startingZone;
    }

    public void setStartingZone(String startingZone) {
        for (ZoneDTO zone:getZonesFrom() ) {
            if(zone.getName().equals(startingZone)) {
                this.startingZone = zone;
                return;
            }
        }
    }

    public BoatDTO getCurrentBoat() {
        return currentBoat;
    }

    public void setCurrentBoat(long id) {
        for (BoatDTO boat:getAllBoats()) {
            if(boat.getId()==id){
                this.currentBoat = boat;
                return;
            }
        }
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
