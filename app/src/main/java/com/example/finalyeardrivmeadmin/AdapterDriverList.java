package com.example.finalyeardrivmeadmin;

import android.content.Context;
import android.content.Intent;
import android.media.Rating;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterDriverList extends RecyclerView.Adapter<AdapterDriverList.DriverListViewHolder>{
    //declare variables
    Context driverContext;
    ArrayList<ModelDriverList> driverArrayList;

    public AdapterDriverList(Context driverContext, ArrayList<ModelDriverList> driverArrayList) {
        this.driverContext = driverContext;
        this.driverArrayList = driverArrayList;
    }

    @NonNull
    @Override
    public AdapterDriverList.DriverListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View refView = LayoutInflater.from(driverContext).inflate(R.layout.item_driver, parent, false);

        return new DriverListViewHolder(refView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDriverList.DriverListViewHolder holder, int position) {
        ModelDriverList mdl = driverArrayList.get(position);
        String getGender = mdl.gender;

        //set values to display
        if(getGender.equals("Male")){
            holder.mivGender.setBackgroundResource(R.drawable.icon_male);
        }
        else{
            holder.mivGender.setBackgroundResource(R.drawable.icon_female);
        }

        holder.mtvName.setText(mdl.lastName + " " + mdl.firstName);
        holder.mtvRating.setText(String.valueOf(mdl.rating));
        holder.mrbDriver.setRating(mdl.rating);

        holder.mcvDriver.setOnClickListener(view -> {
            Intent intent = new Intent(driverContext, DriverDetails.class);
            intent.putExtra("driverID", mdl.userID);

            driverContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return driverArrayList.size();
    }

    public class DriverListViewHolder extends RecyclerView.ViewHolder{
        //declare variables
        ImageView mivGender;
        TextView mtvName, mtvRating;
        RatingBar mrbDriver;
        CardView mcvDriver;

        public DriverListViewHolder(@NonNull View itemView) {
            super(itemView);

            //assign variables
            mivGender = itemView.findViewById(R.id.ivGender);
            mtvName = itemView.findViewById(R.id.tvName);
            mtvRating = itemView.findViewById(R.id.tvRating);
            mrbDriver = itemView.findViewById(R.id.rbDriver);
            mcvDriver = itemView.findViewById(R.id.cvDriver);
        }
    }
}
