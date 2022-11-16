package com.mikeschvedov.shoppinglistms.application

import android.app.Application
import com.mikeschvedov.shoppinglistms.util.logging.LoggerOptions
import com.mikeschvedov.shoppinglistms.util.logging.LoggerService
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        LoggerService.initialize("", LoggerOptions.LogLevel.VERBOSE)
        LoggerService.info("================================================")
        LoggerService.info("========== THE APPLICATION HAS STARTED =========")
        LoggerService.info("================================================")
    }
}