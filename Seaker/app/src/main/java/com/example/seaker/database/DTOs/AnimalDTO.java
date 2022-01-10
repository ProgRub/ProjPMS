package com.example.seaker.database.DTOs;

import android.util.Log;

import java.util.Arrays;

public class AnimalDTO {
    private long id;
    private Iterable<String> behaviours;
    private Iterable<String> reactionsToBoat;
    private String amountOfAnimals;
    private String amountOfOffspring;
    private String speciesName;
    private String confidenceLevel;

    public AnimalDTO(long id, String speciesName, String amountOfAnimals, String amountOfOffspring, Iterable<String> behaviours, Iterable<String> reactionsToBoat, String confidenceLevel) {
        this.id = id;
        this.behaviours = behaviours;
        this.reactionsToBoat = reactionsToBoat;
        this.amountOfAnimals = amountOfAnimals;
        this.amountOfOffspring = amountOfOffspring;
        this.speciesName = speciesName;
        this.confidenceLevel = confidenceLevel;
    }
    public AnimalDTO(String speciesName, String amountOfAnimals, String amountOfOffspring, String behaviours, String reactionsToBoat, String confidenceLevel) {
        this.behaviours = Arrays.asList(behaviours.split(";"));
        this.reactionsToBoat = Arrays.asList(reactionsToBoat.split(";"));
        this.amountOfAnimals = amountOfAnimals;
        this.amountOfOffspring = amountOfOffspring;
        this.speciesName = speciesName;
        this.confidenceLevel = confidenceLevel;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Iterable<String> getBehaviours() {
        return behaviours;
    }

    public void setBehaviours(Iterable<String> behaviours) {
        this.behaviours = behaviours;
    }

    public Iterable<String> getReactionsToBoat() {
        return reactionsToBoat;
    }

    public void setReactionsToBoat(Iterable<String> reactionsToBoat) {
        this.reactionsToBoat = reactionsToBoat;
    }

    public String getAmountOfAnimals() {
        return amountOfAnimals;
    }

    public void setAmountOfAnimals(String amountOfAnimals) {
        this.amountOfAnimals = amountOfAnimals;
    }

    public String getAmountOfOffspring() {
        return amountOfOffspring;
    }

    public void setAmountOfOffspring(String amountOfOffspring) {
        this.amountOfOffspring = amountOfOffspring;
    }

    public String getSpeciesName() {
        return speciesName;
    }

    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }

    public String getConfidenceLevel() {
        return confidenceLevel;
    }

    public void setConfidenceLevel(String confidenceLevel) {
        this.confidenceLevel = confidenceLevel;
    }
}
