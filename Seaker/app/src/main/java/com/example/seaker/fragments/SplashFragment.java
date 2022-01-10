package com.example.seaker.fragments;

import android.os.Bundle;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.seaker.MainActivity;
import com.example.seaker.R;
import com.example.seaker.business.BusinessFacade;

public class SplashFragment extends BaseFragment {


    private static int TIME_OUT = 2*1000; //Time to launch the another activity
    public SplashFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Handler handler = new Handler();
        Runnable runnable = () -> {
            //Second fragment after 2 seconds appears
            MainActivity.switchFragment(new ChooseRoleFragment());
        };

        handler.postDelayed(runnable, TIME_OUT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }
}