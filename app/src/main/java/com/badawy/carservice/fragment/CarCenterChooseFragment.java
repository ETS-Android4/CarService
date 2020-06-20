package com.badawy.carservice.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.badawy.carservice.R;
import com.badawy.carservice.activity.HomepageActivity;
import com.badawy.carservice.adapters.CarCenterHelpGuideViewPager2Adapter;
import com.badawy.carservice.models.CarCenterHelpGuideModel;
import com.badawy.carservice.utils.Constants;
import com.badawy.carservice.utils.MySharedPreferences;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarCenterChooseFragment extends Fragment {

    private CardView carCareCV, carInspectionCV;
    private ImageView helpGuideFinishArrow, navMenuBtn, helpIcon;
    private ConstraintLayout helpGuideContainer, carCenterContainer;
    private ViewPager2 helpGuideViewPager;
    private CarCenterHelpGuideViewPager2Adapter helpAdapter;
    private SpringDotsIndicator dotsIndicator;

    public CarCenterChooseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_car_center_choose, container, false);

        initializeUi(view);

        boolean firstTimeHelpGuide = MySharedPreferences.read(MySharedPreferences.FIRST_TIME_HELP_GUIDE,true);

         if (firstTimeHelpGuide){
         prepareHelpGuide();
         MySharedPreferences.write(MySharedPreferences.FIRST_TIME_HELP_GUIDE,false);
         }

        // Choose Car Care option
        carCareCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                replaceFragment(new CarCenterFragment(), Constants.CAR_CARE);

            }
        });


        // Choose Car Inspection option
        carInspectionCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                replaceFragment(new CarCenterFragment(), Constants.VEHICLE_INSPECTION);

            }
        });

        // open Help Guide
        helpIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareHelpGuide();
            }
        });

        // open Navigation Drawer
        navMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomepageActivity.openDrawer();
            }
        });


        return view;
    }

    private void prepareHelpGuide() {

        carCenterContainer.setVisibility(View.GONE);
        helpGuideContainer.setVisibility(View.VISIBLE);


        ArrayList<CarCenterHelpGuideModel> dataList = new ArrayList<>();
        int[] helpImages = {R.drawable.ic_car_center_choose_bg, R.drawable.ic_car_center_choose_car_care, R.drawable.ic_car_choose_car_inspection};
        String[] helpTitles = {"Car Center", "Car Care", "Vehicle Inspection"};
        String[] helpDescription = {"Let us tell you about the service we perform in our Physical Location"
                , "Cleaning, polishing, and a lot more other services waiting for you to choose from to take the best care of your car "
                , "we fix your car from any mechanical problem, just tell us what the problem is, pick an appointment and we will fix it."};


        for (int i = 0; i < helpTitles.length; i++) {

            dataList.add(new CarCenterHelpGuideModel(helpImages[i], helpTitles[i], helpDescription[i]));

        }

        helpAdapter = new CarCenterHelpGuideViewPager2Adapter(getActivity(), dataList);
        helpGuideViewPager.setAdapter(helpAdapter);
        dotsIndicator.setViewPager2(helpGuideViewPager);


        helpGuideViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (helpGuideViewPager.getCurrentItem() == helpAdapter.getItemCount() - 1) {
                    helpGuideFinishArrow.setVisibility(View.VISIBLE);
                } else
                    helpGuideFinishArrow.setVisibility(View.INVISIBLE);

            }
        });

        helpGuideFinishArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpGuideContainer.setVisibility(View.GONE);
                carCenterContainer.setVisibility(View.VISIBLE);
            }
        });


    }

    private void replaceFragment(Fragment fragment, String serviceName) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.SERVICE_NAME_BUNDLE_KEY, serviceName);
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.homepage_fragment_container, fragment)
                .addToBackStack("CarCenterChooseFragment")
                .commit();


    }

    private void initializeUi(View view) {
        carCareCV = view.findViewById(R.id.carCenterChoose_carCareCV);
        carInspectionCV = view.findViewById(R.id.carCenterChoose_carInspectionCV);
        carCenterContainer = view.findViewById(R.id.carCenterChoose_container);
        helpGuideContainer = view.findViewById(R.id.carCenter_HelpGuide_container);
        navMenuBtn = view.findViewById(R.id.carCenterChoose_navMenuBtn);
        helpGuideViewPager = view.findViewById(R.id.carCenter_HelpGuide_viewPager);
        helpGuideFinishArrow = view.findViewById(R.id.item_carCenterHelpGuide_finishButton);
        dotsIndicator = view.findViewById(R.id.carCenter_HelpGuide_dotsIndicator);
        helpIcon = view.findViewById(R.id.carCenterChoose_helpIcon);


    }

}
