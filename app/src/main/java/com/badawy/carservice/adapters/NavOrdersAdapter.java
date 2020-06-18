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
import com.badawy.carservice.models.OrderModel;
import com.badawy.carservice.models.SparePartModel;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NavOrdersAdapter extends RecyclerView.Adapter<NavOrdersAdapter.OrdersViewHolder> {
    //Global Variables

    private ArrayList<OrderModel> ordersList;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private SimpleDateFormat yearFormatter;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    //Constructor
    public NavOrdersAdapter(Context context, ArrayList<OrderModel> ordersList) {
        if (ordersList!=null){
            this.ordersList = ordersList;

        }
        else{
            this.ordersList = new ArrayList<>();

        }
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
        OrderModel currentOrder = ordersList.get(position);
        SparePartModel firstPart = ordersList.get(position).getProductList().get(0).getSparePartModel();
//        holder.productImage.setImageResource(ordersList.get(position).getProductImage());
        Glide.with(context).load(firstPart.getProductImage()).into(holder.productImage);

        holder.productName.setText(firstPart.getProductName().trim());
        holder.productPrice.setText(ordersList.get(position).getTotalPrice().trim());
        if (currentOrder.getProductList().size() == 1) {
            holder.productPartNumber.setText("Product Number: "+currentOrder.getProductList().get(0).getSparePartModel().getProductID());
//                    ordersList.get(position).getProductList().get(position).getSparePartModel().getProductID());
        } else  if (ordersList.get(position).getProductList().size() > 1)  {

            int listCount = ordersList.get(position).getProductList().size();
            String msg = "+ " + listCount + " more Products";
            holder.productPartNumber.setText(msg);
        }
            String time = formatTimeStamp(ordersList.get(position).getTimestamp());
        holder.orderTime.setText(time);

        holder.orderAddress.setText(currentOrder.getUserProfileObject().getAddress().trim());

    }

    private String formatTimeStamp(Object timeStamp) {
        yearFormatter = new SimpleDateFormat("MMM dd, yyyy EEE h:mm a", Locale.ENGLISH);
        Date date = new Date((long)timeStamp);

        return yearFormatter.format(date);

    }


    // return number of rows in the list
    @Override
    public int getItemCount() {
        return ordersList.size();
    }


    // define the views of an item
    class OrdersViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPartNumber, productPrice, orderAddress, orderTime, orderNumber;

        // Constructor to initialize the views from the Layout
        OrdersViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            // Views inside our layout
            productImage = itemView.findViewById(R.id.item_navOrders_productImage);
            productName = itemView.findViewById(R.id.item_navOrders_productName);
            productPartNumber = itemView.findViewById(R.id.item_navOrders_productPartNumber);
            productPrice = itemView.findViewById(R.id.item_navOrders_productPrice);
            orderTime = itemView.findViewById(R.id.item_navOrders_orderTime);
            orderAddress= itemView.findViewById(R.id.item_navOrders_address);
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
