package com.example.finalyeardrivmeadmin;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

public class PushNotificationService extends FirebaseMessagingService {
    @SuppressLint("NewApi")
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        String noTitle = Objects.requireNonNull(message.getNotification()).getTitle();
        String noText = message.getNotification().getBody();
        String CHANNEL_ID = "MESSAGE";

        NotificationChannel noChannel = new NotificationChannel(
                CHANNEL_ID,
                "Message Notification",
                NotificationManager.IMPORTANCE_HIGH);
        getSystemService(NotificationManager.class).createNotificationChannel(noChannel);

        Intent reqIntent = new Intent(getApplicationContext(), RefundList.class);
        reqIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent orderPending = PendingIntent.getActivity(getApplicationContext(), 1, reqIntent, PendingIntent.FLAG_ONE_SHOT);

        Notification.Builder noBuilder = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle(noTitle)
                .setContentText(noText)
                .setSmallIcon(R.drawable.app_logo_wheel)
                .setAutoCancel(true)
                .setContentIntent(orderPending);

        NotificationManagerCompat.from(this).notify(1, noBuilder.build());
        super.onMessageReceived(message);
    }
}
