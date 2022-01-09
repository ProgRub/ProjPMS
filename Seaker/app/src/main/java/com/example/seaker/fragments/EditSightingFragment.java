package com.example.seaker.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
    private ImageView noSelectedSpecies;
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
    private AutoCompleteTextView searchBar;
    private LinearLayout navSightingBoxBtns;
    private ArrayList<Button> sightingBoxesButtons;

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

        model = new ViewModelProvider(requireActivity()).get(DataViewModel.class);

        if(model.getUserType() == "TeamMember") SetButtonOnClickNextFragment(R.id.buttonBack,new ReportedSightingsTeamMemberFragment(),view);
        else if(model.getUserType() == "Administrator") SetButtonOnClickNextFragment(R.id.buttonBack,new ReportedSightingsAdminManagerFragment(),view);
        else if(model.getUserType() == "CompanyManager") SetButtonOnClickNextFragment(R.id.buttonBack,new ReportedSightingsAdminManagerFragment(),view);

        businessFacade = BusinessFacade.getInstance();

        onStartView(view);

        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

        searchBar.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, species));

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

        int index = 0;
        for(String specie : model.getSpecies()) {
            try{clickSpecie(getView().findViewWithTag(specie), false);}catch (Exception e){}

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

        beaufortSeekBar = (SeekBar) view.findViewById(R.id.beaufort_slider);

        sightingComment = (EditText) view.findViewById(R.id.sighting_comment);

        sightingInformationsLayout = (LinearLayout) view.findViewById(R.id.sightingsInformations);

        noSelectedSpecies = (ImageView) view.findViewById(R.id.no_selected_species);

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

    private void findAndSelect() {
        String insertedText = searchBar.getText().toString();
        try{
            clickSpecie(getView().findViewWithTag(insertedText),true);
            searchBar.setText("");
        }
        catch (Exception e){
            ((MainActivity)getActivity()).onButtonShowPopupWindowClick(getView(), "Specie not found!");
        }

        try{
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
        catch(NullPointerException e ){
        }
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

    private boolean validateSightingReport(){ //true se os campos obrigatórios estão preenchidos

        ScrollView scrollView = (ScrollView) getView().findViewById(R.id.scrollView2);
        HorizontalScrollView horizontalScrollView = (HorizontalScrollView) getView().findViewById(R.id.horizontalSightings);


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

        Double latitude = Double.parseDouble(model.getLatitude());
        Double longitude = Double.parseDouble(model.getLongitude());
        LatLng Funchal = new LatLng(latitude, longitude);

        map.addMarker(new MarkerOptions().position(Funchal).title("Sighting").icon(BitmapDescriptorFactory.fromResource(R.drawable.sighting_pin)));
        moveToCurrentLocation(Funchal);

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
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
        if(String.valueOf(view.getTag()).contains("Selected") && !fromSearch){
            unselectSpecie(view);
            if(sightingInformations.size() == 0 ){
                noSelectedSpecies.setVisibility(View.VISIBLE);
                navSightingBoxBtns.setVisibility((View.GONE));
            }
        }else if(String.valueOf(view.getTag()).contains("Selected") && fromSearch){
            createToast(view.getTag().toString().split("Selected ")[1] + " is already selected.");
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
            textView.setText(sightingInformations.size() +" - " + view.getTag() + " Sighting");

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

            LinearLayout.LayoutParams rel_bottone = new LinearLayout.LayoutParams(150, 150);
            button.setLayoutParams(rel_bottone);

            navSightingBoxBtns.addView(button);
            sightingBoxesButtons.add(button);
            numerateNavButtons();

            view.setTag("Selected "+view.getTag());

            if(sightingInformations.size() > 0 ){
                noSelectedSpecies.setVisibility(View.GONE);
                navSightingBoxBtns.setVisibility((View.VISIBLE));
            }

            sightingInformationsLayout.addView(v);
        }
    }

    private void numerateNavButtons(){
        for(int index = 0; index < ((LinearLayout) navSightingBoxBtns).getChildCount(); index++) {
            View nextChild = ((LinearLayout) navSightingBoxBtns).getChildAt(index);
            if (nextChild instanceof Button) {
                ((Button) nextChild).setText(""+(index+1));
            }
        }
    }

    private void numerateSightingBoxes(){
        for(SightingInformation sightingBox : sightingInformations){
            View nextChild = getView().findViewById(sightingBox.getSightingBoxID());
            TextView title = (TextView) nextChild.findViewById(R.id.title);
            String oldTitle = title.getText().toString();
            String[] oldTitleSplit = oldTitle.split(" - ");

            for(int index = 0; index < ((LinearLayout) sightingInformationsLayout).getChildCount(); index++) {
                View child = sightingInformationsLayout.getChildAt(index);
                if(((TextView) child.findViewById(R.id.title)).getText().equals(oldTitle)){
                    title.setText((index+1) + " - " + oldTitleSplit[1]);
                }
            }
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
                numerateSightingBoxes();
                navSightingBoxBtns.removeView(getView().findViewWithTag(sightingBoxesButtons.get(i).getTag().toString()));
                sightingBoxesButtons.remove(i);
                numerateNavButtons();
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
                ReportSightingFragment.deleteAndInsertSightingInformationIntoBD(sighting_id_number, day, hour, sea_state, latitude_, longitude_, comment, animal);
                showHandler(view, "Sighting successfully edited!");
            } else {
                showHandler(view, "No connectivity");
            }
        } else {
            String aux2[] = sighting_id.split("\\?");
            String index_sighting = aux2[1];
            int index = Integer.parseInt(index_sighting)-1;
            Context cont = (Context) getActivity().getApplicationContext();

            if(ReportSightingFragment.isInternetWorking()){
                ReportSightingFragment.insertSightingInformationIntoBD(day, hour, sea_state, latitude_, longitude_, comment, getIdPerson(), getVesselId(), animal, getTripFrom(), getTripTo());
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
                sightings.get(index).set(9, animal);

                ReportSightingFragment.SaveArrayListToSD(cont, "notSubmittedSightings", sightings);

                showHandler(view, "Sighting successfully changed!");
            }
        }
    }

    private String getTripFrom(){
        Context cont = (Context) getActivity().getApplicationContext();
        ArrayList<ArrayList<String>> sighting_info = ReportSightingFragment.ReadArrayListFromSD(cont, "person_boat_zones");
        return sighting_info.get(1).get(1);
    }

    private String getTripTo(){
        Context cont = (Context) getActivity().getApplicationContext();
        ArrayList<ArrayList<String>> sighting_info = ReportSightingFragment.ReadArrayListFromSD(cont, "person_boat_zones");
        return sighting_info.get(1).get(2);
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
            int index = Integer.parseInt(index_sighting) - 1;
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
                if(model.getUserType() == "TeamMember") MainActivity.switchFragment(new TeamMemberHomeFragment());
                else if(model.getUserType() == "Administrator") MainActivity.switchFragment(new AdminHomeFragment());
                else if(model.getUserType() == "CompanyManager") MainActivity.switchFragment(new CompanyManagerHomeFragment());
            }
        }, 2000);
    }

    private String getIdPerson(){
        Context cont = (Context) getActivity().getApplicationContext();
        ArrayList<ArrayList<String>> sighting_info = ReportSightingFragment.ReadArrayListFromSD(cont, "person_boat_zones");
        return sighting_info.get(0).get(0);
    }

    private String getVesselId(){

        Context cont = (Context) getActivity().getApplicationContext();
        ArrayList<ArrayList<String>> sighting_info = ReportSightingFragment.ReadArrayListFromSD(cont, "person_boat_zones");
        return sighting_info.get(1).get(0);
    }
}
