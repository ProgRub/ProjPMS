package com.example.seaker.fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.seaker.R;


public class TeamMemberHomeFragment extends BaseFragment {


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
        SetButtonOnClickNextFragment(R.id.buttonLogoutTeamMember,new ChooseRoleFragment(),view);
        return view;
    }
}