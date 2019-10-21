package com.crazylegend.kotlinextensions.retrofit.interceptors

import android.content.Context
import com.crazylegend.kotlinextensions.context.isOnline
import com.crazylegend.kotlinextensions.retrofit.throwables.NoConnectionException
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


/**
 * Created by hristijan on 8/23/19 to long live and prosper !
 */

class BasicAuthInterceptor(user: String, password: String, private val context: Context) : Interceptor {

    @Throws(NoConnectionException::class, IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        if (!context.isOnline) {
            throw NoConnectionException()
        }

        return if (context.isOnline) {
            val request = chain.request()
            val authenticatedRequest = request.newBuilder()
                    .header("Authorization", credentials).build()
            chain.proceed(authenticatedRequest)
        } else {
            chain.proceed(chain.request().newBuilder().build())
        }

    }

    private val credentials: String = Credentials.basic(user, password)

}