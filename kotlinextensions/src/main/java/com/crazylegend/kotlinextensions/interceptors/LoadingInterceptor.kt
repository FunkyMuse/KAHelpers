package com.crazylegend.kotlinextensions.interceptors

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by hristijan on 8/5/19 to long live and prosper !
 */
internal class LoadingInterceptor(private val onLoading: (LoadingInfo) -> Unit) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()

        val loadingInfo = LoadingInfo(name = request.url.encodedPath)
        onLoading(loadingInfo)

        try {
            return chain.proceed(request)
        } catch (e: Exception) {
            throw e
        } finally {
            onLoading(LoadingInfo(loadingInfo.name, false))
        }
    }

    internal data class LoadingInfo(
            val name: String = "",
            val isLoading: Boolean = true,
            val time: Long = System.currentTimeMillis()
    )
}