package com.crazylegend.kotlinextensions.retrofit.interceptors

import java.io.IOException


/**
 * Created by Hristijan on 1/25/19 to long live and prosper !
 */
class NoConnectionException : IOException() {

    override val message: String?
        get() = "No Connection"
}