package com.badawy.carservice.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.badawy.carservice.R;
import com.badawy.carservice.activity.HomepageActivity;
import com.badawy.carservice.adapters.SpeedFixCategoryAdapter;
import com.badawy.carservice.adapters.SelectCarRecyclerAdapter;
import com.badawy.carservice.models.CarModel;
import com.badawy.carservice.models.SpeedFixCategoryModel;
import com.badawy.carservice.utils.Constants;
import com.badawy.carservice.utils.MySharedPreferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeliveryCarFragment extends Fragment implements View.OnClickListener, SpeedFixCategoryAdapter.OnItemClickListener {


    // Global Variables
    private boolean isListVisible = true;
    private SelectCarRecyclerAdapter selectCarRecyclerAdapter;
//    private LinearLayout categoryLayout;
//    private ConstraintLayout categoryParent;
    private ImageView arrow, navMenuBtn;
    private CardView sparePartsCV, speedFixCard;
    private RecyclerView carRecyclerView, categoryRecyclerView;

    private ArrayList<CarModel> carList;
    private DatabaseReference dbRef;
    private ArrayList<SpeedFixCategoryModel> speedFixCategoryList;



    public DeliveryCarFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_delivery_car, container, false);


        // Initialize Ui
        initializeUi(view);

        // Get User Cars
        getUserCarsFromSharedPreferences();

        // Fetch Speed Fix Data From Firebase
//        fetchSpeedFixDataFromFirebase();

        // Set ClickListeners for views .. Function implemented in @onClick
        navMenuBtn.setOnClickListener(this);
        speedFixCard.setOnClickListener(this);
        sparePartsCV.setOnClickListener(this);


        return view;
    } // END OF ON CREATE

    private void fetchSpeedFixDataFromFirebase() {
        speedFixCategoryList = new ArrayList<>();

        dbRef = FirebaseDatabase.getInstance().getReference().child(Constants.APP_DATA).child(Constants.SPEED_FIX);
        dbRef.keepSynced(true);

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot child: dataSnapshot.getChildren()
                     ) {
                    speedFixCategoryList.add(new SpeedFixCategoryModel(R.drawable.ic_speedfix_air_conditioning,child.getKey(), (int) ((long) child.getValue())));
                }

                bindSpeedFixData(speedFixCategoryList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void bindSpeedFixData(ArrayList<SpeedFixCategoryModel> speedFixCategoryList) {
//        SpeedFixCategoryAdapter categoryAdapter = new SpeedFixCategoryAdapter(getActivity(), speedFixCategoryList);
//        RecyclerView.LayoutManager categoryManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
//        categoryRecyclerView.setLayoutManager(categoryManager);
//        categoryRecyclerView.setAdapter(categoryAdapter);

    }


    private void initializeUi(View view) {
        sparePartsCV = view.findViewById(R.id.delivery_spareParts_CV);
        navMenuBtn = view.findViewById(R.id.delivery_navMenuBtn);
        speedFixCard = view.findViewById(R.id.delivery_speedFix_CV);
//        categoryLayout = view.findViewById(R.id.delivery_speedFix_categoryLinearLayout);
//        categoryParent = view.findViewById(R.id.delivery_speedFix_category_parent);
//        arrow = view.findViewById(R.id.delivery_speedFix_listArrow);
        carRecyclerView = view.findViewById(R.id.delivery_chooseCarRecyclerView);
//        categoryRecyclerView = view.findViewById(R.id.delivery_speedFix_category_recyclerView);


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

    private void bindUserCars(ArrayList<CarModel> carList) {

        selectCarRecyclerAdapter = new SelectCarRecyclerAdapter(getActivity(), carList);
        carRecyclerView.setAdapter(selectCarRecyclerAdapter);
    }


    private void fakeDataTest() {

        // Old name of speed fix  was maintenance


        // speedFix Category Data
//        int[] categoryImages = {R.drawable.ic_speedfix_fix, R.drawable.ic_speedfix_air_conditioning, R.drawable.ic_speedfix_oil, R.drawable.ic_speedfix_battery};
//        String[] categoryName = {"Fix & Repair", "Air Conditioning", "Oil Change", "Check Battery"};


        // Simple Testing Data for Car Selection
//        byte[] carId = {0, 1, 2, 3, 4};
//        int[] carImage = {R.drawable.ic_nav_cars_car_test, R.drawable.ic_nav_cars_car_test, R.drawable.ic_nav_cars_car_test, R.drawable.ic_nav_cars_car_test, R.drawable.ic_nav_cars_car_test};
//        String[] carName = {"2018 Audi TT", "2014 Mazda Mk4", "2018 Audi TT", "2014 Mazda Mk4", "2018 Audi TT"};

//        // initialize Lists that will Hold the data
//        ArrayList<SelectCarModel> carList = new ArrayList<>();
//        ArrayList<SpeedFixCategoryModel> speedFixCategoryList = new ArrayList<>();
//
//        // Filling category list
//        for (int i = 0; i < categoryImages.length; i++) {
//            speedFixCategoryList.add(new SpeedFixCategoryModel(categoryImages[i], categoryName[i]));
//        }
//
//        // Filling car list
//        for (int i = 0; i < carId.length; i++) {
//            carList.add(new SelectCarModel(carId[i], carImage[i], carName[i]));
//
//        }
//
//
//        // Bind Car Data to CarRecyclerList
//        final SelectCarRecyclerAdapter carAdapter = new SelectCarRecyclerAdapter(getActivity(), carList);
//        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
//        carRecyclerView.setLayoutManager(manager);
//        carRecyclerView.setAdapter(carAdapter);
//
//
//        // Bind Category Data to CategoryRecyclerList
//        speedFixCategoryAdapter categoryAdapter = new speedFixCategoryAdapter(getActivity(), speedFixCategoryList);
//        RecyclerView.LayoutManager categoryManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
//        categoryRecyclerView.setLayoutManager(categoryManager);
//        categoryRecyclerView.setAdapter(categoryAdapter);


    }


    private void showCategory() {

        // Category Animation
//        int CATEGORY_ANIMATION_DURATION = 350;
//        Transition transition = new Slide(Gravity.TOP);
//        transition.setDuration(CATEGORY_ANIMATION_DURATION);
//        transition.addTarget(R.id.delivery_speedFix_categoryLinearLayout);
//        TransitionManager.beginDelayedTransition(categoryParent, transition);
//        categoryLayout.setVisibility(isListVisible ? View.VISIBLE : View.GONE);
//
//        // Arrow Animation
//        if (isListVisible) {
//            arrow.animate().rotation(270);
//        } else {
//            arrow.animate().rotation(90);
//        }
//        // Switch the Click from show to hide and vice versa
//        isListVisible = !isListVisible;
    }


    private void replaceFragment(Fragment fragment) {
        Gson gson  = new Gson();
        String serializedSelectedCarObject = gson.toJson(selectCarRecyclerAdapter.getSelectedCarObject());
        Bundle bundle = new Bundle();
        bundle.putString(Constants.SELECTED_CAR ,serializedSelectedCarObject);
        fragment.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.homepage_fragment_container, fragment)
                .addToBackStack("DeliveryCarFragment")
                .commit();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.delivery_navMenuBtn:
                // Open side menu
                HomepageActivity.openDrawer();
                break;
            case R.id.delivery_speedFix_CV:
                // Show or hide speedFix category
                //showCategory();
                replaceFragment(new DeliveryCarSpeedFixFragment());
                break;
            case R.id.delivery_spareParts_CV:
                replaceFragment(new DeliveryCarSparePartsShopFragment());
                break;
            default:
                break;
        }
    }


    @Override
    public void onItemClick(int position) {

    }
} // END OF CLASS
