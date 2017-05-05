package com.ddsnowboard.fantasystocksandroid;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.jameswk2.FantasyStocksAPI.FantasyStocksAPI;

/**
 * This takes care of what happens when we get a Firebase push notification
 */
public class NotificationService extends FirebaseMessagingService {
    public static final String TAG = "NotificationService";
    public NotificationService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // Send a broadcast to load new floors, since this will reload everything and get the new data
        Intent intent = new Intent(Utilities.LOAD_NEW_FLOOR);
        intent.putExtra(Utilities.FLOOR_ID, FantasyStocksAPI.getInstance().getUser().getPlayers()[0].getFloor().getId());
        sendBroadcast(intent);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentText(remoteMessage.getNotification().getTitle());
        builder.setSmallIcon(R.drawable.letter_f);

        // Also, put a notification in the notification bar
        manager.notify(0, builder.build());
    }
}
