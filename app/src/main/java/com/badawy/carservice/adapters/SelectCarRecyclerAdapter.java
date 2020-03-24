package com.badawy.carservice.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.badawy.carservice.R;
import com.badawy.carservice.models.SelectCarModel;

import java.util.ArrayList;


public class SelectCarRecyclerAdapter extends RecyclerView.Adapter<SelectCarRecyclerAdapter.CarHolder> {


        //Global Variables

        private ArrayList<SelectCarModel> carList;
        private Context context;
        private OnItemClickListener onItemClickListener;
        private int defaultSelectedItem;


        public interface OnItemClickListener {
            void onItemClick(int position);
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            onItemClickListener = listener;
        }

        //Constructor
        public SelectCarRecyclerAdapter(Context context, ArrayList<SelectCarModel> carList) {
            this.carList = carList;
            this.context = context;
            this.defaultSelectedItem = 0;
        }


        //return the layout of items ( How an item should look like )
        @NonNull
        @Override
        public SelectCarRecyclerAdapter.CarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View itemLayout = LayoutInflater.from(context).inflate(R.layout.select_car_item, parent, false);
            return new CarHolder(itemLayout, onItemClickListener);
        }


        // put the data inside the views of an item
        @Override
        public void onBindViewHolder(@NonNull CarHolder holder, final int position) {
            holder.carItemImage.setImageResource(carList.get(position).getCarImage());
            holder.carItemName.setText(carList.get(position).getCarName());


            holder.carItemBackground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    defaultSelectedItem = position;
                    notifyDataSetChanged();

                }
            });


            if (defaultSelectedItem == position) {

                holder.carItemBackground.setBackgroundResource(R.drawable.style_rectangle_corners_solid_red);
                holder.carItemName.setTextColor(Color.WHITE);
            } else {
                holder.carItemBackground.setBackgroundResource(R.drawable.style_grey_stroke_line);
                holder.carItemName.setTextColor(Color.BLACK);

            }


        }

        public byte getCarId() {
            return carList.get(defaultSelectedItem).getCarId();
        }

        // return number of rows in the list
        @Override
        public int getItemCount() {
            return carList.size();
        }


        // define the views of an item
        class CarHolder extends RecyclerView.ViewHolder {

            ImageView carItemImage;
            TextView carItemName;
            ConstraintLayout carItemBackground;

            // Constructor to initialize the views from the Layout
            CarHolder(@NonNull View itemView, final OnItemClickListener listener) {
                super(itemView);

                // Views inside our layout
               carItemImage = itemView.findViewById(R.id.carItem_image);
               carItemName = itemView.findViewById(R.id.carItem_name);
               carItemBackground=itemView.findViewById(R.id.carItem_background);


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
