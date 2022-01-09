package com.example.seaker.database.repositories;

import android.util.Log;

import com.example.seaker.database.DTOs.BoatDTO;
import com.example.seaker.database.specifications.ISpecification;
import com.example.seaker.fragments.ReportSightingFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class BoatRepository extends Repository<BoatDTO> {
    @Override
    public void add(BoatDTO item) {

    }

    @Override
    public Iterable<BoatDTO> getAll() {
        String result = "";
        String login = "http://" + ip + "/seaker/getallboats.php";
        try {
            URL url = new URL(login);
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
            String[] boats = result.split("\\*");
            ArrayList<BoatDTO> boatsReturn=new ArrayList<>();
//            Log.d("RESULT",result);
            for(int i = 0; i < boats.length; i++){
                String[] individualBoat=boats[i].split("\\|");
//                Log.d("BOAT",individualBoat[0]+ " "+individualBoat[1]);
                boatsReturn.add(new BoatDTO(Long.parseLong(individualBoat[0]),individualBoat[1]));
            }
            return boatsReturn;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public BoatDTO getById(long itemId) {
        return null;
    }

    @Override
    public Iterable<BoatDTO> find(ISpecification specification) {
        return null;
    }

    @Override
    public void update(BoatDTO oldItem, BoatDTO newItem) {

    }

    @Override
    public void remove(BoatDTO itemToRemove) {

    }

    @Override
    public void removeById(long itemId) {

    }
}
