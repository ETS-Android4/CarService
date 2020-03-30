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
import com.badawy.carservice.models.NavOrdersModel;

import java.util.ArrayList;

public class NavOrdersAdapter extends RecyclerView.Adapter<NavOrdersAdapter.OrdersViewHolder> {
    //Global Variables

    private ArrayList<NavOrdersModel> ordersList;
    private Context context;
    private OnItemClickListener onItemClickListener;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    //Constructor
    public NavOrdersAdapter(Context context, ArrayList<NavOrdersModel> ordersList) {
        this.ordersList = ordersList;
        this.context = context;
    }


    //return the layout of items ( How an item should look like )
    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLayout = LayoutInflater.from(context).inflate(R.layout.item_nav_orders, parent, false);
        return new OrdersViewHolder(itemLayout, onItemClickListener);
    }


    // put the data inside the views of an item
    @Override
    public void onBindViewHolder(@NonNull OrdersViewHolder holder, final int position) {

        holder.productImage.setImageResource(ordersList.get(position).getProductImage());
        holder.productName.setText(ordersList.get(position).getProductName());
        holder.productPartNumber.setText("Product #"+ ordersList.get(position).getProductPartNumber());
        holder.productPrice.setText(ordersList.get(position).getProductPrice());
        holder.orderDate.setText(ordersList.get(position).getOrderDate());
        holder.orderTime.setText(ordersList.get(position).getOrderTime());
        holder.orderNumber.setText("Order #"+ordersList.get(position).getOrderNumber());

    }


    // return number of rows in the list
    @Override
    public int getItemCount() {
        return ordersList.size();
    }


    // define the views of an item
    class OrdersViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPartNumber, productPrice, orderDate, orderTime, orderNumber;

        // Constructor to initialize the views from the Layout
        OrdersViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            // Views inside our layout
            productImage = itemView.findViewById(R.id.item_navOrders_productImage);
            productName = itemView.findViewById(R.id.item_navOrders_productName);
            productPartNumber = itemView.findViewById(R.id.item_navOrders_productPartNumber);
            productPrice = itemView.findViewById(R.id.item_navOrders_productPrice);
            orderDate = itemView.findViewById(R.id.item_navOrders_date);
            orderTime = itemView.findViewById(R.id.item_navOrders_time);
            orderNumber = itemView.findViewById(R.id.item_navOrders_orderNumber);



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
        }
    }

}
