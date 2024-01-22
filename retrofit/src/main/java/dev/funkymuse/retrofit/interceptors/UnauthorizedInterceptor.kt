package dev.funkymuse.retrofit.interceptors

import dev.funkymuse.retrofit.throwables.UnauthorizedException
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route



class UnauthorizedInterceptor(private val customMessage: String? = null) : Authenticator {

    companion object {
        private const val unAuthorized = 401
    }

    override fun authenticate(route: Route?, response: Response): Request {

        if (!response.isSuccessful && response.code == unAuthorized)
            throw UnauthorizedException(customMessage)

        return response.request
    }

}