package com.example.seaker.activities;

import android.os.Bundle;

import com.example.seaker.R;

public class TeamMemberHomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_member_home);
        SetButtonOnClickNextActivity(R.id.buttonNewSightingReport,ReportSightingActivity.class);
        SetButtonOnClickNextActivity(R.id.buttonReportedSightingsTeamMember,ReportSightingActivity.class);
        SetButtonOnClickNextActivity(R.id.buttonLogoutTeamMember,ChooseRoleActivity.class);
    }
}