package com.example.seaker.jsonwriter;

import java.util.ArrayList;

public class SightingJson {
    private String date;
    private String time;
    private String latitude;
    private String longitude;
    private ArrayList<AnimalJson> animals;
    private String seaState;
    private String boatName;
    private String comment;
    private String reporterName;


    public SightingJson(String date, String time, String latitude, String longitude, ArrayList<AnimalJson> animals, String seaState, String boatName, String comment, String reporterName) {
        this.date = date;
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
        this.animals = animals;
        this.seaState = seaState;
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

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public ArrayList<AnimalJson> getAnimals() {
        return animals;
    }

    public String getSeaState() {
        return seaState;
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
