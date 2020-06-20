package com.badawy.carservice.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.badawy.carservice.R;
import com.badawy.carservice.models.ShoppingCartModel;
import com.badawy.carservice.models.SparePartModel;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ShoppingCartItemViewHolder> {

    //Global Variables
    private ArrayList<ShoppingCartModel> shoppingCartList;
    private Context context;
    private OnClickListener onClickListener;


    public interface OnClickListener {
        void onDeleteIconClick(int position);

        void onIncreaseQuantityClick(int position);

        void onDecreaseQuantityClick(int position);
    }

    public void setOnClickListener(OnClickListener listener) {
        onClickListener = listener;
    }

    //Constructor
    public ShoppingCartAdapter(Context context, OnClickListener onClickListener) {
        this.context = context;
        this.onClickListener = onClickListener;
    }

    public ArrayList<ShoppingCartModel> getShoppingCartList() {
        return shoppingCartList;
    }

    public void setShoppingCartList(ArrayList<ShoppingCartModel> shoppingCartList) {
        this.shoppingCartList = shoppingCartList;
        setOldPrice();
        notifyDataSetChanged();

    }

    private void setOldPrice() {

        for (ShoppingCartModel part : shoppingCartList
        ) {
            part.setOldPrice(part.getSparePartModel().getProductPrice());
        }
    }

    //return the layout of items ( How an item should look like )
    @NonNull
    @Override
    public ShoppingCartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLayout = LayoutInflater.from(context).inflate(R.layout.item_shopping_cart, parent, false);
        return new ShoppingCartItemViewHolder(itemLayout);
    }


    // put the data inside the views of an item
    @Override
    public void onBindViewHolder(@NonNull ShoppingCartItemViewHolder holder, final int position) {
        SparePartModel sparePartObject = shoppingCartList.get(position).getSparePartModel();

        Glide.with(context).load(sparePartObject.getProductImage()).into(holder.partImage);
        holder.partName.setText(sparePartObject.getProductName().trim());
        holder.partNumber.setText(sparePartObject.getProductID().trim());
        holder.partPrice.setText(String.valueOf(sparePartObject.getProductPrice()));
        holder.partQuantity.setText(String.valueOf(shoppingCartList.get(position).getPartQuantity()));


    }


    // return number of rows in the list
    @Override
    public int getItemCount() {
        return shoppingCartList.size();
    }


    // define the views of an item
    class ShoppingCartItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView partImage;
        TextView partName;
        TextView partNumber;
        TextView partPrice;
        TextView partQuantity;
        ImageView increaseQuantity;
        ImageView decreaseQuantity;
        ImageView deleteImage;

        // Constructor to initialize the views from the Layout
        ShoppingCartItemViewHolder(@NonNull View itemView) {
            super(itemView);

            // Views inside our layout

            partImage = itemView.findViewById(R.id.item_shoppingCart_partImage);
            partName = itemView.findViewById(R.id.item_shoppingCart_partName);
            partNumber = itemView.findViewById(R.id.item_shoppingCart_partNumber);
            partPrice = itemView.findViewById(R.id.item_shoppingCart_partPrice);
            partQuantity = itemView.findViewById(R.id.item_shoppingCart_partQuantity);
            increaseQuantity = itemView.findViewById(R.id.item_shoppingCart_increaseQuantity);
            decreaseQuantity = itemView.findViewById(R.id.item_shoppingCart_decreaseQuantity);
            deleteImage = itemView.findViewById(R.id.item_shoppingCart_delete);

            deleteImage.setOnClickListener(this);
            increaseQuantity.setOnClickListener(this);
            decreaseQuantity.setOnClickListener(this);

        }//End of constructor

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.item_shoppingCart_delete:
                    if (onClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onClickListener.onDeleteIconClick(position);
                        }
                    }
                    break;
                case R.id.item_shoppingCart_increaseQuantity:
                    if (onClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onClickListener.onIncreaseQuantityClick(position);
                        }
                    }
                    break;
                case R.id.item_shoppingCart_decreaseQuantity:
                    if (onClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onClickListener.onDecreaseQuantityClick(position);
                        }
                    }
                    break;
                default:
                    break;
            }

        }
    } // End of view Holder

}
