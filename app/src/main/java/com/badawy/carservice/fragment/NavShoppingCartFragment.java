package com.badawy.carservice.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.badawy.carservice.R;
import com.badawy.carservice.activity.CheckOutActivity;
import com.badawy.carservice.activity.HomepageActivity;
import com.badawy.carservice.adapters.ShoppingCartAdapter;
import com.badawy.carservice.models.OrderModel;
import com.badawy.carservice.models.ShoppingCartModel;
import com.badawy.carservice.models.UserProfileModel;
import com.badawy.carservice.utils.Constants;
import com.badawy.carservice.utils.MySharedPreferences;
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
public class NavShoppingCartFragment extends Fragment implements ShoppingCartAdapter.OnClickListener {
    private static final String ORDER_OBJECT = "OrderObject";
    private RecyclerView shoppingCartRV;
    private float finalPrice = 0;
    //    private int defaultPartQuantity = 1;
    private DatabaseReference dbRef;
    private UserProfileModel userDataObject;
    private Gson gson;
    private TextView totalPriceTv;
    private ShoppingCartAdapter shoppingCartAdapter;
    private ArrayList<ShoppingCartModel> shoppingCartList;
    private ConstraintLayout fullCartLayout, emptyCartLayout;
    private ImageView navMenuBtn;
    private Button checkOutBtn;
    private String totalPrice;

    public NavShoppingCartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nav_shopping_cart, container, false);

        initializeUi(view);


        shoppingCartAdapter = new ShoppingCartAdapter(getActivity(), this);

        gson = new Gson();


        getUserData();
        String userId = userDataObject.getUserId();

        dbRef = FirebaseDatabase.getInstance().getReference().child(Constants.USERS)
                .child(userId).child(Constants.SHOPPING_CART);


        fetchUserShoppingCart();


        navMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomepageActivity.openDrawer();
            }
        });
        checkOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Prepare Order Object
                OrderModel orderObject = new OrderModel();
                orderObject.setProductList(shoppingCartList);
                orderObject.setUserProfileObject(userDataObject);
                orderObject.setTotalPrice(totalPrice);

                Bundle bundle =new Bundle();
                bundle.putString(ORDER_OBJECT,gson.toJson(orderObject));

                Fragment checkOutFragment = new CheckOutFragment();
                checkOutFragment.setArguments(bundle);
                replaceFragment(checkOutFragment);

//                    dbRef = FirebaseDatabase.getInstance().getReference().child(Constants.AVAILABLE_APPOINTMENTS)
//                            .child(Constants.DELIVERY).child(Constants.SPEED_FIX).child(bookingObject.getDate())
//                            .child(bookingObject.getTimeID());

//                    final DatabaseReference bookingRef = FirebaseDatabase.getInstance().getReference().child(Constants.BOOKING);
//                    final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child(Constants.USERS);
            }
        });
        return view;
    }

    private void initializeUi(View view) {
        shoppingCartRV = view.findViewById(R.id.navShoppingCartRV);
        navMenuBtn = view.findViewById(R.id.navShoppingCart_navMenuBtn);
        totalPriceTv = view.findViewById(R.id.navShoppingCart_totalAmount);
        checkOutBtn = view.findViewById(R.id.navShoppingCart_checkOutBtn);
        fullCartLayout = view.findViewById(R.id.navShoppingCart_fullCartLayout);
        emptyCartLayout = view.findViewById(R.id.navShoppingCart_emptyCartLayout);
    }

    private void fetchUserShoppingCart() {

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                shoppingCartList = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    ShoppingCartModel shoppingCartObject = ds.getValue(ShoppingCartModel.class);
                    shoppingCartList.add(shoppingCartObject);

                }

                if (shoppingCartList.size() != 0) {
                    emptyCartLayout.setVisibility(View.GONE);
                    fullCartLayout.setVisibility(View.VISIBLE);
                    bindShoppingCartListData();
                    calculatePrice();
                } else {
                    fullCartLayout.setVisibility(View.GONE);
                    emptyCartLayout.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void bindShoppingCartListData() {

        shoppingCartAdapter.setShoppingCartList(shoppingCartList);
        shoppingCartRV.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        shoppingCartRV.setAdapter(shoppingCartAdapter);
    }

    private void getUserData() {
        String userSerializedData = MySharedPreferences.read(MySharedPreferences.USER_DATA, "");
        if (!userSerializedData.equals("")) {
            userDataObject = gson.fromJson(userSerializedData, UserProfileModel.class);
        }

    }


    @Override
    public void onDeleteIconClick(int position) {

        final String productId = shoppingCartList.get(position).getSparePartModel().getProductID();
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 1) {

                    dbRef.setValue(0);

                } else {

                    if (dataSnapshot.hasChild(productId)) {
                        dbRef.child(productId).removeValue();
                    }
                }

                finalPrice = 0;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onIncreaseQuantityClick(int position) {

        int currentQuantity = shoppingCartList.get(position).getPartQuantity();
        int newQuantity = currentQuantity + 1;

        dbRef.child(shoppingCartList.get(position).getSparePartModel().getProductID())
                .child("partQuantity").setValue(newQuantity);

        //doing it manually inside the app
//        float oldPrice = Float.parseFloat(shoppingCartList.get(position).getOldPrice().replaceAll("[^0-9?!\\.]", ""));
//        float newPrice = oldPrice * newQuantity;
//
//        shoppingCartList.get(position).setPartQuantity(newQuantity);
//        shoppingCartList.get(position).getSparePartModel().setProductPrice(newPrice + " EGP ");
//        shoppingCartAdapter.notifyDataSetChanged();

        calculatePrice();
    }

    @Override
    public void onDecreaseQuantityClick(int position) {
        int currentQuantity = shoppingCartList.get(position).getPartQuantity();
        if (!(currentQuantity <= 1)) {
            int newQuantity = currentQuantity - 1;

            dbRef.child(shoppingCartList.get(position).getSparePartModel().getProductID())
                    .child("partQuantity").setValue(newQuantity);
//
//            float oldPrice = Float.parseFloat(shoppingCartList.get(position).getOldPrice().replaceAll("[^0-9?!\\.]", ""));
//            float newPrice = oldPrice * newQuantity;

//
//            shoppingCartList.get(position).setPartQuantity(newQuantity);
//            shoppingCartList.get(position).getSparePartModel().setProductPrice(newPrice + " EGP ");
//            shoppingCartAdapter.notifyDataSetChanged();

            calculatePrice();

        }

    }


    private void calculatePrice() {
        finalPrice = 0;
        if (shoppingCartList != null) {
            for (ShoppingCartModel part : shoppingCartList) {
                float oldPrice = Float.parseFloat(part.getOldPrice().replaceAll("[^0-9?!\\.]", ""));
                float newPrice = oldPrice * part.getPartQuantity();
                finalPrice += newPrice;
            }
        }

         totalPrice = finalPrice + " EGP ";
        totalPriceTv.setText(totalPrice);

    }


    private void fakeDataTest() {

//
//         int[] partImage = {R.drawable.tire, R.drawable.tire, R.drawable.tire};
//         String[] partName = {"Hankook Ventus Prime 3 K125", "Hankook KINERGY ECO 2 K435", "Hankook VENTUS PRIME3 K125"};
//         String[] partNumber = {"8808563446509", "8808563301211", "8808563401720"};
//         String[] partPrice = {"1350.00 EGP", "2139.00 EGP", "780.30 EGP"};
//        for (int i = 0; i < partPrice.length; i++) {
//
//            shoppingCartList.add(new ShoppingCartModel(partImage[i], partName[i], partNumber[i], partPrice[i], defaultPartQuantity));
//        }

    }

    private void replaceFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.homepage_fragment_container, fragment)
                .addToBackStack("NavShoppingCartFragment")
                .commit();


    }


}