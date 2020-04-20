package com.badawy.carservice.fragment;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.badawy.carservice.R;
import com.badawy.carservice.activity.HomepageActivity;
import com.badawy.carservice.adapters.CarCenterHelpGuideViewPager2Adapter;
import com.badawy.carservice.models.CarCenterHelpGuideModel;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarCenterChooseFragment extends Fragment {

    private CardView carCareCV, carInspectionCV;
    private ImageView helpGuideFinishArrow,navMenuBtn,helpIcon;
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

        initUi(view);


        prepareHelpGuide();



        // open Navigation Drawer
        navMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomepageActivity.openDrawer();
            }
        });


        // Choose Car Care option
        carCareCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                replaceFragment(new CarCenterFragment());

            }
        });


        // Choose Car Inspection option
        carInspectionCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                replaceFragment(new CarCenterFragment());

            }
        });

        helpIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareHelpGuide();
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
        String[] helpDescription = {"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor"};


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

    private void replaceFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.homepage_fragment_container, fragment)
                .addToBackStack("CarCenterChooseFragment")
                .commit();


    }

    private void initUi(View view){
        carCareCV = view.findViewById(R.id.carCenterChoose_carCareCV);
        carInspectionCV = view.findViewById(R.id.carCenterChoose_carInspectionCV);
        carCenterContainer = view.findViewById(R.id.carCenterChoose_container);
        helpGuideContainer = view.findViewById(R.id.carCenter_HelpGuide_container);
        navMenuBtn = view.findViewById(R.id.carCenterChoose_navMenuBtn);
        helpGuideViewPager = view.findViewById(R.id.carCenter_HelpGuide_viewPager);
        helpGuideFinishArrow = view.findViewById(R.id.item_carCenterHelpGuide_finishButton);
        dotsIndicator = view.findViewById(R.id.carCenter_HelpGuide_dotsIndicator);
        helpIcon =view.findViewById(R.id.carCenterChoose_helpIcon);


    }

}
