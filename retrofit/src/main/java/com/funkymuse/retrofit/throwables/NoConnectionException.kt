package com.funkymuse.retrofit.throwables

import java.io.IOException



class NoConnectionException(private val customMessage: String? = null) : IOException() {

    override val message: String
        get() = customMessage ?: "No Internet Connection"
}

val Throwable.isNoConnectionException get() = this is NoConnectionException