package com.example.seaker.fragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentContainerView;

import com.example.seaker.MainActivity;
import com.example.seaker.R;
import com.example.seaker.SpecieSummary;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.poi.sl.usermodel.Line;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class CreateReportFragment extends BaseFragment implements OnMapReadyCallback {

    private EditText startDate;
    private EditText endDate;
    private EditText photosPerSighting;
    private ImageButton createSummaryBtn;
    private TextView nrSightingsFound;
    private FragmentContainerView mapBox;
    private GoogleMap map;
    private TextView totalNrIndividuals;
    private TextView chooseFormatText;
    private RadioButton pdfFormat;
    private RadioButton docxFormat;
    private ImageButton exportReportBtn;
    private ImageButton shareViaEmailBtn;
    private LinearLayout summary;
    private int pageHeight = 1120;
    private int pagewidth = 792;
    private File pdfFile;
    private File docxFile;
    private ArrayList<SpecieSummary> speciesSummary;
    private ArrayList<LatLng> coordinatesFromSummary;

    private static final int PERMISSION_REQUEST_CODE = 200;
    public CreateReportFragment() {
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
        View view = inflater.inflate(R.layout.fragment_create_report_file, container, false);
        SetButtonOnClickNextFragment(R.id.buttonBack,new CompanyManagerHomeFragment(),view);

        onStartView(view);

        return view;
    }

    private void onStartView(View view){

        speciesSummary = new ArrayList<SpecieSummary>();
        coordinatesFromSummary = new ArrayList<LatLng>();

        startDate = (EditText) view.findViewById(R.id.startDate);
        endDate = (EditText) view.findViewById(R.id.endDate);
        photosPerSighting = (EditText) view.findViewById(R.id.photos_per_sighting);
        createSummaryBtn = (ImageButton) view.findViewById(R.id.create_summary_btn);
        nrSightingsFound = (TextView) view.findViewById(R.id.nr_sightings_found);
        mapBox = (FragmentContainerView) view.findViewById(R.id.map);
        totalNrIndividuals = (TextView) view.findViewById(R.id.total_nr_individuals);
        chooseFormatText = (TextView) view.findViewById(R.id.chooseFormatText);
        pdfFormat = (RadioButton) view.findViewById(R.id.pdf_format);
        docxFormat = (RadioButton) view.findViewById(R.id.docx_format);
        exportReportBtn = (ImageButton) view.findViewById(R.id.export_report_btn);
        shareViaEmailBtn = (ImageButton) view.findViewById(R.id.share_via_email_btn);
        summary = (LinearLayout) view.findViewById(R.id.summary_layout);

        mapBox.setVisibility(View.GONE);
        totalNrIndividuals.setVisibility(View.GONE);
        chooseFormatText.setVisibility(View.GONE);
        pdfFormat.setVisibility(View.GONE);
        docxFormat.setVisibility(View.GONE);
        exportReportBtn.setVisibility(View.GONE);
        shareViaEmailBtn.setVisibility(View.GONE);

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicking(view, true);
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicking(view, false);
            }
        });

        createSummaryBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                //se já tinha criado um sumário, atualiza
                if(exportReportBtn.getVisibility() == View.VISIBLE) createDifferentSummary(view);
                //caso contrário, cria um novo
                else createSummary(view);
            }
        });

        exportReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pdfFormat.isChecked()){
                    generatePDF(true);
                }else if(docxFormat.isChecked()){
                    generateDocx(true);
                }else{
                    ((MainActivity)getActivity()).onButtonShowPopupWindowClick(view, "Choose a file format.");
                }
            }
        });

        shareViaEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pdfFormat.isChecked()){
                    generatePDF(false);
                    sendViaEmail("pdf");
                }else if(docxFormat.isChecked()){
                    generateDocx(false);
                    sendViaEmail("docx");
                }else{
                    ((MainActivity)getActivity()).onButtonShowPopupWindowClick(view, "Choose a file format.");
                }
            }
        });

        if (checkPermission()) {
            //Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }
    }


    private void addSpecieSummary(String specie, String nrIndiv, String averageNrIndiv, String mostComBeh, String mostComReact, String averBeaufort, String averTrustLvl, String nrPics){
        LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.summary_specie_layout, null);

        TextView specieName = (TextView) v.findViewById(R.id.specie_name);
        specieName.setText(specie);

        TextView nrIndividuals = (TextView) v.findViewById(R.id.nr_individuals);
        nrIndividuals.setText("- Total number of individuals: "+ nrIndiv);

        TextView averageNumberIndividuals = (TextView) v.findViewById(R.id.average_number_individuals);
        averageNumberIndividuals.setText("- Average number of individuals per sighting: "+ averageNrIndiv);

        TextView mostCommonBehavior = (TextView) v.findViewById(R.id.most_common_behavior);
        mostCommonBehavior.setText("- Most common behavior type: "+ mostComBeh);

        TextView mostCommonReaction = (TextView) v.findViewById(R.id.most_common_reaction);
        mostCommonReaction.setText("- Most common reaction to vessel: "+ mostComReact);

        TextView averageBeaufortSeaState = (TextView) v.findViewById(R.id.average_beaufort_sea_state);
        averageBeaufortSeaState.setText("- Average Beaufort sea state: "+ averBeaufort);

        TextView averageTrustLevel = (TextView) v.findViewById(R.id.average_trust_level);
        averageTrustLevel.setText("- Average trust level: "+ averTrustLvl);

        TextView nrPhotos  = (TextView) v.findViewById(R.id.nr_photos);
        nrPhotos.setText("- Photos: "+ nrPics);


        //APENAS PARA TESTAR INSERÇÃO DE FOTOS:
        Random rand = new Random();
        int n = rand.nextInt(10);

        addPhotosOfSummary(v, n);

        speciesSummary.add( new SpecieSummary(v, specie, nrIndiv, averageNrIndiv, mostComBeh, mostComReact, averBeaufort, averTrustLvl, nrPics));

        summary.addView(v);
    }

    public void datePicking(View view, boolean startDate) {
        EditText editText = startDate? this.startDate : this.endDate;
        String actualValue;
        String previousDate[];
        int yy, mm, dd;

        if(!editText.getText().toString().equals("")){
            actualValue = editText.getText().toString();
            previousDate = actualValue.split("/");
            yy = Integer.parseInt(previousDate[2]);
            mm = Integer.parseInt(previousDate[1])-1;
            dd = Integer.parseInt(previousDate[0]);
        }else{
            final Calendar calendar = Calendar.getInstance();
            yy = calendar.get(Calendar.YEAR);
            mm = calendar.get(Calendar.MONTH);
            dd = calendar.get(Calendar.DAY_OF_MONTH);
        }

        DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String date = String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear+1)
                        + "/" + String.valueOf(year);
                editText.setText(date);
            }
        }, yy, mm, dd);
        datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());

        datePicker.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean validateDate(){ //verifica se a start date é superior à end date

        String format = getLocalDateTimeFormatterString(startDate.getText().toString());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDate startLocalDate = LocalDate.parse(startDate.getText().toString(), formatter);

        format = getLocalDateTimeFormatterString(endDate.getText().toString());
        formatter = DateTimeFormatter.ofPattern(format);
        LocalDate endLocalDate = LocalDate.parse(endDate.getText().toString(), formatter);

        if(startLocalDate.isAfter(endLocalDate)) return false;

        return true;
    }

    private String getLocalDateTimeFormatterString(String sighting_date) {
        String[] date_parts = sighting_date.split("/");
        String day = date_parts[0];
        String month = date_parts[1];

        String format = "";
        if(day.length() == 1){
            format +="d/";
        }else if(day.length() ==2){
            format +="dd/";
        }

        if(month.length() == 1){
            format +="M/";
        }else if(month.length() ==2){
            format +="MM/";
        }

        format+="yyyy";

        return format;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createSummary(View view){

        if(startDate.getText().toString().equals("") || endDate.getText().toString().equals("")){
            ((MainActivity)getActivity()).onButtonShowPopupWindowClick(view, "Required field missing!");
            return;
        }

        if(!validateDate()) {
            ((MainActivity)getActivity()).onButtonShowPopupWindowClick(view, "Start Date must be before End Date!");
            return;
        }

        if(photosPerSighting.getText().toString().equals("")){
            ((MainActivity)getActivity()).onButtonShowPopupWindowClick(view, "Required field missing!");
            return;
        }

        if(Integer.parseInt(photosPerSighting.getText().toString()) > 30){
            ((MainActivity)getActivity()).onButtonShowPopupWindowClick(view, "Enter a number from 0 to 30!");
            return;
        }

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);

        totalNrIndividuals.setVisibility(View.VISIBLE);
        chooseFormatText.setVisibility(View.VISIBLE);
        pdfFormat.setVisibility(View.VISIBLE);
        docxFormat.setVisibility(View.VISIBLE);
        exportReportBtn.setVisibility(View.VISIBLE);
        shareViaEmailBtn.setVisibility(View.VISIBLE);

        //Para testar inserção do pin nas coordenadas:
        addCoordinatesFound(32.644313, -16.914312);
        addCoordinatesFound(32.643579, -16.915613);
        addCoordinatesFound(32.642859, -16.916211);

        mapBox.setVisibility(View.VISIBLE); //ADICIONAR AS COORDENADAS ANTES DE TORNAR O MAPA VISIVEL !!!

        //Apenas para testar:
        nrSightingsFound.setText("5" + " sightings found."); //ALTERAR COM VALOR CORRETO
        totalNrIndividuals.setText("Total number of individuals: " + "34"); //ALTERAR COM VALOR CORRETO

        //Adicionar as espécies dos avistamentos encontrados:
        addSpecieSummary("Blue Whale", "12", "2", "Social Interaction", "Approach", "2", "High", "8");
        addSpecieSummary("Fin Whale", "5", "1", "Other", "None", "4", "Low", "2");

    }

    //NOTAS -> MÉTODO CREATESUMMARY:
    //addSpecieSummary -> recebe como parametros os dados para criar o sumário de cada espécie
    //addCoordinatesFound -> recebe como parâmetro a latitude e a longitude de cada avistamento onde irá colocar um pin no mapa
    //nrSightingsFound.setText(NUMBER_SIGHTINGS + " sightings found."); -> NUMBER_SIGHTINGS -> numero de avistamentos entre a start date e end date
    //totalNrIndividuals.setText("Total number of individuals: " + NUMBER_INDIVIDUALS); -> NUMBER_INDIVIDUALS -> numero de individuos total das espécies avistadas entre as datas

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createDifferentSummary(View view){

        //apaga todos os sumários do ecrã:
        for(SpecieSummary specieSummary : speciesSummary){
            summary.removeView(specieSummary.getSummary());
        }

        speciesSummary.clear();
        coordinatesFromSummary.clear();

        createSummary(view);
    }

    private void addCoordinatesFound(Double latitude, Double longitude){
        coordinatesFromSummary.add(new LatLng(latitude, longitude));
    }


    private void addPhotosOfSummary(View view, int numberPhotos){
        LinearLayout photos = (LinearLayout) view.findViewById(R.id.uploaded_photos);

        //SE O NUMERO DE FOTOS NA DB DAQUELA ESPECIE FOR SUPERIOR AO ESCOLHIDO, APENAS INSERE O MAXIMO ESCOLHIDO:
        if(numberPhotos > Integer.parseInt(photosPerSighting.getText().toString())) {
            numberPhotos = Integer.parseInt(photosPerSighting.getText().toString());
        }

        //GUARDAR NUMA LISTA AS IMAGENS

        for(int i = 0; i < numberPhotos; i++){
            LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.photos_layout, null);

            ImageView image = (ImageView) v.findViewById(R.id.photo);

            //ALTERAR A IMAGEM DO IMAGEVIEW PARA A IMAGEM DA LISTA:
            image.setImageDrawable(getResources().getDrawable(R.drawable.blue_whale_btn, getActivity().getApplicationContext().getTheme()));

            photos.addView(v);
        }

    }


    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        MarkerOptions options = new MarkerOptions();

        int sightingNr = 1;
        for (LatLng point : coordinatesFromSummary) {
            options.position(point);
            options.title("Sighting " + sightingNr);
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.sighting_pin));
            map.addMarker(options);
            sightingNr++;
            moveToCurrentLocation(point);
        }
    }

    private void moveToCurrentLocation(LatLng currentLocation)
    {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,15));
        map.animateCamera(CameraUpdateFactory.zoomIn());
        map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }

    private void generatePDF(boolean export) {
        PdfDocument pdfDocument = new PdfDocument();

        Paint paint = new Paint();
        Paint title = new Paint();

        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();

        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);

        Canvas canvas = myPage.getCanvas();

        //canvas.drawBitmap(scaledbmp, 56, 40, paint);

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        title.setTextSize(50);
        title.setColor(ContextCompat.getColor(getActivity(), R.color.dark_blue));
        title.setTextAlign(Paint.Align.CENTER);

        canvas.drawText("BonsAvistamentos", 396, 500, title);
        title.setTextSize(34);
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        title.setColor(ContextCompat.getColor(getActivity(), R.color.black));
        String sentence = "From " + startDate.getText().toString() + " to " + endDate.getText().toString() + "";

        canvas.drawText("Summary of Sightings", 396, 560, title);
        canvas.drawText(sentence, 396, 600, title);

        pdfDocument.finishPage(myPage);

        PdfDocument.PageInfo pageInfo;
        PdfDocument.Page speciePage;
        for(SpecieSummary specieSummary : speciesSummary){
            pageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();

            speciePage = pdfDocument.startPage(pageInfo);
            Canvas specieCanvas = speciePage.getCanvas();

            title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            title.setTextSize(34);
            title.setColor(ContextCompat.getColor(getActivity(), R.color.dark_blue));
            title.setTextAlign(Paint.Align.LEFT);
            specieCanvas.drawText(specieSummary.getSpecie(), 50, 150, title);

            title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            title.setTextSize(30);
            title.setColor(ContextCompat.getColor(getActivity(), R.color.black));
            title.setTextAlign(Paint.Align.LEFT);
            specieCanvas.drawText("- Total number of individuals: "+specieSummary.getTotalNrIndividuals(), 50, 200, title);
            specieCanvas.drawText("- Average number of individuals per sighting: "+specieSummary.getAverageNrIndividualsPerSighting(), 50, 240, title);
            specieCanvas.drawText("- Most common behavior type: "+specieSummary.getMostCommonBehavior(), 50, 280, title);
            specieCanvas.drawText("- Most common reaction to vessel: "+specieSummary.getMostCommonReaction(), 50, 320, title);
            specieCanvas.drawText("- Average Beaufort sea state: "+specieSummary.getAverageBeaufort(), 50, 360, title);
            specieCanvas.drawText("- Average trust level: "+specieSummary.getAverageTrustLvl(), 50, 400, title);
            specieCanvas.drawText("- Photos: "+specieSummary.getNrPhotos(), 50, 440, title);

            pdfDocument.finishPage(speciePage);
        }

        pdfFile = new File(Environment.getExternalStorageDirectory(), "SummaryOfSightings.pdf");

        try {
            pdfDocument.writeTo(new FileOutputStream(pdfFile));
            if(export) Toast.makeText(getActivity(), "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Couldn't export the pdf file.", Toast.LENGTH_SHORT).show();
        }
        pdfDocument.close();
    }

    private boolean checkPermission() {
        int permission1 = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    private void generateDocx(boolean export) {
        try {
            XWPFDocument xwpfDocument = new XWPFDocument();
            XWPFParagraph xwpfParagraph = xwpfDocument.createParagraph();
            XWPFRun xwpfRun = xwpfParagraph.createRun();
            for(int i = 0; i < 12; i++){
                xwpfParagraph = xwpfDocument.createParagraph(); xwpfRun = xwpfParagraph.createRun();
            }

            xwpfParagraph.setAlignment(ParagraphAlignment.CENTER);
            xwpfParagraph.setVerticalAlignment(TextAlignment.CENTER);
            xwpfRun = xwpfParagraph.createRun();

            xwpfParagraph = xwpfDocument.createParagraph();
            xwpfRun = xwpfParagraph.createRun();
            xwpfParagraph.setAlignment(ParagraphAlignment.CENTER);
            xwpfRun.setColor("0051AD");
            xwpfRun.setText("BonsAvistamentos");
            xwpfRun.setFontSize(36);
            xwpfRun.setBold(true);

            xwpfParagraph = xwpfDocument.createParagraph();
            xwpfParagraph.setAlignment(ParagraphAlignment.CENTER);
            xwpfRun = xwpfParagraph.createRun();
            xwpfRun.setText("Summary of Sightings");
            xwpfRun.setFontSize(22);

            xwpfParagraph = xwpfDocument.createParagraph();
            xwpfParagraph.setAlignment(ParagraphAlignment.CENTER);
            xwpfRun = xwpfParagraph.createRun();
            xwpfRun.setText("From " + startDate.getText().toString() + " to " + endDate.getText().toString() + "");
            xwpfRun.setFontSize(22);

            xwpfRun.addBreak(BreakType.PAGE);

            int size = speciesSummary.size();
            int i = 1;
            for(SpecieSummary specieSummary : speciesSummary){
                xwpfParagraph = xwpfDocument.createParagraph();
                xwpfRun = xwpfParagraph.createRun();
                xwpfRun.setColor("0051AD");
                xwpfRun.setText(specieSummary.getSpecie());
                xwpfRun.setFontSize(18);
                xwpfRun.setBold(true);

                xwpfParagraph = xwpfDocument.createParagraph();
                xwpfRun = xwpfParagraph.createRun();
                xwpfRun.setText("- Total number of individuals: "+specieSummary.getTotalNrIndividuals());
                xwpfRun.setFontSize(18);

                xwpfParagraph = xwpfDocument.createParagraph();
                xwpfRun = xwpfParagraph.createRun();
                xwpfRun.setText("- Average number of individuals per sighting: "+specieSummary.getAverageNrIndividualsPerSighting());
                xwpfRun.setFontSize(18);

                xwpfParagraph = xwpfDocument.createParagraph();
                xwpfRun = xwpfParagraph.createRun();
                xwpfRun.setText("- Most common behavior type: "+specieSummary.getMostCommonBehavior());
                xwpfRun.setFontSize(18);

                xwpfParagraph = xwpfDocument.createParagraph();
                xwpfRun = xwpfParagraph.createRun();
                xwpfRun.setText("- Most common reaction to vessel: "+specieSummary.getMostCommonReaction());
                xwpfRun.setFontSize(18);

                xwpfParagraph = xwpfDocument.createParagraph();
                xwpfRun = xwpfParagraph.createRun();
                xwpfRun.setText("- Average Beaufort sea state: "+specieSummary.getAverageBeaufort());
                xwpfRun.setFontSize(18);

                xwpfParagraph = xwpfDocument.createParagraph();
                xwpfRun = xwpfParagraph.createRun();
                xwpfRun.setText("- Average trust level: "+specieSummary.getAverageTrustLvl());
                xwpfRun.setFontSize(18);

                xwpfParagraph = xwpfDocument.createParagraph();
                xwpfRun = xwpfParagraph.createRun();
                xwpfRun.setText("- Photos: "+specieSummary.getNrPhotos());
                xwpfRun.setFontSize(18);

                if(size != i) xwpfRun.addBreak(BreakType.PAGE);
                i++;
            }

            FileOutputStream fileOutputStream = new FileOutputStream(Environment.getExternalStorageDirectory() + "/SummaryOfSightings.docx");
            xwpfDocument.write(fileOutputStream);

            if (fileOutputStream!=null){
                fileOutputStream.flush();
                fileOutputStream.close();
            }
            xwpfDocument.close();

            if(export) Toast.makeText(getActivity(), "DOCX file generated successfully.", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getActivity(), "Couldn't export the docx file.", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendViaEmail(String format){
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"bons_avistamentos@gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Summary of Sightings - "+startDate.getText().toString() + " to " + endDate.getText().toString());

        emailIntent.putExtra(Intent.EXTRA_TEXT, "Generated by Seaker - developed by BehindTheDesk Studios.");

        if(format == "pdf"){
            Uri uri = FileProvider.getUriForFile(getContext(),"com.example.seaker.provider", pdfFile);
            emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        }
        else if(format == "docx"){
            Uri uri = FileProvider.getUriForFile(getContext(),"com.example.seaker.provider", new File(Environment.getExternalStorageDirectory() + "/SummaryOfSightings.docx"));
            emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        }

        startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));
    }

}
