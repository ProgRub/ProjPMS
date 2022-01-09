package com.example.seaker.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.example.seaker.DataViewModel;
import com.example.seaker.R;
import com.example.seaker.business.BusinessFacade;
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
import java.util.ArrayList;

public class AllMembersFragment extends BaseFragment {

    private DataViewModel model;
    private LinearLayout members;
    private ArrayList<UserDTO> teamMembers;

    public AllMembersFragment() {
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
        View view = inflater.inflate(R.layout.fragment_all_members, container, false);
        model = new ViewModelProvider(requireActivity()).get(DataViewModel.class);

        if(model.getUserType().equals("CompanyManager")) SetButtonOnClickNextFragment(R.id.buttonBack,new CompanyManagerHomeFragment(),view);
        else if(model.getUserType().equals("Administrator")) SetButtonOnClickNextFragment(R.id.buttonBack,new AdminHomeFragment(),view);

        onStartView(view);

        return view;
    }

    private void onStartView(View view){
        members = (LinearLayout) view.findViewById(R.id.all_members);
        teamMembers= (ArrayList<UserDTO>) BusinessFacade.getInstance().getAllTeamMembers();
        for (UserDTO teamMember:teamMembers ) {
            addMember(teamMember.getId(), teamMember.getName(),teamMember.getEmail(),teamMember.getPassword(),teamMember.getType());
        }
    }

    private void addMember(long id, String name, String email, String password, String type){
        LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = vi.inflate(R.layout.team_member_box_cm, null);

        //se for administrator, mudará para o layout team_member_box_admin
        if(model.getUserType().equals("Administrator")){
            v = vi.inflate(R.layout.team_member_box_admin, null);

            TextView userPassword = (TextView) v.findViewById(R.id.user_password);
            userPassword.setText(password);

            ImageButton removeButton = (ImageButton) v.findViewById(R.id.remove_member_btn);

            v.setTag(id+"");

            View finalV = v;
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onDeleteClick(finalV);
                }
            });
        }

        TextView userName = (TextView) v.findViewById(R.id.user_name);
        userName.setText(name);

        TextView userEmail = (TextView) v.findViewById(R.id.user_email);
        userEmail.setText(email);

        TextView userType = (TextView) v.findViewById(R.id.user_type);
        userType.setText(type);

        members.addView(v);
    }

    private void onDeleteClick(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setTitle("Removing member");
        builder.setMessage("Are you sure you want to remove this member?");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        long userID = Long.parseLong((String) view.getTag());
                        UserDTO userDTO = BusinessFacade.getInstance().getUserByID(userID);
                        BusinessFacade.getInstance().deleteUser(userDTO);
                        members.removeView(view);
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {@Override public void onClick(DialogInterface dialog, int which) {}});

        AlertDialog dialog = builder.create();
        dialog.show();
    }


}