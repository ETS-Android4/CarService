package com.badawy.carservice.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.badawy.carservice.R;
import com.badawy.carservice.adapters.ServiceListAdapter;
import com.badawy.carservice.models.ServiceTypeModel;
import com.badawy.carservice.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CarCenterSelectServicePopUpActivity extends AppCompatActivity {
    private ArrayList<ServiceTypeModel> carCareList, cycleTypeList, specificFixesList;
    private Intent incomingIntent;
    private RecyclerView cycleTypeRV,specificFixesTypeRV,carCareRV;
    private Gson gson ;
    private ServiceListAdapter cycleTypeAdapter,specificFixesAdapter,carCareAdapter;
    private ArrayList<ServiceTypeModel> selectedList;
    private Button finish;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        incomingIntent = getIntent();
        final String serviceName = incomingIntent.getStringExtra(Constants.SERVICE_NAME_BUNDLE_KEY);


        if (serviceName.equals(Constants.VEHICLE_INSPECTION)) {
            setContentView(R.layout.activity_car_center_pop_up_vehicle_inspection);
            prepareActivityAsPopUpWindow();

            // Initialize Ui
            finish = findViewById(R.id.carCenterPopUp_vehicleInspetion_finishBtn);
            cycleTypeRV = findViewById(R.id.carCenterPopUp_cycleRV);
            specificFixesTypeRV = findViewById(R.id.carCenterPopUp_specificFixesRV);

            // Load Data From Intent
            gson = new Gson();
            Type type = new TypeToken<ArrayList<ServiceTypeModel>>() {}.getType();
            cycleTypeList = gson.fromJson(incomingIntent.getStringExtra(Constants.VEHICLE_INSPECTION_CYCLE),type);
            specificFixesList = gson.fromJson(incomingIntent.getStringExtra(Constants.VEHICLE_INSPECTION_SPECIFIC_FIXES),type);

            // Prepare Recycler Views and Adapters
            cycleTypeAdapter = new ServiceListAdapter(cycleTypeList, this);
            cycleTypeRV.setLayoutManager(new LinearLayoutManager(this));
            cycleTypeRV.setAdapter(cycleTypeAdapter);

            specificFixesAdapter = new ServiceListAdapter(specificFixesList,this);
            specificFixesTypeRV.setLayoutManager(new LinearLayoutManager(this));
            specificFixesTypeRV.setAdapter(specificFixesAdapter);



        }
        else if (serviceName.equals(Constants.CAR_CARE)){

            setContentView(R.layout.activity_car_center_pop_up_car_care);
            prepareActivityAsPopUpWindow();

            // Initialize Ui
            carCareRV = findViewById(R.id.carCenterPopUp_carCare_rV);
            finish = findViewById(R.id.carCenterPopUp_carCare_finishBtn);

            // Load Data From Intent
            gson = new Gson();
            Type type = new TypeToken<ArrayList<ServiceTypeModel>>() {}.getType();
            carCareList = gson.fromJson(incomingIntent.getStringExtra(Constants.CAR_CARE),type);

            // Prepare Recycler View and Adapter
            carCareAdapter = new ServiceListAdapter(carCareList,this);
            carCareRV.setLayoutManager(new LinearLayoutManager(this));
            carCareRV.setAdapter(carCareAdapter);




        }

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent that will carry the results back
                Intent backWithResults = new Intent();

                // Initialize
                selectedList = new ArrayList<>();
                int price = 0;

                // Results of Vehicle Inspection
                if(serviceName.equals(Constants.VEHICLE_INSPECTION)){
                    if (cycleTypeAdapter.getSelectedServiceList()!=null){
                        selectedList.addAll(cycleTypeAdapter.getSelectedServiceList());
                    }

                    if (specificFixesAdapter.getSelectedServiceList()!=null){
                        selectedList.addAll(specificFixesAdapter.getSelectedServiceList());
                    }

                }
                // Results of Car Care
                else if(serviceName.equals(Constants.CAR_CARE)){

                    if (carCareAdapter.getSelectedServiceList()!=null){
                        selectedList.addAll(carCareAdapter.getSelectedServiceList());
                    }

                }

                // Calculate Total Price of Services
                for (int i =0 ; i<selectedList.size();i++){
                    price+=selectedList.get(i).getPrice();
                }

                // Prepare Data to be Sent Back
                String serializedList = gson.toJson(selectedList);

                // Put the Data In The Intent
                backWithResults.putExtra(Constants.SERVICE_TYPES_NAME_RESULT, serializedList);
                backWithResults.putExtra(Constants.SERVICE_TYPES_PRICE_RESULT,price);

                // End the Pop Up Activity
                setResult(RESULT_OK, backWithResults);
                finish();
            }
        });

    }


    private void prepareActivityAsPopUpWindow() {

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);


        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * 0.8));
        WindowManager.LayoutParams params = getWindow().getAttributes();

        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = 20;

        getWindow().setAttributes(params);

    }
}
