package com.crazylegend.kotlinextensions.retrofit

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response


/**
 * Created by hristijan on 10/21/19 to long live and prosper !
 */

fun Interceptor.Chain.proceedRequest(): Response = proceed(request().newBuilder().build())


fun Interceptor.Chain.newRequest(item: (builder: Request.Builder) -> Unit = {}) {
    val builder = request().newBuilder()
    item.invoke(builder)
    proceed(builder.build())
}

