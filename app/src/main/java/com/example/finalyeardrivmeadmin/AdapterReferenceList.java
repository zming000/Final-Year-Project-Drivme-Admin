package com.example.finalyeardrivmeadmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class AdapterReferenceList extends RecyclerView.Adapter<AdapterReferenceList.RefListViewHolder> {
    //declare variables
    Context refContext;
    ArrayList<ModelReferenceList> refArrayList;

    public AdapterReferenceList(Context refContext, ArrayList<ModelReferenceList> refArrayList) {
        this.refContext = refContext;
        this.refArrayList = refArrayList;
    }

    @NonNull
    @Override
    public AdapterReferenceList.RefListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View refView = LayoutInflater.from(refContext).inflate(R.layout.item_reference, parent, false);

        return new RefListViewHolder(refView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterReferenceList.RefListViewHolder holder, int position) {
        ModelReferenceList mrl = refArrayList.get(position);

        holder.mtvRef.setText(mrl.refCode);
        holder.mtvDriverID.setText(mrl.driverID);
        holder.mtvDriverName.setText(mrl.driverName);
        holder.mtvStatus.setText(mrl.status);
    }

    @Override
    public int getItemCount() {
        return refArrayList.size();
    }

    public class RefListViewHolder extends RecyclerView.ViewHolder{
        //declare variables
        TextView mtvRef, mtvDriverID, mtvDriverName, mtvStatus;

        public RefListViewHolder(@NonNull View itemView) {
            super(itemView);

            //assign variables
            mtvRef = itemView.findViewById(R.id.tvRef);
            mtvDriverID = itemView.findViewById(R.id.tvDriverID);
            mtvDriverName = itemView.findViewById(R.id.tvDriverName);
            mtvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
