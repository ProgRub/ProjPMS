package com.example.seaker.fragments;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.seaker.R;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reported_sightings_team_member, container, false);
        SetButtonOnClickNextFragment(R.id.buttonBack,new TeamMemberHomeFragment(),view);

        recentSightings = (LinearLayout) view.findViewById(R.id.recent_sightings);
        otherSightings = (LinearLayout) view.findViewById(R.id.other_sightings);

        //Para testar inserção:
        addSightingToView(4, true, "12/1/2021", "16:21", "Blue Whale", "Diego Briceño");
        addSightingToView(2, false, "14/2/2021", "10:56", "Sperm Whale", "Rúben Rodrigues");
        addSightingToView(1, false, "15/7/2021", "14:11", "Bottlenose Dolphin, Sperm Whale", "Sílvia Fernandes");
        addSightingToView(3, true, "16/9/2021", "19:59", "Bottlenose Dolphin, Fin Whale", "Pedro Campos");

        return view;
    }

    //Função para adicionar um reported_sighting_box ao ecrã - recebe como parâmetros os dados do sighting:
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

        if(!submitted) recentSightings.addView(v);
        else{
            notSubmitted.setVisibility(View.INVISIBLE);
            otherSightings.addView(v);
        }
    }
}