package com.example.seaker.fragments;

import android.os.Bundle;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.seaker.MainActivity;
import com.example.seaker.R;
import com.example.seaker.business.BusinessFacade;
import com.example.seaker.business.ErrorType;
import com.example.seaker.database.DTOs.UserDTO;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddMemberFragment extends BaseFragment {

    private EditText name;
    private EditText email;
    private EditText password;
    private CheckBox teamMemberCheckBox;
    private CheckBox companyManagerCheckBox;
    private ImageButton addMemberBtn;

    public AddMemberFragment() {
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
        View view = inflater.inflate(R.layout.fragment_add_member, container, false);
        SetButtonOnClickNextFragment(R.id.buttonBack, new AdminHomeFragment(), view);

        onStartView(view);

        return view;
    }

    private void onStartView(View view) {

        name = (EditText) view.findViewById(R.id.name_input);
        email = (EditText) view.findViewById(R.id.email_input);
        password = (EditText) view.findViewById(R.id.password_input);
        teamMemberCheckBox = (CheckBox) view.findViewById(R.id.teamMemberCheckBox);
        companyManagerCheckBox = (CheckBox) view.findViewById(R.id.companyManagerCheckBox);
        addMemberBtn = (ImageButton) view.findViewById(R.id.addMemberBtn);

        teamMemberCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    companyManagerCheckBox.setChecked(false);
                }
            }
        });

        companyManagerCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    teamMemberCheckBox.setChecked(false);
                }
            }
        });

        addMemberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BusinessFacade.getInstance().isInternetWorking()){
                    addMember();
                } else {
                    ShowPopupBox("No connection!");
                }
            }
        });
    }

    private void addMember() {

        String person_name = name.getText().toString();
        String person_email = email.getText().toString();
        String person_password = password.getText().toString();
        String person_role = "";
        if (companyManagerCheckBox.isChecked()) {
            person_role = "CompanyManager";
        }
        else if(teamMemberCheckBox.isChecked()){
            person_role = "TeamMember";
        }
        UserDTO user = new UserDTO(person_name, person_email, person_password, person_role);
        ErrorType errorType=BusinessFacade.getInstance().userIsValid(user);
        if (errorType==ErrorType.NoError) {
            insertTeamMemberIntoBD(user);

            // VERIFICAR DEPOIS SE REALMENTE INSERIU PARA PODER MOSTRAR MEMNSAGEM DE SUCESSO!

            ShowPopupBox("Team member successfully added!");

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    MainActivity.switchFragment(new AdminHomeFragment());
                }
            }, 2000);
        }else{
            switch (errorType){

                case NameMissing:
                case EmailMissing:
                case PasswordMissing:
                case UserTypeNotSpecified:
                    ShowPopupBox("Required fields missing!");
                    break;
                case NameHasNumbers:ShowPopupBox("Name can't contain numbers!");
                    break;
                case NameHasSpecialCharacters:ShowPopupBox("Name can't contain special characters!");
                    break;
                case EmailNotValid:ShowPopupBox("Invalid email format!");
                    break;
            }
        }

    }

    public static void insertTeamMemberIntoBD(UserDTO user) {
        BusinessFacade.getInstance().addUser(user);
    }
}