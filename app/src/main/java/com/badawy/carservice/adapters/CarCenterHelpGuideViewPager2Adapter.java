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
import com.badawy.carservice.models.CarCenterHelpGuideModel;

import java.util.ArrayList;

public class CarCenterHelpGuideViewPager2Adapter extends RecyclerView.Adapter<CarCenterHelpGuideViewPager2Adapter.CarCenterHelpGuideViewHolder> {

    private ArrayList<CarCenterHelpGuideModel> dataList ;
    private Context context;

    public CarCenterHelpGuideViewPager2Adapter(Context context, ArrayList<CarCenterHelpGuideModel> dataList) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public CarCenterHelpGuideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayout = LayoutInflater.from(context).inflate(R.layout.item_car_center_help_guide, parent, false);
        return new CarCenterHelpGuideViewHolder(itemLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull CarCenterHelpGuideViewHolder holder, int position) {
        holder.title.setText(dataList.get(position).getTitle());
        holder.description.setText(dataList.get(position).getDescription());
        holder.image.setImageResource(dataList.get(position).getImage());

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class CarCenterHelpGuideViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView description;
        private ImageView image;

        public CarCenterHelpGuideViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_carCenterHelpGuide_title);
            description = itemView.findViewById(R.id.item_carCenterHelpGuide_description);
            image = itemView.findViewById(R.id.item_carCenterHelpGuide_image);
        }
    }
}
