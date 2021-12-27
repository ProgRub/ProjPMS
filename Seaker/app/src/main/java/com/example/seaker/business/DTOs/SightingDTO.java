package com.example.seaker.business.DTOs;

import java.time.LocalDate;
import java.time.LocalTime;

public class SightingDTO {
    private long id;
    private LocalDate day;
    private LocalTime time;
    private int seaStateBeaufort;
    private double longitude;
    private double latitude;
    private String confidenceLevel;
    private String comments;

    private String teamMemberName;
    private Iterable<Long> photoIds;
    private String boatName;
    private Iterable<String> zoneNames;
    private Iterable<String> animalSpeciesNames;

    public SightingDTO(long id, LocalDate day, LocalTime time, int seaStateBeaufort, double longitude, double latitude, String confidenceLevel, String comments, String teamMemberName, Iterable<Long> photoIds, String boatName, Iterable<String> zoneNames, Iterable<String> animalSpeciesNames) {
        this.id = id;
        this.day = day;
        this.time = time;
        this.seaStateBeaufort = seaStateBeaufort;
        this.longitude = longitude;
        this.latitude = latitude;
        this.confidenceLevel = confidenceLevel;
        this.comments = comments;
        this.teamMemberName = teamMemberName;
        this.photoIds = photoIds;
        this.boatName = boatName;
        this.zoneNames = zoneNames;
        this.animalSpeciesNames = animalSpeciesNames;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public int getSeaStateBeaufort() {
        return seaStateBeaufort;
    }

    public void setSeaStateBeaufort(int seaStateBeaufort) {
        this.seaStateBeaufort = seaStateBeaufort;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getConfidenceLevel() {
        return confidenceLevel;
    }

    public void setConfidenceLevel(String confidenceLevel) {
        this.confidenceLevel = confidenceLevel;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getTeamMemberName() {
        return teamMemberName;
    }

    public void setTeamMemberName(String teamMemberName) {
        this.teamMemberName = teamMemberName;
    }

    public Iterable<Long> getPhotoIds() {
        return photoIds;
    }

    public void setPhotoIds(Iterable<Long> photoIds) {
        this.photoIds = photoIds;
    }

    public String getBoatName() {
        return boatName;
    }

    public void setBoatName(String boatName) {
        this.boatName = boatName;
    }

    public Iterable<String> getZoneNames() {
        return zoneNames;
    }

    public void setZoneNames(Iterable<String> zoneNames) {
        this.zoneNames = zoneNames;
    }

    public Iterable<String> getAnimalSpeciesNames() {
        return animalSpeciesNames;
    }

    public void setAnimalSpeciesNames(Iterable<String> animalSpeciesNames) {
        this.animalSpeciesNames = animalSpeciesNames;
    }
}
