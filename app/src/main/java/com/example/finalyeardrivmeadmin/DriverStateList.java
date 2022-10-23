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
import java.util.Objects;

public class DriverStateList extends AppCompatActivity {
    //declare variables
    RecyclerView mrvDriver;
    SwipeRefreshLayout mswipeDriver;
    ArrayList<ModelDriverList> driverList;
    AdapterDriverList dlAdapter;
    FirebaseFirestore getDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_state_list);

        //assign variables
        mrvDriver = findViewById(R.id.rvDriver);
        mswipeDriver = findViewById(R.id.swipeDriver);
        mrvDriver.setLayoutManager(new LinearLayoutManager(this));

        //initialize variables
        getDriver = FirebaseFirestore.getInstance();
        driverList = new ArrayList<>();

        dlAdapter = new AdapterDriverList(this, driverList);
        mrvDriver.setAdapter(dlAdapter);

        getDriverDetailsFromFirestore();

        mswipeDriver.setOnRefreshListener(() -> {
            getDriverDetailsFromFirestore();
            mswipeDriver.setRefreshing(false);
        });
    }

    private void getDriverDetailsFromFirestore() {
        getDriver.collection("User Accounts")
                .whereEqualTo("state", getIntent().getStringExtra("state"))
                .addSnapshotListener((value, error) -> {
                    if(error != null){
                        Toast.makeText(DriverStateList.this, "Error Loading List!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //clear list
                    driverList.clear();

                    //use the id to check if the driver available within the duration requested
                    for(DocumentChange dc : Objects.requireNonNull(value).getDocumentChanges()){
                        if(dc.getType() == DocumentChange.Type.ADDED || dc.getType() == DocumentChange.Type.MODIFIED) {
                            driverList.add(dc.getDocument().toObject(ModelDriverList.class));
                        }
                    }

                    if(driverList.size() == 0){
                        Toast.makeText(DriverStateList.this, "No driver in this state currently!", Toast.LENGTH_SHORT).show();
                    }

                    dlAdapter.notifyDataSetChanged();
                });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(DriverStateList.this, MainActivity.class));
        finishAffinity();
        finish();
    }
}