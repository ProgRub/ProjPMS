package com.example.seaker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseRoleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_role);
        ((Button) findViewById(R.id.buttonTeamMember)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), TeamMemberHomeActivity.class);
                startActivityForResult(myIntent, 0);
            }

        });
        ((Button) findViewById(R.id.buttonCompanyManager)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), CompanyManagerHomeActivity.class);
                startActivityForResult(myIntent, 0);
            }

        });
        ((Button) findViewById(R.id.buttonAdministrator)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), AdminHomeActivity.class);
                startActivityForResult(myIntent, 0);
            }

        });
    }
}