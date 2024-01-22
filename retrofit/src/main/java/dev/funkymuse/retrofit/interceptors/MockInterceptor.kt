package dev.funkymuse.retrofit.interceptors

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.ResponseBody.Companion.toResponseBody

/**
 * Usage:
 * MockInterceptor( Pair("/bar", """{"foo":"baz"}"""))
 */
class MockInterceptor(private val pathAndMessage: Pair<String, String>) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val response = when (chain.request().url.encodedPath) {
            pathAndMessage.first -> pathAndMessage.second
            else -> throw Error("unknown request")
        }

        val mediaType = "application/json".toMediaType()
        val responseBody = response.toResponseBody(mediaType)

        return okhttp3.Response.Builder()
                .protocol(Protocol.HTTP_1_0)
                .request(chain.request())
                .code(200)
                .message("")
                .body(responseBody)
                .build()
    }
}

