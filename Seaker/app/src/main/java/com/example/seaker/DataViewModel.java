package com.example.seaker;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class DataViewModel extends ViewModel {
    private String reportedSightingId;
    private String date;
    private String time;
    private int sea_state;
    private String latitude;
    private String longitude;
    private String comment;

    private ArrayList<String> species_name;
    private ArrayList<String> n_individuals;
    private ArrayList<String> n_offspring;
    private ArrayList<String> trust_level;
    private ArrayList<String> behaviors;
    private ArrayList<String> reactions;

    private String vesselID;
    private String tripFrom;
    private String tripTo;

    public void setReportedSighingId(String value) {
        reportedSightingId = value;
    }

    public String getReportedSighingId() {
        return reportedSightingId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setSpecies(ArrayList<String> species) {
        this.species_name = species;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public ArrayList<String> getSpecies() {
        return species_name;
    }

    public String getTime() {
        return time;
    }

    public void setSea_state(int value) {
        sea_state = value;
    }

    public int getSea_state() {
        return sea_state;
    }

    public void setLatitude(String value) {
        latitude = value;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLongitude(String value) {
        longitude = value;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setComment(String value) {
        comment = value;
    }

    public String getComment() {
        return comment;
    }

    public ArrayList<String> getN_individuals() {
        return n_individuals;
    }

    public void setN_individuals(ArrayList<String> arraylist) {
        this.n_individuals = arraylist;
    }

    public ArrayList<String> getN_offspring() { return n_offspring; }

    public void setN_offspring(ArrayList<String> arraylist) {
        this.n_offspring = arraylist;
    }

    public ArrayList<String> getTrust_level() { return trust_level; }

    public void setTrust_level(ArrayList<String> arraylist) {
        this.trust_level = arraylist;
    }

    public ArrayList<String> getBehaviors() { return behaviors; }

    public void setBehaviors(ArrayList<String> arraylist) {
        this.behaviors = arraylist;
    }

    public ArrayList<String> getReactions() { return reactions; }

    public void setReactions(ArrayList<String> arraylist) {
        this.reactions = arraylist;
    }

    public String getVesselID() {
        return vesselID;
    }

    public void setVesselID(String vesselID) {
        this.vesselID = vesselID;
    }

    public String getTripFrom() {
        return tripFrom;
    }

    public void setTripFrom(String tripFrom) {
        this.tripFrom = tripFrom;
    }

    public String getTripTo() {
        return tripTo;
    }

    public void setTripTo(String tripTo) {
        this.tripTo = tripTo;
    }
}
