package com.badawy.carservice.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.badawy.carservice.R;

public class AccountInfoLabelsAdapter extends RecyclerView.Adapter<AccountInfoLabelsAdapter.AccountInfoLabelsViewHolder> {


    //Global Variables

    private Context context;
    private OnItemClickListener onItemClickListener;
    private String[] accountInfoLabels;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    //Constructor
    public AccountInfoLabelsAdapter(Context context, String[] infoLabels) {
        this.accountInfoLabels = infoLabels;
        this.context = context;
    }


    //return the layout of items ( How an item should look like )
    @NonNull
    @Override
    public AccountInfoLabelsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLayout = LayoutInflater.from(context).inflate(R.layout.account_info_item, parent, false);
        return new AccountInfoLabelsViewHolder(itemLayout, onItemClickListener);
    }


    // put the data inside the views of an item
    @Override
    public void onBindViewHolder(@NonNull AccountInfoLabelsAdapter.AccountInfoLabelsViewHolder holder, final int position) {

        holder.infoLabel.setText(accountInfoLabels[position]);
    }


    // return number of rows in the list
    @Override
    public int getItemCount() {
        return accountInfoLabels.length;
    }


    // define the views of an item
    class AccountInfoLabelsViewHolder extends RecyclerView.ViewHolder {

        TextView infoLabel;
        TextView userInfo;

        // Constructor to initialize the views from the Layout
        AccountInfoLabelsViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            // Views inside our layout
                infoLabel = itemView.findViewById(R.id.item_accountInfoLabel);
                userInfo = itemView.findViewById(R.id.item_accountInfo_userInfo);


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
