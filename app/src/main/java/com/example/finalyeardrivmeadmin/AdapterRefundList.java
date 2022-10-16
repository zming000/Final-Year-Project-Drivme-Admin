package com.example.finalyeardrivmeadmin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterRefundList extends RecyclerView.Adapter<AdapterRefundList.RefundListViewHolder> {
    //declare variables
    Context refundContext;
    ArrayList<ModelRefundList> refundArrayList;

    public AdapterRefundList(Context refundContext, ArrayList<ModelRefundList> refundArrayList) {
        this.refundContext = refundContext;
        this.refundArrayList = refundArrayList;
    }

    @NonNull
    @Override
    public AdapterRefundList.RefundListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View refundView = LayoutInflater.from(refundContext).inflate(R.layout.item_refund, parent, false);

        return new RefundListViewHolder(refundView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRefundList.RefundListViewHolder holder, int position) {
        ModelRefundList refundModel = refundArrayList.get(position);

        holder.mtvOrderID.setText(refundModel.orderID);
        holder.mtvRefundStatus.setText(refundModel.refundStatus);

        holder.mcvRefund.setOnClickListener(view -> {
            Intent intent = new Intent(refundContext, OrderDetails.class);
            intent.putExtra("orderID", refundModel.orderID);

            refundContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return refundArrayList.size();
    }

    public class RefundListViewHolder extends RecyclerView.ViewHolder{
        //declare variables
        TextView mtvOrderID, mtvRefundStatus;
        CardView mcvRefund;

        public RefundListViewHolder(@NonNull View itemView) {
            super(itemView);

            //assign variables
            mtvOrderID = itemView.findViewById(R.id.tvOrderID);
            mtvRefundStatus = itemView.findViewById(R.id.tvRefundStatus);
            mcvRefund = itemView.findViewById(R.id.cvRefund);
        }
    }
}
