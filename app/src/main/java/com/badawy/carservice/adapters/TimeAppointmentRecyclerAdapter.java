package com.badawy.carservice.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.badawy.carservice.R;
import com.badawy.carservice.models.TimeAppointmentModel;

import java.util.ArrayList;

public class TimeAppointmentRecyclerAdapter extends RecyclerView.Adapter<TimeAppointmentRecyclerAdapter.TimeHolder> {


    //Global Variables

    private ArrayList<TimeAppointmentModel> timeList;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private int selectedItem;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    //Constructor
    public TimeAppointmentRecyclerAdapter(Context context, ArrayList<TimeAppointmentModel> timeList) {
        this.timeList = timeList;
        this.context = context;
        this.selectedItem = 0;
    }

    public TimeAppointmentRecyclerAdapter(Context context) {
        this.context = context;
        this.selectedItem =0;
    }

    public void setTimeList(ArrayList<TimeAppointmentModel> timeList) {

        this.timeList = timeList;
    }

    //return the layout of items ( How an item should look like )
    @NonNull
    @Override
    public TimeAppointmentRecyclerAdapter.TimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLayout = LayoutInflater.from(context).inflate(R.layout.item_appointment_time, parent, false);
        return new TimeHolder(itemLayout, onItemClickListener);
    }


    // put the data inside the views of an item
    @Override
    public void onBindViewHolder(@NonNull TimeHolder holder, final int position) {
        holder.time.setText(timeList.get(position).getTime());
        holder.timeOfDay.setText(timeList.get(position).getTimeOfDay());


        holder.itemBackgroundColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedItem = position;
                notifyDataSetChanged();

            }
        });


        if (selectedItem == position) {

            holder.itemBackgroundColor.setBackgroundResource(R.drawable.style_rectangle_full_corners_red);
            holder.time.setTextColor(Color.WHITE);
            holder.timeOfDay.setTextColor(Color.WHITE);
        } else {
            holder.itemBackgroundColor.setBackgroundResource(R.drawable.style_grey_stroke_line);
            holder.time.setTextColor(Color.BLACK);
            holder.timeOfDay.setTextColor(Color.BLACK);

        }


    }

    public TimeAppointmentModel getTimeObject() {
        return timeList.get(selectedItem);
    }

    // return number of rows in the list
    @Override
    public int getItemCount() {
        return timeList.size();
    }


    // define the views of an item
    class TimeHolder extends RecyclerView.ViewHolder {

        ConstraintLayout itemBackgroundColor;
        TextView time;
        TextView timeOfDay;


        // Constructor to initialize the views from the Layout
        TimeHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            // Views inside our layout
            time = itemView.findViewById(R.id.item_appointment_time);
            timeOfDay = itemView.findViewById(R.id.item_appointment_timeOfDay);
            itemBackgroundColor = itemView.findViewById(R.id.item_appointment_background);


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