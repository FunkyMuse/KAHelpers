package com.crazylegend.kotlinextensions.retrofit.interceptors

import android.content.Context
import com.crazylegend.kotlinextensions.context.isOnline
import com.crazylegend.kotlinextensions.retrofit.throwables.NoConnectionException
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit


/**
 * Created by hristijan on 10/4/19 to long live and prosper !
 */


/**
 * In order to cache the response build cache see {@see Context#retrofitCache()}
 *
 *  OkHttpClient.Builder().cache(cache).addNetworkInterceptor(networkCacheInterceptor)
 *
 * @property context Context to check whether there's internet connection
 * @property maxAge Int default is 1 [timeUnit]
 * @property timeUnit TimeUnit default is minute
 * @constructor
 */
class NetworkCacheInterceptor(
        private val context: Context,
        private val maxAge: Int = 1,
        private val timeUnit: TimeUnit = TimeUnit.MINUTES,
        private val cacheControlAbbreviation: String = "Cache-control") : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!context.isOnline) {
            throw NoConnectionException()
        }

        val response = chain.proceed(chain.request())
        val cacheControl = CacheControl.Builder()
                .maxAge(maxAge, timeUnit)
                .build()

        return response.newBuilder()
                .header(cacheControlAbbreviation, cacheControl.toString())
                .build()

    }

}