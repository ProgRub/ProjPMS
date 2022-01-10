package com.example.seaker.database.DTOs;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class SightingDTO {
    private long id;
    private boolean submitted;
    private LocalDate day;
    private LocalTime time;
    private int seaStateBeaufort;
    private double longitude;
    private double latitude;
    private String comments;

    private long teamMemberId;
    private Iterable<Long> photoIds;
    private long boatId;
    private ArrayList<AnimalDTO> sightedAnimals;

    public SightingDTO(long id, boolean submitted, LocalDate day, LocalTime time, int seaStateBeaufort, double longitude, double latitude, String comments, long teamMemberId,  long boatId) {
        this.id = id;
        this.submitted = submitted;
        this.day = day;
        this.time = time;
        this.seaStateBeaufort = seaStateBeaufort;
        this.longitude = longitude;
        this.latitude = latitude;
        this.comments = comments;
        this.teamMemberId = teamMemberId;
        this.boatId = boatId;
        this.sightedAnimals=new ArrayList<>();
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public long getTeamMemberId() {
        return teamMemberId;
    }

    public void setTeamMemberId(long teamMemberId) {
        this.teamMemberId = teamMemberId;
    }

    public Iterable<Long> getPhotoIds() {
        return photoIds;
    }

    public void setPhotoIds(Iterable<Long> photoIds) {
        this.photoIds = photoIds;
    }

    public long getBoatId() {
        return boatId;
    }

    public void setBoatId(long boatId) {
        this.boatId = boatId;
    }

    public boolean isSubmitted() {
        return submitted;
    }

    public void setSubmitted(boolean submitted) {
        this.submitted = submitted;
    }

    public Iterable<AnimalDTO> getSightedAnimals(){return sightedAnimals;}
    public void addSightedAnimal(AnimalDTO animal){sightedAnimals.add(animal);}
}
