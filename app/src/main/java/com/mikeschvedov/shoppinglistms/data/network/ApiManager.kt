package com.mikeschvedov.shoppinglistms.data.network

import com.mikeschvedov.shoppinglistms.data.network.models.PushNotification
import com.mikeschvedov.shoppinglistms.util.utility_models.Failure
import com.mikeschvedov.shoppinglistms.util.utility_models.ResultWrapper
import com.mikeschvedov.shoppinglistms.util.utility_models.Success
import okhttp3.*
import retrofit2.Response
import javax.inject.Inject

class ApiManager @Inject constructor(
    private val notificationApi: NotificationApi,
) : ApiManagerProtocol {

    override suspend fun postNotification(notification: PushNotification): ResultWrapper<Response<ResponseBody>> = try {
        Success(notificationApi.postNotification(notification))
    } catch (e: Exception) {
        Failure(e)
    }

}