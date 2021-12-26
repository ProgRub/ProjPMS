package com.example.seaker;

import android.widget.CompoundButton;
import android.widget.ToggleButton;

import java.time.LocalTime;
import java.util.ArrayList;

public class SightingInformation {

    private int sightingBoxID;
    private ArrayList<ToggleButton> nr_individuals;
    private ArrayList<ToggleButton> nr_offspring;
    private ArrayList<ToggleButton> behavior_type;
    private ArrayList<ToggleButton> reactions_to_vessel;
    private int beaufortSeaState;
    private ArrayList<ToggleButton> trustLevel;

    public SightingInformation(int sightingBoxID){
        this.sightingBoxID = sightingBoxID;
        nr_individuals = new ArrayList<>();
        nr_offspring = new ArrayList<>();
        behavior_type = new ArrayList<>();
        reactions_to_vessel = new ArrayList<>();
        trustLevel = new ArrayList<>();
    }

    public int getSightingBoxID() {
        return this.sightingBoxID;
    }

    public int getBeaufortSeaState() {
        return beaufortSeaState;
    }

    public void setBeaufortSeaState(int value) {
        this.beaufortSeaState = value;
    }

    public void add_NrIndividuals(ToggleButton nr_individual) {
        nr_individual.setOnCheckedChangeListener(nr_individuals_listener);
        this.nr_individuals.add(nr_individual);
    }

    public void add_NrOffspring(ToggleButton nr_offspring) {
        nr_offspring.setOnCheckedChangeListener(nr_offspring_listener);
        this.nr_offspring.add(nr_offspring);
    }

    public void add_TrustLevel(ToggleButton trust_level) {
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
