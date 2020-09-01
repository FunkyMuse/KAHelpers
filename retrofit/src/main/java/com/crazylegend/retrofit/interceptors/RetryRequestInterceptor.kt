package com.crazylegend.retrofit.interceptors

import android.content.Context
import com.crazylegend.retrofit.isOnline
import com.crazylegend.retrofit.throwables.NoConnectionException

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


/**
 * Created by Hristijan on 1/25/19 to long live and prosper !
 */
class RetryRequestInterceptor(private val context: Context) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        if (!context.isOnline) {
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