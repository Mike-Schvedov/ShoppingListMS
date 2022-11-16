package com.mikeschvedov.shoppinglistms.util.logging

import java.io.*
import java.nio.file.Files
import java.nio.file.Paths
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


internal class Logger(private val options: LoggerOptions) {

    private var timeFormat = "YYYY-MM-dd HH:mm:ss.SSS"

    init {
        if (options.writeToFile) {
            if (options.fileName.isEmpty()) throw InvalidLoggerOptionsException("File name cannot be empty.")
            if (options.maxFileByteSize <= 0) throw InvalidLoggerOptionsException("Max file must be more then 0 bytes.")
            if (options.outputFilePath.isEmpty()) throw InvalidLoggerOptionsException("File path cannot be empty.")
        }
    }

    private fun log(logLevel: LoggerOptions.LogLevel, msg: String) {

        if (!shouldLogBeDisplayed(logLevel, options.minLevel)) return

        val stackTrace = Thread.currentThread().stackTrace
        val elementIndex = getElementIndex(stackTrace)
        if (elementIndex == 0) return
        val element = stackTrace[elementIndex]
        val className = getClassName(element)
        val lineNumber = element.lineNumber
        val methodName = element.methodName

        val baseTemplate = "${logLevel.icon} [$logLevel] [$className:$lineNumber] $methodName > $msg \n"
        //val withTime = "${logLevel.icon} ${getTime()} | [$logLevel] [$className:$lineNumber] $methodName > $msg \n"

        if (options.writeToFile) writeToLogFile(baseTemplate)
        println(baseTemplate)
    }

    private fun writeToLogFile(template: String) {
        val file = createFileIfNotExist()
        val fileSize = getFileSize(file)
        val isFull = isFileLimitReached(fileSize, options.maxFileByteSize)
        if (!isFull)
            file.appendText(template)
        else {
            deleteOldLogs(file)
            file.appendText(template)
        }
    }

    private fun deleteOldLogs(file: File) {
        val tempFile = File("${options.outputFilePath}/$LogsDirectoryName/temp.txt")

        val reader = BufferedReader(FileReader(file))
        val writer = BufferedWriter(FileWriter(tempFile))

        // While file is full, remove last log to reduce size
        while (isFileLimitReached(getFileSize(file), options.maxFileByteSize)) {
            var currentLine: String?
            // Skip the first line
            reader.readLine()
            // Copy all lines (except the first) to the temp file
            while ((reader.readLine().also { currentLine = it }) != null) {
                writer.write(currentLine + System.getProperty("line.separator"));
            }
            writer.close()
            reader.close()
            // Rename temp file to original file, this will delete the original file
            tempFile.renameTo(file)
        }
    }

    private fun isFileLimitReached(fileSize: Long, maxFileSize: Long): Boolean {
        return fileSize >= maxFileSize
    }

    private fun createFileIfNotExist(): File {
        // Creating the folder if doest exist already
        val folder = File(options.outputFilePath, LogsDirectoryName)
        if (!folder.exists()) {
            folder.mkdirs()
        }
        // Creating and writing to file
        return File(folder, "${options.fileName}.txt")
    }

    private fun getFileSize(file: File): Long {
        val path = Paths.get(file.path)
        var size: Long = 0
        // size of a file (in bytes)
        try {
            size = Files.size(path)
        } catch (e: IOException) {
            println("File is not yet created.")
        }
        return size
    }

    private fun shouldLogBeDisplayed(
        thisLevel: LoggerOptions.LogLevel,
        minLevel: LoggerOptions.LogLevel
    ): Boolean {
        return thisLevel.ordinal >= minLevel.ordinal
    }

    private fun getClassName(element: StackTraceElement): String {
        val rawName = element.className
        val result = rawName.split('.')
        return result.last()
    }

    private fun getTime(): String? {
        val df: DateFormat = SimpleDateFormat(timeFormat, Locale.getDefault())
        return df.format(Calendar.getInstance().time)
    }

    private fun getElementIndex(stackTrace: Array<StackTraceElement>?): Int {
        if (stackTrace == null) return 0
        for (i in 2..stackTrace.size) {
            val className = stackTrace[i].className ?: ""
            if (className.contains(this.javaClass.simpleName)) continue
            return i
        }
        return 0
    }

    fun verbose(msg: String) {
        log(LoggerOptions.LogLevel.VERBOSE, msg)
    }

    fun debug(msg: String) {
        log(LoggerOptions.LogLevel.DEBUG, msg)
    }

    fun info(msg: String) {
        log(LoggerOptions.LogLevel.INFO, msg)
    }

    fun notice(msg: String) {
        log(LoggerOptions.LogLevel.NOTICE, msg)
    }

    fun warn(msg: String) {
        log(LoggerOptions.LogLevel.WARN, msg)
    }

    fun error(msg: String) {
        log(LoggerOptions.LogLevel.ERROR, msg)
    }

    fun severe(msg: String) {
        log(LoggerOptions.LogLevel.SEVERE, msg)
    }

    fun alert(msg: String) {
        log(LoggerOptions.LogLevel.ALERT, msg)
    }

    fun emergency(msg: String) {
        log(LoggerOptions.LogLevel.EMERGENCY, msg)
    }

    class InvalidLoggerOptionsException(msg: String) : Exception(msg)

    companion object{
        const val LogsDirectoryName = "LOGGER"
    }
}