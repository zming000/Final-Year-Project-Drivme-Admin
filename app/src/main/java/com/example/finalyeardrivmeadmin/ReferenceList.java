package com.example.finalyeardrivmeadmin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ReferenceList extends AppCompatActivity {
    //declare variables
    FloatingActionButton mbtnAddRef;
    RecyclerView mrvReference;
    SwipeRefreshLayout mswipeReference;
    ArrayList<ModelReferenceList> refList;
    AdapterReferenceList rlAdapter;
    FirebaseFirestore refDB, checkRC, addRC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reference_list);

        //assign variables
        mbtnAddRef = findViewById(R.id.btnAddRef);
        mrvReference = findViewById(R.id.rvReference);
        mswipeReference = findViewById(R.id.swipeReference);
        mrvReference.setLayoutManager(new LinearLayoutManager(this));

        //initialize variables
        refDB = FirebaseFirestore.getInstance();
        checkRC = FirebaseFirestore.getInstance();
        addRC = FirebaseFirestore.getInstance();
        refList = new ArrayList<>();

        //initialize adapter
        rlAdapter = new AdapterReferenceList(this, refList);
        mrvReference.setAdapter(rlAdapter);

        getRefDetailsFromFirestore();

        mbtnAddRef.setOnClickListener(view -> {
            //initialize layout
            LayoutInflater dialogInflater = getLayoutInflater();
            View refView = dialogInflater.inflate(R.layout.activity_reference_code, null);

            //initialize dialog builder
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.dialog);
            AlertDialog rcDialog = dialogBuilder.setView(refView).create();

            //assign variables
            TextInputLayout mtilRC = refView.findViewById(R.id.tilRC);
            TextInputEditText metRC = refView.findViewById(R.id.etRC);
            Button mbtnDriver = refView.findViewById(R.id.btnAdd);

            metRC.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    //Nothing
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mtilRC.setErrorEnabled(false);
                }

                @Override
                public void afterTextChanged(Editable s) {
                    //Nothing
                }
            });

            mbtnDriver.setOnClickListener(view1 -> {
                String refCode = Objects.requireNonNull(metRC.getText()).toString().replaceAll("\\s","").toUpperCase();

                if(!refCode.isEmpty()) {
                    checkRC.collection("Reference Code Details").document(refCode).get()
                            .addOnCompleteListener(task2 -> {
                                if (task2.isSuccessful()) {
                                    DocumentSnapshot document2 = task2.getResult();

                                    //check the existence of reference code
                                    if (document2.exists()) {
                                        mtilRC.setError("Existed Reference Code!");
                                    }
                                    else {
                                        Map<String,Object> rc = new HashMap<>();
                                        rc.put("refCode", refCode);
                                        rc.put("driverID", "-");
                                        rc.put("status", "Available");
                                        rc.put("driverName", "-");

                                        addRC.collection("Reference Code Details").document(refCode)
                                                .set(rc)
                                                .addOnSuccessListener(unused -> {
                                                    Toast.makeText(ReferenceList.this, "Reference Code Added Successfully!", Toast.LENGTH_SHORT).show();

                                                    startActivity(new Intent(ReferenceList.this, ReferenceList  .class));
                                                    finishAffinity();
                                                    finish();
                                                })
                                                .addOnFailureListener(e -> Toast.makeText(ReferenceList.this, "Fail to add Reference Code!", Toast.LENGTH_SHORT).show());
                                    }
                                }
                            });
                }
                else{
                    mtilRC.setError("Please input reference code!");
                }
            });

            //display dialog with suitable size
            rcDialog.show();
            rcDialog.getWindow().setLayout(650, 460);
        });

        //swipe down refresh
        mswipeReference.setOnRefreshListener(() -> {
            getRefDetailsFromFirestore();
            mswipeReference.setRefreshing(false);
        });
    }

    private void getRefDetailsFromFirestore() {
        refDB.collection("Reference Code Details")
                .addSnapshotListener((value, error) -> {
                    if(error != null){
                        Toast.makeText(ReferenceList.this, "Error Loading List!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //clear list
                    refList.clear();

                    //use the id to check if the driver available within the duration requested
                    for(DocumentChange dc : Objects.requireNonNull(value).getDocumentChanges()){
                        if(dc.getType() == DocumentChange.Type.ADDED || dc.getType() == DocumentChange.Type.MODIFIED) {
                            refList.add(dc.getDocument().toObject(ModelReferenceList.class));
                        }
                    }

                    if(refList.size() == 0){
                        Toast.makeText(ReferenceList.this, "No reference code added yet!", Toast.LENGTH_SHORT).show();
                    }

                    rlAdapter.notifyDataSetChanged();
                });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ReferenceList.this, MainActivity.class));
        finishAffinity();
        finish();
    }
}