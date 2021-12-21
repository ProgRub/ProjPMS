package com.example.seaker.activities;

import android.os.Bundle;

import com.example.seaker.R;

public class ReportSightingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_sighting);
        SetButtonOnClickNextActivity(R.id.buttonBack,TeamMemberHomeActivity.class);
    }
}
