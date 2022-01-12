package com.example.seaker.database.repositories;

import android.util.Log;

import com.example.seaker.database.DTOs.AnimalDTO;
import com.example.seaker.database.DTOs.SightingDTO;
import com.example.seaker.database.DTOs.ZoneDTO;
import com.example.seaker.database.specifications.ISpecification;

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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class SightingRepository extends Repository<SightingDTO> {
    @Override
    public void add(SightingDTO item) {

    }
    public void add(SightingDTO item, ZoneDTO zoneFrom,ZoneDTO zoneTo){String insertSightingUrl = "http://" + ip + "/seaker/insertsighting.php";
        try {
            URL url = new URL(insertSightingUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("day", "UTF-8")+"="+URLEncoder.encode(item.getDate().format(DateTimeFormatter.ofPattern("dd/MM/uuuu")), "UTF-8")+"&"
                    + URLEncoder.encode("hour", "UTF-8")+"="+URLEncoder.encode(String.valueOf(item.getTime()), "UTF-8")+"&"
                    + URLEncoder.encode("sea_state", "UTF-8")+"="+URLEncoder.encode(String.valueOf(item.getSeaStateBeaufort()), "UTF-8")+"&"
                    + URLEncoder.encode("latitude", "UTF-8")+"="+URLEncoder.encode(String.valueOf(item.getLatitude()), "UTF-8")+"&"
                    + URLEncoder.encode("longitude", "UTF-8")+"="+URLEncoder.encode(String.valueOf(item.getLongitude()), "UTF-8")+"&"
                    + URLEncoder.encode("comment", "UTF-8")+"="+URLEncoder.encode(item.getComments(), "UTF-8")+"&"
                    + URLEncoder.encode("person_id", "UTF-8")+"="+URLEncoder.encode(String.valueOf(item.getTeamMemberId()), "UTF-8")+"&"
                    + URLEncoder.encode("boat_id", "UTF-8")+"="+URLEncoder.encode(String.valueOf(item.getBoatId()), "UTF-8")+"&"
                    + URLEncoder.encode("animal", "UTF-8")+"="+URLEncoder.encode(item.getSightedAnimalsToString(), "UTF-8")+"&"
                    + URLEncoder.encode("trip_from", "UTF-8")+"="+URLEncoder.encode(zoneFrom.getName(), "UTF-8")+"&"
                    + URLEncoder.encode("trip_to", "UTF-8")+"="+URLEncoder.encode(zoneTo.getName(), "UTF-8");

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
    @Override
    public Iterable<SightingDTO> getAll() {
        String result = "";
        String getSightings = "http://" + ip + "/seaker/getallsightings.php";
        try {
            URL url = new URL(getSightings);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("person_id", "UTF-8") + "=" + URLEncoder.encode("NULL", "UTF-8");
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            Log.d("TODOS OS AVISTAMENTOS: ", result);
            ArrayList<SightingDTO> sightings = new ArrayList<>();
            String[] si = result.split("&&&");
            for (int j = 0; j < si.length; j++) {
                String[] sighting = si[j].split("###");
                SightingDTO sightingDTO = new SightingDTO(Long.parseLong(sighting[0]), true, LocalDate.parse(sighting[1], DateTimeFormatter.ofPattern("dd/MM/uuuu")), LocalTime.parse(sighting[2]), Integer.parseInt(sighting[3]), Double.parseDouble(sighting[4]), Double.parseDouble(sighting[5]), sighting[6], Long.parseLong(sighting[7]), Long.parseLong(sighting[9]));
                String[] animalsInfo=sighting[10].split("\\$");
                for(int indexAnimal=0;indexAnimal<animalsInfo.length;indexAnimal++){
                    String[] animalInfo=animalsInfo[indexAnimal].split("\\*");
                    sightingDTO.addSightedAnimal(new AnimalDTO(animalInfo[0],animalInfo[1],animalInfo[2],animalInfo[4],animalInfo[5],animalInfo[3]));
                }
                sightings.add(sightingDTO);
            }
            return sightings;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ConnectException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public SightingDTO getById(long itemId) {
        return null;
    }

    @Override
    public Iterable<SightingDTO> find(ISpecification specification) {
        return null;
    }

    @Override
    public void update(SightingDTO oldItem, SightingDTO newItem) {

    }

    @Override
    public void remove(SightingDTO itemToRemove) {

    }

    @Override
    public void removeById(long itemId) {
        String deleteSightingUrl = "http://" + ip + "/seaker/deletesightingbyid.php";
        try {
            URL url = new URL(deleteSightingUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("id_sighting", "UTF-8")+"="+URLEncoder.encode(String.valueOf(itemId), "UTF-8");
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

    public void editSighting(SightingDTO sighting){
        String updateSightingUrl = "http://" + ip + "/seaker/deleteandinsertsightingbyid.php";
        try {
            URL url = new URL(updateSightingUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("id_sighting", "UTF-8")+"="+URLEncoder.encode(String.valueOf(sighting.getId()), "UTF-8")+"&"
                    + URLEncoder.encode("day", "UTF-8")+"="+URLEncoder.encode(sighting.getDate().format(DateTimeFormatter.ofPattern("dd/MM/uuuu")), "UTF-8")+"&"
                    + URLEncoder.encode("hour", "UTF-8")+"="+URLEncoder.encode(String.valueOf(sighting.getTime()), "UTF-8")+"&"
                    + URLEncoder.encode("sea_state", "UTF-8")+"="+URLEncoder.encode(String.valueOf(sighting.getSeaStateBeaufort()), "UTF-8")+"&"
                    + URLEncoder.encode("latitude", "UTF-8")+"="+URLEncoder.encode(String.valueOf(sighting.getLatitude()), "UTF-8")+"&"
                    + URLEncoder.encode("longitude", "UTF-8")+"="+URLEncoder.encode(String.valueOf(sighting.getLongitude()), "UTF-8")+"&"
                    + URLEncoder.encode("comment", "UTF-8")+"="+URLEncoder.encode(sighting.getComments(), "UTF-8")+"&"
                    + URLEncoder.encode("animal", "UTF-8")+"="+URLEncoder.encode(sighting.getSightedAnimalsToString(), "UTF-8");

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
}
