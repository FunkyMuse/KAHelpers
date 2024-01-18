package com.funkymuse.kotlinextensions.core

import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection




inline fun <R> URL.use(block: (URLConnection) -> R): R {
    var connection: URLConnection? = null
    try {
        connection = openConnection()
        return block(connection)
    } catch (e: Exception) {
        throw e
    } finally {
        (connection as? HttpURLConnection)?.disconnect()
    }
}