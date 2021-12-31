package com.example.seaker.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import androidx.lifecycle.ViewModelProvider;

import com.example.seaker.DataViewModel;
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

import java.text.DecimalFormat;
import java.util.ArrayList;

public class EditSightingFragment extends BaseFragment implements OnMapReadyCallback{

    private DataViewModel model;
    private TextView title;

    private EditText sightingDate;
    private EditText sightingTime;
    private TextView sightingLatitude;
    private TextView sightingLongitude;
    private EditText sightingComment;
    private GoogleMap googleMap;
    private LinearLayout sightingInformationsLayout;
    private BusinessFacade businessFacade;
    private ImageButton takePhoto;
    private ImageButton uploadPhoto;
    private SeekBar beaufortSeekBar;
    private Button whalesBtn;
    private Button dolphinsBtn;
    private Button porpoisesBtn;
    private ArrayList<ImageButton> whalesSpeciesBtns;
    private ArrayList<ImageButton> dolphinSpeciesBtns;
    private ArrayList<ImageButton> porpoiseSpeciesBtns;
    private ImageButton saveChangesBtn;
    private ImageButton deleteSightingBtn;

    private static final DecimalFormat df = new DecimalFormat("0.00000");

    public EditSightingFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_sightings, container, false);

        SetButtonOnClickNextFragment(R.id.buttonBack,new ReportedSightingsTeamMemberFragment(),view);

        businessFacade = BusinessFacade.getInstance();

        onStartView(view);

        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model = new ViewModelProvider(requireActivity()).get(DataViewModel.class);
        title.setText("Sighting #" + model.getReportedSighingId() + " - Editing");
        sightingDate.setText(model.getDate());
        sightingTime.setText(model.getTime());

        Double latitude = Double.parseDouble(model.getLatitude());
        String latitudeString = df.format(latitude);
        Double longitude = Double.parseDouble(model.getLongitude());
        String longitudeString = df.format(longitude);

        sightingLatitude.setText("Latitude: " + latitudeString);
        sightingLongitude.setText("Longitude: " + longitudeString);

        sightingComment.setText(model.getComment());

        int index = 0;
        for(String specie : model.getSpecies()) {
            switch (specie) {
                case "Blue Whale":
                    clickSpecie(getView().findViewById(R.id.blue_whale_btn));
                    break;
                case "Fin Whale":
                    clickSpecie(getView().findViewById(R.id.fin_whale_btn));
                    break;
                case "Sperm Whale":
                    clickSpecie(getView().findViewById(R.id.sperm_whale_btn));
                    break;
                case "North Atlantic Right Whale":
                    clickSpecie(getView().findViewById(R.id.north_atlantic_right_whale_btn));
                    break;
                case "Sowerby's Beaker Whale":
                    clickSpecie(getView().findViewById(R.id.sowerbys_beaker_whale_btn));
                    break;
                case "Sei Whale":
                    clickSpecie(getView().findViewById(R.id.sei_whale_btn));
                    break;
                case "Blainville's Beaked Whale":
                    clickSpecie(getView().findViewById(R.id.blainville_whale_btn));
                    break;
                case "Gervais' Beaked Whale":
                    clickSpecie(getView().findViewById(R.id.gervais_whale_btn));
                    break;
                case "Bryde's Whale":
                    clickSpecie(getView().findViewById(R.id.brides_whale_btn));
                    break;
                case "Minke Whale":
                    clickSpecie(getView().findViewById(R.id.minke_whale_btn));
                    break;
                case "True's Beaked Whale":
                    clickSpecie(getView().findViewById(R.id.trues_whale_btn));
                    break;
                case "Orca Whale":
                    clickSpecie(getView().findViewById(R.id.orca_whale_btn));
                    break;
                case "Short Finned Pilot Whale":
                    clickSpecie(getView().findViewById(R.id.short_finned_pilot_whale_btn));
                    break;
                case "Humpback Whale":
                    clickSpecie(getView().findViewById(R.id.humpback_whale_btn));
                    break;
                case "Northern Bottlenose Whale":
                    clickSpecie(getView().findViewById(R.id.northern_whale_btn));
                    break;
                case "Long Finned Pilot Whale":
                    clickSpecie(getView().findViewById(R.id.long_finned_pilot_whale_btn));
                    break;
                case "False Killer Whale":
                    clickSpecie(getView().findViewById(R.id.false_killer_whale_btn));
                    break;
                case "Melon Whale":
                    clickSpecie(getView().findViewById(R.id.melon_whale_btn));
                    break;
                case "Cuvier's Beaked Whale":
                    clickSpecie(getView().findViewById(R.id.cuviers_whale_btn));
                    break;
                case "Pigmy Whale":
                    clickSpecie(getView().findViewById(R.id.pigmy_whale_btn));
                    break;
                case "Not Specified Whale":
                    clickSpecie(getView().findViewById(R.id.not_specified_whale_btn));
                    break;
                case "Bottlenose Dolphin":
                    clickSpecie(getView().findViewById(R.id.bottlenose_dolphin_btn));
                    break;
                case "Risso's Dolphin":
                    clickSpecie(getView().findViewById(R.id.rissos_dolphin_btn));
                    break;
                case "Rough Toothed Dolphin":
                    clickSpecie(getView().findViewById(R.id.rough_toothed_dolphin_btn));
                    break;
                case "Atlantic Spotted Dolphin":
                    clickSpecie(getView().findViewById(R.id.atlantic_spotted_dolphin_btn));
                    break;
                case "Striped Dolphin":
                    clickSpecie(getView().findViewById(R.id.striped_dolphin_btn));
                    break;
                case "Common Dolphin":
                    clickSpecie(getView().findViewById(R.id.common_dolphin_btn));
                    break;
                case "Fraser's Dolphin":
                    clickSpecie(getView().findViewById(R.id.frasers_dolphin_btn));
                    break;
                case "Not Specified Dolphin":
                    clickSpecie(getView().findViewById(R.id.not_specified_dolphin_btn));
                    break;
                case "Harbour Porpoise":
                    clickSpecie(getView().findViewById(R.id.harbour_porpoise_btn));
                    break;
                case "Not Specified Porpoise":
                    clickSpecie(getView().findViewById(R.id.not_specified_porpoise_btn));
                    break;
            }
            fillSpecieSightingInformation(specie, index);
            index++;
        }

        LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View thumbView = vi.inflate(R.layout.layout_seekbar_thumb, null);

        int beafortValue = model.getSea_state();
        ((TextView) thumbView.findViewById(R.id.tvProgress)).setText(""+beafortValue);


        beaufortSeekBar.setProgress(beafortValue);

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
        beaufortSeekBar.setThumb(getThumb(beafortValue, thumbView));
    }

    private void fillSpecieSightingInformation(String specie, int index){
        SightingInformation sightingInformation = sightingInformations.get(getSightingInformationId(specie));

        sightingInformation.fillNrIndividuals(model.getN_individuals().get(index));
        sightingInformation.fillNrOffspring(model.getN_offspring().get(index));
        sightingInformation.fillBehaviourType(model.getBehaviors().get(index));
        sightingInformation.fillReactionToVessel(model.getReactions().get(index));
        sightingInformation.fillTrustLevel(model.getTrust_level().get(index));
    }

    private int getSightingInformationId(String specie){
        int i=0;
        for(SightingInformation sighting : sightingInformations){
            if(sighting.getSpecieName().equals(specie)){
                 return i;
            }
            i++;
        }
        return -1;
    }

    private void onStartView(View view){
        title = (TextView) view.findViewById(R.id.fragmentTitle);

        sightingDate = (EditText) view.findViewById(R.id.pickDate);
        sightingTime = (EditText) view.findViewById(R.id.pickTime);

        sightingLatitude = (TextView) view.findViewById(R.id.latitude);
        sightingLongitude = (TextView) view.findViewById(R.id.longitude);

        takePhoto = (ImageButton) view.findViewById(R.id.take_photo_btn);
        uploadPhoto = (ImageButton) view.findViewById(R.id.upload_photo_btn);

        beaufortSeekBar = (SeekBar) view.findViewById(R.id.beaufort_slider);

        sightingComment = (EditText) view.findViewById(R.id.sighting_comment);

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

        saveChangesBtn = (ImageButton) view.findViewById(R.id.save_changes_btn);

        saveChangesBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(validateSightingReport()){
                    updateSightingInfo(view);
                }else{
                    ((MainActivity)getActivity()).onButtonShowPopupWindowClick(getView(), "Required fields missing!");
                }
            }
        });

        deleteSightingBtn = (ImageButton) view.findViewById(R.id.delete_sighting_btn);

        deleteSightingBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onDeleteClick(view);
            }
        });

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
    }

    private void onDeleteClick(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setTitle("Delete Sighting");
        builder.setMessage("Are you sure you want to delete this sighting?");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteSighting(view);
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {@Override public void onClick(DialogInterface dialog, int which) {}});

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean validateSightingReport(){ //true se os campos obrigatórios estão preenchidos

        if(sightingInformations.size() == 0) return false; //se não escolheu nenhuma espécie

        for(SightingInformation sighting : sightingInformations){
            if(sighting.getNumberOfIndividualsString().equals("ERROR")) return false; //se não preencheu o número de individuos de alguma espécie
        }

        return true;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        Double latitude = Double.parseDouble(model.getLatitude());
        Double longitude = Double.parseDouble(model.getLongitude());
        LatLng Funchal = new LatLng(latitude, longitude);

        map.addMarker(new MarkerOptions().position(Funchal).title("Sighting").icon(BitmapDescriptorFactory.fromResource(R.drawable.sighting_pin)));
        moveToCurrentLocation(Funchal);

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                //allPoints.add(point);
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

    private void updateSightingInfo(View view){
        TextView tv = (TextView) getView().findViewById(R.id.fragmentTitle);
        String title = tv.getText().toString();
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

        String aux[] = title.split(" ");
        String sighting_id = aux[1];

        Boolean sighting_submitted = !sighting_id.contains("?");
        if (sighting_submitted){
            String sighting_id_number = sighting_id.substring(1);

            if(ReportSightingFragment.isInternetWorking()){
                ReportSightingFragment.deleteAndInsertSightingInformationIntoBD(sighting_id_number, day, hour, sea_state, latitude_, longitude_, comment, "3", "1", animal);
                showHandler(view, "Sighting successfully edited!");
            } else {
                showHandler(view, "No connectivity");
            }
        } else {
            String aux2[] = sighting_id.split("\\?");
            String index_sighting = aux2[1];
            int index = Integer.parseInt(index_sighting);
            Context cont = (Context) getActivity().getApplicationContext();

            if(ReportSightingFragment.isInternetWorking()){
                ReportSightingFragment.insertSightingInformationIntoBD(day, hour, sea_state, latitude_, longitude_, comment, "3", "1", animal);
                showHandler(view, "Sighting submitted!");
                deleteArrayList(index);
            } else {

                ArrayList<ArrayList<String>> sightings = ReportSightingFragment.ReadArrayListFromSD(cont, "notSubmittedSightings");
                sightings.get(index).set(0, day);
                sightings.get(index).set(1, hour);
                sightings.get(index).set(2, sea_state);
                sightings.get(index).set(3, latitude_);
                sightings.get(index).set(4, longitude_);
                sightings.get(index).set(5, comment);
                sightings.get(index).set(6, "3*Sílvia Fernandes");
                sightings.get(index).set(7, "1");
                sightings.get(index).set(8, animal);

                ReportSightingFragment.SaveArrayListToSD(cont, "notSubmittedSightings", sightings);

                showHandler(view, "Sighting successfully changed!");
            }
        }
    }

    private void deleteSighting(View view){
        TextView tv = (TextView) getView().findViewById(R.id.fragmentTitle);
        String title = tv.getText().toString();
        String aux[] = title.split(" ");
        String sighting_id = aux[1];
        Boolean sighting_submitted = !sighting_id.contains("?");
        if (sighting_submitted){
            String sighting_id_number = sighting_id.substring(1);

            if(ReportSightingFragment.isInternetWorking()){
                ReportSightingFragment.deleteSightingInformation(sighting_id_number);
                showHandler(view, "Sighting successfully deleted!");
            } else {
                showHandler(view, "No connectivity");
            }
        } else {
            String aux2[] = sighting_id.split("\\?");
            String index_sighting = aux2[1];
            int index = Integer.parseInt(index_sighting);
            deleteArrayList(index);
            showHandler(view, "Sighting successfully deleted!");
        }
    }

    private void deleteArrayList(int index){
        Context cont = (Context) getActivity().getApplicationContext();
        ArrayList<ArrayList<String>> sightings = ReportSightingFragment.ReadArrayListFromSD(cont, "notSubmittedSightings");
        ArrayList<ArrayList<String>> sightings_ = new ArrayList<>();
        for (int i=0; i < sightings.size(); i++){
            if(index!=i){
                ArrayList<String> auxx = sightings.get(i);
                sightings_.add(auxx);
            }
        }
        ReportSightingFragment.SaveArrayListToSD(cont, "notSubmittedSightings", sightings_);
    }

    private void showHandler(View view, String message){
        ((MainActivity)getActivity()).onButtonShowPopupWindowClick(view, message);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                MainActivity.switchFragment(new TeamMemberHomeFragment());
            }
        }, 2000);
    }
}
