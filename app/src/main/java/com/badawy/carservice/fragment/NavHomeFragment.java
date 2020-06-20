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
public class NavHomeFragment extends Fragment implements View.OnClickListener {

    private final static String TAG_FRAGMENT = "NAV_HOME";
    private CardView carCenterCard, emergencyCard, deliveryCard, aboutUsCard;
    private ImageView navMenuBtn;

    public NavHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_nav_home, container, false);

        initializeUi(view);



        // Click Listeners
        carCenterCard.setOnClickListener(this);
        emergencyCard.setOnClickListener(this);
        deliveryCard.setOnClickListener(this);
        aboutUsCard.setOnClickListener(this);
        navMenuBtn.setOnClickListener(this);

        return view;
    }

    private void initializeUi(View view) {

        navMenuBtn = view.findViewById(R.id.homepage_img_menu);
        carCenterCard = view.findViewById(R.id.homepage_cv_carCenter);
        emergencyCard = view.findViewById(R.id.homepage_cv_emergency);
        deliveryCard = view.findViewById(R.id.homepage_cv_delivery);
        aboutUsCard = view.findViewById(R.id.homepage_cv_about);

    }


    private void replaceFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.homepage_fragment_container, fragment)
                .addToBackStack(TAG_FRAGMENT)
                .commit();


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.homepage_cv_carCenter:
                replaceFragment(new CarCenterChooseFragment());
                break;
            case R.id.homepage_cv_emergency:
                replaceFragment(new EmergencyFragment());
                break;
            case R.id.homepage_cv_delivery:
                replaceFragment(new DeliveryCarFragment());
                break;
            case R.id.homepage_cv_about:
                replaceFragment(new AboutUsFragment());
                break;
            case R.id.homepage_img_menu:
                HomepageActivity.openDrawer();
                break;
            default:
                break;
        }
    }
}
