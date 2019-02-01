package com.crazylegend.kotlinextensions.retrofit

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


/**
 * Created by Hristijan on 1/25/19 to long live and prosper !
 */
class RetryRequestInterceptor(private val context: Context) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        if (!NetworkUtil.isOnline(context)) {
            throw NoConnectionException()
        }
        val request = chain.request()
        var response: Response = chain.proceed(request)
        var tryCount = 0

        while (!response.isSuccessful && tryCount < 3) {
            tryCount++
            response = chain.proceed(request)
        }

        return response
    }
}