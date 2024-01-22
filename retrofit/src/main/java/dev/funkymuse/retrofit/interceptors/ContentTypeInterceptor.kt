package dev.funkymuse.retrofit.interceptors

import okhttp3.Interceptor
import okhttp3.Response


class ContentTypeInterceptor(private val contentType: String = "application/json") : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response = chain
            .proceed(with(chain.request().newBuilder()) {
                header("Content-Type", contentType).build()
            })
}