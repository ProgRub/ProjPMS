package com.example.seaker;

import android.widget.CompoundButton;
import android.widget.ToggleButton;

import java.time.LocalTime;
import java.util.ArrayList;

public class SightingInformation {

    private int sightingBoxID;
    private ArrayList<ToggleButton> nr_individuals;
    private ArrayList<ToggleButton> nr_offspring;

    public SightingInformation(int sightingBoxID){
        this.sightingBoxID = sightingBoxID;
        nr_individuals = new ArrayList<>();
        nr_offspring = new ArrayList<>();
    }

    public int getSightingBoxID() {
        return this.sightingBoxID;
    }

    public void add_NrIndividuals(ToggleButton nr_individual) {
        nr_individual.setOnCheckedChangeListener(nr_individuals_listener);
        this.nr_individuals.add(nr_individual);
    }

    public void add_NrOffspring(ToggleButton nr_offspring) {
        nr_offspring.setOnCheckedChangeListener(nr_offspring_listener);
        this.nr_offspring.add(nr_offspring);
    }

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
