package com.example.finalyeardrivmeadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class RefundList extends AppCompatActivity {
    //declare variables
    RecyclerView mrvRefund;
    SwipeRefreshLayout mswipeRefund;
    ArrayList<ModelRefundList> refundList;
    AdapterRefundList flAdapter;
    FirebaseFirestore getOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_list);

        //assign variables
        mrvRefund = findViewById(R.id.rvRefund);
        mswipeRefund = findViewById(R.id.swipeRefund);
        mrvRefund.setLayoutManager(new LinearLayoutManager(this));

        //initialize variables
        getOrder = FirebaseFirestore.getInstance();
        refundList = new ArrayList<>();

        flAdapter = new AdapterRefundList(this, refundList);
        mrvRefund.setAdapter(flAdapter);

        getRefundDetailsFromFirestore();

        mswipeRefund.setOnRefreshListener(() -> {
            getRefundDetailsFromFirestore();
            mswipeRefund.setRefreshing(false);
        });
    }

    private void getRefundDetailsFromFirestore() {
        getOrder.collection("Trip Details")
                .whereIn("refundStatus", Arrays.asList("Refund Needed", "Refunded"))
                .addSnapshotListener((value, error) -> {
                    if(error != null){
                        Toast.makeText(RefundList.this, "Error Loading List!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //clear list
                    refundList.clear();

                    //use the id to check if the driver available within the duration requested
                    for(DocumentChange dc : Objects.requireNonNull(value).getDocumentChanges()){
                        if(dc.getType() == DocumentChange.Type.ADDED || dc.getType() == DocumentChange.Type.MODIFIED) {
                            refundList.add(dc.getDocument().toObject(ModelRefundList.class));
                        }
                    }

                    if(refundList.size() == 0){
                        Toast.makeText(RefundList.this, "No order needed refund!", Toast.LENGTH_SHORT).show();
                    }

                    flAdapter.notifyDataSetChanged();
                });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RefundList.this, MainActivity.class));
        finishAffinity();
        finish();
    }
}