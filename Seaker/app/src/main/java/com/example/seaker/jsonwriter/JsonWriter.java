package com.example.seaker.jsonwriter;


import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.seaker.MQTTHelper;
import com.example.seaker.MainActivity;
import com.example.seaker.database.DTOs.AnimalDTO;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class JsonWriter {

    public JsonWriter() {}

    public String createSightingJson(SightingJson sighting){

        try
        {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("Date", sighting.getDate());
            jsonObject.put("Time", sighting.getTime());
            jsonObject.put("Latitude", sighting.getLatitude());
            jsonObject.put("Longitude", sighting.getLongitude());

            JSONArray animalArray = new JSONArray();

            for(AnimalJson animal : sighting.getAnimals()){
                JSONObject animalJsonObject = new JSONObject();
                animalJsonObject.put("SpeciesName", animal.getSpeciesName());
                animalJsonObject.put("NumberIndividuals", animal.getNumberIndividuals());
                animalJsonObject.put("NumberOffspring", animal.getNumberOffspring());
                animalJsonObject.put("Behaviours", animal.getBehaviours().toString());
                animalJsonObject.put("ReactionsToBoat", animal.getReactionsToVessel().toString());
                animalJsonObject.put("TrustLevel", animal.getTrustLevel().toString());
                animalArray.put(animalJsonObject);
            }

            jsonObject.put("Animals", animalArray);
            jsonObject.put("SeaState", sighting.getSeaState());
            jsonObject.put("BoatName", sighting.getBoatName());
            jsonObject.put("Comments", sighting.getComment());
            jsonObject.put("ReporterName", sighting.getReporterName());

            //Log.e("TESTING", jsonObject.toString());

            //FileWriter fileWriter = new FileWriter(Environment.getExternalStorageDirectory() + "/sighting.json");
            //fileWriter.write(jsonObject.toString());
            //fileWriter.close();

            return jsonObject.toString();

        } catch (Exception e)
        {
            e.printStackTrace();
            return "null";
        }
    }


    public void createSpecieSummaryJson(ArrayList<SummaryJson> summaries, String startDate, String endDate) throws JSONException, IOException {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("StartDate", startDate);
            jsonObject.put("EndDate", endDate);

            JSONArray speciesArray = new JSONArray();
            for(SummaryJson summary : summaries){
                JSONObject summaryJsonObject = new JSONObject();
                summaryJsonObject.put("Specie", summary.getSpecie());
                summaryJsonObject.put("AnimalsPercentage", summary.getPercent());
                summaryJsonObject.put("TotalNumberIndividuals", summary.getTotalNrIndividuals());
                summaryJsonObject.put("AverageNumberIndividualsPerSighting", summary.getAverageNrIndividualsPerSighting());
                summaryJsonObject.put("MostCommonBehavior", summary.getMostCommonBehavior());
                summaryJsonObject.put("AverageTrustLevel", summary.getAverageTrustLvl());
                summaryJsonObject.put("MostSightedIn", summary.getMostSightedIn());
                speciesArray.put(summaryJsonObject);
            }

            jsonObject.put("SpeciesSummary", speciesArray);

            Log.e("TESTING", jsonObject.toString());

            FileWriter fileWriter = new FileWriter(Environment.getExternalStorageDirectory() + "/SummaryOfSightings.json");
            fileWriter.write(jsonObject.toString());
            fileWriter.close();

    }
}
