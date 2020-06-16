package com.badawy.carservice.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.badawy.carservice.R;
import com.badawy.carservice.models.CarModel;

import java.util.ArrayList;

public class AddCarAdapter extends RecyclerView.Adapter<AddCarAdapter.AddCarViewHolder> {
    private Context context;
    private ArrayList<CarModel> carList;
    private OnItemClickListener onItemClickListener;
    private int checkedPosition = -1;
    private String carID;

    public String getCarID() {
        return carID;
    }

    private void setCarID(String carID) {
        this.carID = carID;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }


    public AddCarAdapter(Context context, ArrayList<CarModel> carList) {
        this.context = context;
        this.carList = carList;
    }

    @NonNull
    @Override
    public AddCarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayout = LayoutInflater.from(context).inflate(R.layout.item_add_car, parent, false);
        return new AddCarViewHolder(itemLayout, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final AddCarViewHolder holder, final int position) {

        String carYear = carList.get(position).getCarYear();
        String carBrand = carList.get(position).getCarBrand();
        String carModel = carList.get(position).getCarModel();
        String builder = carBrand.concat(" - ").concat(carModel).concat(" - ").concat(carYear);
        holder.addCar_name.setText(builder);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedPosition = position;
                notifyDataSetChanged();

            }
        });


        if (checkedPosition == position) {
//            setCarID(carList.get(checkedPosition).getCarID());
            holder.itemView.setBackgroundResource(R.color.grey_line);
        } else {
            holder.itemView.setBackgroundResource(R.color.white);

        }


    }

    public CarModel getCurrentSelectedCar( ){
        if (checkedPosition!= -1){
            return carList.get(checkedPosition);
        }
         return null;
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }


    public void getFilterList(ArrayList<CarModel> filterList) {
        carList = filterList;
        checkedPosition = -1;
//        setCarID(null);
        notifyDataSetChanged();
    }


    static class AddCarViewHolder extends RecyclerView.ViewHolder {
        TextView addCar_name;

        AddCarViewHolder(@NonNull View itemView, final OnItemClickListener onItemClickListener) {
            super(itemView);
            addCar_name = itemView.findViewById(R.id.item_addCar_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {

                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onItemClickListener.onItemClick(position);

                        }

                    }

                }
            });

        }
    }
}
