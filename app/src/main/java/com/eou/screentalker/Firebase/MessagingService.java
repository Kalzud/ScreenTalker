package com.eou.screentalker.Firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.eou.screentalker.Activities.DeclineActionActivity;
import com.eou.screentalker.Activities.PickActionActivity;
import com.eou.screentalker.Activities.Video_callActivity;
import com.eou.screentalker.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
//        getFirebaseMessage(message.getNotification().getTitle(), message.getNotification().getBody());
        // Handle the incoming FCM message
//        if (message.getData().size() > 0) {
//            String title = message.getData().get("title");
//            String body = message.getData().get("body");
//
//            // Handle the notification based on your requirements
//            // For example, show a notification in the system tray
//            showNotification(title, body);
//        }

    }

//    private void getFirebaseMessage(String title, String body) {
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "notify").setSmallIcon(R.drawable.ic_call)
//                .setContentTitle(title)
//                .setContentText(body)
//                .setAutoCancel(true);
//        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
//        managerCompat.notify(102, builder.build());
//    }

    private void getFirebaseMessage(String title, String body) {
        // Create an Intent for the call pick action
//        Intent pickIntent = new Intent(this, PickActionActivity.class);
//        PendingIntent pickPendingIntent = PendingIntent.getActivity(this, 0, pickIntent, PendingIntent.FLAG_IMMUTABLE);
//
//        // Create an Intent for the call decline action
//        Intent declineIntent = new Intent(this, DeclineActionActivity.class);
//        PendingIntent declinePendingIntent = PendingIntent.getActivity(this, 0, declineIntent, PendingIntent.FLAG_IMMUTABLE);

        // Build the notification with call pick and decline actions
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "notify")
                .setSmallIcon(R.drawable.ic_baseline_call_end_24)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true);
//                .addAction(R.drawable.ic_baseline_mic_off_24, "Pick", pickPendingIntent)  // Call pick action
//                .addAction(R.drawable.ic_baseline_call_end_24, "Decline", declinePendingIntent);  // Call decline action

        // Notify using NotificationManagerCompat
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(102, builder.build());
    }


    private void showNotification(String title, String body) {
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, Video_callActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        // Create a notification channel for Android Oreo and higher
        String channelId = "notify";
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_call)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);

        // Get an instance of the NotificationManager service
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Create a notification channel (required for Android Oreo and higher)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "notify",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        // Show the notification
        notificationManager.notify(0, notificationBuilder.build());
        System.out.println("1");
        System.out.println("2");
        System.out.println("3");
        System.out.println("4");
        System.out.println("null");
    }

}
