package com.example.babarmustafa.chatapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class PushService extends Service {

    private FirebaseUser currentUser;

    public PushService() {
    }

    @Override
    public void onCreate() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseDatabase.getInstance().getReference().child("Notifications").child(currentUser.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                currentUser = FirebaseAuth.getInstance().getCurrentUser();

                Log.v("USER", "" + currentUser.getUid());

                NotificationMessage data = dataSnapshot.getValue(NotificationMessage.class);



                //get messages from notification node
                notif(data.getMessage());

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public IBinder onBind(Intent intent) {
        return null;
    }

    public void notif(String message) {


        Intent intent = new Intent(this, MainView.class);

        //view of notification
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder notification = new Notification.Builder(this)
                .setTicker("Office Work")
                .setContentTitle("Push Notification")
                .setContentText(message)
                .setTicker("Notification form my app")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setVibrate(new long[]{500, 500})
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Random random = new Random();
        int randomNumber = random.nextInt(9999 - 1000) + 1000;
        notificationManager.notify(randomNumber, notification.build());
//after delievery of notification removed from notification
        FirebaseDatabase.getInstance().getReference().child("Notifications").child(currentUser.getUid()).removeValue();


    }
}
