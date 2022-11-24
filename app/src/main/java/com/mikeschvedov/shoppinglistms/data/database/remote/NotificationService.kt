package com.mikeschvedov.shoppinglistms.data.database.remote

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mikeschvedov.shoppinglistms.MainActivity
import com.mikeschvedov.shoppinglistms.R
import com.mikeschvedov.shoppinglistms.util.logging.LoggerService
import kotlin.random.Random

private const val CHANNEL_ID = "my_channel"

class NotificationService : FirebaseMessagingService(){

    companion object{
        var sharePref: SharedPreferences? = null

        // Getting and setting the Token into sharedPref
        var token: String?
            get(){
                return sharePref?.getString("token", "")
            }
            set(value){
                sharePref?.edit()?.putString("token", value)?.apply()
            }
    }

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        LoggerService.info("THIS IS THE TOKEN: $newToken.")
        token = newToken
    }

    // When this app receives a notification
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        // Create a notification on our device
        val intent = Intent(this, MainActivity::class.java)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = Random.nextInt()

        createNotificationChannel(notificationManager)

        // clears other activities until our MainActivity opens up
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_ONE_SHOT)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["message"])
            .setColor(Color.parseColor("#FF9800")) // orange_medium
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        // Actually showing the notification
        notificationManager.notify(notificationID, notification)
    }

    private fun createNotificationChannel(notificationManager: NotificationManager){

        val channelName = "NewItemAddedChannel"
        val channel = NotificationChannel(CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH).apply {
            description = "Used to notify about new items added to the list"
            enableLights(true)
            lightColor = Color.parseColor("#FF5722") // orange_strong
        }
        notificationManager.createNotificationChannel(channel)
    }

}