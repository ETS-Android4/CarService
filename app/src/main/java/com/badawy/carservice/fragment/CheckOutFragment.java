package com.badawy.carservice.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.badawy.carservice.R;
import com.badawy.carservice.activity.HomepageActivity;
import com.badawy.carservice.models.OrderModel;
import com.badawy.carservice.models.ShoppingCartModel;
import com.badawy.carservice.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckOutFragment extends Fragment implements View.OnClickListener{
    private static final String ORDER_OBJECT = "OrderObject";

    private Gson gson;
    private ImageView navMenuBtn;
    private Button confirmOrderBtn;
    private ListView productsListView;
    private TextView usernameTv, emailTv, addressTv, phoneTv,cancelOrderTv, orderPriceTv,editUserInfoTv;
    private OrderModel orderObject;
    private Activity activity;


    public CheckOutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_check_out, container, false);
        navMenuBtn = view.findViewById(R.id.checkOut_navMenuBtn);
        activity = getActivity();


        gson = new Gson();
        confirmOrderBtn = view.findViewById(R.id.checkOut_confirmOrderBtn);
        productsListView = view.findViewById(R.id.checkOut_productsListView);
        usernameTv = view.findViewById(R.id.checkOut_username);
        emailTv = view.findViewById(R.id.checkOut_emailAddress);
        addressTv = view.findViewById(R.id.checkOut_address);
        phoneTv = view.findViewById(R.id.checkOut_phoneNumber);
        cancelOrderTv = view.findViewById(R.id.checkOut_cancelOrderTv);
        orderPriceTv = view.findViewById(R.id.checkOut_orderPrice);
        editUserInfoTv= view.findViewById(R.id.checkOut_editUserInfo);

        assert getArguments() != null;
        orderObject = gson.fromJson(getArguments().getString(ORDER_OBJECT), OrderModel.class);

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
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, productList);
        productsListView.setAdapter(arrayAdapter);


        navMenuBtn.setOnClickListener(this);
        cancelOrderTv.setOnClickListener(this);
        editUserInfoTv.setOnClickListener(this);
        confirmOrderBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.checkOut_navMenuBtn:
                HomepageActivity.openDrawer();
                break;

            case R.id.checkOut_cancelOrderTv:
                assert getFragmentManager() != null;
                getFragmentManager().popBackStack();
                break;

            case R.id.checkOut_editUserInfo:
                Activity activity = getActivity();
                if (activity instanceof HomepageActivity) {
                    ((HomepageActivity) activity).openSettings();
                }
                break;

            case R.id.checkOut_confirmOrderBtn:
                makeOrder();
                break;

        }
    }

    private void makeOrder() {
        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

        String orderId = dbRef.push().getKey();
        orderObject.setOrderID(orderId);

        dbRef.child(Constants.BOOKING).child(Constants.ORDERS).child(orderObject.getOrderID()).setValue(orderObject);
        dbRef.child(Constants.USERS).child(orderObject.getUserProfileObject().getUserId()).child(Constants.APPOINTMENTS_ORDERS).child(Constants.ORDERS).child(orderObject.getOrderID()).setValue(0);
        dbRef.child(Constants.USERS).child(orderObject.getUserProfileObject().getUserId()).child(Constants.SHOPPING_CART).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (ShoppingCartModel item: orderObject.getProductList()
                     ) {

                    if (dataSnapshot.hasChild(item.getSparePartModel().getProductID())){

                        if (dataSnapshot.getChildrenCount() == 1){
                            dbRef.child(Constants.USERS).child(orderObject.getUserProfileObject().getUserId()).child(Constants.SHOPPING_CART).setValue(0);
                        }else {
                            dbRef.child(Constants.USERS).child(orderObject.getUserProfileObject().getUserId()).child(Constants.SHOPPING_CART).child(item.getSparePartModel().getProductID()).removeValue();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
