package com.funkymuse.retrofit.throwables

import java.io.IOException

class TooManyRequestsException(private val customMessage: String?) : IOException() {
    override val message: String
        get() = customMessage ?: "Too many network requests, please slow down."
}

val Throwable.isTooManyRequestsException get() = this is TooManyRequestsException