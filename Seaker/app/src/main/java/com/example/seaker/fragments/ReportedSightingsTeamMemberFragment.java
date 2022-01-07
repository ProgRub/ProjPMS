package com.example.seaker.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import com.example.seaker.DataViewModel;
import com.example.seaker.MainActivity;
import com.example.seaker.R;
import com.example.seaker.SightingInformation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ReportedSightingsTeamMemberFragment extends BaseFragment {

    private DataViewModel model;
    private LinearLayout recentSightings;
    private LinearLayout otherSightings;

    public ReportedSightingsTeamMemberFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reported_sightings_team_member, container, false);
        SetButtonOnClickNextFragment(R.id.buttonBack,new TeamMemberHomeFragment(),view);

        model = new ViewModelProvider(requireActivity()).get(DataViewModel.class);

        recentSightings = (LinearLayout) view.findViewById(R.id.recent_sightings);
        otherSightings = (LinearLayout) view.findViewById(R.id.other_sightings);

        Context cont = (Context) getActivity().getApplicationContext();
        ArrayList<ArrayList<String>> sightings = ReportSightingFragment.ReadArrayListFromSD(cont,"notSubmittedSightings");

        if(ReportSightingFragment.isInternetWorking()){

            if(!sightings.isEmpty()){
                for(int i=0;i<sightings.size();i++){
                    ReportSightingFragment.insertSightingInformationIntoBD(sightings.get(i).get(0), sightings.get(i).get(1), sightings.get(i).get(2), sightings.get(i).get(3), sightings.get(i).get(4), sightings.get(i).get(5), sightings.get(i).get(6), sightings.get(i).get(8), sightings.get(i).get(9), sightings.get(i).get(10), sightings.get(i).get(11));
                }
                ArrayList<ArrayList<String>> aux = new ArrayList<>();
                ReportSightingFragment.SaveArrayListToSD(cont, "notSubmittedSightings", aux);
            }

            String allSightings = ReportSightingFragment.getAllSightingsInformations(getIdPerson());
            String[] si = allSightings.split("&&&");
            for(int j=0;j<si.length;j++){
                String[] sighting = si[j].split("###");
                addSightingToView(sighting[0], true, sighting[1], sighting[2], sighting[3], sighting[4], sighting[5], sighting[6], sighting[10], sighting[8]);
            }

        } else {
            if(!sightings.isEmpty()){
                for(int i=sightings.size()-1;i>=0;i--){
                    String sighting_id = "?" + i;
                    addSightingToView(sighting_id, false, sightings.get(i).get(0), sightings.get(i).get(1), sightings.get(i).get(2), sightings.get(i).get(3), sightings.get(i).get(4), sightings.get(i).get(5), sightings.get(i).get(9), sightings.get(i).get(7));
                }
            }
        }

        return view;
    }

    //Função para adicionar um reported_sighting_box ao ecrã - recebe como parâmetros os dados do sighting:
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addSightingToView(String sighting_id, boolean submitted, String sighting_date, String sighting_time, String sea_state, String latitude, String longitude, String comment, String species, String person_name){
        LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.reported_sighting_box, null);

        TextView sightingNumber = (TextView) v.findViewById(R.id.sighting_number);
        TextView notSubmitted = (TextView) v.findViewById(R.id.not_submitted);
        TextView date = (TextView) v.findViewById(R.id.date);
        TextView time = (TextView) v.findViewById(R.id.time);
        TextView sightingSpecies = (TextView) v.findViewById(R.id.sighting_species);
        TextView reportedBy = (TextView) v.findViewById(R.id.reported_by);

        ImageButton editSightingBtn = (ImageButton) v.findViewById(R.id.edit_sighting_btn);

        ArrayList<String> species_name = new ArrayList<>();
        ArrayList<String> n_individuals = new ArrayList<>();
        ArrayList<String> n_offspring = new ArrayList<>();
        ArrayList<String> trust_level = new ArrayList<>();
        ArrayList<String> behaviors = new ArrayList<>();
        ArrayList<String> reactions = new ArrayList<>();
        String[] result1 = species.split("\\$");

        for(int j=0;j<result1.length;j++){
            String[] result2 = result1[j].split("\\*");
            species_name.add(result2[0]);
            n_individuals.add(result2[1]);
            n_offspring.add(result2[2]);
            trust_level.add(result2[3]);
            if(result2.length == 4){
                behaviors.add(" ");
                reactions.add(" ");
            }
            if(result2.length == 5){
                behaviors.add(result2[4]);
                reactions.add(" ");
            }
            if(result2.length == 6){
                behaviors.add(result2[4]);
                reactions.add(result2[5]);
            }
        }
        String species_ = "";
        for(int k=0;k<species_name.size()-1;k++){
            species_ += species_name.get(k) + ", ";
        }
        species_ += species_name.get(species_name.size()-1);
        //MUDAR PARA FRAGMENTO DE EDITAR AVISTAMENTO:
        editSightingBtn.setOnClickListener(item -> {
            model.setReportedSighingId(sighting_id);
            model.setDate(sighting_date);
            model.setTime(sighting_time);
            model.setSea_state(Integer.parseInt(sea_state));
            model.setLatitude(latitude);
            model.setLongitude((longitude));
            model.setComment(comment);
            model.setSpecies(species_name);
            model.setN_individuals(n_individuals);
            model.setN_offspring(n_offspring);
            model.setTrust_level(trust_level);
            model.setReactions(reactions);
            model.setBehaviors(behaviors);
            MainActivity.switchFragment(new EditSightingFragment());
        });

        sightingNumber.setText("Sighting #" + sighting_id);
        date.setText(sighting_date);
        time.setText(sighting_time);
        sightingSpecies.setText(species_);
        reportedBy.setText(person_name);

        if(submitted){
            notSubmitted.setVisibility(View.GONE);
        }

        //Verificar se já passaram 24h:
        String dateTimeString = sighting_date +" "+sighting_time;
        String format = getLocalDateTimeFormatterString(sighting_date, sighting_time);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);
        LocalDateTime after24h = dateTime.plusHours(24);

        if(after24h.isAfter(LocalDateTime.now())){ //menos de 24h
           recentSightings.addView(v);
        }else{ //já não pode editar - mais de 24h
            editSightingBtn.setVisibility(View.GONE);
            otherSightings.addView(v);
        }
    }


    private String getLocalDateTimeFormatterString(String sighting_date, String sighting_time) {
        String[] date_parts = sighting_date.split("/");
        String day = date_parts[0];
        String month = date_parts[1];
        String[] time_parts = sighting_time.split(":");
        String hours = time_parts[0];
        String minutes = time_parts[1];

        String format = "";
        if(day.length() == 1){
            format +="d/";
        }else if(day.length() ==2){
            format +="dd/";
        }

        if(month.length() == 1){
            format +="M/";
        }else if(month.length() ==2){
            format +="MM/";
        }

        format+="yyyy ";

        if(hours.length() == 1){
            format +="H:";
        }else if(hours.length() ==2){
            format +="HH:";
        }

        if(minutes.length() == 1){
            format +="m";
        }else if(minutes.length() ==2){
            format +="mm";
        }

        return format;
    }

    private String getIdPerson(){
        Context cont = (Context) getActivity().getApplicationContext();
        ArrayList<ArrayList<String>> sighting_info = ReportSightingFragment.ReadArrayListFromSD(cont, "person_boat_zones");
        return sighting_info.get(0).get(0);
    }
}