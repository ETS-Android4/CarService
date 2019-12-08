package com.badawy.carservice.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

        navMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomepageActivity.openDrawer();
            }
        });

        return view;
    }


}
