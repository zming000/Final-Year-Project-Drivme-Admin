package com.example.finalyeardrivmeadmin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.NumberPicker;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    //declare variables
    CardView mcvDriver, mcvRefund, mcvSearchDriver, mcvSearchOrder, mcvReference, mcvSuspend;
    FirebaseFirestore checkID, checkOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get notification token
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task1 -> {
                    if (!task1.isSuccessful()) {
                        return;
                    }

                    // Get new FCM registration token
                    String token = task1.getResult();
                    FirebaseFirestore updateToken = FirebaseFirestore.getInstance();

                    Map<String,Object> noToken = new HashMap<>();
                    noToken.put("notificationToken", token);

                    updateToken.collection("User Accounts").document("admin001")
                            .update(noToken);
                });

        mcvDriver = findViewById(R.id.cvDriver);
        mcvRefund = findViewById(R.id.cvRefund);
        mcvSearchDriver = findViewById(R.id.cvSearchDriver);
        mcvSearchOrder = findViewById(R.id.cvSearchOrder);
        mcvReference = findViewById(R.id.cvReference);
        mcvSuspend = findViewById(R.id.cvSuspend);

        mcvDriver.setOnClickListener(stateView -> {
            //initialize layout
            LayoutInflater dialogInflater = getLayoutInflater();
            stateView = dialogInflater.inflate(R.layout.activity_scroll_picker_long, null);

            //initialize dialog builder
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.dialog);
            AlertDialog stateDialog = dialogBuilder.setView(stateView).create();

            //assign variables
            Button mbtnSearchState = stateView.findViewById(R.id.btnSearchState);
            NumberPicker mnpPicker = stateView.findViewById(R.id.npPicker);

            //set the values
            ModelDriverDetails.initState();
            mnpPicker.setMaxValue(ModelDriverDetails.getDetailsArrayList().size() - 1);
            mnpPicker.setMinValue(0);
            mnpPicker.setDisplayedValues(ModelDriverDetails.detailsName());

            //display dialog with suitable size
            stateDialog.show();
            stateDialog.getWindow().setLayout(570, 650);

            mbtnSearchState.setOnClickListener(view1 -> {
                int value = mnpPicker.getValue();

                Intent intent = new Intent(MainActivity.this, DriverStateList.class);
                intent.putExtra("state", ModelDriverDetails.getDetailsArrayList().get(value).getDetailsOption());

                startActivity(intent);
                finish();
            });
        });

        mcvRefund.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, RefundList.class));
            finish();
        });

        mcvSearchDriver.setOnClickListener(searchDriverView -> {
            //initialize layout
            LayoutInflater dialogInflater = getLayoutInflater();
            searchDriverView = dialogInflater.inflate(R.layout.activity_search_driver, null);

            //initialize dialog builder
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.dialog);
            AlertDialog sdDialog = dialogBuilder.setView(searchDriverView).create();

            //assign variables
            TextInputLayout mtilDriverID = searchDriverView.findViewById(R.id.tilDriverID);
            TextInputEditText metDriverID = searchDriverView.findViewById(R.id.etDriverID);
            Button mbtnSearchDriver = searchDriverView.findViewById(R.id.btnSearchDriver);

            metDriverID.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    //Nothing
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mtilDriverID.setErrorEnabled(false);
                }

                @Override
                public void afterTextChanged(Editable s) {
                    //Nothing
                }
            });

            mbtnSearchDriver.setOnClickListener(view1 -> {
                String driverID = Objects.requireNonNull(metDriverID.getText()).toString();
                checkID = FirebaseFirestore.getInstance();

                if(!driverID.isEmpty()) {
                    checkID.collection("User Accounts").document(driverID).get()
                            .addOnCompleteListener(task -> {
                                if(task.isSuccessful()){
                                    DocumentSnapshot doc = task.getResult();

                                    if(!doc.exists()){
                                        mtilDriverID.setError("ID does not exist!");
                                    }
                                    else{
                                        int driverAcc = Objects.requireNonNull(doc.getLong("Account Driver")).intValue();

                                        if(driverAcc == 1){
                                            Intent intent = new Intent(MainActivity.this, DriverDetails.class);
                                            intent.putExtra("driverID", driverID);

                                            startActivity(intent);
                                            sdDialog.dismiss();
                                        }
                                        else{
                                            mtilDriverID.setError("Driver Account haven't activated!");
                                        }
                                    }
                                }
                            });
                }
                else{
                    mtilDriverID.setError("Please input driver ID!");
                }
            });

            //display dialog with suitable size
            sdDialog.show();
            sdDialog.getWindow().setLayout(650, 460);
        });

        mcvSearchOrder.setOnClickListener(searchOrderView -> {
            //initialize layout
            LayoutInflater dialogInflater = getLayoutInflater();
            searchOrderView = dialogInflater.inflate(R.layout.activity_search_order, null);

            //initialize dialog builder
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.dialog);
            AlertDialog soDialog = dialogBuilder.setView(searchOrderView).create();

            //assign variables
            TextInputLayout mtilOrderID = searchOrderView.findViewById(R.id.tilOrderID);
            TextInputEditText metOrderID = searchOrderView.findViewById(R.id.etOrderID);
            Button mbtnSearchOrder = searchOrderView.findViewById(R.id.btnSearchOrder);

            metOrderID.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    //Nothing
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mtilOrderID.setErrorEnabled(false);
                }

                @Override
                public void afterTextChanged(Editable s) {
                    //Nothing
                }
            });

            mbtnSearchOrder.setOnClickListener(view1 -> {
                String orderID = Objects.requireNonNull(metOrderID.getText()).toString();
                checkOrder = FirebaseFirestore.getInstance();

                if(!orderID.isEmpty()) {
                    checkOrder.collection("Trip Details").document(orderID).get()
                            .addOnCompleteListener(task -> {
                                if(task.isSuccessful()){
                                    DocumentSnapshot doc = task.getResult();

                                    if(!doc.exists()){
                                        mtilOrderID.setError("ID does not exist!");
                                    }
                                    else{
                                        Intent intent = new Intent(MainActivity.this, OrderDetails.class);
                                        intent.putExtra("orderID", orderID);

                                        startActivity(intent);
                                        soDialog.dismiss();;
                                    }
                                }
                            });
                }
                else{
                    mtilOrderID.setError("Please input order ID!");
                }
            });

            //display dialog with suitable size
            soDialog.show();
            soDialog.getWindow().setLayout(650, 460);
        });

        mcvReference.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, ReferenceList.class));
            finish();
        });

        mcvSuspend.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, SuspendList.class));
            finish();
        });
    }

    //quit application
    @Override
    public void onBackPressed() {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Leaving Drivme Admin?");
        alertDialogBuilder
                .setMessage("Click yes to exit!")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    finishAffinity();
                    finish();
                })
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());

        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}