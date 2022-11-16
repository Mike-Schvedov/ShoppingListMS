package com.mikeschvedov.shoppinglistms.util.logging

internal object LoggerService {

    var logger: Logger? = null
    private const val LogFileName = "Crowdwise_SDK_logs";

    fun initialize(filePath: String, logLevel: LoggerOptions.LogLevel) {
        logger = Logger(
            LoggerOptions(
                fileName = LogFileName,
                writeToFile = false,
                outputFilePath = filePath,
                minLevel = logLevel
            )
        )
    }

    fun debug(content: String) {
      logger?.debug(content)
    }

    fun info(content: String) {
        logger?.info(content)
    }

    fun error(content: String) {
      logger?.error(content)
    }
}