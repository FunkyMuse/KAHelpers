package com.crazylegend.kotlinextensions.interceptors

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by hristijan on 8/5/19 to long live and prosper !
 */
class AuthorizationInterceptor(private val authorizationName:String = "Bearer", private val tokenProvider: () -> String?) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        // add jwt token
        if (request.header("Authorization") == authorizationName) {
            request = request.newBuilder().removeHeader("Authorization")
                    .addHeader("Authorization", "$authorizationName ${tokenProvider()}")
                    .build()
        }

        return chain.proceed(request)
    }
}