package com.mikeschvedov.shoppinglistms.data.network.models

data class PushNotification(
    val data: NotificationData,
    val to: String
)