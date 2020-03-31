package com.badawy.carservice.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.badawy.carservice.R;
import com.badawy.carservice.activity.AddCarActivity;
import com.badawy.carservice.activity.ForgotPasswordActivity;
import com.badawy.carservice.activity.HomepageActivity;
import com.badawy.carservice.activity.LoginActivity;
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

//VAR FOR TO GO TO THE ADDCAR ACTIVITY
    private TextView gotoaddcar;
    private ImageView gotoaddcar1;



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nav_settings, container, false);
//AHMED TAREK..................................
        gotoaddcar=(TextView) view.findViewById(R.id.settings_addNewCarLabel);
        gotoaddcar1=(ImageView)view.findViewById(R.id.settings_addNewCar);





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


        //AHMED TAREK SAAD THIS FUNCTION FOR OR TO GO TO THE ADDCAR ACTIVITY BY CLICK ON THE (ADD NEW CAR) 31/3/2020.............

      gotoaddcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent = new Intent(getActivity(), AddCarActivity.class);
                startActivity(myintent);

            }
        });
        //AHMED TAREK SAAD THIS FUNCTION FOR OR TO GO TO THE ADDCAR ACTIVITY BY CLICK ON THE (image view (arrow)) 31/3/2020..............
      gotoaddcar1.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent myintent1=new Intent(getActivity(),AddCarActivity.class);
              startActivity(myintent1);
          }
      });







        return view;
    }

}
