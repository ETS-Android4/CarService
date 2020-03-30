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

import java.util.ArrayList;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ShoppingCartItemViewHolder> {
    //Global Variables

    private ArrayList<ShoppingCartModel> shoppingCarList;
    private Context context;
    private OnItemClickListener onItemClickListener;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    //Constructor
    public ShoppingCartAdapter(Context context, ArrayList<ShoppingCartModel> shoppingCartList) {
        this.shoppingCarList = shoppingCartList;
        this.context = context;
    }


    //return the layout of items ( How an item should look like )
    @NonNull
    @Override
    public ShoppingCartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLayout = LayoutInflater.from(context).inflate(R.layout.item_shopping_cart, parent, false);
        return new ShoppingCartItemViewHolder(itemLayout, onItemClickListener);
    }


    // put the data inside the views of an item
    @Override
    public void onBindViewHolder(@NonNull ShoppingCartItemViewHolder holder, final int position) {
        holder.partImage.setImageResource(shoppingCarList.get(position).getPartImage());
        holder.partName.setText(shoppingCarList.get(position).getPartName());
        holder.partNumber.setText(shoppingCarList.get(position).getPartNumber());
        holder.partPrice.setText(shoppingCarList.get(position).getPartPrice());
        holder.partQuantity.setText(String.valueOf(shoppingCarList.get(position).getPartQuantity()));


        holder.increaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = shoppingCarList.get(position).getPartQuantity();
                int newQuantity = currentQuantity+1;
                shoppingCarList.get(position).setPartQuantity(newQuantity);
                notifyDataSetChanged();
            }
        });

        holder.decreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = shoppingCarList.get(position).getPartQuantity();
                if (currentQuantity > 1){

                    int newQuantity = currentQuantity -1;
                    shoppingCarList.get(position).setPartQuantity(newQuantity);
                    notifyDataSetChanged();
                }

            }
        });

    }


    // return number of rows in the list
    @Override
    public int getItemCount() {
        return shoppingCarList.size();
    }


    // define the views of an item
    class ShoppingCartItemViewHolder extends RecyclerView.ViewHolder {

        ImageView partImage;
        TextView partName;
        TextView partNumber;
        TextView partPrice;
        TextView partQuantity;
        ImageView increaseQuantity;
        ImageView decreaseQuantity;

        // Constructor to initialize the views from the Layout
        ShoppingCartItemViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            // Views inside our layout

            partImage = itemView.findViewById(R.id.item_shoppingCart_partImage);
            partName = itemView.findViewById(R.id.item_shoppingCart_partName);
            partNumber = itemView.findViewById(R.id.item_shoppingCart_partNumber);
            partPrice = itemView.findViewById(R.id.item_shoppingCart_partPrice);
            partQuantity = itemView.findViewById(R.id.item_shoppingCart_partQuantity);
            increaseQuantity = itemView.findViewById(R.id.item_shoppingCart_increaseQuantity);
            decreaseQuantity = itemView.findViewById(R.id.item_shoppingCart_decreaseQuantity);

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

}
