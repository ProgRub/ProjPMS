package com.example.seaker.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.seaker.R;

public class LoginManagerAdminFragment extends BaseFragment {

    EditText email;
    EditText password;

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
        //SetButtonOnClickNextFragment(R.id.login_btn,new CompanyManagerHomeFragment(),view); //Se for manager
        SetButtonOnClickNextFragment(R.id.login_btn,new AdminHomeFragment(),view); //Se for admin
        SetButtonOnClickNextFragment(R.id.buttonBack,new ChooseRoleFragment(),view);

        onStartView(view);

        return view;
    }

    private void onStartView(View view) {
        email = (EditText) view.findViewById(R.id.email_input);
        password = (EditText) view.findViewById(R.id.password_input);

    }
}