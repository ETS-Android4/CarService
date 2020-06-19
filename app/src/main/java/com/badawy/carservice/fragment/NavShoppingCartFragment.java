package com.badawy.carservice.fragment;

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

        // Show Progress Bar
        showProgress();
        shoppingCartRV.setVisibility(View.GONE);
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

                    bindShoppingCartListData();
                    calculatePrice();

                } else {
                    hideProgress();
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
        hideProgress();
        shoppingCartRV.setVisibility(View.VISIBLE);
        emptyCartLayout.setVisibility(View.GONE);
        fullCartLayout.setVisibility(View.VISIBLE);

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
        showProgress();
        int currentQuantity = shoppingCartList.get(position).getPartQuantity();
        int newQuantity = currentQuantity + 1;

        dbRef.child(shoppingCartList.get(position).getSparePartModel().getProductID())
                .child("partQuantity").setValue(newQuantity);

        calculatePrice();
        hideProgress();
    }

    @Override
    public void onDecreaseQuantityClick(int position) {

        int currentQuantity = shoppingCartList.get(position).getPartQuantity();
        if (!(currentQuantity <= 1)) {
            showProgress();
            int newQuantity = currentQuantity - 1;

            dbRef.child(shoppingCartList.get(position).getSparePartModel().getProductID())
                    .child("partQuantity").setValue(newQuantity);

            calculatePrice();

            hideProgress();

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


    private void showProgress(){
        if (getActivity() instanceof HomepageActivity){
            ((HomepageActivity) getActivity()).showProgressBar(true);
        }
    }

    private void hideProgress(){
        if (getActivity() instanceof HomepageActivity){
            ((HomepageActivity) getActivity()).showProgressBar(false);
        }
    }



    private void replaceFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.homepage_fragment_container, fragment)
                .addToBackStack("NavShoppingCartFragment")
                .commit();


    }


}