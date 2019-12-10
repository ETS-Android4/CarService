package com.badawy.carservice.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.badawy.carservice.R;

public class AddCarActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        //to be removed later
        //Testing
        String[] year = {"2015", "2016", "2017", "2018", "2019"};
        String[] brand = {"BMW", "Audi", "Mercedes"};
        String[] model = {"X7", "X4", "Benz", "R5", "R9"};

        Spinner yearSpinner = findViewById(R.id.addCar_spinner_year);
        Spinner brandSpinner = findViewById(R.id.addCar_spinner_brand);
        Spinner modelSpinner = findViewById(R.id.addCar_spinner_model);

        ImageView nextBtn = findViewById(R.id.addCar_img_next);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,year);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(adapter);


        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,brand);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        brandSpinner.setAdapter(adapter2);


        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,model);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modelSpinner.setAdapter(adapter3);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddCarActivity.this,HomepageActivity.class));
            }
        });

        //End of Testing

    }


}
