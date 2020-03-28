package com.badawy.carservice.fragment;


import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.badawy.carservice.R;
import com.badawy.carservice.activity.HomepageActivity;
import com.badawy.carservice.adapters.MaintenanceCategoryAdapter;
import com.badawy.carservice.adapters.SelectCarRecyclerAdapter;
import com.badawy.carservice.models.MaintenanceCategoryModel;
import com.badawy.carservice.models.SelectCarModel;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeliveryCarFragment extends Fragment implements View.OnClickListener,MaintenanceCategoryAdapter.OnItemClickListener  {


    // Maintenance Category Data
    private int[] categoryImages = {R.drawable.ic_maintenance_fix, R.drawable.ic_maintenance_air_conditioning, R.drawable.ic_maintenance_oil, R.drawable.ic_maintenance_battery};
    private String[] categoryName = {"Fix & Repair", "Air Conditioning", "Oil Change", "Check Battery"};


    // Simple Testing Data for Car Selection
    private byte[] carId = {0, 1, 2, 3, 4};
    private int[] carImage = {R.drawable.ic_garage_car_test, R.drawable.ic_garage_car_test, R.drawable.ic_garage_car_test, R.drawable.ic_garage_car_test, R.drawable.ic_garage_car_test};
    private String[] carName = {"2018 Audi TT", "2014 Mazda Mk4", "2018 Audi TT", "2014 Mazda Mk4", "2018 Audi TT"};


    // Global Variables
    private boolean isListVisible = true;
    private LinearLayout categoryLayout;
    private ConstraintLayout categoryParent;
    private ImageView arrow, navMenuBtn;
    private CardView sparePartsCV, maintenanceCard;
    private RecyclerView carRecyclerView, categoryRecyclerView;


    public DeliveryCarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_delivery_car, container, false);


        // Init Ui
        initUi(view);

        // Init and Fill Data
        createData();

        // Set ClickListeners for views .. Function implemented in @onClick
        navMenuBtn.setOnClickListener(this);
        maintenanceCard.setOnClickListener(this);
        sparePartsCV.setOnClickListener(this);


        return view;
    } // END OF ON CREATE


    private void initUi(View view) {
        sparePartsCV = view.findViewById(R.id.delivery_spareParts_CV);
        navMenuBtn = view.findViewById(R.id.delivery_navMenuBtn);
        maintenanceCard = view.findViewById(R.id.delivery_maintenance_CV);
        categoryLayout = view.findViewById(R.id.maintenance_categoryLinearLayout);
        categoryParent = view.findViewById(R.id.maintenance_category_parent);
        arrow = view.findViewById(R.id.delivery_maintenance_listArrow);
        carRecyclerView = view.findViewById(R.id.delivery_chooseCarRecyclerView);
        categoryRecyclerView = view.findViewById(R.id.maintenance_category_recyclerView);


    }


    private void createData() {

        // initialize Lists that will Hold the data
        ArrayList<SelectCarModel> carList = new ArrayList<>();
        ArrayList<MaintenanceCategoryModel> maintenanceCategoryList = new ArrayList<>();

        // Filling category list
        for (int i = 0; i < categoryImages.length; i++) {
            maintenanceCategoryList.add(new MaintenanceCategoryModel(categoryImages[i], categoryName[i]));
        }

        // Filling car list
        for (int i = 0; i < carId.length; i++) {
            carList.add(new SelectCarModel(carId[i], carImage[i], carName[i]));

        }


        // Bind Car Data to CarRecyclerList
        final SelectCarRecyclerAdapter carAdapter = new SelectCarRecyclerAdapter(getActivity(), carList);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        carRecyclerView.setLayoutManager(manager);
        carRecyclerView.setAdapter(carAdapter);


        // Bind Category Data to CategoryRecyclerList
        MaintenanceCategoryAdapter categoryAdapter = new MaintenanceCategoryAdapter(getActivity(), maintenanceCategoryList);
        RecyclerView.LayoutManager categoryManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        categoryRecyclerView.setLayoutManager(categoryManager);
        categoryRecyclerView.setAdapter(categoryAdapter);


    }


    private void showCategory() {

        // Category Animation
        int CATEGORY_ANIMATION_DURATION = 350;
        Transition transition = new Slide(Gravity.TOP);
        transition.setDuration(CATEGORY_ANIMATION_DURATION);
        transition.addTarget(R.id.maintenance_categoryLinearLayout);
        TransitionManager.beginDelayedTransition(categoryParent, transition);
        categoryLayout.setVisibility(isListVisible ? View.VISIBLE : View.GONE);

        // Arrow Animation
        if (isListVisible) {
            arrow.animate().rotation(270);
        } else {
            arrow.animate().rotation(90);
        }
        // Switch the Click from show to hide and vice versa
        isListVisible = !isListVisible;
    }


    private void replaceFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.homepage_fragment_container, fragment)
                .addToBackStack("NavHomeFragment")
                .commit();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.delivery_navMenuBtn:
                // Open side menu
                HomepageActivity.openDrawer();
                break;
            case R.id.delivery_maintenance_CV:
                // Show or hide maintenance category
                showCategory();
                break;
            case R.id.delivery_spareParts_CV:
                replaceFragment(new SparePartsShopFragment());
                break;
            default:
                break;
        }
    }


    @Override
    public void onItemClick(int position) {

    }
} // END OF CLASS
