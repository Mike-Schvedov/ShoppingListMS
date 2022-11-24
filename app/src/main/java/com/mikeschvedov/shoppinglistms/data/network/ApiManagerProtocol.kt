package com.mikeschvedov.shoppinglistms.data.network

import com.mikeschvedov.shoppinglistms.data.network.models.PushNotification
import com.mikeschvedov.shoppinglistms.util.utility_models.ResultWrapper
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body

interface ApiManagerProtocol {
    suspend fun postNotification(notification: PushNotification):  ResultWrapper<Response<ResponseBody>>
}