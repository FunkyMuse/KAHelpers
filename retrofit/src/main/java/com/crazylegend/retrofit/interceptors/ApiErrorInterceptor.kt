package com.crazylegend.retrofit.interceptors

import okhttp3.Interceptor
import okhttp3.Response


/**
 * Created by hristijan on 8/5/19 to long live and prosper !
 */

class ApiErrorInterceptor(val onError: (Response) -> Unit) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val response = chain.proceed(chain.request())

        return when {
            !response.isSuccessful -> {
                onError(response)
                Response.Builder().build()
            }
            else -> response
        }
    }
}