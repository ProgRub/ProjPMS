package com.example.seaker.activities;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    public final void SetButtonOnClickNextActivity(int buttonId, Class<?> nextActivity) {
        try {
            ((Button) findViewById(buttonId)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent myIntent = new Intent(view.getContext(), nextActivity);
                    startActivityForResult(myIntent, 0);
                }

            });
        } catch (ClassCastException e) {
            ((ImageButton) findViewById(buttonId)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent myIntent = new Intent(view.getContext(), nextActivity);
                    startActivityForResult(myIntent, 0);
                }

            });
        }
    }

    @Override
    public void onBackPressed() {
        return;
    }

}
