package com.funkymuse.retrofit

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response


fun Interceptor.Chain.proceedRequest(): Response = proceed(request().newBuilder().build())


inline fun Interceptor.Chain.newRequest(item: (builder: Request.Builder) -> Unit = {}) {
    val builder = request().newBuilder()
    item.invoke(builder)
    proceed(builder.build())
}
