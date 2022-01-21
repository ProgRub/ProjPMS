package com.example.seaker;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.example.seaker.business.BusinessFacade;
import com.example.seaker.fragments.BaseFragment;
import com.example.seaker.fragments.ReportSightingFragment;
import com.example.seaker.fragments.SplashFragment;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MQTTHelper mqtt;
    String[] permissions= new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};


    private static FragmentManager supportFragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        supportFragmentManager = getSupportFragmentManager();
        switchFragment(new SplashFragment());

        checkPermissions();

        try {
            mqtt = MQTTHelper.getInstance(getApplicationContext()); //apenas para iniciar conexão ao MQTT
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LinearLayout uploadedPhotosLayout = (LinearLayout) findViewById(R.id.uploaded_photos);

        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.uploaded_photo_layout, null);

        v.findViewById(R.id.delete_photo_btn).setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View view) {
                uploadedPhotosLayout.removeView(v.findViewById(R.id.photo_struct));
            }
        });

        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            try {
                Bitmap captureImage = (Bitmap) data.getExtras().get("data");
                ImageView image = (ImageView) v.findViewById(R.id.photo);
                image.setImageBitmap(Bitmap.createScaledBitmap(captureImage, 240, 240, false));
                uploadedPhotosLayout.addView(v);
            }catch(NullPointerException e){
                // TODO Auto-generated catch block
            }
        }else if(resultCode == Activity.RESULT_OK && requestCode == 200){
            Uri selectedImage = data.getData();
            try {
                ImageView image = (ImageView) v.findViewById(R.id.photo);
                image.setImageBitmap(Bitmap.createScaledBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage), 240, 240, false));
                uploadedPhotosLayout.addView(v);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }catch(NullPointerException e){
                // TODO Auto-generated catch block
            }
        }
    }


    public static void switchFragment(BaseFragment fragment){
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, fragment)
                .commit();
    }

    public void onButtonShowPopupWindowClick(View view, String popup_message) {

        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);
        TextView message = (TextView) popupView.findViewById(R.id.popup_message);
        message.setText(popup_message);

        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, 1000, 500, focusable);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                popupWindow.dismiss();
            }
        }, 2600);
    }

    private  boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(this,p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),10 );
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissionsList[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissionsList, grantResults);
        switch (requestCode) {
            case 10: {
                if (grantResults.length > 0) {
                    String permissionsDenied = "";
                    for (String per : permissionsList) {
                        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                            permissionsDenied += "\n" + per;

                        }

                    }
                }
                return;
            }
        }
    }
}