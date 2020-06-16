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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.badawy.carservice.R;
import com.badawy.carservice.activity.HomepageActivity;
import com.badawy.carservice.activity.SpeedFixPopUpActivity;
import com.badawy.carservice.adapters.TimeAppointmentRecyclerAdapter;
import com.badawy.carservice.models.CarModel;
import com.badawy.carservice.models.ServiceTypeModel;
import com.badawy.carservice.models.BookingModel;
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
public class DeliveryCarSpeedFixFragment extends Fragment {
    private final String TAG = "Speed Fix";
    private final int SERVICE_TYPE_REQUEST_CODE = 1002;

    private SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEE, d MMMM", Locale.ENGLISH);

    private ArrayList<ServiceTypeModel> speedFixServiceList;
    private DatabaseReference dbRef;
    private Gson gson = new Gson();
    private ArrayList<TimeAppointmentModel> timeList;
    private Intent intent;
    private CustomCalendar customCalendar;
    private ConstraintLayout serviceLayout;
    private TextView serviceTypeDescription, serviceTypePrice;
    private TimeAppointmentRecyclerAdapter timeAdapter;
    private RecyclerView timeRecyclerView;
    private String selectedDay;
    private DatabaseReference timeRef;
    private Button orderNowBtn;
    private EditText additionalNote;
    private CarModel selectedCarObject;

    private boolean isServiceSelected = false;


    public DeliveryCarSpeedFixFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_speedfix, container, false);

        initializeUi(view);

        // Get Selected Car
        getSelectedCar();

        // Get Service List From Firebase
        fetchSpeedFixDataFromFirebase();

        // Intent to send lists to Pop up activity
        intent = new Intent(getActivity(), SpeedFixPopUpActivity.class);


        // Init Appointment Time Root
        timeList = new ArrayList<>();
        timeAdapter = new TimeAppointmentRecyclerAdapter(getActivity());
        timeRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(Constants.AVAILABLE_APPOINTMENTS)
                .child(Constants.DELIVERY);
        timeRef.keepSynced(true);


        // Get Current Day to Retrieve Available Appointments
        selectedDay = customCalendar.getDateInYearFormat();
        checkTimeOfSpeedFix();

        customCalendar.setResetDayClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customCalendar.resetDate();
                selectedDay = customCalendar.getDateInYearFormat();
                checkTimeOfSpeedFix();
            }
        });

        customCalendar.setArrowClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customCalendar.getNextDay();
                selectedDay = customCalendar.getDateInYearFormat();
                checkTimeOfSpeedFix();
            }
        });


        serviceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(intent, SERVICE_TYPE_REQUEST_CODE);

            }
        });

        ImageView navMenuBtn = view.findViewById(R.id.speedFix_navMenuBtn);
        navMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomepageActivity.openDrawer();
            }
        });

        orderNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isServiceSelected) {
                    Toast.makeText(getActivity(), "Please Select a service First", Toast.LENGTH_SHORT).show();
                } else {
                    final BookingModel bookingObject = new BookingModel();
                    Gson gson = new Gson();
                    String userSerializedData = MySharedPreferences.read(MySharedPreferences.USER_DATA, "");
                    if (!userSerializedData.equals("")) {
                        UserProfileModel userDataObject = gson.fromJson(userSerializedData, UserProfileModel.class);


                        bookingObject.setServiceName(TAG);
                        bookingObject.setPrice(serviceTypePrice.getText().toString().trim());
                        bookingObject.setServiceDescription(serviceTypeDescription.getText().toString().trim());
                        bookingObject.setDate(selectedDay);
                        bookingObject.setTimeID(String.valueOf(timeAdapter.getTimeId()));
                        bookingObject.setUserId(userDataObject.getUserId());
                        bookingObject.setNote(additionalNote.getText().toString().trim());
                        bookingObject.setCarId(selectedCarObject.getCarID());

                        dbRef = FirebaseDatabase.getInstance().getReference().child(Constants.AVAILABLE_APPOINTMENTS)
                                .child(Constants.DELIVERY).child(Constants.SPEED_FIX).child(bookingObject.getDate())
                                .child(bookingObject.getTimeID());

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
        });
        return view;
    }

    private void initializeUi(View view) {
        customCalendar = view.findViewById(R.id.speedFix_customCalender);
        serviceLayout = view.findViewById(R.id.speedFix_serviceLayout);
        serviceTypeDescription = view.findViewById(R.id.speedFix_serviceTypeDescription);
        serviceTypePrice = view.findViewById(R.id.speedFix_serviceCost);
        timeRecyclerView = view.findViewById(R.id.speedFix_timeRecyclerView);
        orderNowBtn = view.findViewById(R.id.speedFix_orderNowButton);
        additionalNote = view.findViewById(R.id.speedFix_typeNoteET);
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


    private void fetchSpeedFixDataFromFirebase() {
        speedFixServiceList = new ArrayList<>();

        dbRef = FirebaseDatabase.getInstance().getReference().child(Constants.APP_DATA).child(Constants.SPEED_FIX);
        dbRef.keepSynced(true);

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot child : dataSnapshot.getChildren()
                ) {
                    speedFixServiceList.add(new ServiceTypeModel(child.getKey(), (int) ((long) child.getValue())));
                }

                String serializedList = gson.toJson(speedFixServiceList);
                intent.putExtra(Constants.SPEED_FIX, serializedList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    // fetch Available Appointments
    private void checkTimeOfSpeedFix() {
        // Simple Testing Data for Time
        final String[] time = {"12:00", "1:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00", "8:00", "9:00"};
        final String[] timeOfDay = {"pm", "pm", "pm", "pm", "pm", "pm", "pm", "pm", "pm", "pm"};


        timeRef.child(Constants.SPEED_FIX).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (!dataSnapshot.hasChild(selectedDay)) {
                    // Filling time list in Firebase
                    for (int i = 0; i < time.length; i++) {
                        TimeAppointmentModel obj = new TimeAppointmentModel();
                        obj.setId(i);
                        obj.setBooked("no");
                        obj.setTime(time[i]);
                        obj.setTimeOfDay(timeOfDay[i]);


                        timeRef.child(Constants.SPEED_FIX)
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

    private void getSelectedCar() {
        assert getArguments() != null;
        String serializedSelectedCarObject = getArguments().getString(Constants.SELECTED_CAR);
        Gson gson = new Gson();
        Type type = new TypeToken<CarModel>() {
        }.getType();
        selectedCarObject = gson.fromJson(serializedSelectedCarObject, type);

    }

    // Update Time Recycler Adapter
    private void updateView(ArrayList<TimeAppointmentModel> timeList) {
        ArrayList<TimeAppointmentModel> availableTimeList = new ArrayList<>();
        for (TimeAppointmentModel obj : timeList
        ) {
            String appointmentTime = obj.getTime();

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
        timeAdapter.setTimeList(availableTimeList);
        timeRecyclerView.setAdapter(timeAdapter);

    }

    private void fakeTimeTest(View view) {
        // Simple Testing Data for Time
//         byte[] id = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
//         String[] time = {"11:00", "12:00", "1:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00", "8:00", "9:00"};
//         String[] timeOfDay = {"am", "pm", "pm", "pm", "pm", "pm", "pm", "pm", "pm", "pm", "pm"};
//         String[] booked = {"yes", "yes", "yes", "yes", "yes", "no", "no", "no", "no", "no", "no"};
//        // Filling time list
//        for (int i = 0 ; i<time.length;i++){
//
//            if (booked[i].equals("no")){
//                timeList.add(new TimeAppointmentModel(id[i],time[i], timeOfDay[i],booked[i]));
//            }
//
//        }

//        // Testing time list
//        RecyclerView timeRecyclerView = view.findViewById(R.id.speedFix_timeRecyclerView);
//        final TimeAppointmentRecyclerAdapter timeAdapter = new TimeAppointmentRecyclerAdapter(getActivity(), timeList);
//        timeRecyclerView.setAdapter(timeAdapter);


    }


}
