package com.badawy.carservice.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.badawy.carservice.R;
import com.badawy.carservice.models.SparePartDetailsModel;

import java.util.ArrayList;

public class SparePartDetailsAdapter extends RecyclerView.Adapter<SparePartDetailsAdapter.SparePartDetailsViewHolder>{

    private ArrayList<SparePartDetailsModel> detailsList;
    private Context context;


    public SparePartDetailsAdapter(ArrayList<SparePartDetailsModel> detailsList, Context context) {
        this.detailsList = detailsList;
        this.context = context;
    }

    @NonNull
    @Override
    public SparePartDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayout = LayoutInflater.from(context).inflate(R.layout.item_product_details, parent, false);
        return new SparePartDetailsViewHolder(itemLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull SparePartDetailsViewHolder holder, int position) {
        holder.detailName.setText(detailsList.get(position).getDetailName().trim());
        holder.detailValue.setText(detailsList.get(position).getDetailValue().trim());

    }

    @Override
    public int getItemCount() {
        return detailsList.size();
    }

    class SparePartDetailsViewHolder extends RecyclerView.ViewHolder {
        TextView detailName,detailValue;
        SparePartDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            this.detailName = itemView.findViewById(R.id.item_productDetails_name);
            this.detailValue = itemView.findViewById(R.id.item_productDetails_value);
        }
    }
}
