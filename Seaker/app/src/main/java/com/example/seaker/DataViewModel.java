package com.example.seaker;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.seaker.database.entities.User;

import java.time.LocalDateTime;
import java.util.List;

public class DataViewModel extends ViewModel {
    private Integer reportedSightingId;
    private String date;
    private String time;
    private String species;

    public void setReportedSighingId(Integer value) {
        reportedSightingId = value;
    }

    public Integer getReportedSighingId() {
        return reportedSightingId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public String getSpecies() {
        return species;
    }

    public String getTime() {
        return time;
    }
}
