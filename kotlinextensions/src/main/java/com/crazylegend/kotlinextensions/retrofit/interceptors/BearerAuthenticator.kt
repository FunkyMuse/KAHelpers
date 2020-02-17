package com.crazylegend.kotlinextensions.retrofit.interceptors

import android.content.Context
import com.crazylegend.kotlinextensions.context.isOnline
import com.crazylegend.kotlinextensions.retrofit.throwables.NoConnectionException
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


/**
 * Created by hristijan on 8/23/19 to long live and prosper !
 */
class BearerAuthenticator(private val token: String, private val abbreviation: String = "Bearer", private val context: Context) : Interceptor {

    @Throws(NoConnectionException::class, IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        if (!context.isOnline) {
            throw NoConnectionException()
        }

        return if (context.isOnline) {
            val request = chain.request()
            val authenticatedRequest = request.newBuilder()
                    .header("Authorization", "$abbreviation $token").build()
            chain.proceed(authenticatedRequest)
        } else {
            chain.proceed(chain.request().newBuilder().build())
        }

    }


}