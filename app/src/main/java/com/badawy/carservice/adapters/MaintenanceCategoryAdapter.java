package com.badawy.carservice.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.badawy.carservice.R;
import com.badawy.carservice.fragment.DeliveryCarMaintenanceFragment;
import com.badawy.carservice.models.MaintenanceCategoryModel;

import java.util.ArrayList;

public class MaintenanceCategoryAdapter extends RecyclerView.Adapter<MaintenanceCategoryAdapter.myViewHolder> {

    //Global Variables

    private ArrayList<MaintenanceCategoryModel> list;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private int currentPosition;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    //Constructor
    public MaintenanceCategoryAdapter(Context context, ArrayList<MaintenanceCategoryModel> list) {
        this.list = list;
        this.context = context;
        currentPosition = -1;
    }


    //return the layout of items ( How an item should look like )
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLayout = LayoutInflater.from(context).inflate(R.layout.item_maintenance_categories, parent, false);
        return new myViewHolder(itemLayout, onItemClickListener);
    }


    // put the data inside the views of an item
    @Override
    public void onBindViewHolder(@NonNull myViewHolder eachItem, final int position) {

        eachItem.categoryIcon.setImageResource(list.get(position).getCategoryImage());
        eachItem.categoryName.setText(list.get(position).getCategoryName());


        eachItem.categoryBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPosition = position;
                notifyDataSetChanged();
                replaceFragment(v, new DeliveryCarMaintenanceFragment());
            }
        });


        if (currentPosition == position) {

            eachItem.categoryBackground.setBackgroundResource(R.color.grey_line);
        } else {
            eachItem.categoryBackground.setBackgroundResource(R.color.white);

        }


    }

    // return number of rows in the list
    @Override
    public int getItemCount() {
        return list.size();
    }



    private void replaceFragment(View view, Fragment fragment) {
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        activity.getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.homepage_fragment_container, fragment)
                .addToBackStack("NavHomeFragment")
                .commit();

    }

    // define the views of an item
    class myViewHolder extends RecyclerView.ViewHolder {

        // Global Variables
        ImageView categoryIcon;
        TextView categoryName;
        ConstraintLayout categoryBackground;


        // Constructor to initialize the views from the Layout
        myViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            // Views inside our layout
            categoryIcon = itemView.findViewById(R.id.item_maintenance_category_image);
            categoryName = itemView.findViewById(R.id.item_maintenance_category_name);
            categoryBackground = itemView.findViewById(R.id.item_maintenance_category_background);


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
