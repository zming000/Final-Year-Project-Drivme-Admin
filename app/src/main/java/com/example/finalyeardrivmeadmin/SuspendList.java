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

public class SuspendList extends AppCompatActivity {
    //declare variables
    RecyclerView mrvSuspend;
    SwipeRefreshLayout mswipeSuspend;
    ArrayList<ModelDriverList> suspendList;
    AdapterDriverList dlAdapter;
    FirebaseFirestore getDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suspend_list);

        //assign variables
        mrvSuspend = findViewById(R.id.rvSuspend);
        mswipeSuspend = findViewById(R.id.swipeSuspend);
        mrvSuspend.setLayoutManager(new LinearLayoutManager(this));

        //initialize variables
        getDriver = FirebaseFirestore.getInstance();
        suspendList = new ArrayList<>();

        //initialize adapter
        dlAdapter = new AdapterDriverList(this, suspendList);
        mrvSuspend.setAdapter(dlAdapter);

        getSuspendDetailsFromFirestore();

        //swipe down refresh
        mswipeSuspend.setOnRefreshListener(() -> {
            getSuspendDetailsFromFirestore();
            mswipeSuspend.setRefreshing(false);
        });
    }

    private void getSuspendDetailsFromFirestore() {
        getDriver.collection("User Accounts")
                .whereEqualTo("accountStatus", "Suspended")
                .addSnapshotListener((value, error) -> {
                    if(error != null){
                        Toast.makeText(SuspendList.this, "Error Loading List!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //clear list
                    suspendList.clear();

                    //use the id to check if the driver available within the duration requested
                    for(DocumentChange dc : Objects.requireNonNull(value).getDocumentChanges()){
                        if(dc.getType() == DocumentChange.Type.ADDED || dc.getType() == DocumentChange.Type.MODIFIED) {
                            suspendList.add(dc.getDocument().toObject(ModelDriverList.class));
                        }
                    }

                    if(suspendList.size() == 0){
                        Toast.makeText(SuspendList.this, "No suspended driver!", Toast.LENGTH_SHORT).show();
                    }

                    dlAdapter.notifyDataSetChanged();
                });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SuspendList.this, MainActivity.class));
        finishAffinity();
        finish();
    }
}