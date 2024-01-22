package dev.funkymuse.retrofit.interceptors

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit





/**
 * In order to cache the response build cache see [@see Context.retrofitCache()]
 *
 *  OkHttpClient.Builder().cache(cache).addNetworkInterceptor(networkCacheInterceptor)
 *
 * @property maxAge Int default is 1 [timeUnit]
 * @property timeUnit TimeUnit default is minute
 * @constructor
 */
class NetworkCacheInterceptor(
        private val maxAge: Int = 1,
        private val timeUnit: TimeUnit = TimeUnit.MINUTES,
        private val cacheControlAbbreviation: String = "Cache-control") : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val response = chain.proceed(chain.request())
        val cacheControl = CacheControl.Builder()
                .maxAge(maxAge, timeUnit)
                .build()

        return response.newBuilder()
                .header(cacheControlAbbreviation, cacheControl.toString())
                .build()

    }

}