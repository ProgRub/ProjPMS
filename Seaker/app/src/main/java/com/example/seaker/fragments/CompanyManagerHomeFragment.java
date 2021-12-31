package com.example.seaker.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.seaker.MainActivity;
import com.example.seaker.R;

public class CompanyManagerHomeFragment extends BaseFragment {

    private ImageButton logoutBtn;

    public CompanyManagerHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_company_manager_home, container, false);
        SetButtonOnClickNextFragment(R.id.buttonCreateReport,new CreateReportFragment(),view);
        SetButtonOnClickNextFragment(R.id.buttonAllMembersCM,new ChooseRoleFragment(),view);
        SetButtonOnClickNextFragment(R.id.buttonReportedSightingsCM,new ChooseRoleFragment(),view);

        logoutBtn = (ImageButton) view.findViewById(R.id.buttonLogoutCM);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
        return view;
    }

    private void logout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to log out?");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.switchFragment(new ChooseRoleFragment());
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {@Override public void onClick(DialogInterface dialog, int which) {}});

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}