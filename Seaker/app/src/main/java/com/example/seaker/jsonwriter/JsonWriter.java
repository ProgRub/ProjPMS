package com.example.seaker.jsonwriter;


import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.example.seaker.MainActivity;
import com.example.seaker.database.entities.Animal;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class JsonWriter {

    public JsonWriter(){}

    public void createSightingJson(){

        AnimalJson animal1 = new AnimalJson("Blue Whale", 1,0, new ArrayList<String>(Arrays.asList("Social Interaction", "Approach")), new ArrayList<String>(Arrays.asList("Other")));
        AnimalJson animal2 = new AnimalJson("Fin Whale", 3,1, new ArrayList<String>(Arrays.asList("Social Interaction", "Other")), new ArrayList<String>(Arrays.asList("Other")));

        SightingJson sighting = new SightingJson("12/12/2021", "14:35", 35.66342, -16.15432, new ArrayList<AnimalJson>(Arrays.asList(animal1, animal2)), 3, "Low", "Catamaran 1", "Comment :)", "Diego Briceño");

        try
        {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("Date", sighting.getDate());
            jsonObject.put("Time", sighting.getTime());
            jsonObject.put("Latitude", sighting.getLatitude());
            jsonObject.put("Longitude", sighting.getLongitude());

            JSONArray jsonArray = new JSONArray();

            for(AnimalJson animal : sighting.getAnimals()){ //NÃO INSERE N SEI PQ
                JSONObject animalJsonObject = new JSONObject();
                animalJsonObject.put("SpeciesName", animal.getSpeciesName());
                animalJsonObject.put("NumberIndividuals", animal.getNumberIndividuals());
                animalJsonObject.put("NumberOffspring", animal.getNumberOffspring());
                animalJsonObject.put("Behaviours", animal.getBehaviours().toString());
                animalJsonObject.put("ReactionsToBoat", animal.getReactionsToVessel().toString());
                jsonArray.put(animalJsonObject.toString());
            }

            jsonObject.put("SeaState", sighting.getSeaState());
            jsonObject.put("ConfidenceLevel", sighting.getConfidenceLevel());
            jsonObject.put("BoatName", sighting.getBoatName());
            jsonObject.put("Comments", sighting.getComment());
            jsonObject.put("ReporterName", sighting.getReporterName());

            Log.e("TESTING", jsonObject.toString());

            FileWriter fileWriter = new FileWriter("sighting1.json"); //Não sei onde guarda

            fileWriter.write(jsonObject.toString());
            fileWriter.close();

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
