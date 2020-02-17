package com.crazylegend.kotlinextensions.interceptors

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by hristijan on 8/5/19 to long live and prosper !
 */
class ContentTypeInterceptor(private val contentType: String = "application/json") : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response = chain
            .proceed(with(chain.request().newBuilder()) {
                header("Content-Type", contentType).build()
            })
}