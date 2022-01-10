package com.example.seaker;

import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.seaker.fragments.ReportSightingFragment;

import java.time.LocalTime;
import java.util.ArrayList;

public class SightingInformation {

    private int sightingBoxID;
    private String specieName;
    private ArrayList<ToggleButton> nr_individuals;
    private ArrayList<ToggleButton> nr_offspring;
    private ArrayList<ToggleButton> behavior_type;
    private ArrayList<ToggleButton> reactions_to_vessel;
    private ArrayList<ToggleButton> trustLevel;
    private EditText otherBehavior;
    private EditText otherReaction;

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
        return getSpecieName() + "*" +
                getNumberOfIndividualsString() + "*" +
                getNumberOfOffspringString() + "*" +
                getTrustLevelString() + "*" +
                getBehaviorTypesString() + "*" +
                getReactionToVesselString() + "$"
                ;
    }

    public void setSpecieName(String specieName) {
        this.specieName = specieName;
    }

    public String getSpecieName() {
        return specieName;
    }

    public int getSightingBoxID() {
        return this.sightingBoxID;
    }

    public void setBehavior_type(ArrayList<ToggleButton> behavior_type) {
        this.behavior_type = behavior_type;
    }

    public void fillNrIndividuals(String nr_individuals){
        switch (nr_individuals){
            case "1":
                this.nr_individuals.get(0).setChecked(true);
                break;
            case "2":
                this.nr_individuals.get(1).setChecked(true);
                break;
            case "3":
                this.nr_individuals.get(2).setChecked(true);
                break;
            case "4":
                this.nr_individuals.get(3).setChecked(true);
                break;
            case "5":
                this.nr_individuals.get(4).setChecked(true);
                break;
            case "6-10":
                this.nr_individuals.get(5).setChecked(true);
                break;
            case "10-20":
                this.nr_individuals.get(6).setChecked(true);
                break;
            case "25-50":
                this.nr_individuals.get(7).setChecked(true);
                break;
            case "50-100":
                this.nr_individuals.get(8).setChecked(true);
                break;
            case "+100":
                this.nr_individuals.get(9).setChecked(true);
                break;
        }
    }

    public void fillNrOffspring(String nr_offspring){
        switch (nr_offspring){
            case "0":
                this.nr_offspring.get(0).setChecked(true);
                break;
            case "1":
                this.nr_offspring.get(1).setChecked(true);
                break;
            case "2":
                this.nr_offspring.get(2).setChecked(true);
                break;
            case "3":
                this.nr_offspring.get(3).setChecked(true);
                break;
            case "4":
                this.nr_offspring.get(4).setChecked(true);
                break;
            case "5":
                this.nr_offspring.get(5).setChecked(true);
                break;
            case "6-10":
                this.nr_offspring.get(6).setChecked(true);
                break;
            case "10-20":
                this.nr_offspring.get(7).setChecked(true);
                break;
            case "25-50":
                this.nr_offspring.get(8).setChecked(true);
                break;
            case "50-100":
                this.nr_offspring.get(9).setChecked(true);
                break;
            case "+100":
                this.nr_offspring.get(10).setChecked(true);
                break;
        }
    }
    public void fillBehaviourType(Iterable<String> behaviours) {
        for (String behaviourNotTrimmed:behaviours) {
            String behaviour=behaviourNotTrimmed.trim();
            if(behaviour.equals("Traveling")) this.behavior_type.get(0).setChecked(true);
            else if(behaviour.equals("Eating")) this.behavior_type.get(1).setChecked(true);
            else if(behaviour.equals("Resting")) this.behavior_type.get(2).setChecked(true);
            else if(behaviour.equals("Social Interaction")) this.behavior_type.get(3).setChecked(true);
            else if(behaviour.equals(""));
            else {
                this.behavior_type.get(4).setChecked(true);
                this.otherBehavior.setText(behaviour);
            }
        }
    }

    public boolean allOtherFieldsFilled(){
        if(behavior_type.get(4).isChecked()){
            if(otherBehavior.getText().toString().isEmpty()) return false;
        }

        if(reactions_to_vessel.get(3).isChecked()){
            if(otherReaction.getText().toString().isEmpty()) return false;
        }

        return true;
    }
    public void fillReactionToVessel(Iterable<String> reactionsToBoat) {
        for (String reactionNotTrimmed:reactionsToBoat) {
            String reaction=reactionNotTrimmed.trim();
            if(reaction.equals("None")) this.reactions_to_vessel.get(0).setChecked(true);
            else if(reaction.equals("Approach")) this.reactions_to_vessel.get(1).setChecked(true);
            else if(reaction.equals("Avoidance")) this.reactions_to_vessel.get(2).setChecked(true);
            else if(reaction.equals(""));
            else{
                this.reactions_to_vessel.get(3).setChecked(true);
                this.otherReaction.setText(reaction);
            }
        }
    }

    public void fillTrustLevel(String trust_level){
        switch (trust_level){
            case "Low":
                trustLevel.get(0).setChecked(true);
                break;
            case "Middle":
                trustLevel.get(1).setChecked(true);
                break;
            case "High":
                trustLevel.get(2).setChecked(true);
                break;
        }
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

    public String getNumberOfIndividualsString(){
        for(ToggleButton toggleButton : nr_individuals){
            if (toggleButton.isChecked()) return toggleButton.getTextOff().toString();
        }
        return "ERROR";
    }

    public String getNumberOfOffspringString(){
        for(ToggleButton toggleButton : nr_offspring){
            if (toggleButton.isChecked()) return toggleButton.getTextOff().toString();
        }
        return "Not specified";
    }

    public String getBehaviorTypesString(){
        String info = "";
        for(ToggleButton toggleButton : behavior_type){
            if (toggleButton.isChecked()) {
                if (toggleButton.getTextOff().toString().equals("Other")) info += getOtherBehavior() + ";";
                else info += toggleButton.getTextOff().toString() + ";";
            }
        }
        return info;
    }

    public String getReactionToVesselString(){
        String info = "";
        for(ToggleButton toggleButton : reactions_to_vessel){
            if (toggleButton.isChecked()){
                if(toggleButton.getTextOff().toString().equals("Other"))  info += getOtherReaction() + ";";
                else info += toggleButton.getTextOff().toString() + ";";
            }
        }
        return info;
    }

    public String getTrustLevelString(){
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

    public String getOtherBehavior() {
        return otherBehavior.getText().toString();
    }

    public void setOtherBehavior(EditText otherBehavior) {
        this.otherBehavior = otherBehavior;
    }

    public String getOtherReaction() {
        return otherReaction.getText().toString();
    }

    public void setOtherReaction(EditText otherReaction) {
        this.otherReaction = otherReaction;
    }



}
