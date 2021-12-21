package com.example.seaker.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.seaker.R;

public class ReportSightingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_sighting);
        SetButtonOnClickNextActivity(R.id.buttonBack,TeamMemberHomeActivity.class);
    }

    public void selectSpecie(View view) {
        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.sighting_information_box, null);

        if(view.getId() == R.id.blue_whale_btn) {
            TextView textView = (TextView) v.findViewById(R.id.title);
            textView.setText("Blue Whale Sighting");
        }

        LinearLayout insertPoint = (LinearLayout) findViewById(R.id.sightingsInformations);
        insertPoint.addView(v);

    }
}
