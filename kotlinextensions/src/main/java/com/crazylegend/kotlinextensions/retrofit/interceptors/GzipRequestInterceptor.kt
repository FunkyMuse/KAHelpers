package com.crazylegend.kotlinextensions.retrofit.interceptors

import okhttp3.*
import okio.*


/**
 * Created by crazy on 3/12/20 to long live and prosper !
 */
class GzipRequestInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()
        if (originalRequest.body == null || originalRequest.header("Content-Encoding") != null) {
            return chain.proceed(originalRequest)
        }
        val compressedRequest: Request = originalRequest.newBuilder()
                .header("Content-Encoding", "gzip")
                .method(originalRequest.method, originalRequest.body?.let { gzip(it) }?.let { forceContentLength(it) })
                .build()
        return chain.proceed(compressedRequest)
    }

    @Throws(IOException::class)
    private fun forceContentLength(requestBody: RequestBody): RequestBody? {
        val buffer = Buffer()
        requestBody.writeTo(buffer)
        return object : RequestBody() {
            override fun contentType(): MediaType? {
                return requestBody.contentType()
            }

            override fun contentLength(): Long {
                return buffer.size
            }

            @Throws(IOException::class)
            override fun writeTo(sink: BufferedSink) {
                sink.write(buffer.snapshot())
            }
        }
    }


    private fun gzip(body: RequestBody): RequestBody {
        return object : RequestBody() {
            override fun contentType(): MediaType? {
                return body.contentType()
            }

            override fun contentLength(): Long {
                return -1 // We don't know the compressed length in advance!
            }

            @Throws(IOException::class)
            override fun writeTo(sink: BufferedSink) {
                val gzipSink = (GzipSink(sink)).buffer()
                body.writeTo(gzipSink)
                gzipSink.close()
            }
        }
    }
}