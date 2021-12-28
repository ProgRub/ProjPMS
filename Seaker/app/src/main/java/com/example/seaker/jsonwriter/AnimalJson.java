package com.example.seaker.jsonwriter;

import java.util.ArrayList;

public class AnimalJson {
    private String speciesName;
    private int numberIndividuals;
    private int numberOffspring;
    private ArrayList<String> behaviours;
    private ArrayList<String> reactionsToVessel;

    public AnimalJson(String speciesName, int numberIndividuals, int numberOffspring, ArrayList<String> behaviours, ArrayList<String> reactionsToVessel) {
        this.speciesName = speciesName;
        this.numberIndividuals = numberIndividuals;
        this.numberOffspring = numberOffspring;
        this.behaviours = behaviours;
        this.reactionsToVessel = reactionsToVessel;
    }

    public String getSpeciesName() {
        return speciesName;
    }

    public int getNumberIndividuals() {
        return numberIndividuals;
    }

    public int getNumberOffspring() {
        return numberOffspring;
    }

    public ArrayList<String> getBehaviours() {
        return behaviours;
    }

    public ArrayList<String> getReactionsToVessel() {
        return reactionsToVessel;
    }
}
