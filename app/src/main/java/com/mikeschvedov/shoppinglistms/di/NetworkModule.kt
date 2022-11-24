package com.mikeschvedov.shoppinglistms.di

import android.content.Context
import android.net.ConnectivityManager
import com.mikeschvedov.shoppinglistms.data.network.NotificationApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val NOTIFICATION_API_BASE_URL = "https://fcm.googleapis.com"

    //  ------------ Retrofit ------------ //
    @Provides
    fun provideRetrofit(
        httpClient: OkHttpClient,
        gsonConverterFactory: Converter.Factory
    ): Retrofit = Retrofit.Builder()
       .client(httpClient)
        .baseUrl(NOTIFICATION_API_BASE_URL)
        .addConverterFactory(gsonConverterFactory)
        .build()

    //  ------------ OkHttp ------------ //
    @Provides
    fun provideOKHTTPClient(): OkHttpClient =
        OkHttpClient().newBuilder()
            //.addInterceptor(loggingInterceptor)
            .build()

    //  ------------ Notification Api ------------ //
    @Provides
    fun provideNotificationApi(retrofit: Retrofit): NotificationApi = retrofit.create(NotificationApi::class.java)

    //  ------------ GsonFactory ------------ //
    @Provides
    fun provideGsonFactory(): Converter.Factory = GsonConverterFactory.create()

}