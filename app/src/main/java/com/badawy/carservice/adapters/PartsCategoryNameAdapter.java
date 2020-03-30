package com.badawy.carservice.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.badawy.carservice.R;
import com.badawy.carservice.models.PartsCategoryNameModel;

import java.util.ArrayList;

public class PartsCategoryNameAdapter extends RecyclerView.Adapter<PartsCategoryNameAdapter.PartsCategoryNameViewHolder> {

    //Global Variables

    private ArrayList<PartsCategoryNameModel> nameList;
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
    public PartsCategoryNameAdapter(Context context, ArrayList<PartsCategoryNameModel> carList) {
        this.nameList = carList;
        this.context = context;
        this.defaultSelectedItem = 0;
    }


    //return the layout of items ( How an item should look like )
    @NonNull
    @Override
    public PartsCategoryNameAdapter.PartsCategoryNameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLayout = LayoutInflater.from(context).inflate(R.layout.item_shop_category_name, parent, false);
        return new PartsCategoryNameViewHolder(itemLayout, onItemClickListener);
    }


    // put the data inside the views of an item
    @Override
    public void onBindViewHolder(@NonNull PartsCategoryNameViewHolder holder, final int position) {
        holder.partsCategoryName.setText(nameList.get(position).getPartsCategoryName());


        holder.partsCategoryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultSelectedItem = position;
                notifyDataSetChanged();
            }
        });


        if (defaultSelectedItem == position) {
            holder.partsCategoryName.setTextColor(Color.BLACK);
            holder.partsCategoryBlackDot.setVisibility(View.VISIBLE);
            holder.partsCategoryName.setTextSize(13);
        } else {
            holder.partsCategoryName.setTextColor(Color.GRAY);
            holder.partsCategoryBlackDot.setVisibility(View.INVISIBLE);
            holder.partsCategoryName.setTextSize(11);

        }

    }


    // return number of rows in the list
    @Override
    public int getItemCount() {
        return nameList.size();
    }


    // define the views of an item
    class PartsCategoryNameViewHolder extends RecyclerView.ViewHolder {

        ImageView partsCategoryBlackDot;
        TextView partsCategoryName;

        // Constructor to initialize the views from the Layout
        PartsCategoryNameViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            // Views inside our layout
            partsCategoryBlackDot = itemView.findViewById(R.id.shopItem_categoryDot);
            partsCategoryName = itemView.findViewById(R.id.shopItem_categoryName);


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
