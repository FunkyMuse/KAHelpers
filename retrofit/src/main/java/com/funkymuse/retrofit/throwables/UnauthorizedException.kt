package com.funkymuse.retrofit.throwables

import java.io.IOException


class UnauthorizedException(private val customMessage: String?) : IOException() {
    override val message: String
        get() = customMessage ?: "Un-authorized, please check credentials or re-login/authorize"
}

val Throwable.isUnauthorizedException get() = this is UnauthorizedException