package com.example.seaker.activities;

import android.os.Bundle;

import com.example.seaker.R;

public class ChooseRoleActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_role);
        SetButtonOnClickNextActivity(R.id.buttonTeamMember,TeamMemberHomeActivity.class);
        SetButtonOnClickNextActivity(R.id.buttonCompanyManager,CompanyManagerHomeActivity.class);
        SetButtonOnClickNextActivity(R.id.buttonAdministrator,AdminHomeActivity.class);
    }
}