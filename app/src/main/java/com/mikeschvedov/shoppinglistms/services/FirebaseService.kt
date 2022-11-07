package com.mikeschvedov.shoppinglistms.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat.IMPORTANCE_HIGH
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mikeschvedov.shoppinglistms.MainActivity
import com.mikeschvedov.shoppinglistms.R
import kotlin.random.Random

const val TAG = "FirebaseService"
private const val CHANNEL_ID = "my_channel"

class FirebaseService : FirebaseMessagingService() {

    //Called when this device received a message
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        // When we receive a message to this device we want to display a notification
        // and create an intent to open the app if the user clicks on the notification
        setupReceivedNotification(message)

    }



    private fun setupReceivedNotification(message: RemoteMessage) {

        // create a notification manager, that will handle the creation of a new notification
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // create a unique notification ID
        val notificationID = Random.nextInt()

        // create a notification channel if the version is about Oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        // will open the main activity when clicking on notification
        val intent = Intent(this, MainActivity::class.java)
        // will place our activity on top (if we have many)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        // the intent is ready but will run only when we activate it
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_ONE_SHOT)

        // Finally create the notification, set the channel, and set the intent
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["message"])
            .setSmallIcon(R.drawable.ic_baseline_add_shopping_cart_24)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        // Actually display the notification
        notificationManager.notify(notificationID, notification)
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channelName = "channelName"
        val channel = NotificationChannel(CHANNEL_ID, channelName, IMPORTANCE_HIGH).apply {
            description = "my Channel description"
            enableLights(true)
            lightColor = Color.GREEN
        }
        notificationManager.createNotificationChannel(channel)
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        //sendRegistrationToServer(token)
    }

}