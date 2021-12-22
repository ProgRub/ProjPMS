package com.example.seaker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.seaker.R;
import com.example.seaker.fragments.BaseFragment;
import com.example.seaker.fragments.SplashFragment;

public class MainActivity extends AppCompatActivity {

    private static FragmentManager supportFragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        supportFragmentManager = getSupportFragmentManager();
        switchFragment(new SplashFragment());
    }

    public static void switchFragment(BaseFragment fragment){
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, fragment)
                .commit();
    }
}