package com.badawy.carservice.fragment;


import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
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
    private ArrayList<OrderModel> ordersList;
    private FirebaseAuth auth;
    private DatabaseReference appointmentsRef, ordersRef;
    private String userId;
    private ImageView navMenuBtn;
    private ConstraintLayout appointmentsLayout, ordersLayout,emptyLayout;
    private RecyclerView appointmentRV, ordersRV;
    private Activity activity;
    private int appointmentsCount, ordersCount;

    public NavAppointmentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nav_appointments_orders, container, false);

        activity = getActivity();
        initializeUi(view);


        // Get User UID from Auth Service
        auth = FirebaseAuth.getInstance();
        userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();

        // Hide the RecyclerViews to prevent them from loading before the data is fetched from firebase
        appointmentsLayout.setVisibility(View.INVISIBLE);
        ordersLayout.setVisibility(View.INVISIBLE);


        // Initialize Database References
        appointmentsRef = FirebaseDatabase.getInstance().getReference();
        ordersRef = FirebaseDatabase.getInstance().getReference();


        // Fetch Appointments
        fetchAppointmentsFromFirebase(new AppointmentsCallBack() {
            @Override
            public void bindAppointmentsData(ArrayList<BookingModel> fetchedAppointmentsList) {

                // This Method is called when the data is fetched from firebase
                bindAppointmentsDataToAdapter(fetchedAppointmentsList);

            }
        });

        // Fetch Orders
        fetchOrdersDataFromFirebase(new OrdersCallBack() {
            @Override
            public void bindOrdersData(ArrayList<OrderModel> fetchedOrdersList) {

                // This Method is called when the data is fetched from firebase
                bindOrderDataToAdapter(fetchedOrdersList);
            }
        });


        // Click on menu icon
        navMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomepageActivity.openDrawer();
            }
        });


        return view;
    }

    private void initializeUi(View view) {
        appointmentRV = view.findViewById(R.id.nav_appointments_appointmentsRV);
        ordersRV = view.findViewById(R.id.nav_appointments_ordersRV);
        appointmentsLayout = view.findViewById(R.id.nav_appointmentsOrders_appointmentsConstraintLayout);
        emptyLayout = view.findViewById(R.id.nav_appointments_emptyLayout);
        ordersLayout = view.findViewById(R.id.nav_appointmentsOrders_ordersConstraintLayout);
        navMenuBtn = view.findViewById(R.id.nav_appointments_navMenuBtn);

    }


    // [[ APPOINTMENTS ]]
    private interface AppointmentsCallBack {
        void bindAppointmentsData(ArrayList<BookingModel> fetchedAppointmentsList);
    }

    private void fetchAppointmentsFromFirebase(final AppointmentsCallBack appointmentsCallBack) {
        // Start the progress Bar
        ((HomepageActivity) activity).showProgressBar(true);

        // root of user`s appointments
        appointmentsRef
                .child(Constants.USERS)
                .child(userId)
                .child(Constants.APPOINTMENTS_ORDERS).child(Constants.APPOINTMENTS)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        // check if user have any appointments Id`s
                        if (dataSnapshot.hasChildren()) {
                            // get number of appointments .. so after the last appointment data is added to our list .. we can call the Appointments CallBack
                            appointmentsCount = (int) dataSnapshot.getChildrenCount();

                            // if true ... create a new appointment list
                            appointmentList = new ArrayList<>();

                            // for each appointment  id ... go fetch the appointment data from Booking root
                            for (final DataSnapshot ds : dataSnapshot.getChildren()) {

                                // root of booking appointments
                                appointmentsRef
                                        .child(Constants.BOOKING)
                                        .child(Constants.APPOINTMENTS)
                                        .child(Objects.requireNonNull(ds.getKey()))
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                // retrieve appointment data and add it to the list
                                                appointmentList.add(dataSnapshot.getValue(BookingModel.class));
                                                appointmentsCount--;
                                                // repeat until every appointment is added to the list

                                                // when last one is added .. call the CAll back and pass the list to it
                                                if (appointmentsCount == 0) {
                                                    appointmentsCallBack.bindAppointmentsData(appointmentList);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                Toast.makeText(getContext(), databaseError.toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            }

                        }// Else if user don`t have any appointments .. pass null to the call back
                        else {
                            appointmentsCallBack.bindAppointmentsData(null);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }

    private void bindAppointmentsDataToAdapter(ArrayList<BookingModel> fetchedAppointmentList) {
        // if the fetched list is not null .. it means that the user HAVE appointments
        if (fetchedAppointmentList != null) {
            // Stop the progress bar and Show and prepare the appointments recycler View
            ((HomepageActivity) activity).showProgressBar(false);
            appointmentsLayout.setVisibility(View.VISIBLE);
            checkForEmptyAppointmentsAndOrders();
            NavAppointmentsAdapter appointmentsAdapter = new NavAppointmentsAdapter(getActivity(), fetchedAppointmentList);
            appointmentRV.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
            appointmentRV.setAdapter(appointmentsAdapter);
        }
        // else if fetched list is null .. user have no appointments
        else {
            // stop the progress bar and hide appointment layout
            ((HomepageActivity) activity).showProgressBar(false);
            appointmentsLayout.setVisibility(View.GONE);
            checkForEmptyAppointmentsAndOrders();
        }


    }


    // [[ ORDERS ]]
    private interface OrdersCallBack {
        void bindOrdersData(ArrayList<OrderModel> orderList);
    }

    private void fetchOrdersDataFromFirebase(final OrdersCallBack ordersCallBack) {
        // Start the progress Bar
        ((HomepageActivity) activity).showProgressBar(true);


        // root of user`s orders
        ordersRef
                .child(Constants.USERS)
                .child(userId)
                .child(Constants.APPOINTMENTS_ORDERS).child(Constants.ORDERS)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        // check if user have any Orders Id`s
                        if (dataSnapshot.hasChildren()) {
                            // get number of Orders .. so after the last order data is added to our list .. we can call the Orders CallBack
                            ordersCount = (int) dataSnapshot.getChildrenCount();

                            // if true ... create a new orders list
                            ordersList = new ArrayList<>();

                            // for each order  id ... go fetch the order data from Booking root
                            for (DataSnapshot ds : dataSnapshot.getChildren()
                            ) {
                                // root of booking Orders
                                ordersRef
                                        .child(Constants.BOOKING)
                                        .child(Constants.ORDERS)
                                        .child(Objects.requireNonNull(ds.getKey()))
                                        .orderByChild("timestamp")
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                // retrieve order data and add it to the list
                                                ordersList.add(dataSnapshot.getValue(OrderModel.class));
                                                ordersCount--;  // decrease the counter by 1  = indicates how many other orders are left to be added
                                                // repeat until every order is added to the list

                                                // when last order is added (counter = 0).. call the CAll back and pass the list to it
                                                if (ordersCount == 0) {
                                                    ordersCallBack.bindOrdersData(ordersList);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                            }
                        } // Else if user don`t have any orders .. pass null to the call back
                        else {
                            ordersCallBack.bindOrdersData(null);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void bindOrderDataToAdapter(ArrayList<OrderModel> fetchedOrdersList) {
        if (fetchedOrdersList != null) {
            ((HomepageActivity) activity).showProgressBar(false);
            ordersLayout.setVisibility(View.VISIBLE);
            checkForEmptyAppointmentsAndOrders();
            NavOrdersAdapter ordersAdapter = new NavOrdersAdapter(getActivity(), fetchedOrdersList);
            ordersRV.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
            ordersRV.setAdapter(ordersAdapter);
        } else {
            // stop the progress bar and hide appointment layout
            ((HomepageActivity) activity).showProgressBar(false);
            ordersLayout.setVisibility(View.GONE);
            checkForEmptyAppointmentsAndOrders();

        }

    }

    private void checkForEmptyAppointmentsAndOrders(){

        if (ordersLayout.getVisibility()==View.GONE && appointmentsLayout.getVisibility()==View.GONE){
            emptyLayout.setVisibility(View.VISIBLE);
        }
        else{
            emptyLayout.setVisibility(View.GONE);
        }
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
