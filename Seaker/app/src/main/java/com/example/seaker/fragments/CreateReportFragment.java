package com.example.seaker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.seaker.R;

public class CreateReportFragment extends BaseFragment{


    public CreateReportFragment() {
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
        View view = inflater.inflate(R.layout.create_report_file, container, false);
        SetButtonOnClickNextFragment(R.id.buttonBack,new CompanyManagerHomeFragment(),view);
        return view;
    }

}
