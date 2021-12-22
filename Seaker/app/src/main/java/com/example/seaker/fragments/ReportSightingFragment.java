package com.example.seaker.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.seaker.MainActivity;
import com.example.seaker.R;

public class ReportSightingFragment extends BaseFragment {

    public ReportSightingFragment() {
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
        View view = inflater.inflate(R.layout.fragment_report_sighting, container, false);
        SetButtonOnClickNextFragment(R.id.buttonBack,new TeamMemberHomeFragment(),view);
        return view;
    }
}