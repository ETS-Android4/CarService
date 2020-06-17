package com.badawy.carservice.fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.badawy.carservice.R;
import com.badawy.carservice.activity.HomepageActivity;
import com.badawy.carservice.adapters.ProductItemAdapter;
import com.badawy.carservice.adapters.SparePartsCategoryAdapter;
import com.badawy.carservice.models.CarModel;
import com.badawy.carservice.models.SparePartsCategoryModel;
import com.badawy.carservice.models.SparePartModel;
import com.badawy.carservice.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeliveryCarSparePartsShopFragment extends Fragment implements SparePartsCategoryAdapter.OnCategoryClick {

    private ImageView navMenuBtn, shoppingCart;
    private RecyclerView partsCategoryNameRv, productsRv;
    private CarModel selectedCarObject;
    private DatabaseReference dbRef;
    private ArrayList<SparePartsCategoryModel> sparePartsCategoryList;
    private ArrayList<SparePartModel> productsList;

    public DeliveryCarSparePartsShopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_spare_parts_shop, container, false);
        initializeUi(view);

        // Initialize Lists


        // Get Selected Car Data
        getSelectedCar();

        // Retrieve Available Categories For This Car
        getCategoriesData();



        navMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomepageActivity.openDrawer();
            }
        });


        shoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                if (activity instanceof HomepageActivity) {
                    ((HomepageActivity) activity).openShoppingCart();
                }
            }
        });
        return view;
    }

    private void getSelectedCar() {
        assert getArguments() != null;
        String serializedSelectedCarObject = getArguments().getString(Constants.SELECTED_CAR);
        Gson gson = new Gson();
        Type type = new TypeToken<CarModel>() {
        }.getType();
        selectedCarObject = gson.fromJson(serializedSelectedCarObject, type);

    }

    private void getCategoriesData() {
        dbRef = FirebaseDatabase.getInstance().getReference().child(Constants.CARS_SPARE_PARTS).child(selectedCarObject.getCarID());
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sparePartsCategoryList = new ArrayList<>();

                for (DataSnapshot ds: dataSnapshot.getChildren()
                     ) {
                    if (ds.hasChildren()){
                            // retrieve categories
                        List<String> sparePartsIdList = new ArrayList<>();
                        for (DataSnapshot child:ds.getChildren()){
                            sparePartsIdList.add(child.getKey());
                        }
                        sparePartsCategoryList.add(new SparePartsCategoryModel(ds.getKey(),sparePartsIdList));

                    }
                }
                if (sparePartsCategoryList.size()!=0){
                    bindCategoryDataToAdapter();
                }
                else{
                    Toast.makeText(getContext(), "There is no spare parts available for this car", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void bindCategoryDataToAdapter() {
        SparePartsCategoryAdapter sparePartsCategoryAdapter = new SparePartsCategoryAdapter(getActivity(), sparePartsCategoryList, this);
        partsCategoryNameRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        partsCategoryNameRv.setAdapter(sparePartsCategoryAdapter);

    }


    private void initializeUi(View view) {
        navMenuBtn = view.findViewById(R.id.sparePartsShop_navMenuBtn);
        shoppingCart = view.findViewById(R.id.sparePartsShop_shoppingCart);
        partsCategoryNameRv = view.findViewById(R.id.sparePartsShop_categoryNameRV);
        productsRv = view.findViewById(R.id.sparePartsShop_productsRV);
    }


    @Override
    public void onCategoryClick(final int position) {

        fetchProductsOfThisCategory(position);

    }

    private void fetchProductsOfThisCategory(int position) {
        final List<String> idList =  sparePartsCategoryList.get(position).getPartIdList();
        dbRef = FirebaseDatabase.getInstance().getReference().child(Constants.APP_DATA).child(Constants.SPARE_PARTS);

        dbRef.child(sparePartsCategoryList.get(position).getPartsCategoryName())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        productsList = new ArrayList<>();
                        for (String id: idList) {

                            if (dataSnapshot.hasChild(id)){
                                productsList.add(dataSnapshot.child(id).getValue(SparePartModel.class));

                            }
                        }

                        bindProductData();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void bindProductData() {
        ProductItemAdapter productItemAdapter = new ProductItemAdapter(getActivity(),productsList);
        productsRv.setLayoutManager(new GridLayoutManager(getActivity(),2));
        productsRv.setAdapter(productItemAdapter);
    }


    private void fakeDataTest() {


//         String[] nameArray = {"Tires", "Mirrors", "Engine", "Others"};
//         int[] manufacturerImage={R.drawable.ic_hankook,R.drawable.tirelogo2,R.drawable.tirelogo3,R.drawable.hankook,R.drawable.hankook,R.drawable.hankook};
//         String[] productName={"Hankook Ventus Prime 3 K125","Hankook KINERGY ECO 2 K435","Hankook VENTUS PRIME3 K125","Hankook Kinergy ECO K425","Hankook VENTUS PRIME3 K125","Hankook Kinergy ECO K425"};
//         String[] productPartNumber={"8808563446509","8808563301211","8808563401720","8808563401768","8808563411880","8808563401775"};
//         String[] productDescription={"205/55 R16 94W XL SBL ","205/55 R16 91W * SBL ","205/55 R16 91W * SBL BMW 1 3TR16 91W * SBL BMW 1 3TRR16 91W * SBL BMW 1 3TR16 91W * SBL BMW 1 3TR16 91W * SBL BMW 1 3T16 91W * SBL BMW 1 3TR16 91W * SBL BMW 1 3TR16 91W * SBL BMW 1 3T , BMW 1 5T","205/55 R16 91V SBL VOLKSWAGEN Golf VI","P205/55 R16 94H XL 4PR SBL","205/55 R16 91V SBL"};
//         String[] productPrice={"1350.00 EGP","2139.00 EGP","780.30 EGP","2566.50 EGP","1600.00 EGP","1300.00 EGP"};
//         int[] productImage={R.drawable.tire,R.drawable.tire,R.drawable.tire,R.drawable.tire,R.drawable.tire,R.drawable.tire};


//        for (String s : nameArray) {

//            nameList.add(new PartsCategoryNameModel(s));
//        }
//
//        PartsCategoryNameAdapter nameAdapter = new PartsCategoryNameAdapter(getActivity(), nameList);
//        partsCategoryNameRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
//        partsCategoryNameRv.setAdapter(nameAdapter);
//
//        for (int i = 0;i<productName.length;i++){
//
//            productsList.add(new ProductItemModel(manufacturerImage[i],productImage[i],productName[i],productPartNumber[i],productDescription[i],productPrice[i]));
//
//        }

//        ProductItemAdapter productItemAdapter = new ProductItemAdapter(getActivity(),productsList);
//        productsRv.setLayoutManager(new GridLayoutManager(getActivity(),2));
//        productsRv.setAdapter(productItemAdapter);

    }


}
