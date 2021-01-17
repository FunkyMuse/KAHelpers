package com.crazylegend.retrofit.interceptors

import com.crazylegend.retrofit.throwables.NoConnectionException
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


/**
 * Created by hristijan on 8/23/19 to long live and prosper !
 */
class BearerAuthenticator(private val token: String, private val abbreviation: String = "Bearer") : Interceptor {

    @Throws(NoConnectionException::class, IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val authenticatedRequest = request.newBuilder().header("Authorization", "$abbreviation $token").build()
        return chain.proceed(authenticatedRequest)
    }


}