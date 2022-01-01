package com.example.seaker.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.seaker.DataViewModel;
import com.example.seaker.MainActivity;
import com.example.seaker.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class LoginManagerAdminFragment extends BaseFragment {

    private DataViewModel model;
    private EditText email;
    private EditText password;
    private ImageButton loginBtn;

    public LoginManagerAdminFragment() {
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
        View view = inflater.inflate(R.layout.fragment_login_manager_admin, container, false);
        SetButtonOnClickNextFragment(R.id.buttonBack,new ChooseRoleFragment(),view);

        model = new ViewModelProvider(requireActivity()).get(DataViewModel.class);

        onStartView(view);

        return view;
    }

    private void login(){
        if(!validateInput()) return;

        if(verify_login(email.getText().toString(), password.getText().toString(), model.getUserType())){
            SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("isLogged", model.getUserType());
            editor.commit();
            if(model.getUserType() == "CompanyManager") MainActivity.switchFragment(new CompanyManagerHomeFragment());
            else if(model.getUserType() == "Administrator") MainActivity.switchFragment(new AdminHomeFragment());
        }else{
            ((MainActivity)getActivity()).onButtonShowPopupWindowClick(getView(), "Incorrect credentials!");
        }
    }

    private boolean validateInput(){
        String email = this.email.getText().toString();
        String password =  this.password.getText().toString();

        if(email.isEmpty() || password.isEmpty()) {
            ((MainActivity) getActivity()).onButtonShowPopupWindowClick(getView(), "Please, enter your credentials!");
            return false;
        }else if(!isValidEmailAddress(email)){
            ((MainActivity)getActivity()).onButtonShowPopupWindowClick(getView(), "Please, enter a valid email!");
            return false;
        }

        return true;
    }

    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    private void onStartView(View view) {
        email = (EditText) view.findViewById(R.id.email_input);
        password = (EditText) view.findViewById(R.id.password_input);
        loginBtn = (ImageButton) view.findViewById(R.id.login_btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    public Boolean verify_login(String email, String password, String role){
        String result = "";
        String insertSightingUrl = "http://" + ReportSightingFragment.ip + "/seaker/verifylogin.php";
        try {
            URL url = new URL(insertSightingUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("email", "UTF-8")+"="+URLEncoder.encode(email, "UTF-8") +"&"
                    + URLEncoder.encode("password", "UTF-8")+"="+URLEncoder.encode(password, "UTF-8")+"&"
                    + URLEncoder.encode("role", "UTF-8")+"="+URLEncoder.encode(role, "UTF-8");
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line = "";
            while((line = bufferedReader.readLine())!=null){
                result += line;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (result.contains("*")){
            ArrayList<ArrayList<String>> aux = new ArrayList<>();
            ArrayList<String> person_info = new ArrayList<>();
            String[] person = result.split("\\*");
            person_info.add(person[0]);
            person_info.add(person[1]);
            aux.add(person_info);
            Context cont = (Context) getActivity().getApplicationContext();
            ReportSightingFragment.SaveArrayListToSD(cont, "person_boat_zones", aux);
            //SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
            //            SharedPreferences.Editor editor = pref.edit();
            //            editor.putString("userId", person[0]);
            //            editor.putString("userName", person[1]);
            //            editor.commit();
            return true;
        } else {
            return false;
        }
    }

}