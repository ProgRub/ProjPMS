package com.example.seaker.jsonwriter;

import java.util.ArrayList;

public class AnimalJson {
    private String speciesName;
    private String numberIndividuals;
    private String numberOffspring;
    private ArrayList<String> behaviours;
    private ArrayList<String> reactionsToVessel;
    private String trustLevel;

    public AnimalJson(String speciesName, String numberIndividuals, String numberOffspring, ArrayList<String> behaviours, ArrayList<String> reactionsToVessel, String trustLevel) {
        this.speciesName = speciesName;
        this.numberIndividuals = numberIndividuals;
        this.numberOffspring = numberOffspring;
        this.behaviours = behaviours;
        this.reactionsToVessel = reactionsToVessel;
        this.trustLevel = trustLevel;
    }

    public String getSpeciesName() {
        return speciesName;
    }

    public String getNumberIndividuals() {
        return numberIndividuals;
    }

    public String getNumberOffspring() {
        return numberOffspring;
    }

    public ArrayList<String> getBehaviours() {
        return behaviours;
    }

    public ArrayList<String> getReactionsToVessel() {
        return reactionsToVessel;
    }

    public String getTrustLevel() {
        return trustLevel;
    }

}
