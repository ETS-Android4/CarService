package com.badawy.carservice.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.badawy.carservice.R;


import com.badawy.carservice.activity.HomepageActivity;
import com.badawy.carservice.models.AboutUsDataModel;
import com.badawy.carservice.utils.MySharedPreferences;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUsFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener {

    // Views
    private TextView overView, openHours, phoneNumber, emailAddress;
    private ImageView navMenuBtn;
    private Button callUsBtn;
    // Map Variables
    private static final int REQUEST_CALL = 1;
    private MapView mMapView;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private double lat, lng;

    private String number;

    public AboutUsFragment() {
        // Required empty public constructor


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);
        initializeUi(view);



        // Load the Data from shared preference
        loadDataFromSharedPref();



        // Build the map
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);



        // Click Listeners
        navMenuBtn.setOnClickListener(this);
        callUsBtn.setOnClickListener(this);


        return view;
    }


    private void initializeUi(View view) {
        overView = view.findViewById(R.id.aboutUs_overviewDescription);
        openHours = view.findViewById(R.id.aboutUs_openHoursDescription);
        phoneNumber = view.findViewById(R.id.aboutUs_phoneNumberDescription);
        emailAddress = view.findViewById(R.id.aboutUs_emailAddressDescription);
        callUsBtn = view.findViewById(R.id.aboutUs_callUsBtn);
        navMenuBtn = view.findViewById(R.id.aboutUs_navMenuBtn);
        mMapView = view.findViewById(R.id.mapView);
    }

    private void loadDataFromSharedPref() {
        Activity activity = getActivity();
        if (activity!=null){

            // Get Data Object From Shared Preference
            Gson gson = new Gson();
            String serializedAboutUsData = MySharedPreferences.read(MySharedPreferences.ABOUT_US, "");
            AboutUsDataModel obj = gson.fromJson(serializedAboutUsData, AboutUsDataModel.class);

            overView.setText(obj.getOverView());
            openHours.setText(obj.getOpenHours());
            phoneNumber.setText(obj.getPhoneNumber());
            emailAddress.setText(obj.getEmailAddress());
            lat = obj.getLat();
            lng = obj.getLng();
            number=phoneNumber.getText().toString().trim();
        }


    }


    private void callUs(String num) {

        if (num.trim().length() > 0) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);

            } else {
                String dial = "tel:" + num;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }

        }

    }



    // Life Cycle
    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    // Map callback

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        String number = phoneNumber.getText().toString().trim();

        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callUs(number);
            } else {
                Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onMapReady(final GoogleMap map) {

        map.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lng))
                .title("Car Services"));
        LatLng currentLocation = new LatLng(lat, lng);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));


    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.aboutUs_callUsBtn:
                callUs(number);
                break;
            case R.id.aboutUs_navMenuBtn:
                HomepageActivity.openDrawer();
                break;
            default:
                break;

        }
    }
}
