package com.mikeschvedov.shoppinglistms.application

import android.app.Application
import com.mikeschvedov.shoppinglistms.BuildConfig
import com.mikeschvedov.shoppinglistms.util.logging.LoggerOptions
import com.mikeschvedov.shoppinglistms.util.logging.LoggerService
import dagger.hilt.android.HiltAndroidApp
import java.util.MissingFormatArgumentException

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        LoggerService.initialize("", LoggerOptions.LogLevel.VERBOSE)
        LoggerService.info("================================================")
        LoggerService.info("========== THE APPLICATION HAS STARTED =========")
        LoggerService.info("================================================")

        val apiKey = BuildConfig.API_KEY
        if (apiKey == null || apiKey == "null"){
            throw MissingFormatArgumentException("MISSING API KEY, GITHUB IS IGNORING IT")
        }
    }
}//ShoppingList-fSaWbAe join to this.