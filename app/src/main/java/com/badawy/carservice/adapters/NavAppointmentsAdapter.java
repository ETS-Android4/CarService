package com.badawy.carservice.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.badawy.carservice.R;
import com.badawy.carservice.models.BookingModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class NavAppointmentsAdapter extends RecyclerView.Adapter<NavAppointmentsAdapter.AppointmentsViewHolder> {


    //Global Variables

    private ArrayList<BookingModel> appointmentsList;
    private Context context;
    private OnItemClickListener onItemClickListener;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    //Constructor
    public NavAppointmentsAdapter(Context context, ArrayList<BookingModel> appointmentsList) {
        if (appointmentsList != null) {
            this.appointmentsList = appointmentsList;
        } else {
            this.appointmentsList = new ArrayList<>();
        }
        this.context = context;

    }


    //return the layout of items ( How an item should look like )
    @NonNull
    @Override
    public AppointmentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLayout = LayoutInflater.from(context).inflate(R.layout.item_nav_appointments, parent, false);
        return new AppointmentsViewHolder(itemLayout, onItemClickListener);
    }


    // put the data inside the views of an item
    @Override
    public void onBindViewHolder(@NonNull AppointmentsViewHolder holder, final int position) {
        String serviceLabelTxt = appointmentsList.get(position).getServiceName().trim();
        holder.serviceLabel.setText(serviceLabelTxt);
        holder.serviceType.setText(appointmentsList.get(position).getServiceDescription());
        holder.servicePrice.setText(appointmentsList.get(position).getPrice() + " EGP ");
        holder.serviceDate.setText(appointmentsList.get(position).getDate());
        String timeTxt = appointmentsList.get(position).getTimeObject().getTime() + " " + appointmentsList.get(position).getTimeObject().getTimeOfDay();
        holder.serviceTime.setText(timeTxt);
        holder.serviceAddress.setText(appointmentsList.get(position).getAddress().trim());


    }


    // return number of rows in the list
    @Override
    public int getItemCount() {
        return appointmentsList.size();
    }


    // define the views of an item
    class AppointmentsViewHolder extends RecyclerView.ViewHolder {

        TextView serviceLabel, serviceType, servicePrice, serviceDate, serviceTime, serviceAddress;

        // Constructor to initialize the views from the Layout
        AppointmentsViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            // Views inside our layout

            serviceLabel = itemView.findViewById(R.id.item_navAppointments_serviceLabel);
            serviceType = itemView.findViewById(R.id.item_navAppointments_serviceTypeLabel);
            servicePrice = itemView.findViewById(R.id.item_navAppointments_price);
            serviceDate = itemView.findViewById(R.id.item_navAppointments_date);
            serviceTime = itemView.findViewById(R.id.item_navAppointments_time);
            serviceAddress = itemView.findViewById(R.id.item_navAppointments_address);


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