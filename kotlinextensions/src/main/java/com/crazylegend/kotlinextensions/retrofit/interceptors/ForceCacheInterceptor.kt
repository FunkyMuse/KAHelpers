package com.crazylegend.kotlinextensions.retrofit.interceptors

import android.content.Context
import com.crazylegend.kotlinextensions.context.isOnline
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response


/**
 * Created by crazy on 3/12/20 to long live and prosper !
 */

/**
 * The cached response will be returned only when the Internet is available as OkHttp is designed like that.
 * When the Internet is available and data is cached, it returns the data from the cache.
 * Even when the data is cached and the Internet is not available, it returns with the error "no internet available".
 * We can use the following ForceCacheInterceptor at the Application layer in addition to the above one ([NetworkCacheInterceptor], only if not enabled from the server)
 * First use addNetworkInterceptor([NetworkCacheInterceptor])
 * then add this addInterceptor([ForceCacheInterceptor])
 * on your http client builder
 * @property context Context
 * @constructor
 */
class ForceCacheInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        if (!context.isOnline) {
            builder.cacheControl(CacheControl.FORCE_CACHE)
        }
        return chain.proceed(builder.build())
    }
}