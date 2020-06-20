package com.badawy.carservice.fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckOutFragment extends Fragment implements View.OnClickListener {
    private static final String ORDER_OBJECT = "OrderObject";

    private Gson gson;
    private ImageView navMenuBtn;
    private Button confirmOrderBtn;
    private ListView productsListView;
    private TextView usernameTv, emailTv, addressTv, phoneTv, cancelOrderTv, orderPriceTv, editUserInfoTv;
    private OrderModel orderObject;
    private Activity activity;


    public CheckOutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_check_out, container, false);
        navMenuBtn = view.findViewById(R.id.checkOut_navMenuBtn);
        activity = getActivity();

        gson = new Gson();

        initializeUi(view);


        assert getArguments() != null;
        orderObject = gson.fromJson(getArguments().getString(ORDER_OBJECT), OrderModel.class);

        ArrayList<String> productList = new ArrayList<>();

        // check user name is null
        if (orderObject.getUserProfileObject().getUserName() == null) {
            usernameTv.setTextColor(getResources().getColor(R.color.red));
            usernameTv.setText(getResources().getString(R.string.default_username));
        } else {
            usernameTv.setText(orderObject.getUserProfileObject().getUserName());
        }

        if (orderObject.getUserProfileObject().getEmailAddress() == null) {
            emailTv.setTextColor(getResources().getColor(R.color.red));
            emailTv.setText(getResources().getString(R.string.default_username));
        }else{
            emailTv.setText(orderObject.getUserProfileObject().getEmailAddress());
        }
        // check address is null
        if (orderObject.getUserProfileObject().getAddress() == null) {
            addressTv.setTextColor(getResources().getColor(R.color.red));
            addressTv.setText(getResources().getString(R.string.default_address));
        } else {
            addressTv.setText(orderObject.getUserProfileObject().getAddress());
        }

        // check phone number is null
        if (orderObject.getUserProfileObject().getPhoneNumber() == null) {
            phoneTv.setTextColor(getResources().getColor(R.color.red));
            phoneTv.setText(getResources().getString(R.string.default_phoneNumber));
        } else {
            phoneTv.setText(orderObject.getUserProfileObject().getPhoneNumber());
        }
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

    private void initializeUi(View view) {
        confirmOrderBtn = view.findViewById(R.id.checkOut_confirmOrderBtn);
        productsListView = view.findViewById(R.id.checkOut_productsListView);
        usernameTv = view.findViewById(R.id.checkOut_username);
        emailTv = view.findViewById(R.id.checkOut_emailAddress);
        addressTv = view.findViewById(R.id.checkOut_address);
        phoneTv = view.findViewById(R.id.checkOut_phoneNumber);
        cancelOrderTv = view.findViewById(R.id.checkOut_cancelOrderTv);
        orderPriceTv = view.findViewById(R.id.checkOut_orderPrice);
        editUserInfoTv = view.findViewById(R.id.checkOut_editUserInfo);
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
                if (activity instanceof HomepageActivity) {
                    ((HomepageActivity) activity).openSettings();
                }
                break;

            case R.id.checkOut_confirmOrderBtn:
                if (orderObject.getUserProfileObject().getPhoneNumber() == null
                        || orderObject.getUserProfileObject().getAddress() == null
                        || orderObject.getUserProfileObject().getUserName() == null) {
                    Toast.makeText(getContext(), "Please fill your account information, then try again", Toast.LENGTH_SHORT).show();
                    if (activity instanceof HomepageActivity) {
                        ((HomepageActivity) activity).openSettings();
                    }
                } else {
                    showProgress();
                    makeOrder();
                }
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

                for (ShoppingCartModel item : orderObject.getProductList()
                ) {

                    if (dataSnapshot.hasChild(item.getSparePartModel().getProductID())) {

                        if (dataSnapshot.getChildrenCount() == 1) {
                            dbRef.child(Constants.USERS)
                                    .child(orderObject.getUserProfileObject().getUserId())
                                    .child(Constants.SHOPPING_CART)
                                    .setValue(0)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            if (activity instanceof HomepageActivity) {
                                                hideProgress();
                                                ((HomepageActivity) activity).prepareDialog(R.layout.dialog_order_successful);
                                            }
                                        }
                                    });
                        } else {
                            dbRef.child(Constants.USERS)
                                    .child(orderObject.getUserProfileObject().getUserId())
                                    .child(Constants.SHOPPING_CART)
                                    .child(item.getSparePartModel().getProductID())
                                    .removeValue()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            if (activity instanceof HomepageActivity) {
                                                hideProgress();
                                                ((HomepageActivity) activity).prepareDialog(R.layout.dialog_order_successful);
                                            }
                                        }
                                    });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showProgress() {
        if (getActivity() instanceof HomepageActivity) {
            ((HomepageActivity) getActivity()).showProgressBar(true);
        }
    }

    private void hideProgress() {
        if (getActivity() instanceof HomepageActivity) {
            ((HomepageActivity) getActivity()).showProgressBar(false);
        }
    }


}
