package com.mikeschvedov.shoppinglistms.util.logging

internal data class LoggerOptions(
    val fileName: String = "",
    val writeToFile: Boolean = false,
    val outputFilePath: String = "",
    // default file size is approx 1MB
    val maxFileByteSize: Long = 1_000_000,
    val minLevel: LogLevel = LogLevel.VERBOSE
) {
    enum class LogLevel(val icon: String) {
        VERBOSE("\uD83D\uDFE9"), //🟩
        DEBUG("\uD83D\uDFE8"), //🟨
        INFO("\uD83D\uDFE9"), //🟩
        NOTICE("\uD83D\uDFE8"), //🟨
        WARN("\uD83D\uDFE8"), //🟨
        ERROR("\uD83D\uDFE5"), //🟥
        SEVERE("\uD83D\uDFE5"), //🟥
        ALERT("\uD83D\uDFE5"), //🟥
        EMERGENCY("\uD83D\uDFE5") //🟥
    }
}
