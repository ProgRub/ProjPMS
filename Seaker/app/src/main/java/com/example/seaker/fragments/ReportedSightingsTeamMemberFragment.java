package com.example.seaker.fragments;

import android.content.Context;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.seaker.R;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReportedSightingsTeamMemberFragment extends BaseFragment {

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

        recentSightings = (LinearLayout) view.findViewById(R.id.recent_sightings);
        otherSightings = (LinearLayout) view.findViewById(R.id.other_sightings);

        //Para testar inserção:
        addSightingToView(4, true, "27/12/2021", "13:05", "Blue Whale", "Diego Briceño");
        addSightingToView(2, false, "14/2/2021", "10:08", "Sperm Whale", "Rúben Rodrigues");
        addSightingToView(1, false, "3/10/2021", "9:11", "Bottlenose Dolphin, Sperm Whale", "Sílvia Fernandes");
        addSightingToView(3, true, "16/11/2021", "19:59", "Bottlenose Dolphin, Fin Whale", "Pedro Campos");

        return view;
    }

    //Função para adicionar um reported_sighting_box ao ecrã - recebe como parâmetros os dados do sighting:
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addSightingToView(int sighting_id, boolean submitted, String sighting_date, String sighting_time, String species, String team_member_name ){
        LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.reported_sighting_box, null);

        TextView sightingNumber = (TextView) v.findViewById(R.id.sighting_number);
        TextView notSubmitted = (TextView) v.findViewById(R.id.not_submitted);
        TextView date = (TextView) v.findViewById(R.id.date);
        TextView time = (TextView) v.findViewById(R.id.time);
        TextView sightingSpecies = (TextView) v.findViewById(R.id.sighting_species);
        TextView reportedBy = (TextView) v.findViewById(R.id.reported_by);

        ImageButton editSightingBtn = (ImageButton) v.findViewById(R.id.edit_sighting_btn);

        //MUDAR PARA FRAGMENTO DE EDITAR AVISTAMENTO:
        SetButtonOnClickNextFragment(R.id.edit_sighting_btn ,new TeamMemberHomeFragment(), v);

        sightingNumber.setText("Sighting #" + sighting_id);
        date.setText(sighting_date);
        time.setText(sighting_time);
        sightingSpecies.setText(species);
        reportedBy.setText(team_member_name);

        if(submitted){
            notSubmitted.setVisibility(View.INVISIBLE);
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
            editSightingBtn.setVisibility(View.INVISIBLE);
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
        DateTimeFormatter formatter;
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
}