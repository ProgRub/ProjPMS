package com.example.seaker.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.example.seaker.DataViewModel;
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

public class AllMembersFragment extends BaseFragment {

    private DataViewModel model;
    private LinearLayout members;

    public AllMembersFragment() {
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
        View view = inflater.inflate(R.layout.fragment_all_members, container, false);
        model = new ViewModelProvider(requireActivity()).get(DataViewModel.class);

        if(model.getUserType().equals("CompanyManager")) SetButtonOnClickNextFragment(R.id.buttonBack,new CompanyManagerHomeFragment(),view);
        else if(model.getUserType().equals("Administrator")) SetButtonOnClickNextFragment(R.id.buttonBack,new AdminHomeFragment(),view);

        onStartView(view);

        return view;
    }

    private void onStartView(View view){
        members = (LinearLayout) view.findViewById(R.id.all_members);

        String allTeamMembers = getAllTeamMembers();
        String[] tm = allTeamMembers.split("&&&");
        for(int j=0;j<tm.length;j++){
            String[] team_member = tm[j].split("###");
            addMember(team_member[0], team_member[1], team_member[2], team_member[3]);
        }
        //addMember("Diego Briceño", "diego@gmail.com", "diego123", "Team Member");
        //addMember("Sílvia Fernandes", "silvia@gmail.com", "silvia123", "Team Member");
        //addMember("Rúben Rodrigues", "ruben@gmail.com", "ruben123", "Team Member");
        //addMember("Mara Dionísio", "mara@gmail.com", "maraaa", "Company Manager");
        //addMember("Pedro Campos", "pedro@gmail.com", "pedrooo", "Company Manager");
    }

    private void addMember(String name, String email, String password, String type){
        LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = vi.inflate(R.layout.team_member_box_cm, null);

        //se for administrator, mudará para o layout team_member_box_admin
        if(model.getUserType().equals("Administrator")){
            v = vi.inflate(R.layout.team_member_box_admin, null);

            TextView userPassword = (TextView) v.findViewById(R.id.user_password);
            userPassword.setText(password);

            ImageButton removeButton = (ImageButton) v.findViewById(R.id.remove_member_btn);

            View finalV = v;
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onDeleteClick(finalV);
                }
            });
        }

        TextView userName = (TextView) v.findViewById(R.id.user_name);
        userName.setText(name);

        TextView userEmail = (TextView) v.findViewById(R.id.user_email);
        userEmail.setText(email);

        TextView userType = (TextView) v.findViewById(R.id.user_type);
        userType.setText(type);

        members.addView(v);
    }

    private void onDeleteClick(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setTitle("Removing member");
        builder.setMessage("Are you sure you want to remove this member?");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //COLOCAR AQUI A FUNÇÃO PARA APAGAR DA BD
                        members.removeView(view);
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {@Override public void onClick(DialogInterface dialog, int which) {}});

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static String getAllTeamMembers(){
        String result = "";
        String getMembers = "http://" + ReportSightingFragment.ip + "/seaker/getallteammembers.php";
        try {
            URL url = new URL(getMembers);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setDoInput(true);
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
        return result;
    }
}