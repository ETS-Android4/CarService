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
import com.badawy.carservice.models.CarModel;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class NavCarsAdapter extends RecyclerView.Adapter<NavCarsAdapter.NavCarsViewHolder> {
    private Context context;
    private OnDeleteIconClick onDeleteIconClickListener;
    private ArrayList<CarModel> carList;
    private boolean editMode;

    public NavCarsAdapter(Context context, ArrayList<CarModel> carList, OnDeleteIconClick onDeleteIconClickListener) {
        this.context = context;
        this.carList = carList;
        this.onDeleteIconClickListener = onDeleteIconClickListener;
    }

    @NonNull
    @Override
    public NavCarsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayout = LayoutInflater.from(context).inflate(R.layout.item_nav_cars, parent, false);
        return new NavCarsViewHolder(itemLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull NavCarsViewHolder holder, int position) {

        String carName = carList.get(position).getCarModel().concat(" - ")
                .concat(carList.get(position).getCarYear());

        holder.carNameTv.setText(carName);

        Glide.with(context).load(carList.get(position).getCarBrandLogoUri()).into(holder.carBrandLogoIv);

        if (editMode){
            holder.carDeleteIcon.setVisibility(View.VISIBLE);
            holder.carDeleteIcon.animate().alpha(1.0f);
        }
        else {
            holder.carDeleteIcon.setVisibility(View.INVISIBLE);
            holder.carDeleteIcon.animate().alpha(0.0f);
        }


    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    class NavCarsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView carBrandLogoIv, carDeleteIcon;
        TextView carNameTv;
        RecyclerView carHistoryRV;

        public NavCarsViewHolder(@NonNull View itemView) {
            super(itemView);

            carBrandLogoIv = itemView.findViewById(R.id.item_nav_cars_carBrandLogo);
            carDeleteIcon = itemView.findViewById(R.id.item_nav_cars_deleteCarIcon);
            carNameTv = itemView.findViewById(R.id.item_nav_cars_carName);
            carHistoryRV = itemView.findViewById(R.id.item_nav_cars_carHistoryRecyclerView);

            carDeleteIcon.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.item_nav_cars_deleteCarIcon:
                    if (onDeleteIconClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onDeleteIconClickListener.onDeleteIconClick(position);
                        }
                    }
                    break;
            }
        }
    }

    public interface OnDeleteIconClick {
        void onDeleteIconClick(int position);
    }

    public void setOnDeleteIconClickListener(OnDeleteIconClick onDeleteIconClickListener) {
        this.onDeleteIconClickListener = onDeleteIconClickListener;
    }
}
