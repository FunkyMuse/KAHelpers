package com.crazylegend.kotlinextensions.http

import android.content.Context
import com.crazylegend.kotlinextensions.context.isOnline
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection.getFileNameMap


/**
 * Created by hristijan on 7/30/19 to long live and prosper !
 */

// parse some new media type.
fun File.mediaType(): String {
    return getFileNameMap().getContentTypeFor(name) ?: when (extension.toLowerCase()) {
        "json" -> "application/json"
        "js" -> "application/javascript"
        "apk" -> "application/vnd.android.package-archive"
        "md" -> "text/x-markdown"
        "webp" -> "image/webp"
        else -> "application/octet-stream"
    }
}

/**
 * Must not be called on the main thread
 * @receiver Context
 * @param serverUrl String
 * @param timeOut Int default is 10 seconds, timeout is in ms
 * @return Boolean
 */
fun Context.isURLReachable(serverUrl: String, timeOut: Int = 10 * 1000): Boolean {
    if (isOnline) {
        return try {
            with(URL(serverUrl).openConnection() as HttpURLConnection) {
                connectTimeout = timeOut
                connect()
                responseCode == 200
            }
        } catch (e: Throwable) {
            false
        }
    }
    return false
}