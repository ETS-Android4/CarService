package com.badawy.carservice.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.badawy.carservice.R;
import com.badawy.carservice.activity.HomepageActivity;
import com.badawy.carservice.adapters.AccountInfoLabelsAdapter;
import com.badawy.carservice.models.AccountInfoModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavSettingsFragment extends Fragment {

    RecyclerView accountInfoRV;

    String [] accountInfoLabels = {"Your Name","Your Email","Address","Phone Number"};
    ArrayList<AccountInfoModel> accountInfoList = new ArrayList<>();

    public NavSettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nav_settings, container, false);
        ImageView navMenuBtn = view.findViewById(R.id.settings_navMenuBtn);
        accountInfoRV= view.findViewById(R.id.settings_accountInfoRV);

        AccountInfoLabelsAdapter accountInfoLabelsAdapter = new AccountInfoLabelsAdapter(getActivity(),accountInfoLabels);
        accountInfoRV.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        accountInfoRV.setAdapter(accountInfoLabelsAdapter);


        navMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomepageActivity.openDrawer();
            }
        });

        return view;
    }

}
