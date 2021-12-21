package com.example.seaker.database.entities;

import java.time.LocalDate;
import java.time.LocalTime;

public class Sighting {
    private long id;
    private LocalDate day;
    private LocalTime time;
    private int seaStateBeaufort;
    private double longitude;
    private double latitude;
    private ConfidenceLevel confidenceLevel;
    private Iterable<String> comments;

    private long teamMemberId;
    private Iterable<Long> photoIds;
    private long boatId;
    private Iterable<Long> zoneIds;
    private Iterable<Long> animalIds;

    public Sighting(long id, LocalDate day, LocalTime time, int seaStateBeaufort, double longitude, double latitude, ConfidenceLevel confidenceLevel, Iterable<String> comments, long teamMemberId, Iterable<Long> photoIds, long boatId, Iterable<Long> zoneIds, Iterable<Long> animalIds) {
        this.id = id;
        this.day = day;
        this.time = time;
        this.seaStateBeaufort = seaStateBeaufort;
        this.longitude = longitude;
        this.latitude = latitude;
        this.confidenceLevel = confidenceLevel;
        this.comments = comments;
        this.teamMemberId = teamMemberId;
        this.photoIds = photoIds;
        this.boatId = boatId;
        this.zoneIds = zoneIds;
        this.animalIds = animalIds;
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

    public ConfidenceLevel getConfidenceLevel() {
        return confidenceLevel;
    }

    public void setConfidenceLevel(ConfidenceLevel confidenceLevel) {
        this.confidenceLevel = confidenceLevel;
    }

    public Iterable<String> getComments() {
        return comments;
    }

    public void setComments(Iterable<String> comments) {
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

    public Iterable<Long> getZoneIds() {
        return zoneIds;
    }

    public void setZoneIds(Iterable<Long> zoneIds) {
        this.zoneIds = zoneIds;
    }

    public Iterable<Long> getAnimalIds() {
        return animalIds;
    }

    public void setAnimalIds(Iterable<Long> animalIds) {
        this.animalIds = animalIds;
    }
}
