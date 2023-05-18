package com.crazylegend.retrofit.interceptors

import com.crazylegend.retrofit.throwables.TooManyRequestsException
import java.io.IOException
import okhttp3.Interceptor
import okhttp3.Response

class TooManyRequestsInterceptor(
    private val tooManyRequestsResponseCode: Int = 429,
    private val customMessage: String? = null
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        if (response.code == tooManyRequestsResponseCode) {
            throw TooManyRequestsException(customMessage)
        }
        return response
    }
}