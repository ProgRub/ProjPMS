package com.example.seaker.fragments;

import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.seaker.DataViewModel;
import com.example.seaker.MainActivity;
import com.example.seaker.R;
import com.example.seaker.business.BusinessFacade;

public class ChooseRoleFragment extends BaseFragment {

    private ImageButton teamMemberBtn;
    private ImageButton companyMemberBtn;
    private ImageButton adminBtn;

    public ChooseRoleFragment() {
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
        View view = inflater.inflate(R.layout.fragment_choose_role, container, false);
        SetButtonOnClickNextFragment(R.id.buttonTeamMember,new LoginTeamMemberFragment(),view);
        SetButtonOnClickNextFragment(R.id.buttonCompanyManager,new LoginManagerAdminFragment(),view);
        SetButtonOnClickNextFragment(R.id.buttonAdministrator,new LoginManagerAdminFragment(),view);

        teamMemberBtn = (ImageButton) view.findViewById(R.id.buttonTeamMember);
        companyMemberBtn = (ImageButton) view.findViewById(R.id.buttonCompanyManager);
        adminBtn = (ImageButton) view.findViewById(R.id.buttonAdministrator);

        teamMemberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BusinessFacade.getInstance().selectRole("TeamMember");
                MainActivity.switchFragment(new LoginTeamMemberFragment());
            }
        });

        companyMemberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BusinessFacade.getInstance().selectRole("CompanyManager");
                MainActivity.switchFragment(new LoginManagerAdminFragment());
            }
        });

        adminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BusinessFacade.getInstance().selectRole("Administrator");
                MainActivity.switchFragment(new LoginManagerAdminFragment());
            }
        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        String isLogged = pref.getString("isLogged", "");

        if(isLogged.equals("Administrator")) MainActivity.switchFragment(new AdminHomeFragment());
        else if(isLogged.equals("CompanyManager")) MainActivity.switchFragment(new CompanyManagerHomeFragment());
        else if(isLogged.equals("TeamMember")) MainActivity.switchFragment(new TeamMemberHomeFragment());
    }
}