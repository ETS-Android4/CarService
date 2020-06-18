package com.badawy.carservice.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.badawy.carservice.R;
import com.badawy.carservice.activity.CarCenterSelectServicePopUpActivity;
import com.badawy.carservice.activity.HomepageActivity;
import com.badawy.carservice.adapters.SelectCarRecyclerAdapter;
import com.badawy.carservice.adapters.TimeAppointmentRecyclerAdapter;
import com.badawy.carservice.models.BookingModel;
import com.badawy.carservice.models.CarModel;
import com.badawy.carservice.models.ServiceTypeModel;
import com.badawy.carservice.models.TimeAppointmentModel;
import com.badawy.carservice.models.UserProfileModel;
import com.badawy.carservice.utils.Constants;
import com.badawy.carservice.utils.CustomCalendar;
import com.badawy.carservice.utils.MySharedPreferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarCenterFragment extends Fragment implements View.OnClickListener {
    // Constants
    private final int SERVICE_TYPE_REQUEST_CODE = 1003;

    // Firebase
    private DatabaseReference dbRef;
    private Gson gson = new Gson();
    private DatabaseReference timeRef;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEE, d MMMM", Locale.ENGLISH);


    // Lists
    private ArrayList<ServiceTypeModel> cycleList;
    private ArrayList<ServiceTypeModel> specificFixList;
    private ArrayList<ServiceTypeModel> carCareList;
    private ArrayList<TimeAppointmentModel> timeList;
    private ArrayList<CarModel> carList;


    // Views
    private ConstraintLayout serviceLayout;
    private RecyclerView timeRecyclerView, carRecyclerView;
    private CustomCalendar customCalendar;
    private TextView serviceTypeDescription, serviceTypePrice, serviceNameTv;
    private Button bookNowBtn;
    private ImageView navMenuBtn;


    private Intent intent;
    private TimeAppointmentRecyclerAdapter timeAdapter;
    private String selectedDay;
    private SelectCarRecyclerAdapter selectCarRecyclerAdapter;
    private String serviceName;


    private boolean isServiceSelected = false;

    public CarCenterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_car_center, container, false);
        initializeUi(view);


        // Get Service Name to Decide if its Car Care or Vehicle Inspection
        assert getArguments() != null;
         serviceName = getArguments().getString(Constants.SERVICE_NAME_BUNDLE_KEY);

        // Intent to send lists to Pop up activity
        intent = new Intent(getActivity(), CarCenterSelectServicePopUpActivity.class);
        intent.putExtra(Constants.SERVICE_NAME_BUNDLE_KEY, serviceName);

        // Get Current Day to Retrieve Available Appointments
        selectedDay = customCalendar.getDateInYearFormat();


        // Init Appointment Time Root
        timeList = new ArrayList<>();
        timeAdapter = new TimeAppointmentRecyclerAdapter(getActivity());
        timeRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(Constants.AVAILABLE_APPOINTMENTS)
                .child(Constants.CAR_CENTER);
        timeRef.keepSynced(true);


        // Decide if this is car care or Vehicle inspection
        // [[ Car Care]]
        assert serviceName != null;
        if (serviceName.equals(Constants.CAR_CARE)) {

            serviceNameTv.setText(R.string.car_care);

            // init carCare list
            carCareList = new ArrayList<>();

            // Fetch carCare Data from firebase and add to intent
            fetchCarCareDataFromFirebase();


            // Get Available Appointments from firebase
            checkTimeOfCarCare();


            customCalendar.setResetDayClick(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customCalendar.resetDate();
                    selectedDay = customCalendar.getDateInYearFormat();
                    checkTimeOfCarCare();
                }
            });

            customCalendar.setArrowClick(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customCalendar.getNextDay();
                    selectedDay = customCalendar.getDateInYearFormat();
                    checkTimeOfCarCare();
                }
            });


        }

        // [[ Vehicle Inspection ]]
        else if (serviceName.equals(Constants.VEHICLE_INSPECTION)) {
            serviceNameTv.setText(R.string.vehicle_inspection);

            // init Lists
            cycleList = new ArrayList<>();
            specificFixList = new ArrayList<>();

            // Get Available Appointments from firebase
            checkTimeOfVehicleInspection();

            // Fetch carCare Data from firebase and add to intent
            fetchVehicleInspectionDataFromFirebase();


            customCalendar.setResetDayClick(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customCalendar.resetDate();
                    selectedDay = customCalendar.getDateInYearFormat();
                    checkTimeOfVehicleInspection();
                }
            });

            customCalendar.setArrowClick(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customCalendar.getNextDay();
                    selectedDay = customCalendar.getDateInYearFormat();
                    checkTimeOfVehicleInspection();
                }
            });


        }


        // Get User Cars
        getUserCarsFromSharedPreferences();

        serviceLayout.setOnClickListener(this);


        // Add Click Listeners
        navMenuBtn.setOnClickListener(this);
        bookNowBtn.setOnClickListener(this);

        return view;
    }

    // fetch Service Lists
    private void fetchVehicleInspectionDataFromFirebase() {
        dbRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(Constants.APP_DATA)
                .child(Constants.VEHICLE_INSPECTION);
        dbRef.keepSynced(true);

        dbRef.child(Constants.VEHICLE_INSPECTION_CYCLE)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            cycleList.add(new ServiceTypeModel(child.getKey(), (int) ((long) child.getValue())));
                        }
                        String serializedList = gson.toJson(cycleList);
                        intent.putExtra(Constants.VEHICLE_INSPECTION_CYCLE, serializedList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Do nothing
                    }
                });


        dbRef.child(Constants.VEHICLE_INSPECTION_SPECIFIC_FIXES)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            specificFixList.add(new ServiceTypeModel(child.getKey(), (int) ((long) child.getValue())));
                        }
                        String serializedList = gson.toJson(specificFixList);
                        intent.putExtra(Constants.VEHICLE_INSPECTION_SPECIFIC_FIXES, serializedList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Do nothing
                    }
                });

    }

    private void fetchCarCareDataFromFirebase() {
        dbRef = FirebaseDatabase.getInstance().getReference().child(Constants.APP_DATA)
                .child(Constants.CAR_CARE);
        dbRef.keepSynced(true);

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    carCareList.add(new ServiceTypeModel(child.getKey(), (int) ((long) child.getValue())));
                }
                String serializedList = gson.toJson(carCareList);
                intent.putExtra(Constants.CAR_CARE, serializedList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // fetch Available Appointments
    private void checkTimeOfCarCare() {
        // Simple Testing Data for Time
        final String[] time = {"12:00", "1:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00", "8:00", "9:00"};
        final String[] timeOfDay = {"pm", "pm", "pm", "pm", "pm", "pm", "pm", "pm", "pm", "pm"};


        timeRef.child(Constants.CAR_CARE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (!dataSnapshot.hasChild(selectedDay)) {
                    // Filling time list
                    for (int i = 0; i < time.length; i++) {
                        TimeAppointmentModel obj = new TimeAppointmentModel();
                        obj.setId(i);
                        obj.setBooked("no");
                        obj.setTime(time[i]);
                        obj.setTimeOfDay(timeOfDay[i]);


                        timeRef.child(Constants.CAR_CARE)
                                .child(selectedDay)
                                .child(String.valueOf(obj.getId()))
                                .setValue(obj);

                    }
                } else {

                    timeList = new ArrayList<>();
                    // if day exist ... return available appointments
                    for (DataSnapshot ds : dataSnapshot.child(selectedDay).getChildren()) {
                        TimeAppointmentModel obj = new TimeAppointmentModel();
                        obj.setBooked(ds.child("booked").getValue(String.class));
                        obj.setId((int) ((long) ds.child("id").getValue()));
                        obj.setTime(ds.child("time").getValue(String.class));
                        obj.setTimeOfDay(ds.child("timeOfDay").getValue(String.class));
                        timeList.add(obj);
                        updateView(timeList);
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkTimeOfVehicleInspection() {
        // Simple Testing Data for Time
        final String[] time = {"12:00", "1:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00", "8:00", "9:00"};
        final String[] timeOfDay = {"pm", "pm", "pm", "pm", "pm", "pm", "pm", "pm", "pm", "pm"};


        timeRef.child(Constants.VEHICLE_INSPECTION).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (!dataSnapshot.hasChild(selectedDay)) {
                    // Filling time list
                    for (int i = 0; i < time.length; i++) {
                        TimeAppointmentModel obj = new TimeAppointmentModel();
                        obj.setId(i);
                        obj.setBooked("no");
                        obj.setTime(time[i]);
                        obj.setTimeOfDay(timeOfDay[i]);


                        timeRef.child(Constants.VEHICLE_INSPECTION)
                                .child(selectedDay)
                                .child(String.valueOf(obj.getId()))
                                .setValue(obj);

                    }
                } else {

                    timeList = new ArrayList<>();
                    // if day exist ... return available appointments
                    for (DataSnapshot ds : dataSnapshot.child(selectedDay).getChildren()) {
                        TimeAppointmentModel obj = new TimeAppointmentModel();
                        obj.setBooked(ds.child("booked").getValue(String.class));
                        obj.setId((int) ((long) ds.child("id").getValue()));
                        obj.setTime(ds.child("time").getValue(String.class));
                        obj.setTimeOfDay(ds.child("timeOfDay").getValue(String.class));
                        timeList.add(obj);
                        updateView(timeList);
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // Update Time Recycler Adapter
    // TEST AFTER NOON TO CHECK IF DATA IS RETURNED SUCCESSFULLY  AFTER CURR TIME PASS APPOINTMENT TIME
    private void updateView(ArrayList<TimeAppointmentModel> timeList) {
        ArrayList<TimeAppointmentModel> availableTimeList = new ArrayList<>();
        for (TimeAppointmentModel obj : timeList
        ) {  String appointmentTime = obj.getTime();

            if (customCalendar.getDay().equals(dateFormatter.format(Calendar.getInstance().getTime()))) {

                if (customCalendar.getTimeOfDay().equals("am")) {
                    if (obj.getBooked().equals("no")) {
                        availableTimeList.add(obj);
                    }
                } else if (customCalendar.getTimeOfDay().equals("pm")) {

                    if (appointmentTime.compareTo(customCalendar.getTime()) > 0) {
                        if (obj.getBooked().equals("no")) {
                            availableTimeList.add(obj);
                        }
                    }

                }
            } else {

                if (obj.getBooked().equals("no")) {
                    availableTimeList.add(obj);
                }

            }

        }
        //    TimeAppointmentRecyclerAdapter timeAppointmentRecyclerAdapter = new TimeAppointmentRecyclerAdapter(getContext());

        timeAdapter.setTimeList(availableTimeList);
        timeRecyclerView.setAdapter(timeAdapter);
        // timeRecyclerView.setAdapter(timeAppointmentRecyclerAdapter);

    }


    // get User Cars
    private void getUserCarsFromSharedPreferences() {
        Type type = new TypeToken<ArrayList<CarModel>>() {
        }.getType();
        String serializedUserCarList = MySharedPreferences.read(MySharedPreferences.USER_CARS, "");
        if (!serializedUserCarList.equals("")) {

            carList = gson.fromJson(serializedUserCarList, type);
        }

        bindUserCars(carList);
    }

    private void bindUserCars(ArrayList<CarModel> carList) {

         selectCarRecyclerAdapter = new SelectCarRecyclerAdapter(getActivity(), carList);
        carRecyclerView.setAdapter(selectCarRecyclerAdapter);
    }


    // init Ui
    private void initializeUi(View view) {

        serviceNameTv = view.findViewById(R.id.carCenter_serviceTypeName);
        serviceLayout = view.findViewById(R.id.carCenter_serviceLayout);
        serviceTypeDescription = view.findViewById(R.id.carCenter_serviceTypeDescription);
        serviceTypePrice = view.findViewById(R.id.carCenter_serviceCost);
        timeRecyclerView = view.findViewById(R.id.carCenter_timeRecyclerView);
        customCalendar = view.findViewById(R.id.carCenter_customCalender);
        carRecyclerView = view.findViewById(R.id.carCenter_chooseCarRecyclerView);
        bookNowBtn = view.findViewById(R.id.carCenter_bookNowButton);
        navMenuBtn = view.findViewById(R.id.carCenter_navMenuBtn);


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SERVICE_TYPE_REQUEST_CODE && resultCode == RESULT_OK) {

            if (data != null) {
                // retrieve the serialized list and convert it to LIST type
                String serializedReturningList = data.getStringExtra(Constants.SERVICE_TYPES_NAME_RESULT);
                Type type = new TypeToken<ArrayList<ServiceTypeModel>>() {
                }.getType();
                ArrayList<ServiceTypeModel> selectedList = gson.fromJson(serializedReturningList, type);


                StringBuilder builder = new StringBuilder();
                if (selectedList.isEmpty()) {
                    serviceTypeDescription.setText(R.string.click_to_select_service);
                    isServiceSelected = false;
                } else {


                    for (int i = 0; i < selectedList.size(); i++) {


                        if (i + 1 != selectedList.size()) {
                            builder.append(selectedList.get(i).getServiceName()).append(" + ");
                        } else {
                            builder.append(selectedList.get(i).getServiceName());
                        }

                    }

                    serviceTypeDescription.setText(builder.toString());

                }
                int servicePrice = data.getIntExtra(Constants.SERVICE_TYPES_PRICE_RESULT, 0);
                if (servicePrice == 0) {
                    serviceTypePrice.setText(R.string.please_select_a_service_first);
                } else {
                    serviceTypePrice.setText(String.valueOf(servicePrice));

                }
                isServiceSelected = true;
            }

        }
    }


    private void fakeDataTest() {
//        // Simple Testing Data for Time
//        byte[] id = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
//        String[] time = {"11:00", "12:00", "1:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00", "8:00", "9:00"};
//        String[] timeOfDay = {"am", "pm", "pm", "pm", "pm", "pm", "pm", "pm", "pm", "pm", "pm"};
//        String[] booked = {"yes", "yes", "yes", "yes", "yes", "no", "no", "no", "no", "no", "no"};
//
//        // Simple Testing Data for Car Selection
//        byte[] carId = {0, 1, 2, 3, 4};
//        int[] carImage = {R.drawable.ic_nav_cars_car_test, R.drawable.ic_nav_cars_car_test, R.drawable.ic_nav_cars_car_test, R.drawable.ic_nav_cars_car_test, R.drawable.ic_nav_cars_car_test};
//        String[] carName = {"2018 Audi TT", "2014 Mazda Mk4", "2018 Audi TT", "2014 Mazda Mk4", "2018 Audi TT"};
        // Fake Data
//        ArrayList<TimeAppointmentModel> timeList = new ArrayList<>();
//        ArrayList<SelectCarModel> carList = new ArrayList<>();
//
//        // Filling time list
//        for (int i = 0; i < time.length; i++) {
//
//            if (booked[i].equals("no")) {
//                timeList.add(new TimeAppointmentModel(id[i], time[i], timeOfDay[i], booked[i]));
//            }
//
//        }
//
//
//        // Filling car list
//        for (int i = 0; i < carId.length; i++) {
//            carList.add(new SelectCarModel(carId[i], carImage[i], carName[i]));
//
//        }
//
//
//        // Testing time list
//        RecyclerView timeRecyclerView = view.findViewById(R.id.carCenter_timeRecyclerView);
//        final TimeAppointmentRecyclerAdapter timeAdapter = new TimeAppointmentRecyclerAdapter(getActivity(), timeList);
//        timeRecyclerView.setAdapter(timeAdapter);
//
//        //Testing car list
//        RecyclerView carRecyclerView = view.findViewById(R.id.carCenter_chooseCarRecyclerView);
//        final SelectCarRecyclerAdapter carAdapter = new SelectCarRecyclerAdapter(getActivity(), carList);
//        carRecyclerView.setAdapter(carAdapter);
//
//
//        Button book = view.findViewById(R.id.carCenter_bookNowButton);
//        book.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getActivity(), timeAdapter.getTimeId() + "time", Toast.LENGTH_SHORT).show();
//                Toast.makeText(getActivity(), carAdapter.getCarId() + "car", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//         Testing comparing time for future purpose
//        CustomCalendar customCalendar = view.findViewById(R.id.carCenter_customCalender);
//        String time = "6:58";
//        if (customCalendar.getTime().compareTo(time)>0||customCalendar.getTime().equals(time))
//            Toast.makeText(getActivity(), "yes "+customCalendar.getTime()+" is after "+time, Toast.LENGTH_SHORT).show();
//        else Toast.makeText(getActivity(), "no "+customCalendar.getTime()+" less than "+time, Toast.LENGTH_SHORT).show();
    }

    private void bookNow(String tag) {
        if (!isServiceSelected) {
            Toast.makeText(getActivity(), "Please Select a service First", Toast.LENGTH_SHORT).show();
        } else {
        final BookingModel bookingObject = new BookingModel();
        Gson gson = new Gson();
        String userSerializedData = MySharedPreferences.read(MySharedPreferences.USER_DATA, "");
        if (!userSerializedData.equals("")) {
            UserProfileModel userDataObject = gson.fromJson(userSerializedData, UserProfileModel.class);


            bookingObject.setServiceName(serviceNameTv.getText().toString().trim());
            bookingObject.setPrice(serviceTypePrice.getText().toString().trim());
            bookingObject.setServiceDescription(serviceTypeDescription.getText().toString().trim());
            bookingObject.setDate(selectedDay);
            bookingObject.setTimeObject(timeAdapter.getTimeObject());
            bookingObject.setUserId(userDataObject.getUserId());
            bookingObject.setCarId(selectCarRecyclerAdapter.getSelectedCarObject().getCarID());
            bookingObject.setAddress(getResources().getString(R.string.university_address));

            dbRef = FirebaseDatabase.getInstance().getReference().child(Constants.AVAILABLE_APPOINTMENTS)
                    .child(Constants.CAR_CENTER).child(tag).child(bookingObject.getDate())
                    .child(String.valueOf(bookingObject.getTimeObject().getId()));

            final DatabaseReference bookingRef = FirebaseDatabase.getInstance().getReference().child(Constants.BOOKING);
            final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child(Constants.USERS);

            dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    dbRef.child("booked").setValue("yes");
                    String orderID = bookingRef.push().getKey();
                    assert orderID != null;
                    bookingObject.setOrderId(orderID);
                    bookingRef.child(Constants.APPOINTMENTS).child(orderID)
                            .setValue(bookingObject);

                    userRef.child(bookingObject.getUserId()).child(Constants.APPOINTMENTS_ORDERS)
                            .child(Constants.APPOINTMENTS)
                            .child(orderID).setValue(0);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.carCenter_navMenuBtn:
                HomepageActivity.openDrawer();
                break;
            case R.id.carCenter_serviceLayout:
                startActivityForResult(intent, SERVICE_TYPE_REQUEST_CODE);
                break;

            case R.id.carCenter_bookNowButton:
                if (serviceName.equals(Constants.CAR_CARE)){

                    bookNow(Constants.CAR_CARE);
                }
                else
                    bookNow(Constants.VEHICLE_INSPECTION);
                break;


        }
    }
}
