package com.crazylegend.retrofit.interceptors

import android.content.Context
import com.crazylegend.common.isOnline
import com.crazylegend.retrofit.throwables.NoConnectionException
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


/**
 * Created by Hristijan on 1/25/19 to long live and prosper !
 */
class ConnectivityInterceptor(private val context: Context) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        if (!context.isOnline) {
            throw NoConnectionException()
        }

        val builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }
}

