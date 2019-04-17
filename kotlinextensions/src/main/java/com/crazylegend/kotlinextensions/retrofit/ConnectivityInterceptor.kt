package com.crazylegend.kotlinextensions.retrofit

import android.content.Context
import com.crazylegend.kotlinextensions.context.isOnline
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


/**
 * Created by Hristijan on 1/25/19 to long live and prosper !
 */
class ConnectivityInterceptor(private val context: Context) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        if (!context.isOnline){
            throw NoConnectionException()
        }

        val builder = chain.request().newBuilder()

        return chain.proceed(builder.build())
    }
}