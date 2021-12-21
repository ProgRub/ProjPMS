package com.example.seaker.activities;

import android.os.Bundle;

import com.example.seaker.R;

public class CompanyManagerHomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_manager_home);
        SetButtonOnClickNextActivity(R.id.buttonCreateReport,ReportSightingActivity.class);
        SetButtonOnClickNextActivity(R.id.buttonAllMembersCM,ReportSightingActivity.class);
        SetButtonOnClickNextActivity(R.id.buttonReportedSightingsCM,ReportSightingActivity.class);
        SetButtonOnClickNextActivity(R.id.buttonLogoutCM,ChooseRoleActivity.class);
    }
}