package com.example.seaker.business.DTOs;

public class AnimalDTO {
    private long id;
    private Iterable<String> behaviours;
    private Iterable<String> reactionsToBoat;
    private int amountOfAnimals;
    private int amountOfOffspring;
    private String speciesName;

    public AnimalDTO(long id, Iterable<String> behaviours, Iterable<String> reactionsToBoat, int amountOfAnimals, int amountOfOffspring, String speciesName) {
        this.id = id;
        this.behaviours = behaviours;
        this.reactionsToBoat = reactionsToBoat;
        this.amountOfAnimals = amountOfAnimals;
        this.amountOfOffspring = amountOfOffspring;
        this.speciesName = speciesName;
    }
    public AnimalDTO(Iterable<String> behaviours, Iterable<String> reactionsToBoat, int amountOfAnimals, int amountOfOffspring, String speciesName) {
        this.behaviours = behaviours;
        this.reactionsToBoat = reactionsToBoat;
        this.amountOfAnimals = amountOfAnimals;
        this.amountOfOffspring = amountOfOffspring;
        this.speciesName = speciesName;
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

    public int getAmountOfAnimals() {
        return amountOfAnimals;
    }

    public void setAmountOfAnimals(int amountOfAnimals) {
        this.amountOfAnimals = amountOfAnimals;
    }

    public int getAmountOfOffspring() {
        return amountOfOffspring;
    }

    public void setAmountOfOffspring(int amountOfOffspring) {
        this.amountOfOffspring = amountOfOffspring;
    }

    public String getSpeciesName() {
        return speciesName;
    }

    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }
}
