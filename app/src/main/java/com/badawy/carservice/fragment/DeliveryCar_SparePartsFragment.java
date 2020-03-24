package com.badawy.carservice.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
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
public class DeliveryCar_SparePartsFragment extends Fragment implements ServiceListRecyclerAdapter.OnButtonClickListener{


    // Service Data
    private int[] partsCategoryIcons = {R.drawable.ic_delivery_spare_tires, R.drawable.ic_delivery_spare_mirrors, R.drawable.ic_delivery_spare_engine,R.drawable.ic_delivery_spare_others};
    private String[] partsCategoryLabels = {"Tires", "Mirrors", "Engine","Others"};
    private String[] partsCategoryDescription = {"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut ",
            " Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut ",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut ",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut "};
    private Button[] partsCategoryListButtons = new Button[4];
    private String[] partsCategoryButtonLabel = {"See details","See details","See details","See details"};

    public DeliveryCar_SparePartsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.fragment_delivery_car_spare_parts, container, false);


         //Hold the data
        ArrayList<ServiceListModel> partsCategoryList = new ArrayList<>();


        //Initialize Views
        RecyclerView partsCategoryRecyclerView = view.findViewById(R.id.spareParts_partsCategoryList);
        ImageView navMenuBtn = view.findViewById(R.id.spareParts_navMenuBtn);




        // Fill the service List with the Data
            for (int i = 0; i < partsCategoryLabels.length; i++) {

                partsCategoryList.add(new ServiceListModel(partsCategoryIcons[i], partsCategoryLabels[i], partsCategoryDescription[i], partsCategoryListButtons[i],partsCategoryButtonLabel[i]));

            }



        // Send the list to the adapter
        ServiceListRecyclerAdapter adapter = new ServiceListRecyclerAdapter(getActivity(), partsCategoryList,this);


        // Set the adapter and Layout Manager into the Recycler View
        partsCategoryRecyclerView.setAdapter(adapter);
        partsCategoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));


        //Open side menu
        navMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomepageActivity.openDrawer();
            }
        });

        return  view;

    }  // END OF ON CREATE


    // To Define what each button should do when clicked
    @Override
    public void onButtonClick(int position) {

    }


} // End of Class
