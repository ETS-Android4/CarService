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

public class SpeedFixPopUpActivity extends AppCompatActivity {
    private Gson gson ;
    private ArrayList<ServiceTypeModel> speedFixList;
    private ServiceListAdapter speedFixAdapter;
    private RecyclerView speedFixRV;
    private ArrayList<ServiceTypeModel> selectedList;
    private Button finish;
    private Intent incomingIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed_fix_pop_up);
        incomingIntent = getIntent();
        prepareActivityAsPopUpWindow();

        // Initialize Ui
        speedFixRV = findViewById(R.id.speedFixPopUpActivity_rV);
        finish = findViewById(R.id.speedFixPopUpActivity_finishBtn);

        // Load Data From Intent
        gson = new Gson();
        Type type = new TypeToken<ArrayList<ServiceTypeModel>>() {}.getType();
        speedFixList = gson.fromJson(incomingIntent.getStringExtra(Constants.SPEED_FIX),type);

        // Prepare Recycler View and Adapter
        final ServiceListAdapter speedFixAdapter = new ServiceListAdapter(speedFixList,this);
        speedFixRV.setLayoutManager(new LinearLayoutManager(this));
        speedFixRV.setAdapter(speedFixAdapter);


        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent that will carry the results back
                Intent backWithResults = new Intent();

                // Initialize
                selectedList = new ArrayList<>();
                int price = 0;



                    if (speedFixAdapter.getSelectedServiceList()!=null){
                        selectedList.addAll(speedFixAdapter.getSelectedServiceList());
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
