package com.badawy.carservice.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.badawy.carservice.R;
import com.badawy.carservice.models.ServiceTypeModel;

import java.util.ArrayList;
import java.util.List;

public class ServiceListAdapter extends RecyclerView.Adapter<ServiceListAdapter.CarCenterServiceListViewHolder> {
    private ArrayList<ServiceTypeModel> serviceList;
    private List<Integer> indexList = new ArrayList<>();
    private ServiceListAdapter.OnItemClickListener onItemClickListener;

    private Context context;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(ServiceListAdapter.OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    @NonNull
    @Override
    public CarCenterServiceListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLayout = LayoutInflater.from(context).inflate(R.layout.item_car_center, parent, false);
        return new CarCenterServiceListViewHolder(itemLayout, onItemClickListener);
    }

    public ServiceListAdapter(ArrayList<ServiceTypeModel> cycleServiceList, Context context) {
        this.serviceList = cycleServiceList;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull final CarCenterServiceListViewHolder holder, final int position) {

        holder.serviceName.setText(serviceList.get(position).getServiceName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!holder.isSelected) {
                    holder.serviceCheckBox.setChecked(true);
                    indexList.add(position);
                    holder.isSelected = !holder.isSelected;

                } else {
                    holder.serviceCheckBox.setChecked(false);
                    indexList.remove((Integer) position);
                    holder.isSelected = !holder.isSelected;

                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public ArrayList<ServiceTypeModel> getSelectedServiceList() {
        ArrayList<ServiceTypeModel> selectedServiceList = new ArrayList<>();

        if (indexList!=null){
            for (int i : indexList
            ) {
                selectedServiceList.add(serviceList.get(i));
            }
            return selectedServiceList;

        }
        else{
            return null;
        }


    }

    class CarCenterServiceListViewHolder extends RecyclerView.ViewHolder {

        private TextView serviceName;
        private CheckBox serviceCheckBox;
        private boolean isSelected;

        CarCenterServiceListViewHolder(@NonNull View itemView, final ServiceListAdapter.OnItemClickListener listener) {
            super(itemView);

            this.serviceName = itemView.findViewById(R.id.item_carCenter_serviceName);
            this.serviceCheckBox = itemView.findViewById(R.id.item_carCenter_serviceCheckBox);
            this.isSelected = false;
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
