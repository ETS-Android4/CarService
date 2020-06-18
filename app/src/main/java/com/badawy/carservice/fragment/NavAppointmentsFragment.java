package com.badawy.carservice.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.badawy.carservice.R;
import com.badawy.carservice.activity.HomepageActivity;
import com.badawy.carservice.adapters.NavAppointmentsAdapter;
import com.badawy.carservice.adapters.NavOrdersAdapter;
import com.badawy.carservice.models.BookingModel;
import com.badawy.carservice.models.NavAppointmentsModel;
import com.badawy.carservice.models.NavOrdersModel;
import com.badawy.carservice.models.OrderModel;
import com.badawy.carservice.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavAppointmentsFragment extends Fragment {


    private ArrayList<BookingModel> appointmentList;
    private ArrayList<OrderModel> orderList;
    private FirebaseAuth auth;
    private DatabaseReference appointmentsRef, ordersRef;
    private String userId;
    private RecyclerView appointmentRV,ordersRV;

    public NavAppointmentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nav_appointments, container, false);
        ImageView navMenuBtn = view.findViewById(R.id.nav_appointments_navMenuBtn);
        auth = FirebaseAuth.getInstance();
         userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
         appointmentRV = view.findViewById(R.id.nav_appointments_appointmentsRV);
         ordersRV = view.findViewById(R.id.nav_appointments_ordersRV);
        appointmentRV.setVisibility(View.GONE);
        ordersRV.setVisibility(View.GONE);



        appointmentsRef = FirebaseDatabase.getInstance().getReference();
        ordersRef = FirebaseDatabase.getInstance().getReference();


        appointmentsRef.child(Constants.USERS)
                .child(userId)
                .child(Constants.APPOINTMENTS_ORDERS).child(Constants.APPOINTMENTS)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    appointmentList = new ArrayList<>();

                    for (final DataSnapshot ds : dataSnapshot.getChildren()
                    ) {

                        appointmentsRef.child(Constants.BOOKING).child(Constants.APPOINTMENTS).child(Objects.requireNonNull(ds.getKey()))
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        appointmentList.add(dataSnapshot.getValue(BookingModel.class));

                                        bindDataApp();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(getContext(), databaseError.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });


                    }

                }
                else
                {
                    appointmentList = null;
                }

                // modify here as if list is empty do twhhhahttttt ? ? ? ?D?D?D?
                bindDataApp();


                checkOrders();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

    private void bindDataApp() {
        NavAppointmentsAdapter appointmentsAdapter = new NavAppointmentsAdapter(getActivity(),appointmentList);
        appointmentRV.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        appointmentRV.setAdapter(appointmentsAdapter);
        appointmentRV.setVisibility(View.VISIBLE);


    }

    private void checkOrders() {

        ordersRef.child(Constants.USERS)
                .child(userId)
                .child(Constants.APPOINTMENTS_ORDERS).child(Constants.ORDERS)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    orderList = new ArrayList<>();

                    for (DataSnapshot ds :  dataSnapshot.getChildren()
                            ) {

                        ordersRef.child(Constants.BOOKING).child(Constants.ORDERS).child(Objects.requireNonNull(ds.getKey())).orderByChild("timestamp")
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        orderList.add(dataSnapshot.getValue(OrderModel.class));
                                        bindOrderData();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                    }
                } //
                else
                {
                    orderList = null;
                }
                bindOrderData();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void bindOrderData() {
        NavOrdersAdapter ordersAdapter = new NavOrdersAdapter(getActivity(),orderList);
        ordersRV.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        ordersRV.setAdapter(ordersAdapter);
        ordersRV.setVisibility(View.VISIBLE);

    }


    private void fakeDataTest() {

//        private ArrayList<NavOrdersModel> ordersList = new ArrayList<>();
//        private ArrayList<NavAppointmentsModel> appointmentList = new ArrayList<>();

//        String [] serviceLabel = {"Car Care","Oil Change"};
//        String [] serviceType = {"Automated In-Bay Car Wash","Engine Oil"};
//        String [] servicePrice = {"150 EGP","250 EGP"};
//        String [] serviceDate = {"30 March, 2020","2 April, 2020"};
//        String [] serviceTime = {"1:00 PM","7:00 PM"};
//        String [] serviceAddress = {"Culture & Science City, Second 6th of October","Culture & Science City, Second 6th of October"};
//
//        int[] productImage={R.drawable.tire};
//        String[] productName={"Hankook KINERGY ECO 2 K435"};
//        String[] productPartNumber={"8808563301211"};
//        String[] productPrice={"2500.00 EGP"};
//        String[] orderDate={"24 March, 2020"};
//        String[] orderTime={"3:00 PM"};
//        String[] orderNumber={"124550"};
//
//
//        RecyclerView appointmentRV = view.findViewById(R.id.nav_appointments_appointmentsRV);
//        RecyclerView ordersRV = view.findViewById(R.id.nav_appointments_ordersRV);
//
//        for (int i = 0 ; i< serviceLabel.length;i++)
//        {
//            appointmentList.add(new NavAppointmentsModel(serviceLabel[i],serviceType[i],servicePrice[i],serviceDate[i],serviceTime[i],serviceAddress[i]));
//
//        }
//
//
//
//        NavAppointmentsAdapter appointmentsAdapter = new NavAppointmentsAdapter(getActivity(),appointmentList);
//        appointmentRV.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
//        appointmentRV.setAdapter(appointmentsAdapter);
//
//        for (int i = 0 ; i< orderNumber.length;i++)
//        {
//            ordersList.add(new NavOrdersModel(productImage[i],productName[i],productPartNumber[i],productPrice[i],orderNumber[i],orderDate[i],orderTime[i]));
//
//        }
//
//        NavOrdersAdapter ordersAdapter = new NavOrdersAdapter(getActivity(),ordersList);
//        ordersRV.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
//        ordersRV.setAdapter(ordersAdapter);

    }

}
