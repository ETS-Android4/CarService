package com.badawy.carservice.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.badawy.carservice.R;
import com.badawy.carservice.activity.ProductDetailsPopUpActivity;
import com.badawy.carservice.models.SparePartModel;
import com.badawy.carservice.utils.Constants;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ProductItemAdapter extends RecyclerView.Adapter<ProductItemAdapter.ProductsItemViewHolder> {

    //Global Variables

    private ArrayList<SparePartModel> productsList;
    private Context context;
    private OnItemClickListener onItemClickListener;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    //Constructor
    public ProductItemAdapter(Context context, ArrayList<SparePartModel> productsList) {
        this.productsList = productsList;
        this.context = context;
    }


    //return the layout of items ( How an item should look like )
    @NonNull
    @Override
    public ProductsItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLayout = LayoutInflater.from(context).inflate(R.layout.item_shop_product, parent, false);
        return new ProductsItemViewHolder(itemLayout, onItemClickListener);
    }


    // put the data inside the views of an item
    @Override
    public void onBindViewHolder(@NonNull final ProductsItemViewHolder holder, final int position) {

        holder.productName.setText(productsList.get(position).getProductName().trim());
        holder.productPartNumber.setText(productsList.get(position).getProductID().trim());
        holder.productDescription.setText(productsList.get(position).getProductDescription().trim());
        holder.productPrice.setText(productsList.get(position).getProductPrice().trim());
        Glide.with(context).load(productsList.get(position).getProductImage()).into(holder.productImage);
        Glide.with(context).load(productsList.get(position).getManufacturerImage()).into(holder.manufacturerImage);

        holder.productDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(holder.productDetailsBtn.getContext(), ProductDetailsPopUpActivity.class);
                Gson gson = new Gson();
                String serializedObject = gson.toJson(productsList.get(position));
                i.putExtra(Constants.SPARE_PARTS,serializedObject);

                //start activity
                context.startActivity(i);
            }
        });

    }


    // return number of rows in the list
    @Override
    public int getItemCount() {
        return productsList.size();
    }


    // define the views of an item
    class ProductsItemViewHolder extends RecyclerView.ViewHolder {

        ImageView manufacturerImage;
        ImageView productImage;
        TextView productName;
        TextView productPartNumber;
        TextView productDescription;
        TextView productPrice;
        Button productDetailsBtn;

        // Constructor to initialize the views from the Layout
        ProductsItemViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            // Views inside our layout

            manufacturerImage = itemView.findViewById(R.id.productItem_manufacturer);
            productImage = itemView.findViewById(R.id.productItem_image);
            productName = itemView.findViewById(R.id.productItem_name);
            productPartNumber = itemView.findViewById(R.id.productItem_partNumber);
            productDescription = itemView.findViewById(R.id.productItem_description);
            productPrice = itemView.findViewById(R.id.productItem_price);
            productDetailsBtn = itemView.findViewById(R.id.productItem_partDetailsBtn);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                            notifyDataSetChanged();
                        }
                    }
                }
            });


        }//End of constructor
    } // End of view Holder

}// End of the Class
