package com.badawy.carservice.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.badawy.carservice.R;
import com.badawy.carservice.models.ServiceListModel;
import java.util.ArrayList;

public class ServiceListRecyclerAdapter extends RecyclerView.Adapter<ServiceListRecyclerAdapter.myViewHolder> {

    //Global Variables

    private ArrayList<ServiceListModel> list;
    private Context context;
    private OnButtonClickListener onButtonClickListener;



    //Constructor
    public ServiceListRecyclerAdapter(Context context, ArrayList<ServiceListModel> list, OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
        this.list = list;
        this.context = context;
    }



    ///Implemented Methods



    //return the layout of items ( How an item should look like )
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLayout = LayoutInflater.from(context).inflate(R.layout.recycler_list_item, parent, false);


        return new myViewHolder(itemLayout, onButtonClickListener);
    }


    // put the data inside the views of an item
    @Override
    public void onBindViewHolder(@NonNull myViewHolder eachItem, int position) {
        eachItem.serviceIcon.setImageResource(list.get(position).getServiceIcon());
        eachItem.serviceLabel.setText(list.get(position).getServiceLabel());
        eachItem.serviceDescription.setText(list.get(position).getServiceDescription());
        eachItem.serviceButton.setText(list.get(position).getServiceButtonLabel());

    }

    // return number of rows in the list
    @Override
    public int getItemCount() {
        return list.size();
    }










    // define the views of an item
    public static class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Global Variables
        ImageView serviceIcon;
        TextView serviceLabel;
        TextView serviceDescription;
        Button serviceButton;
        OnButtonClickListener onButtonClickListener;



        // Constructor to initialize the views from the Layout
        myViewHolder(@NonNull View itemView, OnButtonClickListener onButtonClickListener) {
            super(itemView);

            // Views inside our layout
            serviceIcon = itemView.findViewById(R.id.delivery_itemImage);
            serviceLabel = itemView.findViewById(R.id.delivery_itemLabel);
            serviceDescription = itemView.findViewById(R.id.delivery_itemDescription);
            serviceButton = itemView.findViewById(R.id.delivery_itemButton);


            // to Handle Clicks
            this.onButtonClickListener = onButtonClickListener;
            serviceButton.setOnClickListener(this);

        }


        // Method that sends the position of the clicked item to the interface of Clicks
        @Override
        public void onClick(View v) {
            onButtonClickListener.onButtonClick(getAdapterPosition());
        }
    }


    // Interface for clicks
    public interface OnButtonClickListener {
        void onButtonClick(int position);
    }




}
