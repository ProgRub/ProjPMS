package com.example.seaker.jsonwriter;

import java.util.ArrayList;

public class SightingJson {
    private String date;
    private String time;
    private double latitude;
    private double longitude;
    private ArrayList<AnimalJson> animals;
    private int seaState;
    private String confidenceLevel;
    private String boatName;
    private String comment;
    private String reporterName;


    public SightingJson(String date, String time, double latitude, double longitude, ArrayList<AnimalJson> animals, int seaState, String confidenceLevel, String boatName, String comment, String reporterName) {
        this.date = date;
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
        this.animals = animals;
        this.seaState = seaState;
        this.confidenceLevel = confidenceLevel;
        this.boatName = boatName;
        this.comment = comment;
        this.reporterName = reporterName;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public ArrayList<AnimalJson> getAnimals() {
        return animals;
    }

    public int getSeaState() {
        return seaState;
    }

    public String getConfidenceLevel() {
        return confidenceLevel;
    }

    public String getBoatName() {
        return boatName;
    }

    public String getComment() {
        return comment;
    }

    public String getReporterName() {
        return reporterName;
    }

}
