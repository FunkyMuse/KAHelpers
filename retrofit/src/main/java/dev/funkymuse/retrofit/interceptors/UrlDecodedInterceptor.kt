package dev.funkymuse.retrofit.interceptors

import okhttp3.Interceptor
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.Buffer
import java.io.IOException



class UrlDecodedInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val postBody = bodyToString(original.body)
        val body = original.body
        var requestBody: RequestBody? = null

        if (body != null) {
            requestBody = postBody.toRequestBody(original.body!!.contentType())
        }

        val request = when (original.method.lowercase()) {
            "post" -> original.newBuilder()
                    .method(original.method, original.body)
                    .post(requestBody!!)
            "put" -> original.newBuilder()
                    .method(original.method, original.body)
                    .put(requestBody!!)
            else -> original.newBuilder()
                    .method(original.method, original.body)
        }

        return chain.proceed(request.build())
    }

    fun bodyToString(request: RequestBody?): String {
        return try {
            val buffer = Buffer()
            if (request != null)
                request.writeTo(buffer)
            else
                return ""
            buffer.readUtf8()
        } catch (e: IOException) {
            "did not work"
        }
    }
}