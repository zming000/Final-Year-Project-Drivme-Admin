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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class OrderDetails extends AppCompatActivity {
    //declare variables
    TextView mtvOrderID, mtvName, mtvContact, mtvDName, mtvDContact, mtvOrderStatus, mtvTripOption, mtvMeetDate, mtvMeetTime, mtvStartDate,
            mtvEndDate, mtvDuration, mtvLocality, mtvAddress, mtvCarPlate, mtvCarModel, mtvCarColour,
            mtvCarTrans, mtvPetrolCompany, mtvPetrolType, mtvNotes, mtvPriceDay, mtvTotal, mstartDate, mendDate, mpriceDay;
    ImageView mivCopy;
    Button mbtnRefund;
    String orderID;
    FirebaseFirestore driverDetails, touristDetails, tripDetails, carDetails, getTotal, getTouristDrivpay,
            getDriverDrivpay, updateDrivTour, updateDrivDriver, updateRefundStatus, updateDriverHistory, updateTouristHistory;

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
        mstartDate = findViewById(R.id.startDate);
        mtvEndDate = findViewById(R.id.tvEndDate);
        mendDate = findViewById(R.id.endDate);
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
        mpriceDay = findViewById(R.id.priceDay);
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
                        String tripOpt = doc.getString("tripOption");

                        if(Objects.requireNonNull(status).equals("Refund Needed")){
                            mbtnRefund.setVisibility(View.VISIBLE);
                        }

                        mtvOrderStatus.setText(doc.getString("orderStatus"));
                        mtvTripOption.setText("By " + doc.getString("tripOption"));
                        mtvMeetDate.setText(doc.getString("meetDate"));
                        mtvMeetTime.setText(doc.getString("meetTime"));

                        int num = Objects.requireNonNull(doc.getLong("duration")).intValue();
                        if(Objects.requireNonNull(tripOpt).equals("Day")) {
                            mstartDate.setText("Start Date:");
                            mendDate.setText("End Date:");
                            mtvStartDate.setText(doc.getString("meetDate"));
                            mtvEndDate.setText(doc.getString("endDate"));

                            if(num > 1) {
                                mtvDuration.setText(num + " days");
                            }
                            else{
                                mtvDuration.setText(num + " day");
                            }
                            mpriceDay.setText("Price per Day (RM):");
                        }
                        else{
                            mstartDate.setText("Start Time:");
                            mendDate.setText("End Time:");
                            mtvStartDate.setText(doc.getString("meetTime"));
                            mtvEndDate.setText(doc.getString("endTime"));

                            if(num > 1) {
                                mtvDuration.setText(num + " hours");
                            }
                            else{
                                mtvDuration.setText(num + " hour");
                            }
                            mpriceDay.setText("Price per Hour (RM):");
                        }
                        mtvPriceDay.setText(String.valueOf(Objects.requireNonNull(doc.getLong("priceDriver")).intValue()));

                        mtvLocality.setText(doc.getString("locality"));
                        mtvAddress.setText(doc.getString("address"));
                        mtvNotes.setText(doc.getString("note"));
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
        updateDriverHistory = FirebaseFirestore.getInstance();
        updateTouristHistory = FirebaseFirestore.getInstance();

        DateFormat fullFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm"); //date time
        DateFormat docID = new SimpleDateFormat("ddMMyyyyHHmmss"); //record time of button clicked

        //get current date time for id
        String transID = docID.format(Calendar.getInstance().getTime());
        //get current date time
        String dateTime = fullFormat.format(Calendar.getInstance().getTime());

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
                                        String driverToken = doc.getString("notificationToken");
                                        float driverPay = Float.parseFloat(Objects.requireNonNull(doc1.getString("drivPay")));
                                        float totalDriverPay = driverPay + driver;

                                        Map<String, Object> driverDriv = new HashMap<>();
                                        driverDriv.put("drivPay", String.format("%.2f", totalDriverPay));

                                        updateDrivDriver.collection("User Accounts").document(driverID)
                                                .update(driverDriv)
                                                .addOnSuccessListener(unused -> getTouristDrivpay.collection("User Accounts").document(Objects.requireNonNull(touristID)).get()
                                                        .addOnCompleteListener(task2 -> {
                                                            if(task2.isSuccessful()){
                                                                //send notification to driver
                                                                FCMSend.pushNotification(
                                                                        OrderDetails.this,
                                                                        driverToken,
                                                                        "Cancellation Fee",
                                                                        "Here is the cancellation fee of " + orderID);

                                                                //update driver transaction history
                                                                Map<String,Object> updateTrans = new HashMap<>();
                                                                updateTrans.put("transType", "Cancelled " + orderID);
                                                                updateTrans.put("transAmount", String.format("%.2f", driver));
                                                                updateTrans.put("transDateTime", dateTime);

                                                                updateDriverHistory.collection("User Accounts").document(driverID).collection("Transaction History").document(transID)
                                                                        .set(updateTrans);

                                                                DocumentSnapshot doc2 = task2.getResult();
                                                                String touristToken = doc2.getString("notificationToken");
                                                                float touristPay = Float.parseFloat(Objects.requireNonNull(doc2.getString("drivPay")));
                                                                float totalTouristPay = touristPay + tourist;

                                                                Map<String, Object> touristDriv = new HashMap<>();
                                                                touristDriv.put("drivPay", String.format("%.2f", totalTouristPay));

                                                                updateDrivTour.collection("User Accounts").document(touristID)
                                                                        .update(touristDriv)
                                                                        .addOnSuccessListener(unused1 -> {
                                                                            //send notification to driver
                                                                            FCMSend.pushNotification(
                                                                                    OrderDetails.this,
                                                                                    touristToken,
                                                                                    "Refunded Successfully",
                                                                                    "You have successfully refunded the money of " + orderID);

                                                                            //update driver transaction history
                                                                            Map<String,Object> updateTTrans = new HashMap<>();
                                                                            updateTTrans.put("transType", "Refunded " + orderID);
                                                                            updateTTrans.put("transAmount", String.format("%.2f", tourist));
                                                                            updateTTrans.put("transDateTime", dateTime);

                                                                            updateTouristHistory.collection("User Accounts").document(touristID).collection("Transaction History").document(transID)
                                                                                    .set(updateTTrans);

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