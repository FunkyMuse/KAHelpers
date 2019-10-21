package com.crazylegend.kotlinextensions.retrofit.throwables

import java.io.IOException


/**
 * Created by Hristijan on 1/25/19 to long live and prosper !
 */
class NoConnectionException(private val customMessage: String? = null) : IOException() {

    override val message: String?
        get() = customMessage ?: "No Internet Connection"
}