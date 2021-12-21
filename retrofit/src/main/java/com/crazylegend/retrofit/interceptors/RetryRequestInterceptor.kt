package com.crazylegend.retrofit.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


/**
 * Created by Hristijan on 1/25/19 to long live and prosper !
 */
class RetryRequestInterceptor(private val maxTries:Int = 3) : Interceptor {

    private var tryCount = 0
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        var response: Response = chain.proceed(request)


        while (!response.isSuccessful && tryCount < maxTries) {
            tryCount++
            response = chain.proceed(request)
        }

        return response
    }
}