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
import com.badawy.carservice.models.SparePartsCategoryModel;

import java.util.ArrayList;
import java.util.List;

public class SparePartsCategoryAdapter extends RecyclerView.Adapter<SparePartsCategoryAdapter.PartsCategoryNameViewHolder> {

    //Global Variables

    private ArrayList<SparePartsCategoryModel> sparePartsCategoryList;
    private Context context;
    private OnCategoryClick mOnCategoryClick;
    private int selectedItem;


    public interface OnCategoryClick {
        void onCategoryClick(int position);
    }

    public void setmOnCategoryClick(OnCategoryClick listener) {
        mOnCategoryClick = listener;
    }

    //Constructor
    public SparePartsCategoryAdapter(Context context, ArrayList<SparePartsCategoryModel> carList, OnCategoryClick onCategoryClick) {
        this.sparePartsCategoryList = carList;
        this.context = context;
        this.selectedItem = 0;
        this.mOnCategoryClick = onCategoryClick;
        onCategoryClick.onCategoryClick(selectedItem);
    }


    //return the layout of items ( How an item should look like )
    @NonNull
    @Override
    public SparePartsCategoryAdapter.PartsCategoryNameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLayout = LayoutInflater.from(context).inflate(R.layout.item_spare_parts_category, parent, false);
        return new PartsCategoryNameViewHolder(itemLayout, mOnCategoryClick);
    }


    // put the data inside the views of an item
    @Override
    public void onBindViewHolder(@NonNull PartsCategoryNameViewHolder holder, final int position) {
        holder.partsCategoryName.setText(sparePartsCategoryList.get(position).getPartsCategoryName());

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectedItem = position;
//                notifyDataSetChanged();
//            }
//        });


        if (selectedItem == position) {
            holder.partsCategoryName.setTextColor(Color.BLACK);
            holder.partsCategoryBlackDot.setVisibility(View.VISIBLE);
            holder.partsCategoryName.setTextSize(13);
        } else {
            holder.partsCategoryName.setTextColor(Color.GRAY);
            holder.partsCategoryBlackDot.setVisibility(View.INVISIBLE);
            holder.partsCategoryName.setTextSize(11);

        }

    }

    private List<String> getSparePartsIdList() {
        return sparePartsCategoryList.get(selectedItem).getPartIdList();
    }

    // return number of rows in the list
    @Override
    public int getItemCount() {
        return sparePartsCategoryList.size();
    }


    // define the views of an item
    class PartsCategoryNameViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView partsCategoryBlackDot;
        TextView partsCategoryName;
        OnCategoryClick onCategoryClick;

        // Constructor to initialize the views from the Layout
        PartsCategoryNameViewHolder(@NonNull View itemView, OnCategoryClick onCategoryClick) {
            super(itemView);

            // Views inside our layout
            partsCategoryBlackDot = itemView.findViewById(R.id.shopItem_categoryDot);
            partsCategoryName = itemView.findViewById(R.id.shopItem_categoryName);
            this.onCategoryClick = onCategoryClick;

            itemView.setOnClickListener(this);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (listener != null) {
//                        int position = getAdapterPosition();
//                        if (position != RecyclerView.NO_POSITION) {
//                            listener.onItemClick(position);
//                            notifyDataSetChanged();
//                        }
//                    }
//
//
//                }
//            });
        }

        @Override
        public void onClick(View v) {
            if (onCategoryClick != null) {

                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    selectedItem = position;
                    notifyDataSetChanged();
                    onCategoryClick.onCategoryClick(selectedItem);
                }
            }

        }
    }
}


