package com.badawy.carservice.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.badawy.carservice.R;
import com.badawy.carservice.activity.HomepageActivity;
import com.badawy.carservice.adapters.ServiceListRecyclerAdapter;
import com.badawy.carservice.models.ServiceListModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeliveryCar_MaintenanceFragment extends Fragment implements ServiceListRecyclerAdapter.OnButtonClickListener {


    // Service Data
    private int[] maintenanceIcons = {R.drawable.ic_maintenance_fix_repair, R.drawable.ic_maintenance_air_conditioning, R.drawable.ic_maintenance_oil_change,R.drawable.ic_maintenance_check_battery};
    private String[] maintenanceLabels = {"Fix & Repair", "Air conditioning", "Oil change","Check Battery"};
    private String[] maintenanceDescription = {"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut ",
                                           "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut ",
                                           "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut ",
                                           "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut "};
    private Button[] maintenanceListButtons = new Button[4];
    private String[] maintenanceButtonLabel = {"Book Now","Order Now","Order Now","Order Now"};

    public DeliveryCar_MaintenanceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view =inflater.inflate(R.layout.fragment_delivery_car_maintenance, container, false);


        // Hold the data
        ArrayList<ServiceListModel> maintenanceCategoryList = new ArrayList<>();


        //Initialize Views
        RecyclerView maintenanceCategoryRecyclerView = view.findViewById(R.id.maintenance_serviceCategoryList);
        ImageView navMenuBtn = view.findViewById(R.id.maintenance_navMenuBtn);




        // Fill the maintenanceCategory List with the Data
        for (int i = 0; i < maintenanceLabels.length; i++) {

            maintenanceCategoryList.add(new ServiceListModel(maintenanceIcons[i], maintenanceLabels[i], maintenanceDescription[i], maintenanceListButtons[i],maintenanceButtonLabel[i]));

        }

        // Send the list to the adapter
        ServiceListRecyclerAdapter adapter = new ServiceListRecyclerAdapter(getActivity(), maintenanceCategoryList,this);


        // Set the adapter and Layout Manager into the Recycler View
        maintenanceCategoryRecyclerView.setAdapter(adapter);
        maintenanceCategoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));


        // Open side menu
        navMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomepageActivity.openDrawer();
            }
        });

        return view;
    } // END OF ON CREATE



    // To Define what each button should do when clicked
    @Override
    public void onButtonClick(int position) {


    }


    private void replaceFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.homepage_fragment_container, fragment)
                .addToBackStack("DeliveryFragment")
                .commit();


    }


} // END OF CLASS
