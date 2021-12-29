package com.example.seaker.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.seaker.R;

public class LoginTeamMemberFragment extends BaseFragment {


    EditText email;
    EditText password;
    Spinner vessel_id;
    Spinner trip_from;
    Spinner trip_to;

    public LoginTeamMemberFragment() {
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
        View view = inflater.inflate(R.layout.fragment_login_team_member, container, false);
        SetButtonOnClickNextFragment(R.id.login_btn,new TeamMemberHomeFragment(),view);
        SetButtonOnClickNextFragment(R.id.buttonBack,new ChooseRoleFragment(),view);

        onStartView (view);

        return view;
    }

    private void onStartView(View view){
        email = (EditText) view.findViewById(R.id.email_input);
        password = (EditText) view.findViewById(R.id.password_input);
        vessel_id = (Spinner) view.findViewById(R.id.vessel_id_spinner);
        trip_from = (Spinner) view.findViewById(R.id.trip_from_spinner);
        trip_to = (Spinner) view.findViewById(R.id.trip_to_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.vessels_array, R.layout.spinner_center_item);
        adapter.setDropDownViewResource(R.layout.spinner_center_item);
        vessel_id.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(getContext(), R.array.trip_from_array, R.layout.spinner_center_item);
        adapter.setDropDownViewResource(R.layout.spinner_center_item);
        trip_from.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(getContext(), R.array.trip_to_array, R.layout.spinner_center_item);
        adapter.setDropDownViewResource(R.layout.spinner_center_item);
        trip_to.setAdapter(adapter);

    }


}