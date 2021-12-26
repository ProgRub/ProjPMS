package com.example.seaker;

import android.app.AlertDialog;
import android.content.Intent;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.seaker.fragments.ReportSightingFragment;

import java.time.LocalTime;
import java.util.ArrayList;

public class SightingInformation {

    private int sightingBoxID;
    private ArrayList<ToggleButton> nr_individuals;
    private ArrayList<ToggleButton> nr_offspring;
    private ArrayList<ToggleButton> behavior_type;
    private ArrayList<ToggleButton> reactions_to_vessel;
    private SeekBar beaufortSeaState;
    private ArrayList<ToggleButton> trustLevel;

    public SightingInformation(int sightingBoxID){
        this.sightingBoxID = sightingBoxID;
        nr_individuals = new ArrayList<>();
        nr_offspring = new ArrayList<>();
        behavior_type = new ArrayList<>();
        reactions_to_vessel = new ArrayList<>();
        trustLevel = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "SightingInformation {" +
                "sightingBoxID=" + sightingBoxID +
                ", nr_individuals=" + getNumberOfIndividualsString() +
                ", nr_offspring=" + getNumberOfOffspringString() +
                ", behavior_type=" + getBehaviorTypesString() +
                ", reactions_to_vessel=" + getReactionToVesselString() +
                ", beaufortSeaState=" + getBeaufortSeaState() +
                ", trustLevel=" + getTrustLevelString() +'}';
    }

    public int getSightingBoxID() {
        return this.sightingBoxID;
    }

    public int getBeaufortSeaState() {
        return beaufortSeaState.getProgress();
    }

    public void setBeaufortSeaState(SeekBar beaufortSeaState) {
        this.beaufortSeaState = beaufortSeaState;
    }

    public void setBehavior_type(ArrayList<ToggleButton> behavior_type) {
        this.behavior_type = behavior_type;
    }

    public void setReactions_to_vessel(ArrayList<ToggleButton> reactions_to_vessel) {
        this.reactions_to_vessel = reactions_to_vessel;
    }

    public void addNrindividuals(ToggleButton nr_individual) {
        nr_individual.setOnCheckedChangeListener(nr_individuals_listener);
        this.nr_individuals.add(nr_individual);
    }

    public void addNroffspring(ToggleButton nr_offspring) {
        nr_offspring.setOnCheckedChangeListener(nr_offspring_listener);
        this.nr_offspring.add(nr_offspring);
    }

    public void addTrustLevel(ToggleButton trust_level) {
        trust_level.setOnCheckedChangeListener(trust_level_listener);
        this.trustLevel.add(trust_level);
    }

    private String getNumberOfIndividualsString(){
        for(ToggleButton toggleButton : nr_individuals){
            if (toggleButton.isChecked()) return toggleButton.getTextOff().toString();
        }
        return "ERROR"; //DEVE APRESENTAR UM ERRO - CAMPO OBRIGATORIO
    }

    private String getNumberOfOffspringString(){
        for(ToggleButton toggleButton : nr_offspring){
            if (toggleButton.isChecked()) return toggleButton.getTextOff().toString();
        }
        return "Not specified";
    }

    private String getBehaviorTypesString(){
        String info = "";
        for(ToggleButton toggleButton : behavior_type){
            if (toggleButton.isChecked()) info += toggleButton.getTextOff().toString() + ";";
        }
        return info;
    }

    private String getReactionToVesselString(){
        String info = "";
        for(ToggleButton toggleButton : reactions_to_vessel){
            if (toggleButton.isChecked()) info += toggleButton.getTextOff().toString() + ";";
        }
        return info;
    }

    private String getTrustLevelString(){
        for(ToggleButton toggleButton : trustLevel){
            if (toggleButton.isChecked()) return toggleButton.getTextOff().toString();
        }
        return "Not specified";
    }



    CompoundButton.OnCheckedChangeListener trust_level_listener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                for(ToggleButton tog : trustLevel){
                    if(buttonView != tog) {
                        tog.setChecked(false);
                    }
                }
            }
        }
    };

    CompoundButton.OnCheckedChangeListener nr_individuals_listener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                for(ToggleButton tog : nr_individuals){
                    if(buttonView != tog) {
                        tog.setChecked(false);
                    }
                }
            }
        }
    };

    CompoundButton.OnCheckedChangeListener nr_offspring_listener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                for(ToggleButton tog : nr_offspring){
                    if(buttonView != tog) {
                        tog.setChecked(false);
                    }
                }
            }
        }
    };

}
