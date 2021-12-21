package com.example.seaker.activities;

import android.os.Bundle;

import com.example.seaker.R;

public class AdminHomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        SetButtonOnClickNextActivity(R.id.buttonAddTeamMember,ReportSightingActivity.class);
        SetButtonOnClickNextActivity(R.id.buttonAllMembersAdmin,ReportSightingActivity.class);
        SetButtonOnClickNextActivity(R.id.buttonReportedSightingsAdmin,ReportSightingActivity.class);
        SetButtonOnClickNextActivity(R.id.buttonLogoutAdmin,ChooseRoleActivity.class);
    }
}