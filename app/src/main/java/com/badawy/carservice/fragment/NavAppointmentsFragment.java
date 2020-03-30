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
import com.badawy.carservice.adapters.NavAppointmentsAdapter;
import com.badawy.carservice.adapters.NavOrdersAdapter;
import com.badawy.carservice.models.NavAppointmentsModel;
import com.badawy.carservice.models.NavOrdersModel;
import com.badawy.carservice.models.ProductItemModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavAppointmentsFragment extends Fragment {

    private String [] serviceLabel = {"Car Care","Oil Change"};
    private String [] serviceType = {"Automated In-Bay Car Wash","Engine Oil"};
    private String [] servicePrice = {"150 EGP","250 EGP"};
    private String [] serviceDate = {"30 March, 2020","2 April, 2020"};
    private String [] serviceTime = {"1:00 PM","7:00 PM"};
    private String [] serviceAddress = {"Culture & Science City, Second 6th of October","Culture & Science City, Second 6th of October"};

    private int[] productImage={R.drawable.tire};
    private String[] productName={"Hankook KINERGY ECO 2 K435"};
    private String[] productPartNumber={"8808563301211"};
    private String[] productPrice={"2500.00 EGP"};
    private String[] orderDate={"24 March, 2020"};
    private String[] orderTime={"3:00 PM"};
    private String[] orderNumber={"124550"};
    private ArrayList<NavOrdersModel> ordersList = new ArrayList<>();
    private ArrayList<NavAppointmentsModel> appointmentList = new ArrayList<>();


    public NavAppointmentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nav_appointments, container, false);

        ImageView navMenuBtn = view.findViewById(R.id.nav_appointments_navMenuBtn);
        navMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomepageActivity.openDrawer();
            }
        });



        RecyclerView appointmentRV = view.findViewById(R.id.nav_appointments_appointmentsRV);
        RecyclerView ordersRV = view.findViewById(R.id.nav_appointments_ordersRV);

        for (int i = 0 ; i< serviceLabel.length;i++)
        {
            appointmentList.add(new NavAppointmentsModel(serviceLabel[i],serviceType[i],servicePrice[i],serviceDate[i],serviceTime[i],serviceAddress[i]));

        }



        NavAppointmentsAdapter appointmentsAdapter = new NavAppointmentsAdapter(getActivity(),appointmentList);
        appointmentRV.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        appointmentRV.setAdapter(appointmentsAdapter);

        for (int i = 0 ; i< orderNumber.length;i++)
        {
            ordersList.add(new NavOrdersModel(productImage[i],productName[i],productPartNumber[i],productPrice[i],orderNumber[i],orderDate[i],orderTime[i]));

        }

        NavOrdersAdapter ordersAdapter = new NavOrdersAdapter(getActivity(),ordersList);
        ordersRV.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        ordersRV.setAdapter(ordersAdapter);


        return view;
    }

}
