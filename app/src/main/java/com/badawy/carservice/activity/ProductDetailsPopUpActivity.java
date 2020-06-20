package com.badawy.carservice.activity;

import androidx.annotation.NonNull;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.badawy.carservice.R;
import com.badawy.carservice.adapters.SparePartDetailsAdapter;
import com.badawy.carservice.models.ShoppingCartModel;
import com.badawy.carservice.models.SparePartModel;
import com.badawy.carservice.models.UserProfileModel;
import com.badawy.carservice.utils.Constants;
import com.badawy.carservice.utils.MySharedPreferences;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class ProductDetailsPopUpActivity extends AppCompatActivity {
    private Intent intent;
    private ImageView productImageView, manufacturerImageView;
    private TextView nameTv, partNumberTv, descriptionTv, priceTv;
    private Button addToCartBtn;
    private RecyclerView detailListRv;
    private DatabaseReference dbRef;
    private UserProfileModel userDataObject;
    private SparePartModel sparePart;
    private Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details_pop_up);
        prepareActivityAsPopUpWindow();

        initializeUi();

        gson = new Gson();

        // Get Product Data Object
        intent = getIntent();
        sparePart = gson.fromJson(intent.getStringExtra(Constants.SPARE_PARTS), SparePartModel.class);

        getUserData();


        bindData(sparePart);

        dbRef = FirebaseDatabase.getInstance().getReference().child(Constants.USERS);
        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProductToUserShoppingCart();
            }
        });


    }

    private void getUserData() {
        String userSerializedData = MySharedPreferences.read(MySharedPreferences.USER_DATA, "");
        if (!userSerializedData.equals("")) {
            userDataObject = gson.fromJson(userSerializedData, UserProfileModel.class);
        }

    }

    private void addProductToUserShoppingCart() {

        final String userId = userDataObject.getUserId();
        final String productID = sparePart.getProductID();

        dbRef.child(userId).child(Constants.SHOPPING_CART).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ShoppingCartModel shoppingCartObject = new ShoppingCartModel();
                shoppingCartObject.setPartQuantity(1);
                shoppingCartObject.setSparePartModel(sparePart);

                if (!dataSnapshot.hasChild(productID)) {
                    dbRef.child(userId).child(Constants.SHOPPING_CART).child(productID).setValue(shoppingCartObject);
                    finish();
                    Toast.makeText(ProductDetailsPopUpActivity.this, "Successfully Added to Shopping Cart", Toast.LENGTH_SHORT).show();

                } else {
                    finish();
                    Toast.makeText(ProductDetailsPopUpActivity.this, "Already Exist in Shopping Cart", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void bindData(SparePartModel sparePart) {


        Glide.with(this).load(sparePart.getManufacturerImage()).into(manufacturerImageView);
        Glide.with(this).load(sparePart.getProductImage()).into(productImageView);
        nameTv.setText(sparePart.getProductName().trim());
        partNumberTv.setText(sparePart.getProductID().trim());
        descriptionTv.setText(sparePart.getProductDescription().trim());
        priceTv.setText(sparePart.getProductPrice().trim());

        SparePartDetailsAdapter sparePartDetailsAdapter = new SparePartDetailsAdapter(sparePart.getProductDetailsList(), this);
        detailListRv.setLayoutManager(new LinearLayoutManager(this));
        detailListRv.setAdapter(sparePartDetailsAdapter);


    }

    private void initializeUi() {

        manufacturerImageView = findViewById(R.id.productItemDetails_manufacturer);
        productImageView = findViewById(R.id.productItemDetails_image);
        nameTv = findViewById(R.id.productItemDetails_name);
        partNumberTv = findViewById(R.id.productItemDetails_partNumber);
        descriptionTv = findViewById(R.id.productItemDetails_description);
        priceTv = findViewById(R.id.productItemDetails_price);
        addToCartBtn = findViewById(R.id.productItemDetails_partDetailsBtn);
        detailListRv = findViewById(R.id.productItemDetails_detailsRecyclerView);
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
