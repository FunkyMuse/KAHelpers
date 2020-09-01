package com.crazylegend.retrofit.interceptors

import com.crazylegend.retrofit.throwables.UnauthorizedException
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route


/**
 * Created by hristijan on 10/21/19 to long live and prosper !
 */
class UnauthorizedInterceptor(private val customMessage: String? = null) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {

        if (!response.isSuccessful && response.code == unAuthorized)
            throw UnauthorizedException(customMessage)

        return response.request
    }

    private val unAuthorized = 401
}