package com.badawy.carservice.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.badawy.carservice.R;
import com.badawy.carservice.activity.HomepageActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavCarsFragment extends Fragment {


    ImageView navCarsEdit;
    TextView navCarsCarName;
    LinearLayout navCarsRemoveCarLayout;
    LinearLayout navCarsCancelLayout;
    boolean isVisible = true;

    public NavCarsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nav_cars, container, false);
        ImageView navMenuBtn = view.findViewById(R.id.navCars_navMenuBtn);
        navCarsRemoveCarLayout = view.findViewById(R.id.navCars_removeCar);
        navCarsCancelLayout = view.findViewById(R.id.navCars_cancel);
        navCarsEdit = view.findViewById(R.id.navCars_edit);
        navCarsCarName = view.findViewById(R.id.navCars_carName);
        navCarsEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isVisible) {

                    navCarsCarName.setVisibility(View.INVISIBLE);
                    navCarsRemoveCarLayout.setVisibility(View.VISIBLE);
                    navCarsCancelLayout.setVisibility(View.VISIBLE);
                    isVisible = !isVisible;
                } else {
                    navCarsCarName.setVisibility(View.VISIBLE);
                    navCarsRemoveCarLayout.setVisibility(View.INVISIBLE);
                    navCarsCancelLayout.setVisibility(View.INVISIBLE);
                    isVisible = !isVisible;
                }

            }
        });


        navCarsCancelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navCarsCarName.setVisibility(View.VISIBLE);
                navCarsRemoveCarLayout.setVisibility(View.INVISIBLE);
                navCarsCancelLayout.setVisibility(View.INVISIBLE);
                isVisible = !isVisible;
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

}
