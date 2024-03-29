package com.mikeschvedov.shoppinglistms.data.services

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.android.datatransport.runtime.scheduling.jobscheduling.SchedulerConfig.Flag
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mikeschvedov.shoppinglistms.MainActivity
import com.mikeschvedov.shoppinglistms.R
import com.mikeschvedov.shoppinglistms.util.getMessageLockState
import com.mikeschvedov.shoppinglistms.util.logging.LoggerService
import com.mikeschvedov.shoppinglistms.util.setMessageLockState
import kotlin.random.Random

private const val CHANNEL_ID = "my_channel"

class NotificationService : FirebaseMessagingService() {

    companion object {
        var sharePref: SharedPreferences? = null

        // Getting and setting the Token into sharedPref
        var token: String?
            get() {
                return sharePref?.getString("token", "")
            }
            set(value) {
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

        if (!getMessageLockState() && tiramisuPermissionsCheck()) {
            LoggerService.info("Lock State is False, will NOT ignore this message")

            // Create a notification on our device
            val intent = Intent(this, MainActivity::class.java)
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationID = Random.nextInt()

            // Keep this check here for future projects to know
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                createNotificationChannel(notificationManager)
            }

            // clears other activities until our MainActivity opens up
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            val pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_IMMUTABLE)
            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(message.data["title"])
                .setContentText(message.data["message"])
                .setColor(Color.GREEN)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()

            // Actually showing the notification
            notificationManager.notify(notificationID, notification)

        } else {
            LoggerService.info("Lock State is True, IGNORING this message")
            //After ignoring the message, we set the lock state back to false
            setMessageLockState(false)
        }
    }

    private fun tiramisuPermissionsCheck(): Boolean {
        // If we are above level 33, check permissions
        //TODO also need to add the permission into the manifest
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            return  ContextCompat.checkSelfPermission(this,
                Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        }else{
            return true
        }
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {

        val channelName = "NewItemAddedChannel"
        val channel = NotificationChannel(
            CHANNEL_ID,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Used to notify about new items added to the list"
            enableLights(true)
            lightColor = Color.GREEN
        }
        notificationManager.createNotificationChannel(channel)
    }

}