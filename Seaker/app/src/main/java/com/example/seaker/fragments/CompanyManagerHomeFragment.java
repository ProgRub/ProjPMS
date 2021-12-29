package com.example.seaker.fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.seaker.R;

public class CompanyManagerHomeFragment extends BaseFragment {


    public CompanyManagerHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_company_manager_home, container, false);
        SetButtonOnClickNextFragment(R.id.buttonCreateReport,new CreateReportFragment(),view);
        SetButtonOnClickNextFragment(R.id.buttonAllMembersCM,new ChooseRoleFragment(),view);
        SetButtonOnClickNextFragment(R.id.buttonReportedSightingsCM,new ChooseRoleFragment(),view);
        SetButtonOnClickNextFragment(R.id.buttonLogoutCM,new ChooseRoleFragment(),view);
        return view;
    }
}