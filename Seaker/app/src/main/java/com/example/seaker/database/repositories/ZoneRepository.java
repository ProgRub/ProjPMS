package com.example.seaker.database.repositories;

import com.example.seaker.database.DTOs.ZoneDTO;
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

public class ZoneRepository extends Repository<ZoneDTO> {
    @Override
    public void add(ZoneDTO item) {

    }

    @Override
    public Iterable<ZoneDTO> getAll() {
        ArrayList<ZoneDTO> zones=new ArrayList<>();
        String result = "";
        String fileUrl = "http://" + ip + "/seaker/getallzonestripfrom.php";
        try {
            URL url = new URL(fileUrl);
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
            String[] zones_trip_from = result.split("\\*");
            for(int i = 0; i < zones_trip_from.length; i++){
                zones.add(new ZoneDTO(zones_trip_from[i],"From"));
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        result = "";
        fileUrl = "http://" + ip + "/seaker/getallzonestripto.php";
        try {
            URL url = new URL(fileUrl);
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
            String[] zones_trip_to = result.split("\\*");
            for(int i = 0; i < zones_trip_to.length; i++){
                zones.add(new ZoneDTO(zones_trip_to[i],"To"));
            }
            return zones;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ZoneDTO getById(long itemId) {
        return null;
    }

    @Override
    public Iterable<ZoneDTO> find(ISpecification specification) {
        return null;
    }

    @Override
    public void update(ZoneDTO oldItem, ZoneDTO newItem) {

    }

    @Override
    public void remove(ZoneDTO itemToRemove) {

    }

    @Override
    public void removeById(long itemId) {

    }
}
