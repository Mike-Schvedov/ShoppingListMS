package com.mikeschvedov.shoppinglistms.data.network

import com.mikeschvedov.shoppinglistms.BuildConfig
import com.mikeschvedov.shoppinglistms.data.network.models.PushNotification

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationApi {
    companion object{
        const val CONTENT_TYPE = "application/json"
    }

    @Headers("Authorization: key=${BuildConfig.API_KEY}", "Content-Type: $CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>
}
