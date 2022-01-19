package com.example.seaker.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.seaker.MainActivity;
import com.example.seaker.R;
import com.example.seaker.business.BusinessFacade;
import com.example.seaker.business.ErrorType;
import com.example.seaker.database.DTOs.UserDTO;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class LoginManagerAdminFragment extends BaseFragment {

    private EditText email;
    private EditText password;
    private ImageButton loginBtn;

    public LoginManagerAdminFragment() {
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
        View view = inflater.inflate(R.layout.fragment_login_manager_admin, container, false);
        SetButtonOnClickNextFragment(R.id.buttonBack, new ChooseRoleFragment(), view);


        onStartView(view);

        return view;
    }

    private void login() {
        if(BusinessFacade.getInstance().isInternetWorking()){
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
                    ShowPopupBox("Wrong login credentials.");
                    break;
                case NoError:
                    SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("isLogged", BusinessFacade.getInstance().getSelectedRole());
                    editor.commit();
                    if (BusinessFacade.getInstance().getSelectedRole() == "CompanyManager")
                        MainActivity.switchFragment(new CompanyManagerHomeFragment());
                    else if (BusinessFacade.getInstance().getSelectedRole() == "Administrator")
                        MainActivity.switchFragment(new AdminHomeFragment());
                    break;
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
        } else {
            ShowPopupBox("No connection!");
        }

    }

    private void onStartView(View view) {
        email = (EditText) view.findViewById(R.id.email_input);
        password = (EditText) view.findViewById(R.id.password_input);
        loginBtn = (ImageButton) view.findViewById(R.id.login_btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }
}