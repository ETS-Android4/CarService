package com.badawy.carservice.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.badawy.carservice.R;
import com.badawy.carservice.activity.HomepageActivity;
import com.badawy.carservice.adapters.ShoppingCartAdapter;
import com.badawy.carservice.models.ShoppingCartModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavShoppingCartFragment extends Fragment {

    private RecyclerView shoppingCartRV;

    private int defaultPartQuantity = 1;

    private int[] partImage = {R.drawable.tire, R.drawable.tire, R.drawable.tire};
    private String[] partName = {"Hankook Ventus Prime 3 K125", "Hankook KINERGY ECO 2 K435", "Hankook VENTUS PRIME3 K125"};
    private String[] partNumber = {"8808563446509", "8808563301211", "8808563401720"};
    private String[] partPrice = {"1350.00 EGP", "2139.00 EGP", "780.30 EGP"};


    private ArrayList<ShoppingCartModel> shoppingCartList = new ArrayList<>();

    public NavShoppingCartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nav_shopping_cart, container, false);

        shoppingCartRV = view.findViewById(R.id.navShoppingCartRV);
        for (int i = 0; i < partPrice.length; i++) {

            shoppingCartList.add(new ShoppingCartModel(partImage[i], partName[i], partNumber[i], partPrice[i], defaultPartQuantity));
        }
        ImageView navMenuBtn = view.findViewById(R.id.navShoppingCart_navMenuBtn);
        navMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomepageActivity.openDrawer();
            }
        });

        ShoppingCartAdapter adapter = new ShoppingCartAdapter(getActivity(), shoppingCartList);
        shoppingCartRV.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        shoppingCartRV.setAdapter(adapter);
        return view;
    }
}
