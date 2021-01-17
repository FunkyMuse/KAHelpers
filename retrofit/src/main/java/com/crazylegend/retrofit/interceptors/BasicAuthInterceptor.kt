package com.crazylegend.retrofit.interceptors

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


/**
 * Created by hristijan on 8/23/19 to long live and prosper !
 */

class BasicAuthInterceptor(user: String, password: String) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val authenticatedRequest = request.newBuilder()
                .header("Authorization", credentials).build()
        return chain.proceed(authenticatedRequest)

    }

    private val credentials: String = Credentials.basic(user, password)

}