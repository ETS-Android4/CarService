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
public class DeliveryCarFragment extends Fragment implements ServiceListRecyclerAdapter.OnButtonClickListener {


    // Service Data
    private int[] serviceIcons = {R.drawable.ic_delivery_quick_check, R.drawable.ic_delivery_maintenance_repair, R.drawable.ic_delivery_spare_parts};
    private String[] serviceLabels = {"Quick Check", "Maintenance & Repair", "Spare Parts"};
    private String[] serviceDescription = {"Order a truck to do a quick check on your car", "this option provides to you oil change,check battery etc.", "show all the available spare parts for your car"};
    private Button[] serviceListButtons = new Button[3];
    private String[] serviceButtonLabel = {"Order now","See service","See service"};
    private Fragment[] serviceFragments = {new DeliveryCar_CheckFragment(),new DeliveryCar_MaintenanceFragment(),new DeliveryCar_SparePartsFragment()};


    public DeliveryCarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_delivery_car, container, false);


        // Hold the data
        ArrayList<ServiceListModel> serviceList = new ArrayList<>();


        //Initialize Views
        RecyclerView serviceRecyclerView = view.findViewById(R.id.delivery_serviceList);
        ImageView navMenuBtn = view.findViewById(R.id.delivery_navMenuBtn);




        // Fill the service List with the Data
        for (int i = 0; i < serviceLabels.length; i++) {

            serviceList.add(new ServiceListModel(serviceIcons[i], serviceLabels[i], serviceDescription[i], serviceListButtons[i],serviceButtonLabel[i]));

        }

        // Send the list to the adapter
        ServiceListRecyclerAdapter adapter = new ServiceListRecyclerAdapter(getActivity(), serviceList,this);


        // Set the adapter and Layout Manager into the Recycler View
        serviceRecyclerView.setAdapter(adapter);
        serviceRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));


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

       replaceFragment(serviceFragments[position]);

    }


    private void replaceFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.homepage_fragment_container, fragment)
                .addToBackStack("DeliveryFragment")
                .commit();


    }


} // END OF CLASS
