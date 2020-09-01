package com.crazylegend.retrofit.throwables

import java.io.IOException


/**
 * Created by hristijan on 10/21/19 to long live and prosper !
 */
class UnauthorizedException(private val customMessage: String?) : IOException() {
    override val message: String?
        get() = customMessage ?: "Un-authorized, please check credentials or re-login/authorize"
}