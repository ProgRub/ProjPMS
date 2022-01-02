package com.example.seaker.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.seaker.R;

import java.util.ArrayList;

public class AllMembersFragment extends BaseFragment {

    private LinearLayout members;

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
        SetButtonOnClickNextFragment(R.id.buttonBack,new AdminHomeFragment(),view);

        onStartView(view);

        return view;
    }

    private void onStartView(View view){
        members = (LinearLayout) view.findViewById(R.id.all_members);

        addMember("Diego Briceño", "diego@gmail.com", "diego123", "Team Member");
        addMember("Sílvia Fernandes", "silvia@gmail.com", "silvia123", "Team Member");
        addMember("Rúben Rodrigues", "ruben@gmail.com", "ruben123", "Team Member");
        addMember("Mara Dionísio", "mara@gmail.com", "maraaa", "Company Manager");
        addMember("Pedro Campos", "pedro@gmail.com", "pedrooo", "Company Manager");
    }

    private void addMember(String name, String email, String password, String type){
        LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.team_member_box, null);

        TextView userName = (TextView) v.findViewById(R.id.user_name);
        userName.setText(name);

        TextView userEmail = (TextView) v.findViewById(R.id.user_email);
        userEmail.setText(email);

        TextView userPassword = (TextView) v.findViewById(R.id.user_password);
        userPassword.setText(password);

        TextView userType = (TextView) v.findViewById(R.id.user_type);
        userType.setText(type);

        ImageButton removeButton = (ImageButton) v.findViewById(R.id.remove_member_btn);

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteClick(v);
            }
        });

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
                        //COLOCAR AQUI A FUNÇÃO PARA APAGAR DA BD
                        members.removeView(view);
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {@Override public void onClick(DialogInterface dialog, int which) {}});

        AlertDialog dialog = builder.create();
        dialog.show();
    }


}