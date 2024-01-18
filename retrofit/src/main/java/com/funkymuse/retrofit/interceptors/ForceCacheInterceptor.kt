package com.funkymuse.retrofit.interceptors

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response




/**
 * The cached response will be returned only when the Internet is available as OkHttp is designed like that.
 * When the Internet is available and data is cached, it returns the data from the cache.
 * Even when the data is cached and the Internet is not available, it returns with the error "no internet available".
 * We can use the following ForceCacheInterceptor at the Application layer in addition to the above one ([NetworkCacheInterceptor], only if not enabled from the server)
 * First use addNetworkInterceptor([NetworkCacheInterceptor])
 * then add this addInterceptor([ForceCacheInterceptor])
 * on your http client builder
 * @constructor
 */
class ForceCacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response =
        chain.proceed(
            chain.request()
                .newBuilder()
                .cacheControl(CacheControl.FORCE_CACHE)
                .build()
        )
}