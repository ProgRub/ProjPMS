package com.example.seaker.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.example.seaker.MainActivity;
import com.example.seaker.R;
import com.example.seaker.SightingInformation;
import com.example.seaker.business.BusinessFacade;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ReportSightingFragment extends BaseFragment implements OnMapReadyCallback {

    private EditText sightingDate;
    private EditText sightingTime;
    private TextView sightingLatitude;
    private TextView sightingLongitude;
    private GoogleMap googleMap;
    private LinearLayout sightingInformationsLayout;
    private BusinessFacade businessFacade;
    private ImageButton takePhoto;
    private ImageButton uploadPhoto;
    private Button whalesBtn;
    private Button dolphinsBtn;
    private Button porpoisesBtn;
    private ArrayList<ImageButton> whalesSpeciesBtns;
    private ArrayList<ImageButton> dolphinSpeciesBtns;
    private ArrayList<ImageButton> porpoiseSpeciesBtns;
    private ImageButton reportSightingBtn;

    private boolean clickedCoordinatesOnce;

    private static final DecimalFormat df = new DecimalFormat("0.00000");


    public ReportSightingFragment() {
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
        View view = inflater.inflate(R.layout.fragment_report_sighting, container, false);
        SetButtonOnClickNextFragment(R.id.buttonBack,new TeamMemberHomeFragment(),view);

        businessFacade = BusinessFacade.getInstance();

        onStartView(view);

        return view;
    }

    private void onStartView(View view){

        clickedCoordinatesOnce = false;

        sightingDate = (EditText) view.findViewById(R.id.pickDate);
        sightingTime = (EditText) view.findViewById(R.id.pickTime);

        sightingLatitude = (TextView) view.findViewById(R.id.latitude);
        sightingLongitude = (TextView) view.findViewById(R.id.longitude);

        takePhoto = (ImageButton) view.findViewById(R.id.take_photo_btn);
        uploadPhoto = (ImageButton) view.findViewById(R.id.upload_photo_btn);

        sightingInformationsLayout = (LinearLayout) view.findViewById(R.id.sightingsInformations);

        whalesBtn = (Button) view.findViewById(R.id.scroll_to_whales_btn);
        dolphinsBtn = (Button) view.findViewById(R.id.scroll_to_dolphins_btn);
        porpoisesBtn = (Button) view.findViewById(R.id.scroll_to_porpoises_btn);

        takePhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                getActivity().startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), 100);
            }
        });

        uploadPhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                getActivity().startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), 200);
            }
        });

        sightingDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                datePicking(view);
            }
        });

        sightingTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                timePicking(view);
            }
        });

        View.OnClickListener onClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                scrollToSpecie(view);
            }
        };

        whalesBtn.setOnClickListener(onClickListener);
        dolphinsBtn.setOnClickListener(onClickListener);
        porpoisesBtn.setOnClickListener(onClickListener);

        onClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                clickSpecie(view);
            }
        };

        whalesSpeciesBtns = new ArrayList<ImageButton>();
        dolphinSpeciesBtns = new ArrayList<ImageButton>();
        porpoiseSpeciesBtns = new ArrayList<ImageButton>();

        whalesSpeciesBtns.add(view.findViewById(R.id.blue_whale_btn));
        whalesSpeciesBtns.add(view.findViewById(R.id.fin_whale_btn));
        whalesSpeciesBtns.add(view.findViewById(R.id.north_atlantic_right_whale_btn));
        whalesSpeciesBtns.add(view.findViewById(R.id.sowerbys_beaker_whale_btn));
        whalesSpeciesBtns.add(view.findViewById(R.id.blainville_whale_btn));
        whalesSpeciesBtns.add(view.findViewById(R.id.gervais_whale_btn));
        whalesSpeciesBtns.add(view.findViewById(R.id.sei_whale_btn));
        whalesSpeciesBtns.add(view.findViewById(R.id.brides_whale_btn));
        whalesSpeciesBtns.add(view.findViewById(R.id.minke_whale_btn));
        whalesSpeciesBtns.add(view.findViewById(R.id.trues_whale_btn));
        whalesSpeciesBtns.add(view.findViewById(R.id.orca_whale_btn));
        whalesSpeciesBtns.add(view.findViewById(R.id.short_finned_pilot_whale_btn));
        whalesSpeciesBtns.add(view.findViewById(R.id.humpback_whale_btn));
        whalesSpeciesBtns.add(view.findViewById(R.id.sperm_whale_btn));
        whalesSpeciesBtns.add(view.findViewById(R.id.northern_whale_btn));
        whalesSpeciesBtns.add(view.findViewById(R.id.long_finned_pilot_whale_btn));
        whalesSpeciesBtns.add(view.findViewById(R.id.false_killer_whale_btn));
        whalesSpeciesBtns.add(view.findViewById(R.id.melon_whale_btn));
        whalesSpeciesBtns.add(view.findViewById(R.id.cuviers_whale_btn));
        whalesSpeciesBtns.add(view.findViewById(R.id.pigmy_whale_btn));
        whalesSpeciesBtns.add(view.findViewById(R.id.not_specified_whale_btn));

        for(ImageButton whaleSpecie : whalesSpeciesBtns){
            whaleSpecie.setOnClickListener(onClickListener);
        }

        dolphinSpeciesBtns.add(view.findViewById(R.id.bottlenose_dolphin_btn));
        dolphinSpeciesBtns.add(view.findViewById(R.id.rissos_dolphin_btn));
        dolphinSpeciesBtns.add(view.findViewById(R.id.rough_toothed_dolphin_btn));
        dolphinSpeciesBtns.add(view.findViewById(R.id.atlantic_spotted_dolphin_btn));
        dolphinSpeciesBtns.add(view.findViewById(R.id.striped_dolphin_btn));
        dolphinSpeciesBtns.add(view.findViewById(R.id.common_dolphin_btn));
        dolphinSpeciesBtns.add(view.findViewById(R.id.frasers_dolphin_btn));
        dolphinSpeciesBtns.add(view.findViewById(R.id.not_specified_dolphin_btn));

        for(ImageButton dolphinSpecie : dolphinSpeciesBtns){
            dolphinSpecie.setOnClickListener(onClickListener);
        }

        porpoiseSpeciesBtns.add(view.findViewById(R.id.harbour_porpoise_btn));
        porpoiseSpeciesBtns.add(view.findViewById(R.id.not_specified_porpoise_btn));

        for(ImageButton porpoiseSpecie : porpoiseSpeciesBtns){
            porpoiseSpecie.setOnClickListener(onClickListener);
        }

        reportSightingBtn = (ImageButton) view.findViewById(R.id.reportSightingBtn);

        reportSightingBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                insertSightingInformationIntoBD(view);
            }
        });


        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        String date = String.valueOf(dd) + "/" + String.valueOf(mm+1) + "/" + String.valueOf(yy);

        sightingDate.setText(date);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        sightingTime.setText(String.valueOf(hour) +":"+String.valueOf(minute));

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        //CARREGAR AS COORDENADAS DEPENDENDO DA VIAGEM
        LatLng Funchal = new LatLng(32.643579, -16.914312 );
        map.addMarker(new MarkerOptions().position(Funchal).title("Funchal"));
        moveToCurrentLocation(Funchal);
        sightingLatitude.setText("Latitude: "+ df.format(Funchal.latitude));
        sightingLongitude.setText("Longitude: "+ df.format(Funchal.longitude));

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                //allPoints.add(point);
                clickedCoordinatesOnce = true;
                map.clear();
                map.addMarker(new MarkerOptions().position(point).title("Sighting").icon(BitmapDescriptorFactory.fromResource(R.drawable.sighting_pin)));
                //moveToCurrentLocation(point);
                sightingLatitude.setText("Latitude: "+ df.format(point.latitude));
                sightingLongitude.setText("Longitude: "+ df.format(point.longitude));
            }
        });
    }

    private void moveToCurrentLocation(LatLng currentLocation)
    {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,15));
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }

    public void clickSpecie(View view){
        if(String.valueOf(view.getTag()).contains("Selected")){
            unselectSpecie(view);
        }else{
            selectedSpecie(view);

            LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.sighting_information_box, null);

            setViewID(view, v);

            buttonListenersSightingInfo(v);
            sightingInformations.get(sightingInformations.size() - 1).setSpecieName(String.valueOf(view.getTag()));

            TextView textView = (TextView) v.findViewById(R.id.title);
            textView.setText(String.valueOf(view.getTag()) + " Sighting");

            view.setTag("Selected "+String.valueOf(view.getTag()));

            sightingInformationsLayout.addView(v);
        }
    }

    public void scrollToSpecie(View view){
        HorizontalScrollView species = (HorizontalScrollView) getView().findViewById(R.id.all_species);

        RelativeLayout whales = (RelativeLayout) getView().findViewById(R.id.whales_species);
        RelativeLayout dolphins = (RelativeLayout) getView().findViewById(R.id.dolphins_species);
        RelativeLayout porpoise = (RelativeLayout) getView().findViewById(R.id.porpoises_species);

        switch(view.getId()){
            case R.id.scroll_to_whales_btn:
                focusOnView(species, whales);
                ((Button)view).setTypeface(null, Typeface.BOLD);
                ((Button)getView().findViewById(R.id.scroll_to_dolphins_btn)).setTypeface(null, Typeface.NORMAL);
                ((Button)getView().findViewById(R.id.scroll_to_porpoises_btn)).setTypeface(null, Typeface.NORMAL);
                break;
            case R.id.scroll_to_dolphins_btn:
                focusOnView(species, dolphins);
                ((Button)view).setTypeface(null, Typeface.BOLD);
                ((Button)getView().findViewById(R.id.scroll_to_whales_btn)).setTypeface(null, Typeface.NORMAL);
                ((Button)getView().findViewById(R.id.scroll_to_porpoises_btn)).setTypeface(null, Typeface.NORMAL);
                break;
            case R.id.scroll_to_porpoises_btn:
                focusOnView(species, porpoise);
                ((Button)view).setTypeface(null, Typeface.BOLD);
                ((Button)getView().findViewById(R.id.scroll_to_whales_btn)).setTypeface(null, Typeface.NORMAL);
                ((Button)getView().findViewById(R.id.scroll_to_dolphins_btn)).setTypeface(null, Typeface.NORMAL);
                break;
        }
    }

    private final void focusOnView(final HorizontalScrollView scroll, final View view) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                int vLeft = view.getLeft();
                int vRight = view.getRight();
                int sWidth = scroll.getWidth();
                scroll.smoothScrollTo(((vLeft + vRight - sWidth) / 2), 0);
            }
        });
    }

    public void datePicking(View view) {
        EditText editText = (EditText) getView().findViewById(R.id.pickDate);

        String actualValue = editText.getText().toString();
        String previousDate[] = actualValue.split("/");

        int yy = Integer.parseInt(previousDate[2]);
        int mm = Integer.parseInt(previousDate[1])-1;
        int dd = Integer.parseInt(previousDate[0]);

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

    public void timePicking(View view) {
        EditText editText = (EditText) getView().findViewById(R.id.pickTime);
        String actualValue = editText.getText().toString();
        String previousTime[] = actualValue.split(":");

        int hour = Integer.parseInt(previousTime[0]);
        int minute = Integer.parseInt(previousTime[1]);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                editText.setText( selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void setViewID(View view, View v) {
        switch (view.getId()) {
            case R.id.blue_whale_btn:
                v.setId(R.id.blue_whale_sighting);
                break;
            case R.id.fin_whale_btn:
                v.setId(R.id.fin_whale_sighting);
                break;
            case R.id.north_atlantic_right_whale_btn:
                v.setId(R.id.north_atlantic_right_whale_sighting);
                break;
            case R.id.sowerbys_beaker_whale_btn:
                v.setId(R.id.sowerbys_beaker_whale_sighting);
                break;
            case R.id.blainville_whale_btn:
                v.setId(R.id.blainville_whale_sighting);
                break;
            case R.id.gervais_whale_btn:
                v.setId(R.id.gervais_whale_sighting);
                break;
            case R.id.sei_whale_btn:
                v.setId(R.id.sei_whale_sighting);
                break;
            case R.id.brides_whale_btn:
                v.setId(R.id.brides_whale_sighting);
                break;
            case R.id.minke_whale_btn:
                v.setId(R.id.minke_whale_sighting);
                break;
            case R.id.trues_whale_btn:
                v.setId(R.id.trues_whale_sighting);
                break;
            case R.id.orca_whale_btn:
                v.setId(R.id.orca_whale_sighting);
                break;
            case R.id.short_finned_pilot_whale_btn:
                v.setId(R.id.short_finned_pilot_whale_sighting);
                break;
            case R.id.humpback_whale_btn:
                v.setId(R.id.humpback_whale_sighting);
                break;
            case R.id.sperm_whale_btn:
                v.setId(R.id.sperm_whale_sighting);
                break;
            case R.id.northern_whale_btn:
                v.setId(R.id.northern_whale_sighting);
                break;
            case R.id.long_finned_pilot_whale_btn:
                v.setId(R.id.long_finned_pilot_whale_sighting);
                break;
            case R.id.false_killer_whale_btn:
                v.setId(R.id.false_killer_whale_sighting);
                break;
            case R.id.melon_whale_btn:
                v.setId(R.id.melon_whale_sighting);
                break;
            case R.id.cuviers_whale_btn:
                v.setId(R.id.cuviers_whale_sighting);
                break;
            case R.id.pigmy_whale_btn:
                v.setId(R.id.pigmy_whale_sighting);
                break;
            case R.id.not_specified_whale_btn:
                v.setId(R.id.not_specified_whale_sighting);
                break;
            case R.id.bottlenose_dolphin_btn:
                v.setId(R.id.bottlenose_dolphin_sighting);
                break;
            case R.id.rissos_dolphin_btn:
                v.setId(R.id.rissos_dolphin_sighting);
                break;
            case R.id.rough_toothed_dolphin_btn:
                v.setId(R.id.rough_toothed_dolphin_sighting);
                break;
            case R.id.atlantic_spotted_dolphin_btn:
                v.setId(R.id.atlantic_spotted_dolphin_sighting);
                break;
            case R.id.striped_dolphin_btn:
                v.setId(R.id.striped_dolphin_sighting);
                break;
            case R.id.common_dolphin_btn:
                v.setId(R.id.common_dolphin_sighting);
                break;
            case R.id.frasers_dolphin_btn:
                v.setId(R.id.frasers_dolphin_sighting);
                break;
            case R.id.not_specified_dolphin_btn:
                v.setId(R.id.not_specified_dolphin_sighting);
                break;
            case R.id.harbour_porpoise_btn:
                v.setId(R.id.harbour_porpoise_sighting);
                break;
            case R.id.not_specified_porpoise_btn:_btn:
            v.setId(R.id.not_specified_porpoise_sighting);
                break;
        }
    }

    public Drawable getThumb(int progress, View thumbView) {
        ((TextView) thumbView.findViewById(R.id.tvProgress)).setText(progress + "");

        thumbView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Bitmap bitmap = Bitmap.createBitmap(thumbView.getMeasuredWidth(), thumbView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        thumbView.layout(0, 0, thumbView.getMeasuredWidth(), thumbView.getMeasuredHeight());
        thumbView.draw(canvas);

        return new BitmapDrawable(getResources(), bitmap);
    }


    //CONTEM INFORMAÇÃO SOBRE OS SIGHTINGS:
    ArrayList<SightingInformation> sightingInformations = new ArrayList<SightingInformation>();
    ArrayList<ToggleButton> auxToggle;

    private void buttonListenersSightingInfo(View v){
        SightingInformation sighting = new SightingInformation(v.getId());
        auxToggle = new ArrayList<ToggleButton>();

        auxToggle.add((ToggleButton) v.findViewById(R.id.nr_individuals_1));
        auxToggle.add((ToggleButton) v.findViewById(R.id.nr_individuals_2));
        auxToggle.add((ToggleButton) v.findViewById(R.id.nr_individuals_3));
        auxToggle.add((ToggleButton) v.findViewById(R.id.nr_individuals_4));
        auxToggle.add((ToggleButton) v.findViewById(R.id.nr_individuals_5));
        auxToggle.add((ToggleButton) v.findViewById(R.id.nr_individuals_6_10));
        auxToggle.add((ToggleButton) v.findViewById(R.id.nr_individuals_10_20));
        auxToggle.add((ToggleButton) v.findViewById(R.id.nr_individuals_25_50));
        auxToggle.add((ToggleButton) v.findViewById(R.id.nr_individuals_50_100));
        auxToggle.add((ToggleButton) v.findViewById(R.id.nr_individuals_100));

        for(ToggleButton tog : auxToggle){
            sighting.addNrindividuals(tog);
        }

        auxToggle = new ArrayList<ToggleButton>();

        auxToggle.add((ToggleButton) v.findViewById(R.id.nr_offspring_0));
        auxToggle.add((ToggleButton) v.findViewById(R.id.nr_offspring_1));
        auxToggle.add((ToggleButton) v.findViewById(R.id.nr_offspring_2));
        auxToggle.add((ToggleButton) v.findViewById(R.id.nr_offspring_3));
        auxToggle.add((ToggleButton) v.findViewById(R.id.nr_offspring_4));
        auxToggle.add((ToggleButton) v.findViewById(R.id.nr_offspring_5));
        auxToggle.add((ToggleButton) v.findViewById(R.id.nr_offspring_6_10));
        auxToggle.add((ToggleButton) v.findViewById(R.id.nr_offspring_10_20));
        auxToggle.add((ToggleButton) v.findViewById(R.id.nr_offspring_25_50));
        auxToggle.add((ToggleButton) v.findViewById(R.id.nr_offspring_50_100));
        auxToggle.add((ToggleButton) v.findViewById(R.id.nr_offspring_100));

        for(ToggleButton tog : auxToggle){
            sighting.addNroffspring(tog);
        }

        auxToggle = new ArrayList<ToggleButton>();

        auxToggle.add((ToggleButton) v.findViewById(R.id.traveling_behavior));
        auxToggle.add((ToggleButton) v.findViewById(R.id.eating_behavior));
        auxToggle.add((ToggleButton) v.findViewById(R.id.resting_behavior));
        auxToggle.add((ToggleButton) v.findViewById(R.id.social_int_behavior));
        auxToggle.add((ToggleButton) v.findViewById(R.id.other_behavior));

        sighting.setBehavior_type(auxToggle);

        auxToggle = new ArrayList<ToggleButton>();

        auxToggle.add((ToggleButton) v.findViewById(R.id.none_reaction));
        auxToggle.add((ToggleButton) v.findViewById(R.id.approach_reaction));
        auxToggle.add((ToggleButton) v.findViewById(R.id.avoidance_reaction));
        auxToggle.add((ToggleButton) v.findViewById(R.id.other_reaction));

        sighting.setReactions_to_vessel(auxToggle);

        LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View thumbView = vi.inflate(R.layout.layout_seekbar_thumb, null);

        ((SeekBar) v.findViewById(R.id.beaufort_slider)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBar.setThumb(getThumb(progress, thumbView));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        ((TextView) thumbView.findViewById(R.id.tvProgress)).setText("6");

        ((SeekBar) v.findViewById(R.id.beaufort_slider)).setThumb(getThumb(6, thumbView));

        sighting.setBeaufortSeaState(((SeekBar) v.findViewById(R.id.beaufort_slider)));

        auxToggle = new ArrayList<ToggleButton>();

        auxToggle.add((ToggleButton) v.findViewById(R.id.low_trust_level));
        auxToggle.add((ToggleButton) v.findViewById(R.id.middle_trust_level));
        auxToggle.add((ToggleButton) v.findViewById(R.id.high_trust_level));

        for(ToggleButton tog : auxToggle){
            sighting.addTrustLevel(tog);
        }

        sightingInformations.add(sighting);

    }


    public void unselectSpecie(View view) {

        view.setTag(String.valueOf(view.getTag()).replace("Selected ", ""));

        LinearLayout insertPoint = (LinearLayout) getView().findViewById(R.id.sightingsInformations);

        ImageButton btn1 = (ImageButton) getView().findViewById(view.getId());

        Drawable drawable;

        switch (view.getId()) {
            case R.id.blue_whale_btn:
                drawable = getResources().getDrawable(R.drawable.blue_whale_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(getView().findViewById(R.id.blue_whale_sighting));
                deleteFromArray(R.id.blue_whale_sighting);
                break;
            case R.id.fin_whale_btn:
                drawable = getResources().getDrawable(R.drawable.fin_whale_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(getView().findViewById(R.id.fin_whale_sighting));
                deleteFromArray(R.id.fin_whale_sighting);
                break;
            case R.id.north_atlantic_right_whale_btn:
                drawable = getResources().getDrawable(R.drawable.north_atlantic_right_whale_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(getView().findViewById(R.id.north_atlantic_right_whale_sighting));
                deleteFromArray(R.id.north_atlantic_right_whale_sighting);
                break;
            case R.id.sowerbys_beaker_whale_btn:
                drawable = getResources().getDrawable(R.drawable.sowerbys_beaker_whale_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(getView().findViewById(R.id.sowerbys_beaker_whale_sighting));
                deleteFromArray(R.id.sowerbys_beaker_whale_sighting);
                break;
            case R.id.blainville_whale_btn:
                drawable = getResources().getDrawable(R.drawable.blainville_whale_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(getView().findViewById(R.id.blainville_whale_sighting));
                deleteFromArray(R.id.blainville_whale_sighting);
                break;
            case R.id.gervais_whale_btn:
                drawable = getResources().getDrawable(R.drawable.gervais_whale_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(getView().findViewById(R.id.gervais_whale_sighting));
                deleteFromArray(R.id.gervais_whale_sighting);
                break;
            case R.id.sei_whale_btn:
                drawable = getResources().getDrawable(R.drawable.sei_whale_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(getView().findViewById(R.id.sei_whale_sighting));
                deleteFromArray(R.id.sei_whale_sighting);
                break;
            case R.id.brides_whale_btn:
                drawable = getResources().getDrawable(R.drawable.brides_whale_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(getView().findViewById(R.id.brides_whale_sighting));
                deleteFromArray(R.id.brides_whale_sighting);
                break;
            case R.id.minke_whale_btn:
                drawable = getResources().getDrawable(R.drawable.minke_whale_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(getView().findViewById(R.id.minke_whale_sighting));
                deleteFromArray(R.id.minke_whale_sighting);
                break;
            case R.id.trues_whale_btn:
                drawable = getResources().getDrawable(R.drawable.trues_whale_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(getView().findViewById(R.id.trues_whale_sighting));
                deleteFromArray(R.id.trues_whale_sighting);
                break;
            case R.id.orca_whale_btn:
                drawable = getResources().getDrawable(R.drawable.orca_whale_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(getView().findViewById(R.id.orca_whale_sighting));
                deleteFromArray(R.id.orca_whale_sighting);
                break;
            case R.id.short_finned_pilot_whale_btn:
                drawable = getResources().getDrawable(R.drawable.short_finned_pilot_whale_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(getView().findViewById(R.id.short_finned_pilot_whale_sighting));
                deleteFromArray(R.id.short_finned_pilot_whale_sighting);
                break;
            case R.id.humpback_whale_btn:
                drawable = getResources().getDrawable(R.drawable.humpback_whale_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(getView().findViewById(R.id.humpback_whale_sighting));
                deleteFromArray(R.id.humpback_whale_sighting);
                break;
            case R.id.sperm_whale_btn:
                drawable = getResources().getDrawable(R.drawable.sperm_whale_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(getView().findViewById(R.id.sperm_whale_sighting));
                deleteFromArray(R.id.sperm_whale_sighting);
                break;
            case R.id.northern_whale_btn:
                drawable = getResources().getDrawable(R.drawable.northern_whale_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(getView().findViewById(R.id.northern_whale_sighting));
                deleteFromArray(R.id.northern_whale_sighting);
                break;
            case R.id.long_finned_pilot_whale_btn:
                drawable = getResources().getDrawable(R.drawable.long_finned_pilot_whale_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(getView().findViewById(R.id.long_finned_pilot_whale_sighting));
                deleteFromArray(R.id.long_finned_pilot_whale_sighting);
                break;
            case R.id.false_killer_whale_btn:
                drawable = getResources().getDrawable(R.drawable.false_killer_whale_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(getView().findViewById(R.id.false_killer_whale_sighting));
                deleteFromArray(R.id.false_killer_whale_sighting);
                break;
            case R.id.melon_whale_btn:
                drawable = getResources().getDrawable(R.drawable.melon_whale_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(getView().findViewById(R.id.melon_whale_sighting));
                deleteFromArray(R.id.melon_whale_sighting);
                break;
            case R.id.cuviers_whale_btn:
                drawable = getResources().getDrawable(R.drawable.cuviers_whale_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(getView().findViewById(R.id.cuviers_whale_sighting));
                deleteFromArray(R.id.cuviers_whale_sighting);
                break;
            case R.id.pigmy_whale_btn:
                drawable = getResources().getDrawable(R.drawable.pigmy_whale_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(getView().findViewById(R.id.pigmy_whale_sighting));
                deleteFromArray(R.id.pigmy_whale_sighting);
                break;
            case R.id.not_specified_whale_btn:
                drawable = getResources().getDrawable(R.drawable.not_specified_whale_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(getView().findViewById(R.id.not_specified_whale_sighting));
                deleteFromArray(R.id.not_specified_whale_sighting);
                break;
            case R.id.bottlenose_dolphin_btn:
                drawable = getResources().getDrawable(R.drawable.bottlenose_dolphin_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(getView().findViewById(R.id.bottlenose_dolphin_sighting));
                deleteFromArray(R.id.bottlenose_dolphin_sighting);
                break;
            case R.id.rissos_dolphin_btn:
                drawable = getResources().getDrawable(R.drawable.rissos_dolphin_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(getView().findViewById(R.id.rissos_dolphin_sighting));
                deleteFromArray(R.id.rissos_dolphin_sighting);
                break;
            case R.id.rough_toothed_dolphin_btn:
                drawable = getResources().getDrawable(R.drawable.rough_toothed_dolphin_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(getView().findViewById(R.id.rough_toothed_dolphin_sighting));
                deleteFromArray(R.id.rough_toothed_dolphin_sighting);
                break;
            case R.id.atlantic_spotted_dolphin_btn:
                drawable = getResources().getDrawable(R.drawable.atlantic_spotted_dolphin_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(getView().findViewById(R.id.atlantic_spotted_dolphin_sighting));
                deleteFromArray(R.id.atlantic_spotted_dolphin_sighting);
                break;
            case R.id.striped_dolphin_btn:
                drawable = getResources().getDrawable(R.drawable.striped_dolphin_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(getView().findViewById(R.id.striped_dolphin_sighting));
                deleteFromArray(R.id.striped_dolphin_sighting);
                break;
            case R.id.common_dolphin_btn:
                drawable = getResources().getDrawable(R.drawable.common_dolphin_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(getView().findViewById(R.id.common_dolphin_sighting));
                deleteFromArray(R.id.common_dolphin_sighting);
                break;
            case R.id.frasers_dolphin_btn:
                drawable = getResources().getDrawable(R.drawable.frasers_dolphin_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(getView().findViewById(R.id.frasers_dolphin_sighting));
                deleteFromArray(R.id.frasers_dolphin_sighting);
                break;
            case R.id.not_specified_dolphin_btn:
                drawable = getResources().getDrawable(R.drawable.not_specified_dolphin_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(getView().findViewById(R.id.not_specified_dolphin_sighting));
                deleteFromArray(R.id.not_specified_dolphin_sighting);
                break;
            case R.id.harbour_porpoise_btn:
                drawable = getResources().getDrawable(R.drawable.harbour_porpoise_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(getView().findViewById(R.id.harbour_porpoise_sighting));
                deleteFromArray(R.id.harbour_porpoise_sighting);
                break;
            case R.id.not_specified_porpoise_btn:_btn:
            drawable = getResources().getDrawable(R.drawable.not_specified_porpoise_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(getView().findViewById(R.id.not_specified_porpoise_sighting));
                deleteFromArray(R.id.not_specified_porpoise_sighting);
                break;
        }
    }

    private void deleteFromArray(int sightingId){
        int i=0;
        for(SightingInformation sighting : sightingInformations){
            if(sighting.getSightingBoxID() == sightingId){
                sightingInformations.remove(i);
                return;
            }
            i++;
        }
    }

    public void selectedSpecie(View view){
        ImageButton btn1 = (ImageButton) getView().findViewById(view.getId());

        Drawable drawable;
        switch (view.getId()){
            case R.id.blue_whale_btn:
                drawable = getResources().getDrawable( R.drawable.selected_blue_whale_btn );
                btn1.setBackgroundDrawable(drawable);
                break;
            case R.id.fin_whale_btn:
                drawable = getResources().getDrawable( R.drawable.selected_fin_whale_btn );
                btn1.setBackgroundDrawable(drawable);
                break;
            case R.id.north_atlantic_right_whale_btn:
                drawable = getResources().getDrawable( R.drawable.selected_north_atlantic_right_whale_btn );
                btn1.setBackgroundDrawable(drawable);
                break;
            case R.id.sowerbys_beaker_whale_btn:
                drawable = getResources().getDrawable( R.drawable.selected_sowerbys_beaker_whale_btn );
                btn1.setBackgroundDrawable(drawable);
                break;
            case R.id.blainville_whale_btn:
                drawable = getResources().getDrawable( R.drawable.selected_blainville_whale_btn );
                btn1.setBackgroundDrawable(drawable);
                break;
            case R.id.gervais_whale_btn:
                drawable = getResources().getDrawable( R.drawable.selected_gervais_whale_btn );
                btn1.setBackgroundDrawable(drawable);
                break;
            case R.id.sei_whale_btn:
                drawable = getResources().getDrawable( R.drawable.selected_sei_whale_btn );
                btn1.setBackgroundDrawable(drawable);
                break;
            case R.id.brides_whale_btn:
                drawable = getResources().getDrawable( R.drawable.selected_brides_whale_btn );
                btn1.setBackgroundDrawable(drawable);
                break;
            case R.id.minke_whale_btn:
                drawable = getResources().getDrawable( R.drawable.selected_minke_whale_btn );
                btn1.setBackgroundDrawable(drawable);
                break;
            case R.id.trues_whale_btn:
                drawable = getResources().getDrawable( R.drawable.selected_trues_whale_btn );
                btn1.setBackgroundDrawable(drawable);
                break;
            case R.id.orca_whale_btn:
                drawable = getResources().getDrawable( R.drawable.selected_orca_whale_btn );
                btn1.setBackgroundDrawable(drawable);
                break;
            case R.id.short_finned_pilot_whale_btn:
                drawable = getResources().getDrawable( R.drawable.selected_short_finned_pilot_whale_btn );
                btn1.setBackgroundDrawable(drawable);
                break;
            case R.id.humpback_whale_btn:
                drawable = getResources().getDrawable( R.drawable.selected_humpback_whale_btn );
                btn1.setBackgroundDrawable(drawable);
                break;
            case R.id.sperm_whale_btn:
                drawable = getResources().getDrawable( R.drawable.selected_sperm_whale_btn );
                btn1.setBackgroundDrawable(drawable);
                break;
            case R.id.northern_whale_btn:
                drawable = getResources().getDrawable( R.drawable.selected_northern_whale_btn );
                btn1.setBackgroundDrawable(drawable);
                break;
            case R.id.long_finned_pilot_whale_btn:
                drawable = getResources().getDrawable( R.drawable.selected_long_finned_pilot_whale_btn );
                btn1.setBackgroundDrawable(drawable);
                break;
            case R.id.false_killer_whale_btn:
                drawable = getResources().getDrawable( R.drawable.selected_false_killer_whale_btn );
                btn1.setBackgroundDrawable(drawable);
                break;
            case R.id.melon_whale_btn:
                drawable = getResources().getDrawable( R.drawable.selected_melon_whale_btn );
                btn1.setBackgroundDrawable(drawable);
                break;
            case R.id.cuviers_whale_btn:
                drawable = getResources().getDrawable( R.drawable.selected_cuviers_whale_btn );
                btn1.setBackgroundDrawable(drawable);
                break;
            case R.id.pigmy_whale_btn:
                drawable = getResources().getDrawable( R.drawable.selected_pigmy_whale_btn );
                btn1.setBackgroundDrawable(drawable);
                break;
            case R.id.not_specified_whale_btn:
                drawable = getResources().getDrawable( R.drawable.selected_not_specified_whale_btn );
                btn1.setBackgroundDrawable(drawable);
                break;
            case R.id.bottlenose_dolphin_btn:
                drawable = getResources().getDrawable(R.drawable.selected_bottlenose_dolphin_btn);
                btn1.setBackgroundDrawable(drawable);
                break;
            case R.id.rissos_dolphin_btn:
                drawable = getResources().getDrawable(R.drawable.selected_rissos_dolphin_btn);
                btn1.setBackgroundDrawable(drawable);
                break;
            case R.id.rough_toothed_dolphin_btn:
                drawable = getResources().getDrawable(R.drawable.selected_rough_toothed_dolphin_btn);
                btn1.setBackgroundDrawable(drawable);
                break;
            case R.id.atlantic_spotted_dolphin_btn:
                drawable = getResources().getDrawable(R.drawable.selected_atlantic_spotted_dolphin_btn);
                btn1.setBackgroundDrawable(drawable);
                break;
            case R.id.striped_dolphin_btn:
                drawable = getResources().getDrawable(R.drawable.selected_striped_dolphin_btn);
                btn1.setBackgroundDrawable(drawable);
                break;
            case R.id.common_dolphin_btn:
                drawable = getResources().getDrawable(R.drawable.selected_common_dolphin_btn);
                btn1.setBackgroundDrawable(drawable);
                break;
            case R.id.frasers_dolphin_btn:
                drawable = getResources().getDrawable(R.drawable.selected_frasers_dolphin_btn);
                btn1.setBackgroundDrawable(drawable);
                break;
            case R.id.not_specified_dolphin_btn:
                drawable = getResources().getDrawable(R.drawable.selected_not_specified_dolphin_btn);
                btn1.setBackgroundDrawable(drawable);
                break;
            case R.id.harbour_porpoise_btn:
                drawable = getResources().getDrawable(R.drawable.selected_harbour_porpoise_btn);
                btn1.setBackgroundDrawable(drawable);
                break;
            case R.id.not_specified_porpoise_btn:_btn:
            drawable = getResources().getDrawable(R.drawable.selected_not_specified_porpoise_btn);
                btn1.setBackgroundDrawable(drawable);
                break;
        }
    }


    public void createSightingDTO(){
        EditText editText = (EditText) getView().findViewById(R.id.pickDate);
        String day = editText.getText().toString();
        EditText editText1 = (EditText) getView().findViewById(R.id.pickTime);
        String hour = editText1.getText().toString();
        TextView textView = (TextView) getView().findViewById(R.id.latitude);
        String latitude = textView.getText().toString();
        TextView textView1 = (TextView) getView().findViewById(R.id.longitude);
        String longitude = textView1.getText().toString();
        EditText editText3 = (EditText) getView().findViewById(R.id.sighting_comment);
        String comment = editText3.getText().toString();

        ArrayList<String> speciesNames = new ArrayList<>();

        for(SightingInformation sighting : sightingInformations){
            speciesNames.add(sighting.getSpecieName());
        }

        String[] partsLatitude = latitude.split(": ");
        String[] partsLongitude = longitude.split(": ");

        double latitudeValue = Double.parseDouble(partsLatitude[1]);
        double longitudeValue = Double.parseDouble(partsLongitude[1]);

        //Data e Tempo não dá para fazer parse para localdate e localtime, dá erro (não sei pq)
        //O confidence level é para cada espécie (não para o avistamento)
        //Falta conseguir aceder ao nome do team member, nome do barco e nome das zonas
        //Como se sabe a relação entre os sightingDTO o animalDTOs?
        //O beaufort sea state, pela forma q está feito, deve ser para cada especie avistada
        //Que ID vamos dar?

        //SightingDTO sightingDTO = new SightingDTO(1, day, hour, 4, longitudeValue, latitudeValue, "LOW",  comment, "TEAM_MEMBER_NAME", "PHOTOS", "BOAT_NAME", "ZONE_NAMES", speciesNames);
        //businessFacade.addSighting(sightingDTO);
    }

    public void testingBtn(View view){
        for(SightingInformation sighting : sightingInformations){
            Log.d("TESTING", sighting.toString() );
        }
    }

    public void insertSightingInformationIntoBD(View view){

        EditText editText = (EditText) getView().findViewById(R.id.pickDate);
        String day = editText.getText().toString();
        EditText editText1 = (EditText) getView().findViewById(R.id.pickTime);
        String hour = editText1.getText().toString();
        TextView textView = (TextView) getView().findViewById(R.id.latitude);
        String latitude = textView.getText().toString();
        TextView textView1 = (TextView) getView().findViewById(R.id.longitude);
        String longitude = textView1.getText().toString();
        EditText editText3 = (EditText) getView().findViewById(R.id.sighting_comment);
        String comment = editText3.getText().toString();
        String animal = "";

        for(SightingInformation sighting : sightingInformations){
            animal += sighting.toString();
        }

        String insertSightingUrl = "http://IP/seaker/insertsighting.php";
        try {
            URL url = new URL(insertSightingUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("day", "UTF-8")+"="+URLEncoder.encode(day, "UTF-8")+"&"
                    + URLEncoder.encode("hour", "UTF-8")+"="+URLEncoder.encode(hour, "UTF-8")+"&"
                    + URLEncoder.encode("sea_state", "UTF-8")+"="+URLEncoder.encode("1", "UTF-8")+"&"
                    + URLEncoder.encode("latitude", "UTF-8")+"="+URLEncoder.encode(latitude, "UTF-8")+"&"
                    + URLEncoder.encode("longitude", "UTF-8")+"="+URLEncoder.encode(longitude, "UTF-8")+"&"
                    + URLEncoder.encode("comment", "UTF-8")+"="+URLEncoder.encode(comment, "UTF-8")+"&"
                    + URLEncoder.encode("person_id", "UTF-8")+"="+URLEncoder.encode("3", "UTF-8")+"&"
                    + URLEncoder.encode("boat_id", "UTF-8")+"="+URLEncoder.encode("1", "UTF-8")+"&"
                    + URLEncoder.encode("animal", "UTF-8")+"="+URLEncoder.encode(animal, "UTF-8");

            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String result = "";
            String line = "";
            while((line = bufferedReader.readLine())!=null){
                result += line;
            }
            Snackbar.make(view, result, Snackbar.LENGTH_LONG).show();

            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(validateSightingReport()){ //se todos os campos obrigatórios estão preenchidos
            sightingInformations.clear();
            ((MainActivity)getActivity()).onButtonShowPopupWindowClick(view, "Sighting successfully reported!");
            //Espera 2 segundos
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    MainActivity.switchFragment(new TeamMemberHomeFragment());
                }
            }, 2000);

        }else{ //se não preencheu todos os campos obrigatórios
            ((MainActivity)getActivity()).onButtonShowPopupWindowClick(view, "Required fields missing!");
        }
    }

    private boolean validateSightingReport(){ //true se os campos obrigatórios estão preenchidos

        if(!clickedCoordinatesOnce) return false; //se não clicou no mapa nenhuma vez

        if(sightingDate.getText().toString().equals("")) return false; //se não preencheu a data
        if(sightingTime.getText().toString().equals("")) return false; //se não preencheu o tempo

        if(sightingInformations.size() == 0) return false; //se não escolheu nenhuma espécie

        for(SightingInformation sighting : sightingInformations){
            if(sighting.getNumberOfIndividualsString().equals("ERROR")) return false; //se não preencheu o número de individuos de alguma espécie
        }

        return true;
    }

    public void getAllSightingsInformations(View view){
        String insertSightingUrl = "http://IP/seaker/getallsightings.php";
        try {
            URL url = new URL(insertSightingUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setDoInput(true);
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String result = "";
            String line = "";
            while((line = bufferedReader.readLine())!=null){
                result += line;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            Log.d("TODOS OS AVISTAMENTOS: ", result);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}