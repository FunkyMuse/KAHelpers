package dev.funkymuse.root

import android.util.Log.d
import android.util.Log.e
import android.util.Log.getStackTraceString
import android.util.Log.i
import android.util.Log.w
import java.io.File
import java.util.Arrays
import kotlin.system.measureTimeMillis


/**
 * Installs file as APK without asking permission. Requires root permissions.
 */
fun File.installAPKNoPrompt(): File {
    "pm install -r ${this.absolutePath}".runCommandAsSu()
    return this
}


private fun File.installSystemAPKLowerThanLollipop(): File {
    "busybox mv ${this.absolutePath} /system/app/".log().runCommandAsSu()
    return this
}

private fun File.installSystemAPKLollipopOrHigher(): File {
    val apkDirectory = "/system/priv-app/$nameWithoutExtension"
    "mkdir $apkDirectory".runCommandAsSu()
    "cp ${this.absolutePath} $apkDirectory".runCommandAsSu()
    return this
}

fun File.forceDelete() {
    "rm -r ${this.canonicalPath}".runCommandAsSu()
}

fun File.forceMkdir() {
    "mkdir $absolutePath".runCommandAsSu()
}


/**
 * Copies this files to the destination one, no matter file system permissions. Requires root permissions.
 * @return Copied file.
 */
fun File.copyToAsSu(destination: File): File {
    "busybox cp ${this.absolutePath} ${destination.absolutePath}".runCommandAsSu()
    return destination
}


private fun List<String>.runCommand() = Runtime.getRuntime().exec(toTypedArray()).apply { waitFor() }.inputStream.bufferedReader().readText()

/**
 * Executes command on shell and returns standard output.
 * @return Standard output of the command.
 */
fun String.runCommand() = Runtime.getRuntime().exec(this).apply { waitFor() }.inputStream.bufferedReader().readText()

/**
 * Executes command on shell as root (superuser) and returns standard output.
 * NOTE: This call needs superuser privileges.
 * @return Standard output of the command.
 */
fun String.runCommandAsSu() = listOf("su", "-c", this).runCommand()

/**
 * Powers off the device
 */
fun shutdown() {
    "reboot -p".runCommand()
}

/**
 * Reboots the device
 */
fun reboot() {
    "reboot".runCommand()
}


private const val TAG = "LogUtils"

private fun <T> T.asString() = when (this) {
    is Array<*> -> Arrays.toString(this)
    is ByteArray -> Arrays.toString(this)
    is ShortArray -> Arrays.toString(this)
    is IntArray -> Arrays.toString(this)
    is LongArray -> Arrays.toString(this)
    else -> this.toString()
}

/**
 * Log this object as debug.
 */
fun <T> T.logDebug(tag: String = TAG) = apply {
    d(tag, this.asString())
}

/**
 * Log this object as debug (shorter name)
 */
fun <T> T.log(tag: String = TAG) = logDebug(tag)

/**
 * Log this object as info.
 */
fun <T> T.logInfo(tag: String = TAG) = apply {
    i(tag, this.asString())
}

/**
 * Log this object as warning.
 */
fun <T> T.logWarning(tag: String = TAG) = apply {
    w(tag, this.asString())
}


/**
 * Log this object as error.
 */
fun <T> T.logError(tag: String = TAG) = apply {
    e(tag, this.asString())
}

/**
 * Log stacktrace of exception as error
 */
fun <T : Exception> T.log(tag: String = TAG) = apply {
    getStackTraceString(this).logError(tag)
}

/**
 * Executes block and logs execution time for it as debug.
 */
inline fun <T> logExecutionTime(tag: String = "LogUtils", block: () -> T?): T? {
    var value: T? = null
    measureTimeMillis {
        value = block()
    }.apply {
        "$tag: $this".logDebug(tag)
    }
    return value
}

/**
 * Logs the call stack trace as debug.
 */
fun logCallTrace(tag: String = TAG) {
    try {
        throw IllegalStateException()
    } catch (e: IllegalStateException) {
        getStackTraceString(e).logDebug(tag)
    }
}

/**
 * Catches and logs any exception for this block and continue execution.
 * Note that execution of block will exit on first statement that throws an exception.
 * Exception is logged as ERROR level.
 */
inline fun ignoreErrors(block: () -> Unit) {
    try {
        block()
    } catch (e: Exception) {
        e.log()
    }
}