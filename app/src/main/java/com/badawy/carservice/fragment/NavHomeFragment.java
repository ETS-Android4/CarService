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
public class NavHomeFragment extends Fragment {


    public NavHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_nav_home, container, false);
        ImageView navMenuBtn = view.findViewById(R.id.homepage_img_menu);

        CardView carCenterCard = view.findViewById(R.id.homepage_cv_carCenter);
        CardView emergencyCard = view.findViewById(R.id.homepage_cv_emergency);
        CardView deliveryCard = view.findViewById(R.id.homepage_cv_delivery);
        CardView aboutUsCard = view.findViewById(R.id.homepage_cv_about);

        carCenterCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new CarCenterFragment());
            }
        });

        emergencyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new EmergencyFragment());
            }
        });

        deliveryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new DeliveryCarFragment());
            }
        });
        aboutUsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new AboutUsFragment());
            }
        });

        navMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomepageActivity.openDrawer();
            }
        });

        return view;
    }



    private void replaceFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.homepage_fragment_container, fragment)
                .addToBackStack("NavHomeFragment")
                .commit();


    }


}
