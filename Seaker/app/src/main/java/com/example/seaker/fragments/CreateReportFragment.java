package com.example.seaker.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentContainerView;

import com.example.seaker.MainActivity;
import com.example.seaker.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

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
                createSummary(view);
            }
        });

        exportReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //EXPORT REPORT
            }
        });

        shareViaEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SHARE VIA EMAIL
            }
        });

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

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);

        mapBox.setVisibility(View.VISIBLE);
        totalNrIndividuals.setVisibility(View.VISIBLE);
        chooseFormatText.setVisibility(View.VISIBLE);
        pdfFormat.setVisibility(View.VISIBLE);
        docxFormat.setVisibility(View.VISIBLE);
        exportReportBtn.setVisibility(View.VISIBLE);
        shareViaEmailBtn.setVisibility(View.VISIBLE);

        //Apenas para testar:
        nrSightingsFound.setText("5 sightings found.");
        totalNrIndividuals.setText("Total number of individuals: " + "34");
        addSpecieSummary("Blue Whale", "12", "2", "Social Interaction", "Approach", "2", "High", "8");
        addSpecieSummary("Fin Whale", "5", "1", "Other", "None", "4", "Low", "2");

    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        MarkerOptions options = new MarkerOptions();
        ArrayList<LatLng> latlngs = new ArrayList<>();

        //Para testar inserção do pin nas coordenadas ao carregar o mapa:
        latlngs.add(new LatLng(32.644313, -16.914312));
        latlngs.add(new LatLng(32.643579, -16.915613));
        latlngs.add(new LatLng(32.642859, -16.916211));
        latlngs.add(new LatLng(32.649159, -16.913136));
        latlngs.add(new LatLng(32.646859, -16.917214));

        int sightingNr = 1;
        for (LatLng point : latlngs) {
            options.position(point);
            options.title("Sighting " + sightingNr);
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.sighting_pin));
            map.addMarker(options);
            sightingNr++;
        }
    }

}
