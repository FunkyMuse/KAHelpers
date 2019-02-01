package com.crazylegend.kotlinextensions.retrofit

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


/**
 * Created by Hristijan on 1/25/19 to long live and prosper !
 */
class ConnectivityInterceptor(private val context: Context) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        if (!NetworkUtil.isOnline(context)){
            throw NoConnectionException()
        }

        val builder = chain.request().newBuilder()

        return chain.proceed(builder.build())
    }
}