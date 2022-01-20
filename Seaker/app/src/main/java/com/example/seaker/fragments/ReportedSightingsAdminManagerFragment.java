package com.example.seaker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.seaker.MainActivity;
import com.example.seaker.R;
import com.example.seaker.business.BusinessFacade;
import com.example.seaker.database.DTOs.AnimalDTO;
import com.example.seaker.database.DTOs.SightingDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ReportedSightingsAdminManagerFragment extends BaseFragment {

    private LinearLayout recentSightings;
    private ImageButton backBtn;
    private LinearLayout otherSightings;
    private TextView noRecentSightings;
    private TextView noOtherSightings;

    public ReportedSightingsAdminManagerFragment() {
        // Required empty public constructor
    }

    public ReportedSightingsAdminManagerFragment(BaseFragment backFragment) {
        super(backFragment);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reported_sightings_admin_manager, container, false);
        recentSightings = (LinearLayout) view.findViewById(R.id.recent_sightings);
        otherSightings = (LinearLayout) view.findViewById(R.id.other_sightings);
        noRecentSightings = (TextView) view.findViewById(R.id.no_recent_sightings);
        noOtherSightings = (TextView) view.findViewById(R.id.no_other_sightings);
        backBtn = (ImageButton) view.findViewById(R.id.buttonBack);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToPreviousFragment();
            }
        });

        if(BusinessFacade.getInstance().isInternetWorking()){
            for (SightingDTO sighting:BusinessFacade.getInstance().getAllSightings()) {
                addSightingToView(sighting);
            }
        }

        return view;
    }

    //Função para adicionar um reported_sighting_box ao ecrã - recebe como parâmetros os dados do sighting:
    private void addSightingToView(SightingDTO sighting){
        LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.reported_sighting_box, null);

        TextView sightingNumber = (TextView) v.findViewById(R.id.sighting_number);
        TextView notSubmitted = (TextView) v.findViewById(R.id.not_submitted);
        TextView date = (TextView) v.findViewById(R.id.date);
        TextView time = (TextView) v.findViewById(R.id.time);
        TextView sightingSpecies = (TextView) v.findViewById(R.id.sighting_species);
        TextView reportedBy = (TextView) v.findViewById(R.id.reported_by);

        ImageButton editSightingBtn = (ImageButton) v.findViewById(R.id.edit_sighting_btn);

        String species_ = "";
        ArrayList<AnimalDTO> sightedAnimals = (ArrayList<AnimalDTO>) sighting.getSightedAnimals();
        for(int k=0;k<sightedAnimals.size()-1;k++){
            species_ += sightedAnimals.get(k).getSpeciesName() + ", ";
        }
        species_ += sightedAnimals.get(sightedAnimals.size()-1).getSpeciesName();

        //MUDAR PARA FRAGMENTO DE EDITAR AVISTAMENTO:
        editSightingBtn.setOnClickListener(item -> {
            MainActivity.switchFragment(new EditSightingFragment(this,sighting));
        });

        sightingNumber.setText("Sighting #" + sighting.getId());
        String sightingDate = sighting.getDate().format(DateTimeFormatter.ofPattern("dd/MM/uuuu"));
        String sightingTime = sighting.getTime().toString();
        date.setText(sightingDate);
        time.setText(sightingTime);
        sightingSpecies.setText(species_);
        reportedBy.setText(BusinessFacade.getInstance().getUserByID(sighting.getTeamMemberId()).getName());

        if(sighting.isSubmitted()){
            notSubmitted.setVisibility(View.GONE);
        }

        //Verificar se já passaram 24h:
        String dateTimeString = sightingDate +" "+ sightingTime;
        String format = getLocalDateTimeFormatterString(sightingDate, sightingTime);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);
        LocalDateTime after24h = dateTime.plusHours(24);

        if(after24h.isAfter(LocalDateTime.now())){ //menos de 24h
            noRecentSightings.setVisibility(View.GONE);
            recentSightings.addView(v);
        }else{ //mais de 24h
            noOtherSightings.setVisibility(View.GONE);
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