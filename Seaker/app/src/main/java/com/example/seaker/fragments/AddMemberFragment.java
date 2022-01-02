package com.example.seaker.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.seaker.MainActivity;
import com.example.seaker.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddMemberFragment extends BaseFragment {

    private EditText name;
    private EditText email;
    private EditText password;
    private CheckBox teamMemberCheckBox;
    private CheckBox companyManagerCheckBox;
    private ImageButton addMemberBtn;

    public AddMemberFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_member, container, false);
        SetButtonOnClickNextFragment(R.id.buttonBack,new AdminHomeFragment(),view);

        onStartView(view);

        return view;
    }

    private void onStartView(View view){

        name = (EditText) view.findViewById(R.id.name_input);
        email = (EditText) view.findViewById(R.id.email_input);
        password = (EditText) view.findViewById(R.id.password_input);
        teamMemberCheckBox = (CheckBox) view.findViewById(R.id.teamMemberCheckBox);
        companyManagerCheckBox = (CheckBox) view.findViewById(R.id.companyManagerCheckBox);
        addMemberBtn = (ImageButton) view.findViewById(R.id.addMemberBtn);

        teamMemberCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    companyManagerCheckBox.setChecked(false);
                }
            }
        });

        companyManagerCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    teamMemberCheckBox.setChecked(false);
                }
            }
        });

        addMemberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMember();
            }
        });

    }

    private void addMember(){
        if(!validateFields()) return;

        //AQUI É NECESSÁRIO VERIFICAR SE O EMAIL NÃO EXISTE NA BASE DE DADOS

        ((MainActivity)getActivity()).onButtonShowPopupWindowClick(getView(), "Team member successfully added!");

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                MainActivity.switchFragment(new AdminHomeFragment());
            }
        }, 2000);
    }

    private boolean validateFields(){

        boolean missing = false;

        if(name.getText().toString().equals("")) missing = true;
        else{
            if(containsNumbers(name.getText().toString())){
                ((MainActivity)getActivity()).onButtonShowPopupWindowClick(getView(), "Name can't contain numbers!");
                return false;
            }

            if(containsSpecialCharacters(name.getText().toString())){
                ((MainActivity)getActivity()).onButtonShowPopupWindowClick(getView(), "Name can't contain special characters!");
                return false;
            }
        }
        if(email.getText().toString().equals("")) missing = true;
        else{
            if(!isValidEmailAddress(email.getText().toString())){
                ((MainActivity)getActivity()).onButtonShowPopupWindowClick(getView(), "Invalid email format!");
                return false;
            }
        }

        if (password.getText().toString().equals("")) missing = true;
        if (!teamMemberCheckBox.isChecked() && !companyManagerCheckBox.isChecked()) missing = true;

        if(missing){
            ((MainActivity)getActivity()).onButtonShowPopupWindowClick(getView(), "Required fields missing!");
            return false;
        }

        return true;
    }

    private boolean containsNumbers(String string){
        return string.matches(".*\\d.*");
    }

    private boolean containsSpecialCharacters(String string){
        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(string);
        return m.find();
    }

    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }


}