package com.example.seaker.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.seaker.MainActivity;
import com.example.seaker.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BaseFragment#} factory method to
 * create an instance of this fragment.
 */
public class BaseFragment extends Fragment {
    private FragmentManager fragmentManager;

    public BaseFragment() {
        fragmentManager = getFragmentManager();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base, container, false);
    }


    public final void SetButtonOnClickNextFragment(int buttonId, BaseFragment nextFragment,View view) {
        try {
            ((Button) view.findViewById(buttonId)).setOnClickListener( a-> {
                MainActivity.switchFragment(nextFragment);
            });
        } catch (ClassCastException e) {
            ((ImageButton) view.findViewById(buttonId)).setOnClickListener(a -> {
                MainActivity.switchFragment(nextFragment);
            });
        }
    }

    public final void ShowPopupBox(String message){((MainActivity) getActivity()).onButtonShowPopupWindowClick(getView(), message);}
}