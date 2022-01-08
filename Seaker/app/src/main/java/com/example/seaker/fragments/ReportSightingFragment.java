package com.example.seaker.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.seaker.DataViewModel;
import com.example.seaker.MQTTHelper;
import com.example.seaker.MainActivity;
import com.example.seaker.R;
import com.example.seaker.SightingInformation;
import com.example.seaker.business.BusinessFacade;
import com.example.seaker.jsonwriter.AnimalJson;
import com.example.seaker.jsonwriter.JsonWriter;
import com.example.seaker.jsonwriter.SightingJson;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
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
    private SeekBar beaufortSeekBar;
    private LinearLayout sightingInformationsLayout;
    private ImageView noSelectedSpecies;
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
    private DataViewModel model;
    private JsonWriter jsonWriter;
    private AutoCompleteTextView searchBar;
    private LinearLayout navSightingBoxBtns;
    private ArrayList<Button> sightingBoxesButtons;
    private MQTTHelper mqtt;

    public static final String ip = "192.168.1.80"; //erro propositadamente, para n se esquecerem de alterar :P

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

        jsonWriter = new JsonWriter();
        businessFacade = BusinessFacade.getInstance();

        try {
            mqtt = MQTTHelper.getInstance(getActivity().getApplicationContext());
            if(!mqtt.isConnected()) mqtt.tryConnect(getActivity().getApplicationContext());
        } catch (MqttException e) {
            e.printStackTrace();
        }

        onStartView(view);

        return view;
    }

    private void onStartView(View view){
        model = new ViewModelProvider(requireActivity()).get(DataViewModel.class);
        clickedCoordinatesOnce = false;

        sightingDate = (EditText) view.findViewById(R.id.pickDate);
        sightingTime = (EditText) view.findViewById(R.id.pickTime);

        sightingLatitude = (TextView) view.findViewById(R.id.latitude);
        sightingLongitude = (TextView) view.findViewById(R.id.longitude);

        takePhoto = (ImageButton) view.findViewById(R.id.take_photo_btn);
        uploadPhoto = (ImageButton) view.findViewById(R.id.upload_photo_btn);

        beaufortSeekBar = (SeekBar) view.findViewById(R.id.beaufort_slider);

        sightingInformationsLayout = (LinearLayout) view.findViewById(R.id.sightingsInformations);

        noSelectedSpecies = (ImageView) view.findViewById(R.id.no_selected_species);

        whalesBtn = (Button) view.findViewById(R.id.scroll_to_whales_btn);
        dolphinsBtn = (Button) view.findViewById(R.id.scroll_to_dolphins_btn);
        porpoisesBtn = (Button) view.findViewById(R.id.scroll_to_porpoises_btn);

        searchBar = (AutoCompleteTextView) view.findViewById(R.id.search_bar);

        String[] species = new String[]{"Blue Whale", "Fin Whale", "North Atlantic Right Whale",
                "Sei Whale", "Minke Whale", "Bryde's Whale", "Humpback Whale",
                "Sperm Whale", "Northern Bottlenose Whale", "Cuvier's Beaked Whale",
                "Blainville's Beaked Whale", "Gervais' Beaked Whale", "True's Beaked Whale",
                "Orca Whale", "Short Finned Pilot Whale", "Long Finned Pilot Whale",
                "False Killer Whale", "Melon Whale", "Pigmy Whale", "Sowerby's Beaker Whale",
                "Not Specified Whale", "Risso's Dolphin", "Bottlenose Dolphin", "Rough Toothed Dolphin",
                "Atlantic Spotted Dolphin", "Striped Dolphin", "Common Dolphin", "Fraser's Dolphin",
                "Not Specified Dolphin", "Harbour Porpoise", "Not Specified Porpoise"};

        searchBar.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, species));

        searchBar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                findAndSelect();
            }
        });

        navSightingBoxBtns= (LinearLayout) view.findViewById(R.id.navSightingBoxes);
        navSightingBoxBtns.setVisibility((View.GONE));
        sightingBoxesButtons = new ArrayList<Button>();

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
                clickSpecie(view, false);
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
                insertSighting(view);
            }
        });

        LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View thumbView = vi.inflate(R.layout.layout_seekbar_thumb, null);

        ((TextView) thumbView.findViewById(R.id.tvProgress)).setText("6");

        beaufortSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBar.setThumb(getThumb(progress, thumbView));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        beaufortSeekBar.setThumb(getThumb(6, thumbView));

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

    private void findAndSelect() {
        String insertedText = searchBar.getText().toString();
        try{
            clickSpecie(getView().findViewWithTag(insertedText),true);
            searchBar.setText("");
        }
        catch (Exception e){
            createToast(insertedText + " is already selected.");
        }

        try{
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
        catch(NullPointerException e ){

        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        ScrollView scrollView = (ScrollView) getView().findViewById(R.id.scrollView2);
        googleMap = map;

        googleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                scrollView.requestDisallowInterceptTouchEvent(true);
            }
        });

        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                scrollView.requestDisallowInterceptTouchEvent(false);
            }
        });

        LatLng coordenadas = new LatLng(0, 0);
        if(model.getTripFrom().contains("Funchal")) coordenadas = new LatLng(32.645621, -16.909784);
        else if(model.getTripFrom().contains("Porto Santo")) coordenadas = new LatLng(33.062203, -16.316115);
        else if(model.getTripFrom().contains("Câmara de Lobos")) coordenadas = new LatLng(32.647886, -16.974977);

        map.addMarker(new MarkerOptions().position(coordenadas).title("Departure"));
        moveToCurrentLocation(coordenadas);

        sightingLatitude.setText("Latitude: "+ df.format(coordenadas.latitude));
        sightingLongitude.setText("Longitude: "+ df.format(coordenadas.longitude));

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                clickedCoordinatesOnce = true;
                map.clear();
                map.addMarker(new MarkerOptions().position(point).title("Sighting").icon(BitmapDescriptorFactory.fromResource(R.drawable.sighting_pin)));
                moveToCurrentLocation(point);
                sightingLatitude.setText("Latitude: "+ df.format(point.latitude));
                sightingLongitude.setText("Longitude: "+ df.format(point.longitude));
            }
        });
    }

    private void moveToCurrentLocation(LatLng currentLocation)
    {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,14));
    }

    public void clickSpecie(View view, boolean fromSearch){
        if(String.valueOf(view.getTag()).contains("Selected") && !fromSearch) {
            unselectSpecie(view);
            if (sightingInformations.size() == 0) {
                noSelectedSpecies.setVisibility(View.VISIBLE);
                navSightingBoxBtns.setVisibility((View.GONE));
            }
        }else{
            if(fromSearch){
                createToast(view.getTag() + " Selected.");
            }
            selectedSpecie(view);

            LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.sighting_information_box, null);

            setViewID(view, v);

            buttonListenersSightingInfo(v);
            sightingInformations.get(sightingInformations.size() - 1).setSpecieName(String.valueOf(view.getTag()));

            TextView textView = (TextView) v.findViewById(R.id.title);
            textView.setText(view.getTag() + " Sighting");

            LayoutInflater vin = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View buttonLayout = vin.inflate(R.layout.sighting_information_nav_btn_layout, null);

            Button button = (Button) buttonLayout.findViewById(R.id.button);
            button.setText(view.getTag().toString());
            button.setTag(view.getTag().toString()+"Nav");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HorizontalScrollView horizontalSightings = (HorizontalScrollView) getView().findViewById(R.id.horizontalSightings);
                    horizontalSightings.smoothScrollTo(v.getLeft(), v.getTop());
                }
            });

            navSightingBoxBtns.addView(button);
            sightingBoxesButtons.add(button);

            view.setTag("Selected "+ view.getTag());

            if(sightingInformations.size() > 0 ){
                noSelectedSpecies.setVisibility(View.GONE);
                navSightingBoxBtns.setVisibility((View.VISIBLE));
            }

            sightingInformationsLayout.addView(v);
        }
    }

    private void createToast(String message){
        Toast toast = Toast.makeText(getActivity(), message,Toast.LENGTH_LONG);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            View toastView = toast.getView();

            toastView.getBackground().setColorFilter(Color.parseColor("#005E8C"), PorterDuff.Mode.SRC_IN);

            TextView text = toastView.findViewById(android.R.id.message);
            text.setTextColor(Color.parseColor("#FFFFFF"));
        }
        toast.show();
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
        datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());

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

        ToggleButton other_behavior_toggle = (ToggleButton) v.findViewById(R.id.other_behavior);

        other_behavior_toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    v.findViewById(R.id.other_behavior_layout).setVisibility(View.VISIBLE);
                }else{
                    v.findViewById(R.id.other_behavior_layout).setVisibility(View.GONE);
                }
            }
        });

        sighting.setOtherBehavior((EditText) v.findViewById(R.id.other_behavior_text));

        auxToggle.add(other_behavior_toggle);

        sighting.setBehavior_type(auxToggle);

        auxToggle = new ArrayList<ToggleButton>();

        auxToggle.add((ToggleButton) v.findViewById(R.id.none_reaction));
        auxToggle.add((ToggleButton) v.findViewById(R.id.approach_reaction));
        auxToggle.add((ToggleButton) v.findViewById(R.id.avoidance_reaction));

        ToggleButton other_reaction_toggle = (ToggleButton) v.findViewById(R.id.other_reaction);

        other_reaction_toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    v.findViewById(R.id.other_reaction_layout).setVisibility(View.VISIBLE);
                }else{
                    v.findViewById(R.id.other_reaction_layout).setVisibility(View.GONE);
                }
            }
        });

        sighting.setOtherReaction((EditText) v.findViewById(R.id.other_reaction_text));

        auxToggle.add(other_reaction_toggle);

        sighting.setReactions_to_vessel(auxToggle);

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
                navSightingBoxBtns.removeView(getView().findViewWithTag(sightingBoxesButtons.get(i).getTag().toString()));
                sightingBoxesButtons.remove(i);
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

    public void insertSighting(View view){

        EditText editText = (EditText) getView().findViewById(R.id.pickDate);
        String day = editText.getText().toString();
        EditText editText1 = (EditText) getView().findViewById(R.id.pickTime);
        String hour = editText1.getText().toString();
        TextView textView = (TextView) getView().findViewById(R.id.latitude);
        String latitude = textView.getText().toString();
        String[] result = latitude.split(": ");
        String latitude_ = result[1];
        TextView textView1 = (TextView) getView().findViewById(R.id.longitude);
        String longitude = textView1.getText().toString();
        String[] result1 = longitude.split(": ");
        String longitude_ = result1[1];
        SeekBar sb = (SeekBar) getView().findViewById(R.id.beaufort_slider);
        String sea_state = Integer.toString(sb.getProgress());
        EditText editText3 = (EditText) getView().findViewById(R.id.sighting_comment);
        String comment = editText3.getText().toString();
        String animal = "";

        for(SightingInformation sighting : sightingInformations){
            animal += sighting.toString();
        }

        if(validateSightingReport()){ //se todos os campos obrigatórios estão preenchidos
            if(isInternetWorking()){
                insertSightingInformationIntoBD(day, hour, sea_state, latitude_, longitude_, comment, getIdPerson(), getVesselId(), animal, getTripFrom(), getTripTo());
            } else {
                insertSightingInformationIntoFile(day, hour, sea_state, latitude_, longitude_, comment, getIdPerson(), getPersonName(), getVesselId(), animal, getTripFrom(), getTripTo());
            }

            createJsonFile(); //CRIAR JSON FILE -> SISTEMA 2

            sightingInformations.clear();
            ((MainActivity)getActivity()).onButtonShowPopupWindowClick(view, "Sighting successfully reported!");

            //Espera 2 segundos
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    MainActivity.switchFragment(new TeamMemberHomeFragment());
                }
            }, 2000);
        }
    }

    private void createJsonFile(){

        ArrayList<AnimalJson> animalJsons = new ArrayList<AnimalJson>();
        for(SightingInformation sightingInformation : sightingInformations){
            String specieName = sightingInformation.getSpecieName();
            String nr_individuals = sightingInformation.getNumberOfIndividualsString();
            String nr_offspring = sightingInformation.getNumberOfOffspringString();

            String behaviors = sightingInformation.getBehaviorTypesString();
            String[] behaviorsTypes = behaviors.split(";");
            ArrayList<String> behaviorsTypesList = new ArrayList<String>();
            for(String behaviorType : behaviorsTypes){
                behaviorsTypesList.add(behaviorType);
            }

            String reactions = sightingInformation.getReactionToVesselString();
            String[] reactionToVessel = reactions.split(";");
            ArrayList<String> reactionsList = new ArrayList<String>();
            for(String reaction : reactionToVessel){
                reactionsList.add(reaction);
            }

            String trustLevel = sightingInformation.getTrustLevelString();

            AnimalJson animal = new AnimalJson(specieName, nr_individuals, nr_offspring, behaviorsTypesList, reactionsList, trustLevel);

            animalJsons.add(animal);
        }

        String day = sightingDate.getText().toString();
        String hour = sightingTime.getText().toString();
        String latitude = sightingLatitude.getText().toString();
        String[] result = latitude.split(": ");
        String latitude_ = result[1];
        String longitude = sightingLongitude.getText().toString();
        String[] result1 = longitude.split(": ");
        String longitude_ = result1[1];
        String sea_state = Integer.toString(beaufortSeekBar.getProgress());
        EditText editText3 = (EditText) getView().findViewById(R.id.sighting_comment);
        String comment = editText3.getText().toString();

        SightingJson sighting = new SightingJson(day, hour, latitude_, longitude_, animalJsons, sea_state, getVesselId(), comment, getPersonName());

        String jsonAsString = jsonWriter.createSightingJson(sighting);
        jsonAsString = jsonAsString.replace("\\", "");


        //se ocorrer um erro na publicação da mensagem, é guardada localmente até ser possível envia-la:
        if(!isInternetWorking() && !mqtt.isConnected()){
            insertSightingJsonStringIntoFile(jsonAsString);
        }else{
            mqtt.publish(getContext(), jsonAsString);
        }
    }

    public void insertSightingJsonStringIntoFile(String jsonAsString){
        ArrayList<String> notPublishedJsons = ReadJsonArrayFromSD(getContext(), "notpublishedjsons");
        notPublishedJsons.add(jsonAsString);
        SaveSightingJsonStringToSD(getContext(), "notpublishedjsons", notPublishedJsons);
    }


    public static ArrayList<String> ReadJsonArrayFromSD(Context mContext,String filename){
        try {
            FileInputStream fis = mContext.openFileInput(filename + ".dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<String> obj= (ArrayList<String>) ois.readObject();
            fis.close();
            return obj;

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void SaveSightingJsonStringToSD(Context mContext, String filename, ArrayList<String> list){
        try {
            FileOutputStream fos = mContext.openFileOutput(filename + ".dat", mContext.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(list);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void insertSightingInformationIntoFile(String day, String hour, String sea_state, String latitude_, String longitude_, String comment, String person_id, String person_name, String boat_id, String animal, String trip_from, String trip_to){

        ArrayList<String> sighting = new ArrayList<>();
        sighting.add(day);
        sighting.add(hour);
        sighting.add(sea_state);
        sighting.add(latitude_);
        sighting.add(longitude_);
        sighting.add(comment);
        sighting.add(person_id);
        sighting.add(person_name);
        sighting.add(boat_id);
        sighting.add(animal);
        sighting.add(trip_from);
        sighting.add(trip_to);

        Context cont = (Context) getActivity().getApplicationContext();
        ArrayList<ArrayList<String>> sightings = ReadArrayListFromSD(cont, "notSubmittedSightings");
        sightings.add(sighting);
        SaveArrayListToSD(cont, "notSubmittedSightings", sightings);
        //Log.d("file", ReadArrayListFromSD(cont, "notSubmittedSightings").toString());
    }

    public static void insertSightingInformationIntoBD(String day, String hour, String sea_state, String latitude, String longitude, String comment, String person_id, String boat_id, String animal, String trip_from, String trip_to){

        String insertSightingUrl = "http://" + ip + "/seaker/insertsighting.php";
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
                    + URLEncoder.encode("sea_state", "UTF-8")+"="+URLEncoder.encode(sea_state, "UTF-8")+"&"
                    + URLEncoder.encode("latitude", "UTF-8")+"="+URLEncoder.encode(latitude, "UTF-8")+"&"
                    + URLEncoder.encode("longitude", "UTF-8")+"="+URLEncoder.encode(longitude, "UTF-8")+"&"
                    + URLEncoder.encode("comment", "UTF-8")+"="+URLEncoder.encode(comment, "UTF-8")+"&"
                    + URLEncoder.encode("person_id", "UTF-8")+"="+URLEncoder.encode(person_id, "UTF-8")+"&"
                    + URLEncoder.encode("boat_id", "UTF-8")+"="+URLEncoder.encode(boat_id, "UTF-8")+"&"
                    + URLEncoder.encode("animal", "UTF-8")+"="+URLEncoder.encode(animal, "UTF-8")+"&"
                    + URLEncoder.encode("trip_from", "UTF-8")+"="+URLEncoder.encode(trip_from, "UTF-8")+"&"
                    + URLEncoder.encode("trip_to", "UTF-8")+"="+URLEncoder.encode(trip_to, "UTF-8");

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

    public static void deleteAndInsertSightingInformationIntoBD(String id_sighting, String day, String hour, String sea_state, String latitude, String longitude, String comment, String animal){

        String updateSightingUrl = "http://" + ip + "/seaker/deleteandinsertsightingbyid.php";
        try {
            URL url = new URL(updateSightingUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("id_sighting", "UTF-8")+"="+URLEncoder.encode(id_sighting, "UTF-8")+"&"
                    + URLEncoder.encode("day", "UTF-8")+"="+URLEncoder.encode(day, "UTF-8")+"&"
                    + URLEncoder.encode("hour", "UTF-8")+"="+URLEncoder.encode(hour, "UTF-8")+"&"
                    + URLEncoder.encode("sea_state", "UTF-8")+"="+URLEncoder.encode(sea_state, "UTF-8")+"&"
                    + URLEncoder.encode("latitude", "UTF-8")+"="+URLEncoder.encode(latitude, "UTF-8")+"&"
                    + URLEncoder.encode("longitude", "UTF-8")+"="+URLEncoder.encode(longitude, "UTF-8")+"&"
                    + URLEncoder.encode("comment", "UTF-8")+"="+URLEncoder.encode(comment, "UTF-8")+"&"
                    + URLEncoder.encode("animal", "UTF-8")+"="+URLEncoder.encode(animal, "UTF-8");

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

    public static void deleteSightingInformation(String id_sighting){

        String deleteSightingUrl = "http://" + ip + "/seaker/deletesightingbyid.php";
        try {
            URL url = new URL(deleteSightingUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("id_sighting", "UTF-8")+"="+URLEncoder.encode(id_sighting, "UTF-8");
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

    private boolean validateSightingReport(){ //true se os campos obrigatórios estão preenchidos
        ScrollView scrollView = (ScrollView) getView().findViewById(R.id.scrollView2);
        HorizontalScrollView horizontalScrollView = (HorizontalScrollView) getView().findViewById(R.id.horizontalSightings);

        if(!clickedCoordinatesOnce){ //se não clicou no mapa nenhuma vez
            ((MainActivity)getActivity()).onButtonShowPopupWindowClick(getView(), "Sighting coordinates missing!");
            scrollView.smoothScrollTo(0, (getView().findViewById(R.id.coordinates)).getTop());
            return false;
        }

        if(sightingInformations.size() == 0){ //se não escolheu nenhuma espécie
            ((MainActivity)getActivity()).onButtonShowPopupWindowClick(getView(), "Choose the sighting species!");
            scrollView.smoothScrollTo(0, (getView().findViewById(R.id.choose_sighting_species)).getTop());
            return false;
        }

        for(SightingInformation sighting : sightingInformations){ //se não preencheu o número de individuos de alguma espécie
            if(sighting.getNumberOfIndividualsString().equals("ERROR")){
                ((MainActivity)getActivity()).onButtonShowPopupWindowClick(getView(), "Select the number of "+ sighting.getSpecieName()+"s!");
                scrollView.smoothScrollTo(0, (getView().findViewById(R.id.sighting_informations)).getTop());
                horizontalScrollView.smoothScrollTo((getView().findViewById(sighting.getSightingBoxID())).getLeft(), (getView().findViewById(sighting.getSightingBoxID())).getTop());
                return false;
            }
        }

        for(SightingInformation sighting : sightingInformations){ //se não preencheu o número de individuos de alguma espécie
            if(!sighting.allOtherFieldsFilled()){
                ((MainActivity)getActivity()).onButtonShowPopupWindowClick(getView(), "Specifiy the "+ sighting.getSpecieName()+"'s 'Other' field!");
                scrollView.smoothScrollTo(0, (getView().findViewById(R.id.sighting_informations)).getTop());
                horizontalScrollView.smoothScrollTo((getView().findViewById(sighting.getSightingBoxID())).getLeft(), (getView().findViewById(sighting.getSightingBoxID())).getTop());
                return false;
            }
        }

        return true;
    }

    public static String getAllSightingsInformations(String person_id) {
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
            String post_data = URLEncoder.encode("person_id", "UTF-8") + "=" + URLEncoder.encode(person_id, "UTF-8");
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
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ConnectException e){
            e.printStackTrace();
            return "NO_CONNECTION";
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static void SaveArrayListToSD(Context mContext, String filename, ArrayList<ArrayList<String>> list){
        try {
            FileOutputStream fos = mContext.openFileOutput(filename + ".dat", mContext.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(list);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static ArrayList<ArrayList<String>> ReadArrayListFromSD(Context mContext,String filename){
        try {
            FileInputStream fis = mContext.openFileInput(filename + ".dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<ArrayList<String>> obj= (ArrayList<ArrayList<String>>) ois.readObject();
            fis.close();
            return obj;

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static boolean isInternetWorking() {
        boolean success = false;
        try {
            URL url = new URL("https://google.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(1000);
            connection.connect();
            success = connection.getResponseCode() == 200;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }

    private String getIdPerson(){
        Context cont = (Context) getActivity().getApplicationContext();
        ArrayList<ArrayList<String>> sighting_info = ReadArrayListFromSD(cont, "person_boat_zones");
        return sighting_info.get(0).get(0);
    }

    private String getPersonName(){
        Context cont = (Context) getActivity().getApplicationContext();
        ArrayList<ArrayList<String>> sighting_info = ReadArrayListFromSD(cont, "person_boat_zones");
        return sighting_info.get(0).get(1);
    }

    private String getVesselId(){

        Context cont = (Context) getActivity().getApplicationContext();
        ArrayList<ArrayList<String>> sighting_info = ReadArrayListFromSD(cont, "person_boat_zones");
        return sighting_info.get(1).get(0);
    }

    private String getTripFrom(){
        Context cont = (Context) getActivity().getApplicationContext();
        ArrayList<ArrayList<String>> sighting_info = ReadArrayListFromSD(cont, "person_boat_zones");
        return sighting_info.get(1).get(1);
    }

    private String getTripTo(){
        Context cont = (Context) getActivity().getApplicationContext();
        ArrayList<ArrayList<String>> sighting_info = ReadArrayListFromSD(cont, "person_boat_zones");
        return sighting_info.get(1).get(2);
    }
}