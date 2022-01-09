package com.example.seaker.database.repositories;

import android.content.Context;
import android.util.Log;
import android.content.SharedPreferences;


import com.example.seaker.database.DTOs.UserDTO;
import com.example.seaker.database.specifications.ISpecification;
import com.example.seaker.fragments.ReportSightingFragment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class UserRepository extends Repository<UserDTO> {
    @Override
    public void add(UserDTO item) {
        String insertTeamMember = "http://" + ip + "/seaker/addteammember.php";
        try {
            URL url = new URL(insertTeamMember);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(item.getName(), "UTF-8") + "&"
                    + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(item.getEmail(), "UTF-8") + "&"
                    + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(item.getPassword(), "UTF-8") + "&"
                    + URLEncoder.encode("role", "UTF-8") + "=" + URLEncoder.encode(item.getType(), "UTF-8");

            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ConnectException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Iterable<UserDTO> getAll() {
        String allTeamMembers = "";
        String getMembers = "http://" + ip + "/seaker/getallteammembers.php";
        try {
            URL url = new URL(getMembers);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setDoInput(true);
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line = "";
            while((line = bufferedReader.readLine())!=null){
                allTeamMembers += line;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] tm = allTeamMembers.split("&&&");
        ArrayList<UserDTO> users=new ArrayList<>();
        for(int j=0;j<tm.length;j++){
            String[] team_member = tm[j].split("###");
            users.add(new UserDTO(Long.parseLong(team_member[0]),team_member[1], team_member[2], team_member[3], team_member[4]) );
        }
        return users;
    }

    @Override
    public UserDTO getById(long itemId) {

        String teamMember = "";
        String getMembers = "http://" + ip + "/seaker/getteammemberbyid.php";
        try {
            URL url = new URL(getMembers);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("id_team_member", "UTF-8") + "=" + URLEncoder.encode(Long.toString(itemId), "UTF-8");
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line = "";
            while((line = bufferedReader.readLine())!=null){
                teamMember += line;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] team_member = teamMember.split("###");
        return new UserDTO(Long.parseLong(team_member[0]),team_member[1], team_member[2], team_member[3], team_member[4]);
    }

    @Override
    public Iterable<UserDTO> find(ISpecification specification) {
        return null;
    }

    @Override
    public void update(UserDTO oldItem, UserDTO newItem) {

    }

    @Override
    public void remove(UserDTO itemToRemove) {

    }

    @Override
    public void removeById(long itemId) {

        String disableTeamMember = "http://" + ip + "/seaker/deletememberteambyid.php";
        try {
            URL url = new URL(disableTeamMember);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("id_team_member", "UTF-8")+"="+URLEncoder.encode(Long.toString(itemId), "UTF-8");
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String result = "";
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
    }

    public boolean loginAdminCompanyManager(UserDTO loginCredentials) {String result = "";
        String login = "http://" + ip + "/seaker/verifylogin.php";
        try {
            URL url = new URL(login);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("email", "UTF-8")+"="+URLEncoder.encode(loginCredentials.getEmail(), "UTF-8") +"&"
                    + URLEncoder.encode("password", "UTF-8")+"="+URLEncoder.encode(loginCredentials.getPassword(), "UTF-8")+"&"
                    + URLEncoder.encode("role", "UTF-8")+"="+URLEncoder.encode(loginCredentials.getType(), "UTF-8");
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
//            Context cont = (Context) getActivity().getApplicationContext();
//            ReportSightingFragment.SaveArrayListToSD(cont, "person_boat_zones", aux);
            return true;
        } else {
            return false;
        }
//        if (result.contains("*")){
//            ArrayList<ArrayList<String>> aux = new ArrayList<>();
//            ArrayList<String> person_info = new ArrayList<>();
//            String[] person = result.split("\\*");
//            person_info.add(person[0]);
//            person_info.add(person[1]);
//            aux.add(person_info);
//            Context cont = (Context) getActivity().getApplicationContext();
//            ReportSightingFragment.SaveArrayListToSD(cont, "person_boat_zones", aux);
//
//            SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
//            SharedPreferences.Editor editor = pref.edit();
//            editor.putString("userId", person[0]);
//            editor.putString("userName", person[1]);
//            editor.commit();
//            return true;
//        } else {
//            return false;
//        }
    }
}
