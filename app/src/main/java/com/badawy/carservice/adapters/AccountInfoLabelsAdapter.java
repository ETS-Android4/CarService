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

import java.util.List;

public class AccountInfoLabelsAdapter extends RecyclerView.Adapter<AccountInfoLabelsAdapter.AccountInfoLabelsViewHolder> {


    //Global Variables

    private Context context;
    private OnItemClickListener onClickListener;
    private String[] accountInfoLabels;
    private List<String> userData;


    public interface OnItemClickListener {
        void onEditClick(int position);
    }

    public void setOnClickListener(OnItemClickListener listener) {
        onClickListener = listener;
    }

    //Constructor
    public AccountInfoLabelsAdapter(Context context, String[] infoLabels, List<String> userData, OnItemClickListener onClickListener) {
        this.accountInfoLabels = infoLabels;
        this.context = context;
        this.userData = userData;
        this.onClickListener = onClickListener;
    }


    //return the layout of items ( How an item should look like )
    @NonNull
    @Override
    public AccountInfoLabelsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLayout = LayoutInflater.from(context).inflate(R.layout.item_account_info, parent, false);
        return new AccountInfoLabelsViewHolder(itemLayout);
    }


    // put the data inside the views of an item
    @Override
    public void onBindViewHolder(@NonNull AccountInfoLabelsAdapter.AccountInfoLabelsViewHolder holder, final int position) {
        holder.infoLabel.setText(accountInfoLabels[position]);
        if (userData.get(position).equals(context.getResources().getString(R.string.default_address)) || userData.get(position).equals(context.getResources().getString(R.string.default_phoneNumber))) {
            holder.userInfo.setTextColor(context.getResources().getColor(R.color.red));
            holder.userInfo.setText(userData.get(position));

        } else {

            holder.userInfo.setText(userData.get(position));
        }
        if (position == 1) {
            holder.editIcon.setVisibility(View.GONE);
        }
    }


    // return number of rows in the list
    @Override
    public int getItemCount() {
        return accountInfoLabels.length;
    }


    // define the views of an item
    class AccountInfoLabelsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView infoLabel;
        TextView userInfo;
        ImageView editIcon;

        // Constructor to initialize the views from the Layout
        AccountInfoLabelsViewHolder(@NonNull View itemView) {
            super(itemView);

            // Views inside our layout
            infoLabel = itemView.findViewById(R.id.item_accountInfoLabel);
            userInfo = itemView.findViewById(R.id.item_accountInfo_userInfo);
            editIcon = itemView.findViewById(R.id.item_accountInfo_edit);

            editIcon.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.item_accountInfo_edit) {

                if (onClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onClickListener.onEditClick(position);
                    }
                }

            }
        }
    }

}
