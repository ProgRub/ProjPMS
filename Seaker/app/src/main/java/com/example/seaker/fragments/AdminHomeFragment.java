package com.example.seaker.fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.seaker.R;

public class AdminHomeFragment extends BaseFragment {


    public AdminHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_home, container, false);
        SetButtonOnClickNextFragment(R.id.buttonAddTeamMember,new ChooseRoleFragment(),view);
        SetButtonOnClickNextFragment(R.id.buttonAllMembersAdmin,new ChooseRoleFragment(),view);
        SetButtonOnClickNextFragment(R.id.buttonReportedSightingsAdmin,new ChooseRoleFragment(),view);
        SetButtonOnClickNextFragment(R.id.buttonLogoutAdmin,new ChooseRoleFragment(),view);
        return view;
    }
}