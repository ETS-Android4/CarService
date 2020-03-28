package com.badawy.carservice.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.badawy.carservice.R;
import com.badawy.carservice.adapters.SelectCarRecyclerAdapter;
import com.badawy.carservice.adapters.TimeAppointmentRecyclerAdapter;
import com.badawy.carservice.models.SelectCarModel;
import com.badawy.carservice.models.TimeAppointmentModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarCenterFragment extends Fragment {

    // Simple Testing Data for Time
    private byte  []id = {0,1,2,3,4,5,6,7,8,9,10,11};
    private String [] time = {"11:00","12:00","1:00","2:00","3:00","4:00","5:00","6:00","7:00","8:00","9:00"};
    private String [] timeOfDay = {"am","pm","pm","pm","pm","pm","pm","pm","pm","pm","pm"};
    private String []booked ={ "yes", "yes", "yes", "yes", "yes", "no", "no", "no", "no", "no", "no"};

    // Simple Testing Data for Car Selection
    private byte[] carId = {0,1,2,3,4};
    private int [] carImage = {R.drawable.ic_nav_cars_car_test,R.drawable.ic_nav_cars_car_test,R.drawable.ic_nav_cars_car_test,R.drawable.ic_nav_cars_car_test,R.drawable.ic_nav_cars_car_test};
    private String [] carName = {"2018 Audi TT","2014 Mazda Mk4","2018 Audi TT","2014 Mazda Mk4","2018 Audi TT"};



    public CarCenterFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_car_center, container, false);
        ArrayList<TimeAppointmentModel> timeList = new ArrayList<>();
        ArrayList<SelectCarModel> carList = new ArrayList<>();



        // Filling time list
        for (int i = 0 ; i<time.length;i++){

            if (booked[i].equals("no")){
                timeList.add(new TimeAppointmentModel(id[i],time[i], timeOfDay[i],booked[i]));
            }

        }


        // Filling car list
        for (int i = 0 ; i<carId.length;i++){
                carList.add(new SelectCarModel(carId[i],carImage[i],carName[i]));

        }


        // Testing time list
        RecyclerView timeRecyclerView = view.findViewById(R.id.carCenter_timeRecyclerView);
        final TimeAppointmentRecyclerAdapter timeAdapter = new TimeAppointmentRecyclerAdapter(getActivity(), timeList);
        timeRecyclerView.setAdapter(timeAdapter);

        //Testing car list
          RecyclerView carRecyclerView = view.findViewById(R.id.carCenter_chooseCarRecyclerView);
        final SelectCarRecyclerAdapter carAdapter = new SelectCarRecyclerAdapter(getActivity(), carList);
        carRecyclerView.setAdapter(carAdapter);





        Button book = view.findViewById(R.id.carCenter_bookNowButton);
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), timeAdapter.getTimeId()+"time", Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), carAdapter.getCarId()+"car", Toast.LENGTH_SHORT).show();
            }
        });



//         Testing comparing time for future purpose
//        CustomCalendar customCalendar = view.findViewById(R.id.carCenter_customCalender);
//        String time = "6:58";
//        if (customCalendar.getTime().compareTo(time)>0||customCalendar.getTime().equals(time))
//            Toast.makeText(getActivity(), "yes "+customCalendar.getTime()+" is after "+time, Toast.LENGTH_SHORT).show();
//        else Toast.makeText(getActivity(), "no "+customCalendar.getTime()+" less than "+time, Toast.LENGTH_SHORT).show();
        return view;
    }


}
