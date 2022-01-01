package com.example.seaker.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.lifecycle.ViewModelProvider;

import com.example.seaker.DataViewModel;
import com.example.seaker.MainActivity;
import com.example.seaker.R;

import java.util.ArrayList;


public class TeamMemberHomeFragment extends BaseFragment {

    private DataViewModel model;
    private ImageButton logoutBtn;

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

        logoutBtn = (ImageButton) view.findViewById(R.id.buttonLogoutTeamMember);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

        String userId = pref.getString("userId", "");
        String userName = pref.getString("userName", "");

        if(!userId.equals("") && !userName.equals("")){
            ArrayList<ArrayList<String>> aux = new ArrayList<>();
            ArrayList<String> person_info = new ArrayList<>();
            person_info.add(userId);
            person_info.add(userName);
            aux.add(person_info);
            Context cont = (Context) getActivity().getApplicationContext();
            ReportSightingFragment.SaveArrayListToSD(cont, "person_boat_zones", aux);

            String vessel_id = pref.getString("vesselID", "");
            String tripFrom_id = pref.getString("tripFrom", "");
            String tripTo_id = pref.getString("tripTo", "");
            model.setUserType("TeamMember");
            model.setVesselID("vessel_id");
            model.setTripFrom("tripFrom_id");
            model.setTripTo("tripTo_id");

            ArrayList<String> sighting = new ArrayList<>();
            sighting.add(vessel_id);
            sighting.add(tripFrom_id + "*" + tripTo_id);

            ArrayList<ArrayList<String>> sightings = ReportSightingFragment.ReadArrayListFromSD(cont, "person_boat_zones");
            sightings.add(sighting);
            ReportSightingFragment.SaveArrayListToSD(cont, "person_boat_zones", sightings);
        }


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
                        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                        SharedPreferences.Editor editor = pref.edit();
                        editor.clear();
                        editor.commit();
                        MainActivity.switchFragment(new ChooseRoleFragment());
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {@Override public void onClick(DialogInterface dialog, int which) {}});

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}