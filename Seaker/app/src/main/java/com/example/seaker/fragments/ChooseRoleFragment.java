package com.example.seaker.fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.seaker.R;

public class ChooseRoleFragment extends BaseFragment {


    public ChooseRoleFragment() {
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
        View view = inflater.inflate(R.layout.fragment_choose_role, container, false);
        SetButtonOnClickNextFragment(R.id.buttonTeamMember,new TeamMemberHomeFragment(),view);
        SetButtonOnClickNextFragment(R.id.buttonCompanyManager,new CompanyManagerHomeFragment(),view);
        SetButtonOnClickNextFragment(R.id.buttonAdministrator,new AdminHomeFragment(),view);
        return view;
    }
}