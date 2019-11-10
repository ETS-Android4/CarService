package com.badawy.carservice.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.badawy.carservice.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceCenter_CarCareFragment extends Fragment {


    public ServiceCenter_CarCareFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_service_center_car_care, container, false);
    }

}
