package com.badawy.carservice.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.badawy.carservice.R;
import com.badawy.carservice.adapters.AddCarAdapter;
import com.badawy.carservice.models.CarModel;
import com.badawy.carservice.utils.Constants;
import com.badawy.carservice.utils.MyCustomSystemUi;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class AddCarActivity extends AppCompatActivity {

    private DatabaseReference dbRef;
    private ArrayList<CarModel> carList;
    private RecyclerView addCarRV;
    private EditText searchET;
    private AddCarAdapter addCarAdapter;
    private ImageView nextBtn;
    private FirebaseAuth firebaseAuth;
    private final int requestCode = 11;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        carList = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        initializeUi();

        initSearchEditText();

        getCarListFromFirebase();


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCarToUserProfile();
            }
        });

    }

    // fetch data from firebase and View it
    private void getCarListFromFirebase() {
        dbRef = FirebaseDatabase.getInstance().getReference().child(Constants.APP_DATA);


        dbRef.child(Constants.CARS).orderByChild("carBrand").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()
                ) {
                    CarModel obj = ds.getValue(CarModel.class);
                    carList.add(obj);

                }

                prepareCarListAdapter(carList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void prepareCarListAdapter(ArrayList<CarModel> carList) {
        addCarAdapter = new AddCarAdapter(this, carList);
        addCarRV.setLayoutManager(new LinearLayoutManager(this));
        addCarRV.setAdapter(addCarAdapter);


    }


    // Search Functionality
    private void initSearchEditText() {
        searchET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCustomSystemUi.showKeyboard(getApplicationContext(), searchET);
            }
        });


        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterList(s.toString());
            }
        });
    }

    private void filterList(String text) {
        ArrayList<CarModel> filterList = new ArrayList<>();
        for (CarModel obj : carList) {
            String builder = obj.getCarBrand().concat(" - ")
                    .concat(obj.getCarModel())
                    .concat(" - ").concat(obj.getCarYear());

            if (builder.toLowerCase().contains(text.toLowerCase())) {

                filterList.add(obj);
            }
        }
        addCarAdapter.getFilterList(filterList);

    }

    @Override
    public void onBackPressed() {

    }

    // Add Car to User`s Profile
    private void addCarToUserProfile() {
        final CarModel selectedCar = addCarAdapter.getCurrentSelectedCar();

        if (selectedCar != null) {

            dbRef = FirebaseDatabase.getInstance().getReference().child(Constants.USERS);
            dbRef.child(Objects.requireNonNull(firebaseAuth.getUid())).child(Constants.USER_CARS)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.hasChild(selectedCar.getCarID())) {

                                dbRef.child(Objects.requireNonNull(firebaseAuth.getUid())).child(Constants.USER_CARS)
                                        .child(selectedCar.getCarID()).setValue(selectedCar);
                                Toast.makeText(AddCarActivity.this, "Car Added Successfully ! ", Toast.LENGTH_SHORT).show();
                            } else {
                                // if user already have the car .. do something
                                Toast.makeText(AddCarActivity.this, "already have it !", Toast.LENGTH_SHORT).show();
                            }
                            setResult(requestCode);
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


        } else {
            Toast.makeText(AddCarActivity.this, "Please Select a Car First! ", Toast.LENGTH_SHORT).show();
        }
    }

    // init Ui
    private void initializeUi() {
        nextBtn = findViewById(R.id.addCar_img_next);
        searchET = findViewById(R.id.addCar_searchET);
        addCarRV = findViewById(R.id.addCar_rv);

    }


}
