package com.example.seaker.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.example.seaker.DataViewModel;
import com.example.seaker.MainActivity;
import com.example.seaker.R;
import com.example.seaker.business.BusinessFacade;
import com.example.seaker.business.ErrorType;
import com.example.seaker.database.DTOs.BoatDTO;
import com.example.seaker.database.DTOs.UserDTO;
import com.example.seaker.database.DTOs.ZoneDTO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class LoginTeamMemberFragment extends BaseFragment {


    private EditText email;
    private EditText password;
    private Spinner vessel_id;
    private Spinner trip_from;
    private Spinner trip_to;
    private ImageButton login_btn;
    private DataViewModel model;

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

        SetButtonOnClickNextFragment(R.id.buttonBack, new ChooseRoleFragment(), view);

        model = new ViewModelProvider(requireActivity()).get(DataViewModel.class);

        onStartView(view);

        return view;
    }

    private void login() {
        UserDTO loginCredentials = new UserDTO("", this.email.getText().toString(), this.password.getText().toString(), BusinessFacade.getInstance().getSelectedRole());
        ErrorType errorType = BusinessFacade.getInstance().loginIsValid(loginCredentials);
        switch (errorType) {
            case EmailMissing:
            case PasswordMissing:
                ShowPopupBox("Please, enter your credentials!");
                break;
            case EmailNotValid:
                ShowPopupBox("Please, enter a valid email!");
                break;
            case WrongLoginData:
                ShowPopupBox("Incorrect credentials!");
                break;
            case VesselMissing:
                ShowPopupBox("Please, choose the vessel!");
                break;
            case ZoneFromMissing:
                ShowPopupBox("Please, choose the trip's departure!");
                break;
            case ZoneToMissing:
                ShowPopupBox("Please, choose the trip's destination!");
                break;
            case NoError:
                BusinessFacade.getInstance().setCurrentBoat(Long.parseLong(vessel_id.getSelectedItem().toString().split("\\.")[0]));
                BusinessFacade.getInstance().setZoneFrom(trip_from.getSelectedItem().toString());
                BusinessFacade.getInstance().setZoneTo(trip_to.getSelectedItem().toString());

                SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                UserDTO loggedInUser = BusinessFacade.getInstance().getLoggedInUser();
                editor.putString("isLogged", BusinessFacade.getInstance().getSelectedRole());
                editor.putString("userId", String.valueOf(loggedInUser.getId()));
                editor.putString("userName", loggedInUser.getName());
                editor.putString("vesselID", String.valueOf(BusinessFacade.getInstance().getCurrentBoat().getId()));
                editor.putString("tripFrom", BusinessFacade.getInstance().getStartingZone().getName());
                editor.putString("tripTo", BusinessFacade.getInstance().getEndingZone().getName());
                editor.commit();
//                SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
//                SharedPreferences.Editor editor = pref.edit();
//                editor.putString("isLogged", model.getUserType());
//                editor.putString("vesselID", vessel_id[0]);
//                editor.putString("tripFrom", trip_from.getSelectedItem().toString());
//                editor.putString("tripTo", trip_to.getSelectedItem().toString());
//                editor.commit();
                //Faz reset do ficheiro:
                try {
                    Context cont = (Context) getActivity().getApplicationContext();
                    FileOutputStream fos = cont.openFileOutput("notpublishedjsons.dat", cont.MODE_PRIVATE);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(new ArrayList<String>());
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    Context cont = (Context) getActivity().getApplicationContext();
                    FileOutputStream fos = cont.openFileOutput("notSubmittedSightings.dat", cont.MODE_PRIVATE);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(new ArrayList<>());
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                MainActivity.switchFragment(new TeamMemberHomeFragment());
                break;
        }
    }

    private void onStartView(View view) {
        email = (EditText) view.findViewById(R.id.email_input);
        password = (EditText) view.findViewById(R.id.password_input);
        vessel_id = (Spinner) view.findViewById(R.id.vessel_id_spinner);
        trip_from = (Spinner) view.findViewById(R.id.trip_from_spinner);
        trip_to = (Spinner) view.findViewById(R.id.trip_to_spinner);
        login_btn = (ImageButton) view.findViewById(R.id.login_btn);

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getContext(), R.layout.spinner_center_item);
        for (BoatDTO boat : BusinessFacade.getInstance().getAllBoats()) {
            adapter.add(String.format("%d. %s", boat.getId(), boat.getName()));
        }
        vessel_id.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter1 = new ArrayAdapter<CharSequence>(getContext(), R.layout.spinner_center_item);
        ArrayAdapter<CharSequence> adapter2 = new ArrayAdapter<CharSequence>(getContext(), R.layout.spinner_center_item);

        for (ZoneDTO zoneDTO : BusinessFacade.getInstance().getAllZonesFrom()) {
            adapter1.add(zoneDTO.getName());
        }
        trip_from.setAdapter(adapter1);
        for (ZoneDTO zoneDTO : BusinessFacade.getInstance().getAllZonesTo()) {
            adapter2.add(zoneDTO.getName());
        }
        trip_to.setAdapter(adapter2);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }
}