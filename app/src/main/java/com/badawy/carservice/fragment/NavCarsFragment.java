package com.badawy.carservice.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.badawy.carservice.R;
import com.badawy.carservice.activity.AddCarActivity;
import com.badawy.carservice.activity.HomepageActivity;
import com.badawy.carservice.adapters.NavCarsAdapter;
import com.badawy.carservice.models.CarModel;
import com.badawy.carservice.models.UserProfileModel;
import com.badawy.carservice.utils.Constants;
import com.badawy.carservice.utils.MySharedPreferences;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavCarsFragment extends Fragment implements NavCarsAdapter.OnDeleteIconClick, View.OnClickListener {
    private final int requestCode = 11;
    private TextView userNameTv, cancelTv;
    private ArrayList<CarModel> carList;
    private UserProfileModel userDataObject;
    private NavCarsAdapter navCarsAdapter;
    private ConstraintLayout editLayout;
    private ImageView editIcon, navMenuBtn;
    private Button addCarBtn;
    private RecyclerView carsRecyclerView;
    private AlertDialog.Builder carDialogBuilder;
    private AlertDialog carAlertDialog;
    private Activity activity;

    public NavCarsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nav_cars, container, false);

        activity = getActivity();
        initializeUi(view);

        carsRecyclerView.setVisibility(View.GONE);
        showProgress();
        getUserData();
        getUserCarsFromSharedPreferences();


        String welcomeText = ("Hello, " + userDataObject.getUserName().trim());
        userNameTv.setText(welcomeText);


        editIcon.setOnClickListener(this);
        addCarBtn.setOnClickListener(this);
        cancelTv.setOnClickListener(this);
        navMenuBtn.setOnClickListener(this);
        return view;
    }

    private void initializeUi(View view) {
        navMenuBtn = view.findViewById(R.id.navCars_navMenuBtn);
        cancelTv = view.findViewById(R.id.nav_cars_cancelTv);
        editLayout = view.findViewById(R.id.nav_cars_editLayout);
        editIcon = view.findViewById(R.id.navCars_edit);
        addCarBtn = view.findViewById(R.id.nav_cars_addCarBtn);
        carsRecyclerView = view.findViewById(R.id.nav_cars_carsRecyclerView);
        userNameTv = view.findViewById(R.id.navCars_username);

    }

    // get User Cars
    private void getUserCarsFromSharedPreferences() {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<CarModel>>() {
        }.getType();
        String serializedUserCarList = MySharedPreferences.read(MySharedPreferences.USER_CARS, "");
        if (!serializedUserCarList.equals("")) {

            carList = gson.fromJson(serializedUserCarList, type);
        }

        bindUserCars(carList);
    }

    private void getUserData() {
        // Get User`s Data From Shared Preferences
        Gson gson = new Gson();
        String userSerializedData = MySharedPreferences.read(MySharedPreferences.USER_DATA, "");

        // If Its Not null Then Add all Fields in the Correct Order to a List
        if (!userSerializedData.equals("")) {
            userDataObject = gson.fromJson(userSerializedData, UserProfileModel.class);
        }
    }

    private void bindUserCars(ArrayList<CarModel> carList) {

        navCarsAdapter = new NavCarsAdapter(getActivity(), carList, this);
        carsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        carsRecyclerView.setAdapter(navCarsAdapter);
        hideProgress();
        carsRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDeleteIconClick(int position) {
        String carIDToDelete = carList.get(position).getCarID();
        prepareDialog(carIDToDelete);
    }


    private void prepareDialog(final String carId) {
        // Dismiss any old dialog.
        if (carAlertDialog != null) {
            carAlertDialog.dismiss();
        }

        carDialogBuilder = new AlertDialog.Builder(getActivity());
        carDialogBuilder.setMessage("Are you sure you want to delete this car ? ");
        carDialogBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showProgress();
                removeCarFromFirebase(carId);
            }
        });
        carDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                carAlertDialog.dismiss();
                isEditLayoutVisible(false);
            }
        });
        carAlertDialog = carDialogBuilder.create();
        carAlertDialog.show();


    }

    private void removeCarFromFirebase(final String carId) {

        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child(Constants.USERS).child(userDataObject.getUserId()).child(Constants.USER_CARS);
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 1) {
                    hideProgress();
                    Toast.makeText(activity, "You must have at least 1 car! ", Toast.LENGTH_SHORT).show();
                    isEditLayoutVisible(false);
                } else {
                    dbRef.child(carId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            hideProgress();
                            Toast.makeText(activity, "successfully deleted! ", Toast.LENGTH_SHORT).show();
                            getUserCarsFromSharedPreferences();
                            isEditLayoutVisible(false);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void isEditLayoutVisible(boolean status) {

        if (!status) {
            navCarsAdapter.setEditMode(false);
            navCarsAdapter.notifyDataSetChanged();
            editLayout.setVisibility(View.GONE);
            editLayout.animate().alpha(0.0f);
        } else {
            navCarsAdapter.setEditMode(true);
            navCarsAdapter.notifyDataSetChanged();
            editLayout.setVisibility(View.VISIBLE);
            editLayout.animate().alpha(1.0f);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.nav_cars_cancelTv:
                isEditLayoutVisible(false);
                break;
            case R.id.navCars_edit:
                isEditLayoutVisible(true);
                break;
            case R.id.nav_cars_addCarBtn:
                startActivityForResult(new Intent(getActivity(), AddCarActivity.class), requestCode);
                break;
            case R.id.navCars_navMenuBtn:

                HomepageActivity.openDrawer();
                break;
            default:
                break;

        }
    }

    private void showProgress() {
        if (activity instanceof HomepageActivity) {
            ((HomepageActivity) activity).showProgressBar(true);
        }
    }

    private void hideProgress() {
        if (activity instanceof HomepageActivity) {
            ((HomepageActivity) activity).showProgressBar(false);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11) {
            getUserCarsFromSharedPreferences();
            isEditLayoutVisible(false);


        }
    }
}
