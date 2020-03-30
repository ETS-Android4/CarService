package com.badawy.carservice.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.badawy.carservice.R;
import com.badawy.carservice.activity.HomepageActivity;
import com.badawy.carservice.adapters.TimeAppointmentRecyclerAdapter;
import com.badawy.carservice.models.TimeAppointmentModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeliveryCarMaintenanceFragment extends Fragment {
    // Simple Testing Data for Time
    private byte  []id = {0,1,2,3,4,5,6,7,8,9,10,11};
    private String [] time = {"11:00","12:00","1:00","2:00","3:00","4:00","5:00","6:00","7:00","8:00","9:00"};
    private String [] timeOfDay = {"am","pm","pm","pm","pm","pm","pm","pm","pm","pm","pm"};
    private String []booked ={ "yes", "yes", "yes", "yes", "yes", "no", "no", "no", "no", "no", "no"};

    public DeliveryCarMaintenanceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_maintenance, container, false);
        ArrayList<TimeAppointmentModel> timeList = new ArrayList<>();

        ImageView navMenuBtn = view.findViewById(R.id.maintenance_navMenuBtn);
        navMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomepageActivity.openDrawer();
            }
        });

        // Filling time list
        for (int i = 0 ; i<time.length;i++){

            if (booked[i].equals("no")){
                timeList.add(new TimeAppointmentModel(id[i],time[i], timeOfDay[i],booked[i]));
            }

        }

        // Testing time list
        RecyclerView timeRecyclerView = view.findViewById(R.id.maintenance_timeRecyclerView);
        final TimeAppointmentRecyclerAdapter timeAdapter = new TimeAppointmentRecyclerAdapter(getActivity(), timeList);
        timeRecyclerView.setAdapter(timeAdapter);

        return view;
    }
}
