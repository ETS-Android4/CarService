package com.badawy.carservice.fragment;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.badawy.carservice.R;
import com.badawy.carservice.activity.HomepageActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarCenterChooseFragment extends Fragment {

    public CarCenterChooseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_car_center_choose, container, false);
        CardView carCareCV = view.findViewById(R.id.carCenterChoose_carCareCV);
        CardView carInspectionCV = view.findViewById(R.id.carCenterChoose_carInspectionCV);

        ImageView navMenuBtn = view.findViewById(R.id.carCenterChoose_navMenuBtn);
        navMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomepageActivity.openDrawer();
            }
        });

        carCareCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                replaceFragment(new CarCenterFragment());

            }
        });
         carInspectionCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                replaceFragment(new CarCenterFragment());

            }
        });



        return view;
    }

    private void replaceFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.homepage_fragment_container, fragment)
                .addToBackStack("CarCenterChooseFragment")
                .commit();


    }

}
