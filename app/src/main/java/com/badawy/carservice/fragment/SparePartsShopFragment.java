package com.badawy.carservice.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.badawy.carservice.R;
import com.badawy.carservice.activity.HomepageActivity;
import com.badawy.carservice.activity.ShoppingCartActivity;
import com.badawy.carservice.adapters.PartsCategoryNameAdapter;
import com.badawy.carservice.adapters.ProductItemAdapter;
import com.badawy.carservice.models.PartsCategoryNameModel;
import com.badawy.carservice.models.ProductItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SparePartsShopFragment extends Fragment {

    private ImageView navMenuBtn, shoppingCart;
    private RecyclerView partsCategoryNameRv, productsRv;

    private String[] nameArray = {"Tires", "Mirrors", "Engine", "Others"};
    private ArrayList<PartsCategoryNameModel> nameList = new ArrayList<>();




    private int[] manufacturerImage={R.drawable.ic_hankook,R.drawable.tirelogo2,R.drawable.tirelogo3,R.drawable.hankook,R.drawable.hankook,R.drawable.hankook};
    private String[] productName={"Hankook Ventus Prime 3 K125","Hankook KINERGY ECO 2 K435","Hankook VENTUS PRIME3 K125","Hankook Kinergy ECO K425","Hankook VENTUS PRIME3 K125","Hankook Kinergy ECO K425"};
    private String[] productPartNumber={"8808563446509","8808563301211","8808563401720","8808563401768","8808563411880","8808563401775"};
    private String[] productDescription={"205/55 R16 94W XL SBL ","205/55 R16 91W * SBL ","205/55 R16 91W * SBL BMW 1 3TR16 91W * SBL BMW 1 3TRR16 91W * SBL BMW 1 3TR16 91W * SBL BMW 1 3TR16 91W * SBL BMW 1 3T16 91W * SBL BMW 1 3TR16 91W * SBL BMW 1 3TR16 91W * SBL BMW 1 3T , BMW 1 5T","205/55 R16 91V SBL VOLKSWAGEN Golf VI","P205/55 R16 94H XL 4PR SBL","205/55 R16 91V SBL"};
    private String[] productPrice={"1350.00 EGP","2139.00 EGP","780.30 EGP","2566.50 EGP","1600.00 EGP","1300.00 EGP"};
    private int[] productImage={R.drawable.tire,R.drawable.tire,R.drawable.tire,R.drawable.tire,R.drawable.tire,R.drawable.tire};

    private ArrayList<ProductItemModel> productsList = new ArrayList<>();
    public SparePartsShopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_spare_parts_shop, container, false);
        initUI(view);

        for (String s : nameArray) {

            nameList.add(new PartsCategoryNameModel(s));
        }

        PartsCategoryNameAdapter nameAdapter = new PartsCategoryNameAdapter(getActivity(), nameList);
        partsCategoryNameRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        partsCategoryNameRv.setAdapter(nameAdapter);

        for (int i = 0;i<productName.length;i++){

            productsList.add(new ProductItemModel(manufacturerImage[i],productImage[i],productName[i],productPartNumber[i],productDescription[i],productPrice[i]));

        }

        ProductItemAdapter productItemAdapter = new ProductItemAdapter(getActivity(),productsList);
        productsRv.setLayoutManager(new GridLayoutManager(getActivity(),2));
        productsRv.setAdapter(productItemAdapter);


        navMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomepageActivity.openDrawer();
            }
        });
        shoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),ShoppingCartActivity.class));
            }
        });
        return view;
    }

    private void initUI(View view) {
        navMenuBtn = view.findViewById(R.id.sparePartsShop_navMenuBtn);
        shoppingCart = view.findViewById(R.id.sparePartsShop_shoppingCart);
        partsCategoryNameRv = view.findViewById(R.id.sparePartsShop_categoryNameRV);
        productsRv = view.findViewById(R.id.sparePartsShop_productsRV);
    }
}
