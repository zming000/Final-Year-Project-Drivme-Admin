package com.example.finalyeardrivmeadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class OrderDetails extends AppCompatActivity {
    //declare variables
    TextView mtvOrderID, mtvName, mtvContact, mtvDName, mtvDContact, mtvOrderStatus, mtvTripOption, mtvMeetDate, mtvMeetTime, mtvStartDate,
            mtvEndDate, mtvDuration, mtvLocality, mtvAddress, mtvCarPlate, mtvCarModel, mtvCarColour,
            mtvCarTrans, mtvPetrolCompany, mtvPetrolType, mtvNotes, mtvPriceDay, mtvTotal;
    ImageView mivCopy;
    Button mbtnRefund;
    String orderID;
    FirebaseFirestore driverDetails, touristDetails, tripDetails, carDetails, getTotal, getTouristDrivpay, getDriverDrivpay, updateDrivTour, updateDrivDriver, updateRefundStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        //assign variables
        mtvOrderID = findViewById(R.id.tvOrderID);
        mtvName = findViewById(R.id.tvName);
        mtvContact = findViewById(R.id.tvContact);
        mtvDName = findViewById(R.id.tvDName);
        mtvDContact = findViewById(R.id.tvDContact);
        mtvOrderStatus = findViewById(R.id.tvOrderStatus);
        mtvTripOption = findViewById(R.id.tvTripOption);
        mtvMeetDate = findViewById(R.id.tvMeetDate);
        mtvMeetTime = findViewById(R.id.tvMeetTime);
        mtvStartDate = findViewById(R.id.tvStartDate);
        mtvEndDate = findViewById(R.id.tvEndDate);
        mtvDuration = findViewById(R.id.tvDuration);
        mtvLocality = findViewById(R.id.tvLocality);
        mtvAddress = findViewById(R.id.tvAddress);
        mtvCarPlate = findViewById(R.id.tvCarPlate);
        mtvCarModel = findViewById(R.id.tvCarModel);
        mtvCarColour = findViewById(R.id.tvCarColour);
        mtvCarTrans = findViewById(R.id.tvCarTrans);
        mtvPetrolCompany = findViewById(R.id.tvPetrolCompany);
        mtvPetrolType = findViewById(R.id.tvPetrolType);
        mtvNotes = findViewById(R.id.tvNotes);
        mtvPriceDay = findViewById(R.id.tvPriceDay);
        mtvTotal = findViewById(R.id.tvTotal);
        mivCopy = findViewById(R.id.ivCopy);
        mbtnRefund = findViewById(R.id.btnRefund);

        orderID = getIntent().getStringExtra("orderID");

        //set text
        /*order id*/
        mtvOrderID.setText(orderID);

        /*trip details*/
        tripDetails = FirebaseFirestore.getInstance();
        tripDetails.collection("Trip Details").document(orderID).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        String status = doc.getString("refundStatus");

                        if(status.equals("Refund Needed")){
                            mbtnRefund.setVisibility(View.VISIBLE);
                        }

                        mtvOrderStatus.setText(doc.getString("orderStatus"));
                        mtvTripOption.setText("By " + doc.getString("tripOption"));
                        mtvMeetDate.setText(doc.getString("startDate"));
                        mtvMeetTime.setText(doc.getString("time"));
                        mtvStartDate.setText(doc.getString("startDate"));
                        mtvEndDate.setText(doc.getString("endDate"));

                        int num = Objects.requireNonNull(doc.getLong("duration")).intValue();

                        if(num > 1) {
                            mtvDuration.setText(num + " days");
                        }
                        else{
                            mtvDuration.setText(num + " day");
                        }

                        mtvLocality.setText(doc.getString("locality"));
                        mtvAddress.setText(doc.getString("address"));
                        mtvNotes.setText(doc.getString("note"));
                        mtvPriceDay.setText(String.valueOf(Objects.requireNonNull(doc.getLong("priceDay")).intValue()));
                        mtvTotal.setText(String.valueOf(Objects.requireNonNull(doc.getLong("total")).intValue()));

                        String touristID = doc.getString("touristID");
                        String driverID = doc.getString("driverID");
                        String carPlate = doc.getString("carPlate");

                        /*driver details*/
                        driverDetails = FirebaseFirestore.getInstance();
                        driverDetails.collection("User Accounts").document(Objects.requireNonNull(driverID)).get()
                                .addOnCompleteListener(task1 -> {
                                    DocumentSnapshot driver = task1.getResult();

                                    mtvDName.setText(driver.getString("lastName") + " " + driver.getString("firstName"));
                                    mtvDContact.setText(driver.getString("phoneNumber"));
                                });

                        /*tourist details*/
                        touristDetails = FirebaseFirestore.getInstance();
                        touristDetails.collection("User Accounts").document(Objects.requireNonNull(touristID)).get()
                                .addOnCompleteListener(task1 -> {
                                    DocumentSnapshot tour = task1.getResult();

                                    mtvName.setText(tour.getString("lastName") + " " + tour.getString("firstName"));
                                    mtvContact.setText(tour.getString("phoneNumber"));
                                });

                        /*car details*/
                        carDetails = FirebaseFirestore.getInstance();
                        carDetails.collection("User Accounts").document(Objects.requireNonNull(touristID)).collection("Car Details").document(Objects.requireNonNull(carPlate)).get()
                                .addOnCompleteListener(task2 -> {
                                    DocumentSnapshot car = task2.getResult();

                                    mtvCarPlate.setText(car.getString("carPlate"));
                                    mtvCarModel.setText(car.getString("carModel"));
                                    mtvCarColour.setText(car.getString("carColour"));
                                    mtvCarTrans.setText(car.getString("carTransmission"));
                                    mtvPetrolCompany.setText(car.getString("petrolCompany"));
                                    mtvPetrolType.setText(car.getString("petrolType"));
                                });
                    }
                });

        mivCopy.setOnClickListener(view -> {
            ClipboardManager copyAddress = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData address = ClipData.newPlainText("address", mtvAddress.getText().toString());
            copyAddress.setPrimaryClip(address);

            Toast.makeText(OrderDetails.this, "Copied address to clipboard!", Toast.LENGTH_SHORT).show();
        });

        mbtnRefund.setOnClickListener(view -> {
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(OrderDetails.this);
            alertDialogBuilder.setTitle("Refund Booking");
            alertDialogBuilder
                    .setMessage("Are you sure to allow to refund this booking?")
                    .setCancelable(false)
                    .setPositiveButton("Yes, Allow", (dialog, id) -> refundBooking())
                    .setNegativeButton("No", (dialog, id) -> dialog.cancel());

            android.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        });
    }

    private void refundBooking() {
        getTotal = FirebaseFirestore.getInstance();
        updateDrivTour = FirebaseFirestore.getInstance();
        updateDrivDriver = FirebaseFirestore.getInstance();
        getDriverDrivpay = FirebaseFirestore.getInstance();
        getTouristDrivpay = FirebaseFirestore.getInstance();
        updateRefundStatus = FirebaseFirestore.getInstance();

        getTotal.collection("Trip Details").document(orderID).get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        DocumentSnapshot doc = task.getResult();
                        float total = doc.getLong("total");
                        float driver = total / 10;
                        float tourist = driver * 9;
                        String driverID = doc.getString("driverID");
                        String touristID = doc.getString("touristID");

                        getDriverDrivpay.collection("User Accounts").document(Objects.requireNonNull(driverID)).get()
                                .addOnCompleteListener(task1 -> {
                                    if(task1.isSuccessful()){
                                        DocumentSnapshot doc1 = task1.getResult();
                                        float driverPay = Float.parseFloat(Objects.requireNonNull(doc1.getString("drivPay")));
                                        float totalDriverPay = driverPay + driver;

                                        Map<String, Object> driverDriv = new HashMap<>();
                                        driverDriv.put("drivPay", String.format("%.2f", totalDriverPay));

                                        updateDrivDriver.collection("User Accounts").document(driverID)
                                                .update(driverDriv)
                                                .addOnSuccessListener(unused -> getTouristDrivpay.collection("User Accounts").document(Objects.requireNonNull(touristID)).get()
                                                        .addOnCompleteListener(task2 -> {
                                                            if(task2.isSuccessful()){
                                                                DocumentSnapshot doc2 = task2.getResult();
                                                                float touristPay = Float.parseFloat(Objects.requireNonNull(doc2.getString("drivPay")));
                                                                float totalTouristPay = touristPay + tourist;

                                                                Map<String, Object> touristDriv = new HashMap<>();
                                                                touristDriv.put("drivPay", String.format("%.2f", totalTouristPay));

                                                                updateDrivTour.collection("User Accounts").document(touristID)
                                                                        .update(touristDriv)
                                                                        .addOnSuccessListener(unused1 -> {
                                                                            Map<String, Object> refund = new HashMap<>();
                                                                            refund.put("refundStatus", "Refunded");

                                                                            updateRefundStatus.collection("Trip Details").document(orderID)
                                                                                    .update(refund)
                                                                                    .addOnSuccessListener(unused2 -> {
                                                                                        Toast.makeText(OrderDetails.this, "Refunded Successfully", Toast.LENGTH_SHORT).show();
                                                                                        startActivity(new Intent(OrderDetails.this, MainActivity.class));
                                                                                        finishAffinity();
                                                                                        finish();
                                                                                    });
                                                                        });
                                                            }
                                                        }));
                                    }
                                });
                    }
                });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}