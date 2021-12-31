package com.example.seaker.fragments;

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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

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

        SetButtonOnClickNextFragment(R.id.buttonBack,new ChooseRoleFragment(),view);

        model = new ViewModelProvider(requireActivity()).get(DataViewModel.class);

        onStartView (view);

        return view;
    }

    private void login(){
        if(!validateInput()) return;

        if(verify_login(email.getText().toString(), password.getText().toString(), "TeamMember")){
            model.setVesselID(vessel_id.getSelectedItem().toString());
            model.setTripFrom(trip_from.getSelectedItem().toString());
            model.setTripTo(trip_to.getSelectedItem().toString());

            MainActivity.switchFragment(new TeamMemberHomeFragment());
        }else{
            ((MainActivity)getActivity()).onButtonShowPopupWindowClick(getView(), "Incorrect credentials!");
        }
    }

    private boolean validateInput(){
        String email = this.email.getText().toString();
        String password =  this.password.getText().toString();
        String vesselId = vessel_id.getSelectedItem().toString();
        String tripFrom = trip_from.getSelectedItem().toString();
        String tripTo = trip_to.getSelectedItem().toString();

        if(email.isEmpty() || password.isEmpty()) {
            ((MainActivity) getActivity()).onButtonShowPopupWindowClick(getView(), "Please, enter your credentials!");
            return false;
        }else if(!isValidEmailAddress(email)){
            ((MainActivity)getActivity()).onButtonShowPopupWindowClick(getView(), "Please, enter a valid email!");
            return false;
        }else if(vesselId.isEmpty()){
            ((MainActivity)getActivity()).onButtonShowPopupWindowClick(getView(), "Please, choose the vessel ID!");
            return false;
        }else if(tripFrom.isEmpty()){
            ((MainActivity)getActivity()).onButtonShowPopupWindowClick(getView(), "Please, choose the trip's departure!");
            return false;
        }else if(tripTo.isEmpty()){
            ((MainActivity)getActivity()).onButtonShowPopupWindowClick(getView(), "Please, choose the trip's destination!");
            return false;
        }

        return true;
    }

    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    private void onStartView(View view){
        email = (EditText) view.findViewById(R.id.email_input);
        password = (EditText) view.findViewById(R.id.password_input);
        vessel_id = (Spinner) view.findViewById(R.id.vessel_id_spinner);
        trip_from = (Spinner) view.findViewById(R.id.trip_from_spinner);
        trip_to = (Spinner) view.findViewById(R.id.trip_to_spinner);
        login_btn = (ImageButton) view.findViewById(R.id.login_btn);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.vessels_array, R.layout.spinner_center_item);
        adapter.setDropDownViewResource(R.layout.spinner_center_item);
        vessel_id.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(getContext(), R.array.trip_from_array, R.layout.spinner_center_item);
        adapter.setDropDownViewResource(R.layout.spinner_center_item);
        trip_from.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(getContext(), R.array.trip_to_array, R.layout.spinner_center_item);
        adapter.setDropDownViewResource(R.layout.spinner_center_item);
        trip_to.setAdapter(adapter);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    public static Boolean verify_login(String email, String password, String role){
        String result = "";
        String insertSightingUrl = "http://" + ReportSightingFragment.ip + "/seaker/verifylogin.php";
        try {
            URL url = new URL(insertSightingUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("email", "UTF-8")+"="+URLEncoder.encode(email, "UTF-8") +"&"
                    + URLEncoder.encode("password", "UTF-8")+"="+URLEncoder.encode(password, "UTF-8")+"&"
                    + URLEncoder.encode("role", "UTF-8")+"="+URLEncoder.encode(role, "UTF-8");
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line = "";
            while((line = bufferedReader.readLine())!=null){
                result += line;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.equals("true");
    }
}