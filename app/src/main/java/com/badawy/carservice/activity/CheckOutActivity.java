package com.badawy.carservice.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.badawy.carservice.R;
import com.badawy.carservice.models.OrderModel;
import com.badawy.carservice.models.ShoppingCartModel;
import com.google.gson.Gson;

import java.util.ArrayList;

public class CheckOutActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String ORDER_OBJECT = "OrderObject";

    private Gson gson;
    private ImageView navMenuBtn;
    private Button confirmOrderBtn;
    private ListView productsListView;
    private TextView usernameTv, emailTv, addressTv, phoneTv,cancelOrderTv, orderPriceTv,editUserInfoTv;
    private OrderModel orderObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        navMenuBtn = findViewById(R.id.checkOut_navMenuBtn);
        Intent incomingIntent = getIntent();
        gson = new Gson();
        confirmOrderBtn = findViewById(R.id.checkOut_confirmOrderBtn);
        productsListView = findViewById(R.id.checkOut_productsListView);
        usernameTv = findViewById(R.id.checkOut_username);
        emailTv = findViewById(R.id.checkOut_emailAddress);
        addressTv = findViewById(R.id.checkOut_address);
        phoneTv = findViewById(R.id.checkOut_phoneNumber);
        cancelOrderTv = findViewById(R.id.checkOut_cancelOrderTv);
        orderPriceTv = findViewById(R.id.checkOut_orderPrice);
        editUserInfoTv= findViewById(R.id.checkOut_editUserInfo);


        orderObject = gson.fromJson(incomingIntent.getStringExtra(ORDER_OBJECT), OrderModel.class);
        ArrayList<String> productList = new ArrayList<>();

        usernameTv.setText(orderObject.getUserProfileObject().getUserName());
        emailTv.setText(orderObject.getUserProfileObject().getEmailAddress());
        addressTv.setText(orderObject.getUserProfileObject().getAddress());
        phoneTv.setText(orderObject.getUserProfileObject().getPhoneNumber());
        orderPriceTv.setText(orderObject.getTotalPrice());
        for (ShoppingCartModel item : orderObject.getProductList()
        ) {

            String temp = item.getPartQuantity() + " X " + item.getSparePartModel().getProductName();
            productList.add(temp.trim());

        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productList);
        productsListView.setAdapter(arrayAdapter);


        navMenuBtn.setOnClickListener(this);
        cancelOrderTv.setOnClickListener(this);
        editUserInfoTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.checkOut_navMenuBtn:
                HomepageActivity.openDrawer();
                break;

            case R.id.checkOut_cancelOrderTv:
                finish();
                break;

            case R.id.checkOut_editUserInfo:
                Toast.makeText(this, "Change Your information from Settings & Account ", Toast.LENGTH_SHORT).show();
        }

    }
}
