package com.example.seaker;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.seaker.fragments.BaseFragment;
import com.example.seaker.fragments.SplashFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    View thumbView;

    private static FragmentManager supportFragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        supportFragmentManager = getSupportFragmentManager();
        switchFragment(new SplashFragment());

    }

    public void testingBtn(View view){
        for(SightingInformation sighting : sightingInformations){
            Log.d("TESTING", sighting.toString() );
        }
    }


    public static void switchFragment(BaseFragment fragment){
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, fragment)
                .commit();
    }

    public void clickSpecie(View view){

        if(String.valueOf(view.getTag()).contains("Selected")){
            unselectSpecie(view);
        }else{
            selectedSpecie(view);

            LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.sighting_information_box, null);

            setViewID(view, v);

            buttonListenersSightingInfo(v);

            TextView textView = (TextView) v.findViewById(R.id.title);
            textView.setText(String.valueOf(view.getTag()) + " Sighting");

            view.setTag("Selected "+String.valueOf(view.getTag()));

            LinearLayout insertPoint = (LinearLayout) findViewById(R.id.sightingsInformations);
            insertPoint.addView(v);
        }
    }

    public void scrollToSpecie(View view){
        HorizontalScrollView species = (HorizontalScrollView) findViewById(R.id.all_species);

        RelativeLayout whales = (RelativeLayout) findViewById(R.id.whales_species);
        RelativeLayout dolphins = (RelativeLayout) findViewById(R.id.dolphins_species);
        RelativeLayout porpoise = (RelativeLayout) findViewById(R.id.porpoises_species);

        switch(view.getId()){
            case R.id.scroll_to_whales_btn:
                focusOnView(species, whales);
                ((Button)view).setTypeface(null, Typeface.BOLD);
                ((Button)findViewById(R.id.scroll_to_dolphins_btn)).setTypeface(null, Typeface.NORMAL);
                ((Button)findViewById(R.id.scroll_to_porpoises_btn)).setTypeface(null, Typeface.NORMAL);
                break;
            case R.id.scroll_to_dolphins_btn:
                focusOnView(species, dolphins);
                ((Button)view).setTypeface(null, Typeface.BOLD);
                ((Button)findViewById(R.id.scroll_to_whales_btn)).setTypeface(null, Typeface.NORMAL);
                ((Button)findViewById(R.id.scroll_to_porpoises_btn)).setTypeface(null, Typeface.NORMAL);
                break;
            case R.id.scroll_to_porpoises_btn:
                focusOnView(species, porpoise);
                ((Button)view).setTypeface(null, Typeface.BOLD);
                ((Button)findViewById(R.id.scroll_to_whales_btn)).setTypeface(null, Typeface.NORMAL);
                ((Button)findViewById(R.id.scroll_to_dolphins_btn)).setTypeface(null, Typeface.NORMAL);
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
        EditText editText = (EditText) findViewById(R.id.pickDate);

        String actualValue = editText.getText().toString();
        String previousDate[] = actualValue.split("/");

        int yy = Integer.parseInt(previousDate[2]);
        int mm = Integer.parseInt(previousDate[1])-1;
        int dd = Integer.parseInt(previousDate[0]);

        DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
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
        EditText editText = (EditText) findViewById(R.id.pickTime);
        String actualValue = editText.getText().toString();
        String previousTime[] = actualValue.split(":");

        int hour = Integer.parseInt(previousTime[0]);
        int minute = Integer.parseInt(previousTime[1]);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
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

    public Drawable getThumb(int progress) {
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

        ((SeekBar) v.findViewById(R.id.beaufort_slider)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBar.setThumb(getThumb(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        thumbView = vi.inflate(R.layout.layout_seekbar_thumb, null);

        ((TextView) thumbView.findViewById(R.id.tvProgress)).setText("6");

        ((SeekBar) v.findViewById(R.id.beaufort_slider)).setThumb(getThumb(6));

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

        LinearLayout insertPoint = (LinearLayout) findViewById(R.id.sightingsInformations);

        ImageButton btn1 = (ImageButton) findViewById(view.getId());

        Drawable drawable;

        switch (view.getId()) {
            case R.id.blue_whale_btn:
                drawable = getResources().getDrawable(R.drawable.blue_whale_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(findViewById(R.id.blue_whale_sighting));
                deleteFromArray(R.id.blue_whale_sighting);
                break;
            case R.id.fin_whale_btn:
                drawable = getResources().getDrawable(R.drawable.fin_whale_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(findViewById(R.id.fin_whale_sighting));
                deleteFromArray(R.id.fin_whale_sighting);
                break;
            case R.id.north_atlantic_right_whale_btn:
                drawable = getResources().getDrawable(R.drawable.north_atlantic_right_whale_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(findViewById(R.id.north_atlantic_right_whale_sighting));
                deleteFromArray(R.id.north_atlantic_right_whale_sighting);
                break;
            case R.id.sowerbys_beaker_whale_btn:
                drawable = getResources().getDrawable(R.drawable.sowerbys_beaker_whale_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(findViewById(R.id.sowerbys_beaker_whale_sighting));
                deleteFromArray(R.id.sowerbys_beaker_whale_sighting);
                break;
            case R.id.blainville_whale_btn:
                drawable = getResources().getDrawable(R.drawable.blainville_whale_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(findViewById(R.id.blainville_whale_sighting));
                deleteFromArray(R.id.blainville_whale_sighting);
                break;
            case R.id.gervais_whale_btn:
                drawable = getResources().getDrawable(R.drawable.gervais_whale_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(findViewById(R.id.gervais_whale_sighting));
                deleteFromArray(R.id.gervais_whale_sighting);
                break;
            case R.id.sei_whale_btn:
                drawable = getResources().getDrawable(R.drawable.sei_whale_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(findViewById(R.id.sei_whale_sighting));
                deleteFromArray(R.id.sei_whale_sighting);
                break;
            case R.id.brides_whale_btn:
                drawable = getResources().getDrawable(R.drawable.brides_whale_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(findViewById(R.id.brides_whale_sighting));
                deleteFromArray(R.id.brides_whale_sighting);
                break;
            case R.id.minke_whale_btn:
                drawable = getResources().getDrawable(R.drawable.minke_whale_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(findViewById(R.id.minke_whale_sighting));
                deleteFromArray(R.id.minke_whale_sighting);
                break;
            case R.id.trues_whale_btn:
                drawable = getResources().getDrawable(R.drawable.trues_whale_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(findViewById(R.id.trues_whale_sighting));
                deleteFromArray(R.id.trues_whale_sighting);
                break;
            case R.id.orca_whale_btn:
                drawable = getResources().getDrawable(R.drawable.orca_whale_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(findViewById(R.id.orca_whale_sighting));
                deleteFromArray(R.id.orca_whale_sighting);
                break;
            case R.id.short_finned_pilot_whale_btn:
                drawable = getResources().getDrawable(R.drawable.short_finned_pilot_whale_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(findViewById(R.id.short_finned_pilot_whale_sighting));
                deleteFromArray(R.id.short_finned_pilot_whale_sighting);
                break;
            case R.id.humpback_whale_btn:
                drawable = getResources().getDrawable(R.drawable.humpback_whale_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(findViewById(R.id.humpback_whale_sighting));
                deleteFromArray(R.id.humpback_whale_sighting);
                break;
            case R.id.sperm_whale_btn:
                drawable = getResources().getDrawable(R.drawable.sperm_whale_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(findViewById(R.id.sperm_whale_sighting));
                deleteFromArray(R.id.sperm_whale_sighting);
                break;
            case R.id.northern_whale_btn:
                drawable = getResources().getDrawable(R.drawable.northern_whale_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(findViewById(R.id.northern_whale_sighting));
                deleteFromArray(R.id.northern_whale_sighting);
                break;
            case R.id.long_finned_pilot_whale_btn:
                drawable = getResources().getDrawable(R.drawable.long_finned_pilot_whale_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(findViewById(R.id.long_finned_pilot_whale_sighting));
                deleteFromArray(R.id.long_finned_pilot_whale_sighting);
                break;
            case R.id.false_killer_whale_btn:
                drawable = getResources().getDrawable(R.drawable.false_killer_whale_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(findViewById(R.id.false_killer_whale_sighting));
                deleteFromArray(R.id.false_killer_whale_sighting);
                break;
            case R.id.melon_whale_btn:
                drawable = getResources().getDrawable(R.drawable.melon_whale_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(findViewById(R.id.melon_whale_sighting));
                deleteFromArray(R.id.melon_whale_sighting);
                break;
            case R.id.cuviers_whale_btn:
                drawable = getResources().getDrawable(R.drawable.cuviers_whale_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(findViewById(R.id.cuviers_whale_sighting));
                deleteFromArray(R.id.cuviers_whale_sighting);
                break;
            case R.id.pigmy_whale_btn:
                drawable = getResources().getDrawable(R.drawable.pigmy_whale_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(findViewById(R.id.pigmy_whale_sighting));
                deleteFromArray(R.id.pigmy_whale_sighting);
                break;
            case R.id.not_specified_whale_btn:
                drawable = getResources().getDrawable(R.drawable.not_specified_whale_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(findViewById(R.id.not_specified_whale_sighting));
                deleteFromArray(R.id.not_specified_whale_sighting);
                break;
            case R.id.bottlenose_dolphin_btn:
                drawable = getResources().getDrawable(R.drawable.bottlenose_dolphin_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(findViewById(R.id.bottlenose_dolphin_sighting));
                deleteFromArray(R.id.bottlenose_dolphin_sighting);
                break;
            case R.id.rissos_dolphin_btn:
                drawable = getResources().getDrawable(R.drawable.rissos_dolphin_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(findViewById(R.id.rissos_dolphin_sighting));
                deleteFromArray(R.id.rissos_dolphin_sighting);
                break;
            case R.id.rough_toothed_dolphin_btn:
                drawable = getResources().getDrawable(R.drawable.rough_toothed_dolphin_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(findViewById(R.id.rough_toothed_dolphin_sighting));
                deleteFromArray(R.id.rough_toothed_dolphin_sighting);
                break;
            case R.id.atlantic_spotted_dolphin_btn:
                drawable = getResources().getDrawable(R.drawable.atlantic_spotted_dolphin_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(findViewById(R.id.atlantic_spotted_dolphin_sighting));
                deleteFromArray(R.id.atlantic_spotted_dolphin_sighting);
                break;
            case R.id.striped_dolphin_btn:
                drawable = getResources().getDrawable(R.drawable.striped_dolphin_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(findViewById(R.id.striped_dolphin_sighting));
                deleteFromArray(R.id.striped_dolphin_sighting);
                break;
            case R.id.common_dolphin_btn:
                drawable = getResources().getDrawable(R.drawable.common_dolphin_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(findViewById(R.id.common_dolphin_sighting));
                deleteFromArray(R.id.common_dolphin_sighting);
                break;
            case R.id.frasers_dolphin_btn:
                drawable = getResources().getDrawable(R.drawable.frasers_dolphin_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(findViewById(R.id.frasers_dolphin_sighting));
                deleteFromArray(R.id.frasers_dolphin_sighting);
                break;
            case R.id.not_specified_dolphin_btn:
                drawable = getResources().getDrawable(R.drawable.not_specified_dolphin_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(findViewById(R.id.not_specified_dolphin_sighting));
                deleteFromArray(R.id.not_specified_dolphin_sighting);
                break;
            case R.id.harbour_porpoise_btn:
                drawable = getResources().getDrawable(R.drawable.harbour_porpoise_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(findViewById(R.id.harbour_porpoise_sighting));
                deleteFromArray(R.id.harbour_porpoise_sighting);
                break;
            case R.id.not_specified_porpoise_btn:_btn:
            drawable = getResources().getDrawable(R.drawable.not_specified_porpoise_btn);
                btn1.setBackgroundDrawable(drawable);
                insertPoint.removeView(findViewById(R.id.not_specified_porpoise_sighting));
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
        ImageButton btn1 = (ImageButton) findViewById(view.getId());

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
}