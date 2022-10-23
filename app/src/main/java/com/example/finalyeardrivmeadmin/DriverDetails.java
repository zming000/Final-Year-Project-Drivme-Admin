package com.example.finalyeardrivmeadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.taufiqrahman.reviewratings.BarLabels;
import com.taufiqrahman.reviewratings.RatingReviews;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DriverDetails extends AppCompatActivity {
    //declare variables
    ImageView mivGender;
    TextView mtvName, mtvContact, mtvRating, mtvEmail, mtvAge, mtvRace, mtvDrivExp, mtvLanguages, mtvState, mtvAreas, mtvPriceDay, mtvPriceHour;
    RatingBar mrbDriver;
    RatingReviews mrrDriver;
    Button mbtnSuspend, mbtnUnsuspend;
    FirebaseFirestore detailsDB, updateAcc;
    ArrayList<String> languages, areas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_details);

        //assign variables
        mivGender = findViewById(R.id.ivGender);
        mtvName = findViewById(R.id.tvName);
        mtvContact = findViewById(R.id.tvContact);
        mtvPriceDay = findViewById(R.id.tvPriceDay);
        mtvPriceHour = findViewById(R.id.tvPriceHour);
        mtvRating = findViewById(R.id.tvRating);
        mrbDriver = findViewById(R.id.rbDriver);
        mtvEmail = findViewById(R.id.tvEmail);
        mtvAge = findViewById(R.id.tvAge);
        mtvRace = findViewById(R.id.tvRace);
        mtvDrivExp = findViewById(R.id.tvDrivExp);
        mtvLanguages = findViewById(R.id.tvLanguages);
        mtvState = findViewById(R.id.tvState);
        mtvAreas = findViewById(R.id.tvAreas);
        mrrDriver = findViewById(R.id.rrDriver);
        mbtnSuspend = findViewById(R.id.btnSuspend);
        mbtnUnsuspend = findViewById(R.id.btnUnsuspend);

        //initialize firestore
        detailsDB = FirebaseFirestore.getInstance();

        String driverID = getIntent().getStringExtra("driverID");

        detailsDB.collection("User Accounts").document(driverID).get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        DocumentSnapshot doc = task.getResult();

                        String status = doc.getString("accountStatus");

                        if(Objects.requireNonNull(status).equals("Suspended")){
                            mbtnSuspend.setVisibility(View.GONE);
                            mbtnUnsuspend.setVisibility(View.VISIBLE);
                        }

                        String gender = doc.getString("gender");
                        languages = (ArrayList<String>) doc.get("languages");
                        areas = (ArrayList<String>) doc.get("familiarAreas");

                        //set values to display
                        if(Objects.requireNonNull(gender).equals("Male")){
                            mivGender.setBackgroundResource(R.drawable.icon_male);
                        }
                        else{
                            mivGender.setBackgroundResource(R.drawable.icon_female);
                        }
                        mtvRating.setText(String.valueOf(doc.getDouble("rating")));
                        mrbDriver.setRating(Float.parseFloat(String.valueOf(doc.getDouble("rating"))));
                        mtvName.setText(doc.getString("lastName") + " " + doc.getString("firstName"));
                        mtvContact.setText(doc.getString("phoneNumber"));
                        mtvPriceDay.setText("RM" + String.valueOf(doc.getLong("priceDay").intValue()) + " / day");
                        mtvPriceHour.setText("RM" + String.valueOf(doc.getLong("priceHour").intValue()) + " / hour");
                        mtvEmail.setText(doc.getString("email"));
                        mtvAge.setText(doc.getString("age") + " Years Old");
                        mtvRace.setText(doc.getString("race"));
                        mtvDrivExp.setText(doc.getString("drivingExperience") + " Years Of Driving Experience");
                        mtvState.setText(doc.getString("state"));

                        //languages
                        //initialize string builder
                        StringBuilder lanSB = new StringBuilder();

                        for(int j = 0; j < Objects.requireNonNull(languages).size(); j++){
                            //concat array value
                            lanSB.append(languages.get(j));

                            if(j != languages.size()-1){
                                lanSB.append(", ");
                            }
                        }
                        mtvLanguages.setText("[" + lanSB + "]");

                        //areas
                        //initialize string builder
                        StringBuilder areaSB = new StringBuilder();

                        for(int j = 0; j < Objects.requireNonNull(areas).size(); j++){
                            //concat array value
                            areaSB.append(areas.get(j));

                            if(j != areas.size()-1){
                                areaSB.append(", ");
                            }
                        }
                        mtvAreas.setText("[" + areaSB + "]");

                        //rating and reviews
                        Pair[] colors = new Pair[]{
                                new Pair<>(Color.parseColor("#0c96c7"), Color.parseColor("#00fe77")),
                                new Pair<>(Color.parseColor("#7b0ab4"), Color.parseColor("#ff069c")),
                                new Pair<>(Color.parseColor("#fe6522"), Color.parseColor("#fdd116")),
                                new Pair<>(Color.parseColor("#104bff"), Color.parseColor("#67cef6")),
                                new Pair<>(Color.parseColor("#ff5d9b"), Color.parseColor("#ffaa69"))
                        };

                        int[] raters = new int[]{
                                Objects.requireNonNull(doc.getLong("5 stars")).intValue(),
                                Objects.requireNonNull(doc.getLong("4 stars")).intValue(),
                                Objects.requireNonNull(doc.getLong("3 stars")).intValue(),
                                Objects.requireNonNull(doc.getLong("2 stars")).intValue(),
                                Objects.requireNonNull(doc.getLong("1 star")).intValue()
                        };

                        mrrDriver.createRatingBars(100, BarLabels.STYPE3, colors, raters);
                    }
                });

        mbtnSuspend.setOnClickListener(view -> {
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(DriverDetails.this);
            alertDialogBuilder.setTitle("Suspend Driver");
            alertDialogBuilder
                    .setMessage("Are you sure suspending this driver?")
                    .setCancelable(false)
                    .setPositiveButton("Suspend", (dialog, id) -> {
                        updateAcc = FirebaseFirestore.getInstance();

                        Map<String,Object> accStatus = new HashMap<>();
                        accStatus.put("accountStatus", "Suspended");

                        updateAcc.collection("User Accounts").document(driverID)
                                .update(accStatus)
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(DriverDetails.this, "Suspended the driver!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(DriverDetails.this, MainActivity.class));
                                    finishAffinity();
                                    finish();
                                });
                    })
                    .setNegativeButton("No", (dialog, id) -> dialog.cancel());

            android.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        });

        mbtnUnsuspend.setOnClickListener(view -> {
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(DriverDetails.this);
            alertDialogBuilder.setTitle("Unsuspend Driver");
            alertDialogBuilder
                    .setMessage("Are you sure unsuspending this driver?")
                    .setCancelable(false)
                    .setPositiveButton("Unsuspend", (dialog, id) -> {
                        updateAcc = FirebaseFirestore.getInstance();

                        Map<String,Object> accStatus = new HashMap<>();
                        accStatus.put("accountStatus", "Offline");

                        updateAcc.collection("User Accounts").document(driverID)
                                .update(accStatus)
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(DriverDetails.this, "Unsuspended the driver!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(DriverDetails.this, MainActivity.class));
                                    finishAffinity();
                                    finish();
                                });
                    })
                    .setNegativeButton("No", (dialog, id) -> dialog.cancel());

            android.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}