package com.example.seaker.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.lifecycle.ViewModelProvider;

import com.example.seaker.DataViewModel;
import com.example.seaker.MQTTHelper;
import com.example.seaker.MainActivity;
import com.example.seaker.R;
import com.example.seaker.business.BusinessFacade;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.ArrayList;


public class TeamMemberHomeFragment extends BaseFragment {

    private DataViewModel model;
    private ImageButton logoutBtn;
    private MQTTHelper mqtt;

    public TeamMemberHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team_member_home, container, false);
        SetButtonOnClickNextFragment(R.id.buttonNewSightingReport,new ReportSightingFragment(),view);
        SetButtonOnClickNextFragment(R.id.buttonReportedSightingsTeamMember,new ReportedSightingsTeamMemberFragment(),view);

        model = new ViewModelProvider(requireActivity()).get(DataViewModel.class);

        //MQTT:
        try {
            mqtt = MQTTHelper.getInstance(getActivity().getApplicationContext());
            if(!mqtt.isConnected()) mqtt.tryConnect(getActivity().getApplicationContext());
        } catch (MqttException e) {
            e.printStackTrace();
        }
        BusinessFacade.getInstance().loadPreferences();
        logoutBtn = (ImageButton) view.findViewById(R.id.buttonLogoutTeamMember);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        publishNotPublishedSightings();

        return view;
    }

    private void logout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to log out?");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BusinessFacade.getInstance().clearPreferences();
                        MainActivity.switchFragment(new ChooseRoleFragment());
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {@Override public void onClick(DialogInterface dialog, int which) {}});

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void publishNotPublishedSightings(){
        Context cont = (Context) getActivity().getApplicationContext();
        ArrayList<String> sightingJson = ReportSightingFragment.ReadJsonArrayFromSD(cont,"notpublishedjsons");

        if(BusinessFacade.getInstance().isInternetWorking() && mqtt.isConnected()){
            ArrayList<Integer> publishedJsonIndexes = new ArrayList<>();
            if(!sightingJson.isEmpty()){
                for(int json=0; json < sightingJson.size(); json++){
                    mqtt.publish(getContext(), sightingJson.get(json));
                }
                sightingJson = new ArrayList<>();
                ReportSightingFragment.SaveSightingJsonStringToSD(cont, "notpublishedjsons", sightingJson);
            }
        }
    }

}