package com.example.seaker.database.repositories;

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

public class UserRepository extends IRepository<UserDTO>{
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
            /*String result = "";
            String line = "";
            while((line = bufferedReader.readLine())!=null){
                result += line;
            }*/

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
        return null;
    }

    @Override
    public UserDTO getById(long itemId) {
        return null;
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

    }
}
